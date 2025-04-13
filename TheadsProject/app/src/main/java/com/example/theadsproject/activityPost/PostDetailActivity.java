package com.example.theadsproject.activityPost;

import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.theadsproject.R;
import com.example.theadsproject.UserSessionManager;
import com.example.theadsproject.activityCommon.LoginActivity;
import com.example.theadsproject.activityCommon.RegisterActivity;
import com.example.theadsproject.activityHome.BarActivity;
import com.example.theadsproject.adapter.CommentAdapter;
import com.example.theadsproject.adapter.ImageAdapter;
import com.example.theadsproject.adapter.PostAdapter;
import com.example.theadsproject.commonClass.TimeUtils;
import com.example.theadsproject.dto.CommentRequest;
import com.example.theadsproject.dto.CommentResponse;
import com.example.theadsproject.dto.PostRequest;
import com.example.theadsproject.dto.PostResponse;
import com.example.theadsproject.dto.UserResponse;
import com.example.theadsproject.entity.User;
import com.example.theadsproject.retrofit.ApiService;
import com.example.theadsproject.retrofit.RetrofitClient;
import com.google.gson.Gson;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostDetailActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private CommentAdapter commentAdapter;
    private List<CommentResponse> commentList;
    private ImageView ivBack;

    private List<Object> selectedImages = new ArrayList<>();
    private ImageAdapter imageAdapter;
    private RecyclerView rvSelectedImages;
    private EditText etComment;
    private ImageButton ibSend;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_post_detail);

        ivBack = findViewById(R.id.ivBack);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PostDetailActivity.this, BarActivity.class);
                startActivity(intent);
                finish();
            }
        });

        recyclerView = findViewById(R.id.rvComments);
        recyclerView.setLayoutManager(new LinearLayoutManager(PostDetailActivity.this));

        Long postId = getIntent().getLongExtra("postId", -1);
        if (postId != -1) {
            loadPostDetail(postId);
            loadPostComments(postId);
        } else {
            Toast.makeText(this, "Không tìm thấy bài viết", Toast.LENGTH_SHORT).show();
            finish();
        }

        rvSelectedImages = findViewById(R.id.rvSelectedImages);
        rvSelectedImages.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        imageAdapter = new ImageAdapter(this, selectedImages);
        rvSelectedImages.setAdapter(imageAdapter);

        // Ẩn RecyclerView nếu chưa có ảnh
        rvSelectedImages.setVisibility(View.GONE);

        etComment = findViewById(R.id.etComment);

        ImageButton ibImage = findViewById(R.id.ibImage);
        ibImage.setOnClickListener(v -> openGallery());

        ImageButton ibSend = findViewById(R.id.ibSend);
        ibSend.setOnClickListener(v -> uploadComment(postId));
    }

    private ActivityResultLauncher<Intent> pickImagesLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    ClipData clipData = result.getData().getClipData();
                    if (clipData != null) {
                        for (int i = 0; i < clipData.getItemCount(); i++) {
                            selectedImages.add(clipData.getItemAt(i).getUri());
                        }
                    } else {
                        Uri imageUri = result.getData().getData();
                        if (imageUri != null) {
                            selectedImages.add(imageUri);
                        }
                    }
                    imageAdapter.notifyDataSetChanged();
                    rvSelectedImages.setVisibility(View.VISIBLE);
                }
            }
    );

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setType("image/*");
        pickImagesLauncher.launch(intent);
    }

    private void loadPostComments(Long postId) {
        ApiService apiService = RetrofitClient.getApiService();
        Call<List<CommentResponse>> call = apiService.getPostComments(postId);
        call.enqueue(new Callback<List<CommentResponse>>() {
            @Override
            public void onResponse(Call<List<CommentResponse>> call, Response<List<CommentResponse>> response) {
                if (response.isSuccessful()) {
                    commentList = response.body();
                    commentAdapter = new CommentAdapter(PostDetailActivity.this, commentList);
                    recyclerView.setAdapter(commentAdapter);
                }
                else {
                    Log.e("API_ERROR", "Không lấy được dữ liệu");
                }
            }

            @Override
            public void onFailure(Call<List<CommentResponse>> call, Throwable t) {
                Log.e("API_ERROR", "Lỗi kết nối: " + t.getMessage());
            }
        });
    }

    private void loadPostDetail(Long postId) {
        ApiService apiService = RetrofitClient.getApiService();
        apiService.getPostById(postId).enqueue(new Callback<PostResponse>() {
            @Override
            public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    PostResponse post = response.body();
                    View includeView = findViewById(R.id.icPost); // view include
                    PostItemView postItemView = new PostItemView(includeView);
                    postItemView.bind(post, PostDetailActivity.this);
                } else {
                    Log.e("API_ERROR", "Không load được chi tiết post");
                }
            }

            @Override
            public void onFailure(Call<PostResponse> call, Throwable t) {
                Log.e("API_ERROR", "Lỗi kết nối: " + t.getMessage());
            }
        });
    }

    private void uploadComment(Long postId) {
        String content = etComment.getText().toString().trim();
        if (content.isEmpty() && selectedImages.isEmpty()) {
            Toast.makeText(this, "Nội dung hoặc ảnh không được để trống!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Chuyển Uri thành String
        ArrayList<String> mediaUrls = new ArrayList<>();
        for (Object obj : selectedImages) {
            if (obj instanceof Uri) {
                mediaUrls.add(obj.toString());  // Bạn sẽ cần xử lý upload thực tế sau
            } else if (obj instanceof String) {
                mediaUrls.add((String) obj);
            }
        }

        // Lấy userId từ session
        UserSessionManager sessionManager = new UserSessionManager(this);
        User user = sessionManager.getUser();

        if (user == null) {
            Toast.makeText(this, "Không tìm thấy thông tin người dùng!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Tạo request
        CommentRequest request = new CommentRequest(content, mediaUrls, "public", user.getUserId(), postId);

        // Log JSON
        Gson gson = new Gson();
        Log.d("UPLOAD_COMMENT", "Request JSON: " + gson.toJson(request));

        // Gọi API
        ApiService apiService = RetrofitClient.getApiService();
        Call<Void> call = apiService.createComment(request);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(PostDetailActivity.this, "Bình luận thành công!", Toast.LENGTH_SHORT).show();
                    // Clear nội dung và ảnh sau khi gửi
                    etComment.setText("");
                    selectedImages.clear();
                    imageAdapter.notifyDataSetChanged();
                    rvSelectedImages.setVisibility(View.GONE);

                    loadPostComments(postId);

                } else {
                    try {
                        String error = response.errorBody() != null ? response.errorBody().string() : "Lỗi không xác định";
                        Log.e("UPLOAD_COMMENT", "Lỗi: " + error);
                    } catch (Exception e) {
                        Log.e("UPLOAD_COMMENT", "Lỗi đọc errorBody", e);
                    }
                    Toast.makeText(PostDetailActivity.this, "Lỗi khi bình luận!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("UPLOAD_COMMENT", "Lỗi kết nối: " + t.getMessage());
                Toast.makeText(PostDetailActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}