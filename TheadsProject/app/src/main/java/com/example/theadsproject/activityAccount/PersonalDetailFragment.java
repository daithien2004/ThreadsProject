package com.example.theadsproject.activityAccount;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import android.widget.Button;
import android.widget.FrameLayout;

import android.widget.ImageView;
import android.widget.TextView;

import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

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


public class PersonalDetailFragment extends Fragment {

    private RecyclerView rvPosts;
    private PostAdapter postAdapter;
    private List<PostResponse> posts = new ArrayList<>();
    private TextView tvName, tvDescription, tvBio, tvFollower;
    private ImageView ivAvatar;

    public PersonalDetailFragment() {
        super(R.layout.fragment_personal_detail);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FrameLayout frameLayout = view.findViewById(R.id.frame_layout);
        if (savedInstanceState == null) {
            getChildFragmentManager().beginTransaction()
                    .replace(R.id.frame_layout, new TabPersonalDetailFragment()) // Thay thế với TabPersonalDetailFragment
                    .commit();
        }

        tvName = view.findViewById(R.id.tvName);
        tvDescription = view.findViewById(R.id.tvDescription);
        tvBio = view.findViewById(R.id.tvBio);
        ivAvatar = view.findViewById(R.id.ivAvatar);
        tvFollower = view.findViewById(R.id.tvFollower);

        // Lấy thông tin user từ UserSessionManager
        UserSessionManager sessionManager = new UserSessionManager(requireContext());
        User user = sessionManager.getUser();

        if (user != null) {
            tvName.setText(user.getUsername());
            tvDescription.setText(user.getNickName());
            tvBio.setText(user.getBio());

            // Load ảnh đại diện (Sử dụng Glide hoặc Picasso)
            if (user.getImage() != null && !user.getImage().isEmpty()) {
                Glide.with(this).load(user.getImage()).apply(RequestOptions.circleCropTransform()).into(ivAvatar);
            }
        } else {
            Toast.makeText(getContext(), "Không tìm thấy thông tin người dùng!", Toast.LENGTH_SHORT).show();
        }

        ApiService apiService = RetrofitClient.getApiService();
        apiService.getFollowerCount(user.getUserId()).enqueue(new Callback<Long>() {
            @Override
            public void onResponse(Call<Long> call, Response<Long> response) {
                if (response.isSuccessful()) {
                    tvFollower.setText(response.body().toString() + " người theo dõi");
                } else {
                    tvFollower.setText("0");
                }
            }

            @Override
            public void onFailure(Call<Long> call, Throwable t) {
                Log.e("API_ERROR", "Lỗi kết nối: " + t.getMessage());
            }
        });


        ImageView imgMenu = view.findViewById(R.id.imSetting);
        imgMenu.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), SettingActivity.class);
            startActivity(intent);
        });

        Button btEdit = view.findViewById(R.id.btnEditProfile);
        btEdit.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), EditPersonalPageActivity.class);
            startActivity(intent);
        });
    }
    @Override
    public void onResume() {
        super.onResume();
        // Lấy thông tin user mới nhất từ SessionManager
        UserSessionManager sessionManager = new UserSessionManager(requireContext());
        User user = sessionManager.getUser();

        if (user != null) {
            tvName.setText(user.getUsername());
            tvDescription.setText(user.getNickName());
            tvBio.setText(user.getBio());

            if (user.getImage() != null && !user.getImage().isEmpty()) {
                Glide.with(this).load(user.getImage()).apply(RequestOptions.circleCropTransform()).into(ivAvatar);
            }
        }
    }



}

