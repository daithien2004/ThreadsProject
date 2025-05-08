package com.example.theadsproject.activityHome;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.theadsproject.LoginRequiredDialogFragment;
import com.example.theadsproject.R;
import com.example.theadsproject.UserSessionManager;
import com.example.theadsproject.dto.PostResponse;
import com.example.theadsproject.entity.User;
import com.example.theadsproject.retrofit.ApiService;
import com.example.theadsproject.retrofit.RetrofitClient;

import java.util.List;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.theadsproject.dto.PostResponse;
import com.example.theadsproject.R;
import com.example.theadsproject.adapter.PostAdapter;
import com.example.theadsproject.retrofit.ApiService;
import com.example.theadsproject.retrofit.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FollowingFragment extends Fragment {
    private RecyclerView recyclerView;
    private PostAdapter postAdapter;
    private List<PostResponse> postList;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_following, container, false);
        recyclerView = view.findViewById(R.id.rvPosts);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        UserSessionManager sessionManager = new UserSessionManager();
        if (!sessionManager.isLoggedIn()) {
            new LoginRequiredDialogFragment().show(getParentFragmentManager(), "login_required_dialog");
        } else {
            fetchPosts();
        }
        return view;
    }

    private void fetchPosts() {
        ApiService apiService = RetrofitClient.getApiService();
        UserSessionManager sessionManager = new UserSessionManager();
        User user = sessionManager.getUser();

        apiService.getPostsByFollowing(user.getUserId()).enqueue(new Callback<List<PostResponse>>() {
            @Override
            public void onResponse(retrofit2.Call<List<PostResponse>> call, Response<List<PostResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    postList = response.body();
                    postAdapter = new PostAdapter(getContext(), postList);
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

}