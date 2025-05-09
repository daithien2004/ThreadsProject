package com.example.theadsproject.activityAccount;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.theadsproject.R;
import com.example.theadsproject.UserSessionManager;
import com.example.theadsproject.adapter.PostAdapter;
import com.example.theadsproject.dto.PostResponse;
import com.example.theadsproject.entity.User;
import com.example.theadsproject.retrofit.ApiService;
import com.example.theadsproject.retrofit.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllLikedActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PostAdapter postAdapter;
    private List<PostResponse> postList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_liked);

        recyclerView = findViewById(R.id.rvPosts);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        fetchLikedPosts();
    }

    private void fetchLikedPosts() {
        ApiService apiService = RetrofitClient.getApiService();
        UserSessionManager sessionManager = new UserSessionManager();
        User user = sessionManager.getUser();

        apiService.getLikedPostsByUser(user.getUserId()).enqueue(new Callback<List<PostResponse>>() {
            @Override
            public void onResponse(Call<List<PostResponse>> call, Response<List<PostResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    postList = response.body();
                    postAdapter = new PostAdapter(AllLikedActivity.this, postList);
                    recyclerView.setAdapter(postAdapter);
                } else {
                    Log.e("API_ERROR", "Không lấy được danh sách bài viết đã thích");
                }
            }

            @Override
            public void onFailure(Call<List<PostResponse>> call, Throwable t) {
                Log.e("API_ERROR", "Lỗi kết nối: " + t.getMessage());
            }
        });
        LinearLayout likedLayout = findViewById(R.id.lnLiked);
        likedLayout.setOnClickListener(v -> {
            finish();  // kết thúc activity này để quay về màn hình trước đó
        });
    }
}
