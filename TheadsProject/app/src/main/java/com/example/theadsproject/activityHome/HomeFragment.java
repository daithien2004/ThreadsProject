package com.example.theadsproject.activityHome;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.theadsproject.activityPost.ConfigPostFragment;
import com.example.theadsproject.dto.PostResponse;
import com.example.theadsproject.R;
import com.example.theadsproject.adapter.PostAdapter;
import com.example.theadsproject.retrofit.ApiService;
import com.example.theadsproject.retrofit.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {
    private RecyclerView recyclerView;
    private PostAdapter postAdapter;
    private List<PostResponse> postList;

    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    long deletedId = result.getData().getLongExtra("deleted_post_id", -1);
                    if (deletedId != -1) {
                        // Gọi hàm xóa comment khỏi RecyclerView adapter
                        removePostById(deletedId);
                    }
                }
            }
    );

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = view.findViewById(R.id.rvPosts);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        fetchPosts();
        return view;
    }

    public void fetchPosts() {
        ApiService apiService = RetrofitClient.getApiService();
        apiService.getAllPosts().enqueue(new Callback<List<PostResponse>>() {
            @Override
            public void onResponse(Call<List<PostResponse>> call, Response<List<PostResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    postList = response.body();
                    postAdapter = new PostAdapter(getContext(), postList, activityResultLauncher);
                    recyclerView.setAdapter(postAdapter);
                } else {
                    Log.e("API_ERROR", "Không lấy được dữ liệu");
                }
            }

            @Override
            public void onFailure(Call<List<PostResponse>> call, Throwable t) {
                Log.e("API_ERROR", "Lỗi kết nối: " + t.getMessage());
            }
        });
    }

    public void removePostById(long commentId) {
        int indexToRemove = -1;

        // Duyệt qua commentList để tìm bình luận có commentId khớp
        for (int i = 0; i < postList.size(); i++) {
            if (postList.get(i).getPostId() == commentId) { // So sánh id để tìm bình luận cần xóa
                indexToRemove = i;
                break;
            }
        }

        // Nếu tìm thấy bình luận cần xóa, thực hiện xóa và cập nhật RecyclerView
        if (indexToRemove != -1) {
            postList.remove(indexToRemove);  // Xóa bình luận khỏi danh sách
            postAdapter.notifyItemRemoved(indexToRemove);   // Cập nhật RecyclerView để hiển thị lại danh sách
        }
    }
}


