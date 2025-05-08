package com.example.theadsproject.activityAccount;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.theadsproject.R;
import com.example.theadsproject.UserSessionManager;
import com.example.theadsproject.adapter.PostAdapter;
import com.example.theadsproject.dto.PostResponse;
import com.example.theadsproject.entity.User;
import com.example.theadsproject.retrofit.ApiService;
import com.example.theadsproject.retrofit.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ThreadsFragment extends Fragment {
    private RecyclerView rvPosts;
    private PostAdapter postAdapter;
    private List<PostResponse> posts = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_threads, container, false);

        // Setup RecyclerView
        rvPosts = view.findViewById(R.id.rvPostsPersonal);
        rvPosts.setLayoutManager(new LinearLayoutManager(getContext()));

        postAdapter = new PostAdapter(getContext(), posts);
        rvPosts.setAdapter(postAdapter);

        // Gọi hàm loadUserPosts để tải dữ liệu
        loadUserPosts();

        return view;
    }

    private void loadUserPosts() {
        UserSessionManager sessionManager = new UserSessionManager();
        User user = sessionManager.getUser();
        ApiService apiService = RetrofitClient.getApiService();
        Call<List<PostResponse>> call = apiService.getUserPosts(user.getUserId()); // Giả sử API trả về List<PostResponse>

        call.enqueue(new Callback<List<PostResponse>>() {
            @Override
            public void onResponse(Call<List<PostResponse>> call, Response<List<PostResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    posts.clear();
                    posts.addAll(response.body());
                    postAdapter.notifyDataSetChanged();
                } else {
                    // Ghi log lỗi chi tiết
                    int statusCode = response.code();
                    String errorBody = "";
                    try {
                        errorBody = response.errorBody().string(); // Đọc nội dung lỗi
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    Toast.makeText(getContext(), "Lỗi khi tải bài đăng! (Mã: " + statusCode + ")", Toast.LENGTH_SHORT).show();
                    System.out.println(" API Error - Status Code: " + statusCode);
                    System.out.println(" API Error Body: " + errorBody);
                }
            }

            @Override
            public void onFailure(Call<List<PostResponse>> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi kết nối!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
