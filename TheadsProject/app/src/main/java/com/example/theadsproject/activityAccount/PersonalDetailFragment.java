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
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.theadsproject.R;
import com.example.theadsproject.UserSessionManager;
import com.example.theadsproject.activityHome.BarActivity;
import com.example.theadsproject.dto.UserResponse;
import com.example.theadsproject.retrofit.ApiService;
import com.example.theadsproject.retrofit.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PersonalDetailFragment extends Fragment {
    private static final String ARG_USER_ID = "arg_user_id";

    private long viewedUserId;

    private TextView tvName, tvDescription, tvBio, tvFollower;
    private ImageView ivAvatar, imSetting;
    private AppCompatButton btnEditProfile;

    public PersonalDetailFragment() {
        super(R.layout.fragment_personal_detail);
    }

    /**
     * Factory để tạo instance với userId cụ thể
     */
    public static PersonalDetailFragment newInstance(long userId) {
        Bundle args = new Bundle();
        args.putLong(ARG_USER_ID, userId);
        PersonalDetailFragment frag = new PersonalDetailFragment();
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Nếu có truyền userId thì dùng, không thì lấy user đăng nhập
        if (getArguments() != null && getArguments().containsKey(ARG_USER_ID)) {
            viewedUserId = getArguments().getLong(ARG_USER_ID);
        } else {
            viewedUserId = new UserSessionManager(requireContext())
                    .getUser()
                    .getUserId();
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Ánh xạ
        tvName        = view.findViewById(R.id.tvName);
        tvDescription = view.findViewById(R.id.tvDescription);
        tvBio         = view.findViewById(R.id.tvBio);
        tvFollower    = view.findViewById(R.id.tvFollower);
        ivAvatar      = view.findViewById(R.id.ivAvatar);
        imSetting     = view.findViewById(R.id.imSetting);
        btnEditProfile= view.findViewById(R.id.btnEditProfile);

        // Load phần header (profile info)
        loadProfileData();

        // Thêm Tab fragment vào FrameLayout
        FrameLayout frame = view.findViewById(R.id.frame_layout);
        if (savedInstanceState == null) {
            FragmentTransaction ft = getChildFragmentManager().beginTransaction();
            TabPersonalDetailFragment tabFragment = TabPersonalDetailFragment.newInstance(viewedUserId);
            ft.replace(R.id.frame_layout, tabFragment);
            ft.commit();
        }

        // Nút Setting (chỉ hiện khi là profile của chính user)
        if (viewedUserId == new UserSessionManager(requireContext()).getUser().getUserId()) {
            imSetting.setOnClickListener(v ->
                    startActivity(new Intent(requireContext(), SettingActivity.class))
            );
            btnEditProfile.setOnClickListener(v ->
                    startActivity(new Intent(requireContext(), EditPersonalPageActivity.class))
            );
        } else {
            imSetting.setVisibility(View.GONE);
            btnEditProfile.setVisibility(View.GONE);
        }
    }

    /**
     * Gọi API lấy user info & follower count
     */
    private void loadProfileData() {
        ApiService api = RetrofitClient.getApiService();

        // 1. Lấy profile của user bất kỳ
        api.getUserById(viewedUserId).enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> c, Response<UserResponse> r) {
                if (!r.isSuccessful() || r.body() == null) {
                    Toast.makeText(getContext(), "Không lấy được profile", Toast.LENGTH_SHORT).show();
                    return;
                }
                UserResponse u = r.body();
                tvName.setText(u.getUsername());
                tvDescription.setText(u.getNickName());
                tvBio.setText(u.getBio());
                Glide.with(PersonalDetailFragment.this)
                        .load(u.getImage())
                        .apply(RequestOptions.circleCropTransform())
                        .into(ivAvatar);

                // 2. Lấy follower count
                api.getFollowerCount(viewedUserId).enqueue(new Callback<Long>() {
                    @Override
                    public void onResponse(Call<Long> c2, Response<Long> r2) {
                        if (r2.isSuccessful() && r2.body() != null) {
                            tvFollower.setText(r2.body() + " người theo dõi");
                        } else {
                            tvFollower.setText("0 người theo dõi");
                        }
                    }
                    @Override public void onFailure(Call<Long> c2, Throwable t) {
                        tvFollower.setText("0 người theo dõi");
                        Log.e("PersonalDetail", "Lỗi followerCount", t);
                    }
                });
            }
            @Override
            public void onFailure(Call<UserResponse> c, Throwable t) {
                Toast.makeText(getContext(), "Lỗi kết nối!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
