package com.example.theadsproject.activitySearch;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.theadsproject.LoginRequiredDialogFragment;
import com.example.theadsproject.R;
import com.example.theadsproject.UserSessionManager;
import com.example.theadsproject.adapter.SearchAdapter;
import com.example.theadsproject.dto.UserResponse;
import com.example.theadsproject.entity.User;
import com.example.theadsproject.retrofit.ApiService;
import com.example.theadsproject.retrofit.RetrofitClient;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchFragment extends Fragment {

    private EditText edtSearch;
    private RecyclerView recyclerView;
    private SearchAdapter adapter;
    private List<UserResponse> userList = new ArrayList<>();
    Long userId;
    private UserSessionManager sessionManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        edtSearch = view.findViewById(R.id.edtSearch);
        recyclerView = view.findViewById(R.id.recyclerSearchResults);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new SearchAdapter(getContext(), userList, userId);
        recyclerView.setAdapter(adapter);

        loadAllUsers();

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().isEmpty()) {
                    loadAllUsers();  // Nếu không tìm kiếm thì load tất cả người dùng
                } else {
                    searchUsers(s.toString().trim()); // Tìm kiếm theo từ khóa
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        return view;
    }

    private void loadAllUsers() {
        sessionManager = new UserSessionManager();
        ApiService apiService = RetrofitClient.getApiService();

        apiService.getAllUsers().enqueue(new Callback<List<UserResponse>>() {
            @Override
            public void onResponse(Call<List<UserResponse>> call, Response<List<UserResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<UserResponse> allUsers = response.body();
                    Log.d("ALL_USERS", "Tổng số user: " + allUsers.size());

                    userList.clear();
                    userList.addAll(allUsers);
                    adapter.notifyDataSetChanged();

                    if (sessionManager.isLoggedIn()) {
                        // Lấy danh sách những người dùng mà người dùng hiện tại đang theo dõi
                        apiService.getFollowingUsers(userId).enqueue(new Callback<List<UserResponse>>() {
                            @Override
                            public void onResponse(Call<List<UserResponse>> call, Response<List<UserResponse>> followResponse) {
                                if (followResponse.isSuccessful() && followResponse.body() != null) {
                                    List<UserResponse> followingUsers = followResponse.body();
                                    List<Long> followingIds = new ArrayList<>();
                                    for (UserResponse u : followingUsers) {
                                        if (u.getUserId() != null) {
                                            followingIds.add(u.getUserId());
                                            Log.d("FOLLOWING_USER", "ID: " + u.getUserId());
                                        }
                                    }

                                    // Ánh xạ trạng thái theo dõi
                                    for (UserResponse user : allUsers) {
                                        boolean isFollowed = followingIds.contains(user.getUserId());
                                        user.setFollowing(isFollowed);
                                        Log.d("CHECK_USER", "User ID: " + user.getUserId() + ", isFollowing: " + isFollowed);
                                    }

                                    userList.clear();
                                    userList.addAll(allUsers);
                                    adapter.notifyDataSetChanged();
                                } else {
                                    Log.e("FOLLOW_API", "Không thể tải danh sách đã theo dõi hoặc null body");
                                }
                            }

                            @Override
                            public void onFailure(Call<List<UserResponse>> call, Throwable t) {
                                Log.e("FOLLOW_API", "Lỗi kết nối: " + t.getMessage());
                            }
                        });
                    }
                } else {
                    Log.e("ALL_USERS", "API không thành công hoặc body null");
                }
            }

            @Override
            public void onFailure(Call<List<UserResponse>> call, Throwable t) {
                Log.e("ALL_USERS", "Lỗi kết nối: " + t.getMessage());
            }
        });
    }

    private void searchUsers(String keyword) {
        ApiService apiService = RetrofitClient.getApiService();
        apiService.searchUsers(keyword).enqueue(new Callback<List<UserResponse>>() {
            @Override
            public void onResponse(Call<List<UserResponse>> call, Response<List<UserResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    userList.clear();
                    userList.addAll(response.body());
                    adapter.notifyDataSetChanged();
                } else {
                    Log.e("SEARCH_API", "Không có kết quả");
                }
            }

            @Override
            public void onFailure(Call<List<UserResponse>> call, Throwable t) {
                Log.e("SEARCH_API", "Lỗi kết nối: " + t.getMessage());
            }
        });
    }
}

