package com.example.theadsproject.activityAccount;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.theadsproject.R;
import com.example.theadsproject.UserSessionManager;
import com.example.theadsproject.adapter.PostAdapter;
import com.example.theadsproject.dto.PostResponse;
import com.example.theadsproject.dto.RepostResponse;
import com.example.theadsproject.entity.User;
import com.example.theadsproject.retrofit.ApiService;
import com.example.theadsproject.retrofit.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RepostsFragment extends Fragment {

    private RecyclerView rvPosts;
    private PostAdapter postAdapter;
    private List<PostResponse> posts = new ArrayList<>();
    private long userId;  // Thêm trường userId

    // Thêm phương thức newInstance để khởi tạo RepostsFragment với userId
    public static RepostsFragment newInstance(long userId) {
        RepostsFragment fragment = new RepostsFragment();
        Bundle args = new Bundle();
        args.putLong("user_id", userId);  // Đưa userId vào Bundle
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userId = getArguments().getLong("user_id", -1);  // Lấy userId từ arguments
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reposts, container, false);

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

        // Giữ nguyên cách sử dụng username từ sessionManager thay vì userId
        Call<List<RepostResponse>> call = apiService.getMyReposts(user.getUsername());

        call.enqueue(new Callback<List<RepostResponse>>() {
            @Override
            public void onResponse(Call<List<RepostResponse>> call, Response<List<RepostResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    posts.clear();
                    for (RepostResponse repost : response.body()) {
                        // Convert từng RepostResponse -> PostResponse để tái sử dụng PostAdapter
                        PostResponse post = new PostResponse();
                        post.setPostId(repost.getPostId());
                        post.setContent(repost.getContent());
                        post.setMediaUrls(new ArrayList<>(repost.getMediaUrls()));
                        post.setVisibility(repost.getVisibility());
                        post.setCreatedAt(repost.getCreatedAt());
                        post.setUser(repost.getUser()); // Lưu ý: đây là người đăng gốc, không phải người repost

                        posts.add(post);
                    }
                    postAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getContext(), "Lỗi khi tải bài repost!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<RepostResponse>> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi kết nối!", Toast.LENGTH_SHORT).show();
            }
        });
    }

}

//public class RepostsFragment extends Fragment {
//
//    private RecyclerView rvPosts;
//    private PostAdapter postAdapter;
//    private List<PostResponse> posts = new ArrayList<>();
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        View view = inflater.inflate(R.layout.fragment_reposts, container, false);
//
//        // Setup RecyclerView
//        rvPosts = view.findViewById(R.id.rvPostsPersonal);
//        rvPosts.setLayoutManager(new LinearLayoutManager(getContext()));
//
//        postAdapter = new PostAdapter(getContext(), posts);
//        rvPosts.setAdapter(postAdapter);
//
//        // Gọi hàm loadUserPosts để tải dữ liệu
//        loadUserPosts();
//
//        return view;
//    }
//
//    private void loadUserPosts() {
//        UserSessionManager sessionManager = new UserSessionManager(requireContext());
//        User user = sessionManager.getUser();
//        ApiService apiService = RetrofitClient.getApiService();
//
//        Call<List<RepostResponse>> call = apiService.getMyReposts(user.getUsername());
//
//        call.enqueue(new Callback<List<RepostResponse>>() {
//            @Override
//            public void onResponse(Call<List<RepostResponse>> call, Response<List<RepostResponse>> response) {
//                if (response.isSuccessful() && response.body() != null) {
//                    posts.clear();
//                    for (RepostResponse repost : response.body()) {
//                        // Convert từng RepostResponse -> PostResponse để tái sử dụng PostAdapter
//                        PostResponse post = new PostResponse();
//                        post.setPostId(repost.getPostId());
//                        post.setContent(repost.getContent());
//                        post.setMediaUrls(new ArrayList<>(repost.getMediaUrls()));
//                        post.setVisibility(repost.getVisibility());
//                        post.setCreatedAt(repost.getCreatedAt());
//                        post.setUser(repost.getUser()); // Lưu ý: đây là người đăng gốc, không phải người repost
//
//                        posts.add(post);
//                    }
//                    postAdapter.notifyDataSetChanged();
//                } else {
//                    Toast.makeText(getContext(), "Lỗi khi tải bài repost!", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<RepostResponse>> call, Throwable t) {
//                Toast.makeText(getContext(), "Lỗi kết nối!", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//}