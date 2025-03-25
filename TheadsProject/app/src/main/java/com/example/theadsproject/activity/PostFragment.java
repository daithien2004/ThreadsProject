package com.example.theadsproject.activity;

import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.theadsproject.DTO.PostResponse;
import com.example.theadsproject.DTO.UserResponse;
import com.example.theadsproject.R;
import com.example.theadsproject.adapter.ImageAdapter;
import com.example.theadsproject.retrofit.ApiService;
import com.example.theadsproject.retrofit.RetrofitClient;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostFragment extends Fragment {
    private List<Object> imageList = new ArrayList<>();
    private ImageAdapter imageAdapter;
    private ActivityResultLauncher<Intent> pickImagesLauncher;
    private EditText edtPostContent;
    private Button btnPost ;
    private RecyclerView rvImages;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Xử lý chọn ảnh từ thư viện
        pickImagesLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        ClipData clipData = result.getData().getClipData();
                        if (clipData != null) {
                            for (int i = 0; i < clipData.getItemCount(); i++) {
                                imageList.add(clipData.getItemAt(i).getUri());
                            }
                        } else {
                            Uri imageUri = result.getData().getData();
                            if (imageUri != null) {
                                imageList.add(imageUri);
                            }
                        }
                        imageAdapter.notifyDataSetChanged();
                        updateRecyclerViewVisibility();
                    }
                }
        );
//        // ẩn thanh bar
//        requireActivity().getWindow().getDecorView().setSystemUiVisibility(
//                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
//                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post, container, false);

        rvImages = view.findViewById(R.id.rvImages);
        rvImages.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        rvImages.setVisibility(View.GONE); // Ẩn ban đầu

        edtPostContent = view.findViewById(R.id.edtPostContent);
        btnPost = view.findViewById(R.id.btnPost);

        // Khởi tạo Adapter
        imageAdapter = new ImageAdapter(requireContext(), imageList);
        rvImages.setAdapter(imageAdapter);

        view.findViewById(R.id.imGallery).setOnClickListener(v -> openGallery());
        btnPost.setOnClickListener(v -> uploadPost());

        // Lắng nghe thay đổi trong EditText
        edtPostContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updatePostButtonState();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Lắng nghe thay đổi trong danh sách ảnh
        imageAdapter.setOnDataChangedListener(this::updatePostButtonState);

        // Ẩn BottomAppBar & BottomNavigationView
        if (getActivity() != null) {
            BottomAppBar bottomAppBar = getActivity().findViewById(R.id.bottomAppBar);
            BottomNavigationView bottomNav = getActivity().findViewById(R.id.bottomNavigationView);

            if (bottomAppBar != null) bottomAppBar.setVisibility(View.GONE);
            if (bottomNav != null) bottomNav.setVisibility(View.GONE);
        }

        // Xử lý khi nhấn nút đóng (imgClose)
        ImageView imgClose = view.findViewById(R.id.imgClose);
        imgClose.setOnClickListener(v -> {
            if (getActivity() != null) {
                BottomAppBar bottomAppBar = getActivity().findViewById(R.id.bottomAppBar);
                BottomNavigationView bottomNav = getActivity().findViewById(R.id.bottomNavigationView);

                if (bottomAppBar != null) bottomAppBar.setVisibility(View.VISIBLE);
                if (bottomNav != null) bottomNav.setVisibility(View.VISIBLE);
            }

            // Quay lại HomeFragment
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frame_layout, new HomeFragment())
                    .commit();
        });

        return view;
    }


    // Mở thư viện ảnh để chọn nhiều ảnh
    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setType("image/*");
        pickImagesLauncher.launch(intent);
    }
    private void updatePostButtonState() {
        boolean hasText = !edtPostContent.getText().toString().trim().isEmpty();
        boolean hasImages = imageAdapter.getItemCount() > 0;

        if (hasText || hasImages) {
            btnPost.setBackgroundTintList(ContextCompat.getColorStateList(getContext(), R.color.blue)); // Chuyển sang màu xanh biển
        } else {
            btnPost.setBackgroundTintList(ContextCompat.getColorStateList(getContext(), R.color.gray)); // Chuyển về màu xám
        }
    }

    // Cập nhật hiển thị RecyclerView
    private void updateRecyclerViewVisibility() {
        if (rvImages != null) {
            rvImages.setVisibility(imageList.isEmpty() ? View.GONE : View.VISIBLE);
        }
    }

    // Xóa ảnh khỏi danh sách
    public void removeImage(int position) {
        imageList.remove(position);
        imageAdapter.notifyDataSetChanged();
        updateRecyclerViewVisibility();
    }

    // Gửi bài viết lên server
    private void uploadPost() {
        String content = edtPostContent.getText().toString().trim();
        if (content.isEmpty() && imageList.isEmpty()) {
            Toast.makeText(requireContext(), "Nội dung hoặc ảnh không được để trống!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Chuyển Uri thành String
        List<String> mediaUrls = new ArrayList<>();
        for (Object obj : imageList) {
            if (obj instanceof Uri) {
                mediaUrls.add(obj.toString());
            } else if (obj instanceof String) {
                mediaUrls.add((String) obj);
            }
        }

        // Test: Thêm một ảnh giả
        mediaUrls.add("https://gamek.mediacdn.vn/133514250583805952/2021/7/20/photo-1-1626771137936858492623.jpg");

        // Tạo request
        UserResponse user = new UserResponse(9L);
        PostResponse postResponse = new PostResponse(content, mediaUrls, "public", user);

        // Log JSON request
        Gson gson = new Gson();
        Log.d("UPLOAD_POST", "Request JSON: " + gson.toJson(postResponse));

        // Gửi request lên server
        ApiService apiService = RetrofitClient.getApiService();
        Call<Void> call = apiService.createPost(postResponse);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.d("UPLOAD_POST", "Response Code: " + response.code());
                if (response.isSuccessful()) {
                    Toast.makeText(requireContext(), "Đăng bài thành công!", Toast.LENGTH_SHORT).show();
                    goToHomeFragment();
                } else {
                    try {
                        String errorBody = response.errorBody() != null ? response.errorBody().string() : "Không có lỗi cụ thể";
                        Log.e("UPLOAD_POST", "Lỗi khi đăng bài: " + errorBody);
                    } catch (Exception e) {
                        Log.e("UPLOAD_POST", "Lỗi khi đọc errorBody", e);
                    }
                    Toast.makeText(requireContext(), "Lỗi khi đăng bài!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("UPLOAD_POST", "Lỗi kết nối: " + t.getMessage());
                Toast.makeText(requireContext(), "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Chuyển về HomeFragment sau khi đăng bài thành công
    private void goToHomeFragment() {
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_layout, new HomeFragment())
                .commit();
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();

        BottomAppBar bottomAppBar = getActivity().findViewById(R.id.bottomAppBar);
        BottomNavigationView bottomNav = getActivity().findViewById(R.id.bottomNavigationView);

        if (bottomAppBar != null) bottomAppBar.setVisibility(View.VISIBLE);
        if (bottomNav != null) bottomNav.setVisibility(View.VISIBLE);
    }

}
