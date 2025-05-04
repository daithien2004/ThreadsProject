package com.example.theadsproject.activityPost;

import android.app.ProgressDialog;
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
import com.example.theadsproject.commonClass.FileUtil;
import com.example.theadsproject.commonClass.ImagePickerHelper;
import com.example.theadsproject.commonClass.TimeUtils;
import com.example.theadsproject.config.CloudinaryConfig;
import com.example.theadsproject.dto.CommentRequest;
import com.example.theadsproject.dto.CommentResponse;
import com.example.theadsproject.dto.PostRequest;
import com.example.theadsproject.dto.PostResponse;
import com.example.theadsproject.dto.UserResponse;
import com.example.theadsproject.entity.User;
import com.example.theadsproject.handler.PostLikeHandler;
import com.example.theadsproject.retrofit.ApiService;
import com.example.theadsproject.retrofit.RetrofitClient;
import com.example.theadsproject.service.ImageUploadService;
import com.google.gson.Gson;

import java.io.File;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

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
    private ImageButton ibSend, ibImage;
    private ActivityResultLauncher<Intent> pickImagesLauncher;
    private PostLikeHandler postLikeHandler = new PostLikeHandler();

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

        ibImage = findViewById(R.id.ibImage);
        ibImage.setOnClickListener(v -> ImagePickerHelper.openGallery(this, pickImagesLauncher));

        ibSend = findViewById(R.id.ibSend);
        ibSend.setOnClickListener(v -> uploadComment(postId));

        pickImagesLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        List<Uri> imageUris = ImagePickerHelper.handleGalleryResult(result.getData());
                        selectedImages.addAll(imageUris);
                        imageAdapter.notifyDataSetChanged();
                        rvSelectedImages.setVisibility(View.VISIBLE);
                    }
                }
        );
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
                    if (!PostDetailActivity.this.isFinishing() && !PostDetailActivity.this.isDestroyed()) {
                        PostResponse post = response.body();
                        View includeView = findViewById(R.id.icPost); // view include
                        PostItemView postItemView = new PostItemView(includeView, PostDetailActivity.this, postLikeHandler);
                        postItemView.bind(post, PostDetailActivity.this);
                    }

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

        // Hiển thị loading dialog
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Đang tải lên...");
        progressDialog.show();

        // Tạo list để lưu URL ảnh từ Cloudinary
        ArrayList<String> mediaUrls = new ArrayList<>();
        CountDownLatch latch = new CountDownLatch(selectedImages.size());

        // Upload từng ảnh lên Cloudinary
        for (Object obj : selectedImages) {
            if (obj instanceof Uri) {
                Uri imageUri = (Uri) obj;
                ImageUploadService.uploadImage(this, imageUri, new ImageUploadService.UploadCallback() {
                    @Override
                    public void onSuccess(String imageUrl) {
                        mediaUrls.add(imageUrl); // Đây sẽ là Cloudinary URL
                        latch.countDown();
                    }

                    @Override
                    public void onFailure(String error) {
                        latch.countDown();
                        runOnUiThread(() -> {
                            Toast.makeText(PostDetailActivity.this,
                                    "Lỗi upload ảnh: " + error, Toast.LENGTH_SHORT).show();
                        });
                    }
                });
            }
        }

        // Chờ tất cả ảnh upload xong rồi mới tạo comment
        new Thread(() -> {
            try {
                latch.await();
                runOnUiThread(() -> {
                    // Lấy userId từ session
                    UserSessionManager sessionManager = new UserSessionManager(this);
                    User user = sessionManager.getUser();

                    if (user == null) {
                        progressDialog.dismiss();
                        Toast.makeText(this, "Không tìm thấy thông tin người dùng!",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Tạo request với URL ảnh từ Cloudinary
                    CommentRequest request = new CommentRequest(content, mediaUrls, "public",
                            user.getUserId(), postId, null);

                    // Gọi API tạo comment
                    ApiService apiService = RetrofitClient.getApiService();
                    Call<Void> call = apiService.createComment(request);
                    call.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            progressDialog.dismiss();
                            if (response.isSuccessful()) {
                                Toast.makeText(PostDetailActivity.this,
                                        "Bình luận thành công!", Toast.LENGTH_SHORT).show();

                                // Clear form
                                etComment.setText("");
                                selectedImages.clear();
                                imageAdapter.notifyDataSetChanged();
                                rvSelectedImages.setVisibility(View.GONE);

                                // Reload comments
                                loadPostComments(postId);
                            } else {
                                Toast.makeText(PostDetailActivity.this,
                                        "Lỗi khi bình luận!", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            progressDialog.dismiss();
                            Toast.makeText(PostDetailActivity.this,
                                    "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
                runOnUiThread(() -> {
                    progressDialog.dismiss();
                    Toast.makeText(PostDetailActivity.this,
                            "Lỗi xử lý ảnh", Toast.LENGTH_SHORT).show();
                });
            }
        }).start();
    }
}