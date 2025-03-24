package com.example.theadsproject.activity;

import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.theadsproject.DTO.PostResponse;
import com.example.theadsproject.DTO.UserResponse;
import com.example.theadsproject.R;
import com.example.theadsproject.adapter.ImageAdapter;
import com.example.theadsproject.entity.User;
import com.example.theadsproject.retrofit.ApiService;
import com.example.theadsproject.retrofit.RetrofitClient;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostFragment extends Fragment {
    private List<Object> imageList = new ArrayList<>(); // Chứa cả Uri và String
    private ImageAdapter imageAdapter;
    private ActivityResultLauncher<Intent> pickImagesLauncher;
    private EditText edtContent;
    private Button btnPost;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
                    }
                }
        );
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post, container, false);

        RecyclerView rvImages = view.findViewById(R.id.rvImages);
        rvImages.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));

        edtContent = view.findViewById(R.id.edtPostContent);
        btnPost = view.findViewById(R.id.btnPost);

        // Khởi tạo Adapter với danh sách ảnh hỗn hợp (Uri và String)
        imageAdapter = new ImageAdapter(requireContext(), imageList);
        rvImages.setAdapter(imageAdapter);

        view.findViewById(R.id.imGallery).setOnClickListener(v -> openGallery());
        btnPost.setOnClickListener(v -> uploadPost());

        return view;
    }

    // Mở thư viện ảnh để chọn nhiều ảnh
    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setType("image/*");
        pickImagesLauncher.launch(intent);
    }

    // Thêm ảnh từ web vào danh sách
    public void addImageFromWeb(String imageUrl) {
        imageList.add(imageUrl);
        imageAdapter.notifyDataSetChanged();
    }

    // Gửi bài viết lên server
    private void uploadPost() {
        String content = edtContent.getText().toString().trim();
        if (content.isEmpty() && imageList.isEmpty()) {
            Toast.makeText(requireContext(), "Nội dung hoặc ảnh không được để trống!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Chuyển Uri thành String
        List<String> mediaUrls = new ArrayList<>();
        for (Object obj : imageList) {
            if (obj instanceof Uri) {
                mediaUrls.add(obj.toString());  // Sử dụng Uri.toString()
            } else if (obj instanceof String) {
                mediaUrls.add((String) obj);
            }
        }

        // Chuẩn bị request
        UserResponse user = new UserResponse(9L);
        mediaUrls.add("https://gamek.mediacdn.vn/133514250583805952/2021/7/20/photo-1-1626771137936858492623.jpg"); // Ảnh giả

        PostResponse postResponse = new PostResponse(content, mediaUrls, "public", user);

        // ✅ Log dữ liệu JSON trước khi gửi request
        Gson gson = new Gson();
        String json = gson.toJson(postResponse);
        Log.d("UPLOAD_POST", "Request JSON: " + json);

        // 🔥 Thêm log kiểm tra JSON request
        gson = new Gson();
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
}
