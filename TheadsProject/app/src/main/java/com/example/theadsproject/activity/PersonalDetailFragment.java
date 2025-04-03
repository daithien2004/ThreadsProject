package com.example.theadsproject.activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.theadsproject.R;
import com.example.theadsproject.adapter.PostAdapter;
import com.example.theadsproject.dto.PostResponse;
import com.example.theadsproject.retrofit.ApiService;
import com.example.theadsproject.retrofit.RetrofitClient;

import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PersonalDetailFragment extends Fragment {

    private RecyclerView rvPosts;
    private PostAdapter postAdapter;
    private List<PostResponse> posts = new ArrayList<>();

    public PersonalDetailFragment() {
        super(R.layout.fragment_personal_detail);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvPosts = view.findViewById(R.id.rvPosts);
        rvPosts.setLayoutManager(new LinearLayoutManager(getContext()));

        postAdapter = new PostAdapter(getContext(), posts);
        rvPosts.setAdapter(postAdapter);

        loadUserPosts();
    }

    private void loadUserPosts() {
        ApiService apiService = RetrofitClient.getApiService();
        Call<List<PostResponse>> call = apiService.getUserPosts(1L); // Đảm bảo API trả về List<PostResponse>

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
