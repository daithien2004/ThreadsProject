package com.example.theadsproject.activityPost;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.theadsproject.R;
import com.example.theadsproject.UserSessionManager;
import com.example.theadsproject.adapter.CommentAdapter;
import com.example.theadsproject.adapter.ImageAdapter;
import com.example.theadsproject.commonClass.TimeUtils;
import com.example.theadsproject.dto.CommentRequest;
import com.example.theadsproject.dto.CommentResponse;
import com.example.theadsproject.entity.User;
import com.example.theadsproject.retrofit.ApiService;
import com.example.theadsproject.retrofit.RetrofitClient;
import com.example.theadsproject.service.ImageUploadService;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentDetailActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private CommentAdapter commentAdapter;
    private List<CommentResponse> commentList;
    private ImageView ivBack;

    private List<Object> selectedImages = new ArrayList<>();
    private ImageAdapter imageAdapter;
    private RecyclerView rvSelectedImages;
    private EditText etComment;
    private ImageButton ibSend;
    Long postId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_detail);

        // Khởi tạo views
        initViews();

        // Lấy commentId từ intent
        Long commentId = getIntent().getLongExtra("commentId", -1);
        postId = getIntent().getLongExtra("postId", -1);
        if (commentId != -1) {
            loadCommentDetail(commentId);
            loadCommentReplies(commentId);
        } else {
            Toast.makeText(this, "Không tìm thấy bình luận", Toast.LENGTH_SHORT).show();
            finish();
        }

        setupListeners();
    }

    private void initViews() {
        ivBack = findViewById(R.id.ivBack);
        recyclerView = findViewById(R.id.rvReplies);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        rvSelectedImages = findViewById(R.id.rvSelectedImages);
        rvSelectedImages.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        imageAdapter = new ImageAdapter(this, selectedImages);
        rvSelectedImages.setAdapter(imageAdapter);

        etComment = findViewById(R.id.etComment);
        ibSend = findViewById(R.id.ibSend);
    }

    private void loadCommentDetail(Long commentId) {
        ApiService apiService = RetrofitClient.getApiService();
        apiService.getCommentById(commentId).enqueue(new Callback<CommentResponse>() {
            @Override
            public void onResponse(Call<CommentResponse> call, Response<CommentResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    CommentResponse comment = response.body();
                    View includeView = findViewById(R.id.commentDetail);
                    bindCommentData(includeView, comment);
                }
            }

            @Override
            public void onFailure(Call<CommentResponse> call, Throwable t) {
                Toast.makeText(CommentDetailActivity.this,
                        "Lỗi khi tải bình luận", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void bindCommentData(View view, CommentResponse comment) {
        ImageView imgAvatar = view.findViewById(R.id.ivUserAvatar);
        TextView tvNickname = view.findViewById(R.id.tvNickname);
        TextView tvContent = view.findViewById(R.id.tvTextPost);
        TextView tvTime = view.findViewById(R.id.tvTimePost);
        RecyclerView rvImages = view.findViewById(R.id.rvImages);

        // Set data
        tvNickname.setText(comment.getUser().getNickName());
        tvContent.setText(comment.getContent());

        // Load avatar
        Glide.with(this)
                .load(comment.getUser().getImage())
                .apply(RequestOptions.circleCropTransform())
                .into(imgAvatar);

        // Set time
        long timestampMillis = comment.getCreateAt().atZone(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli();
        if (comment.getCreateAt() != null) {
            String timeAgo = TimeUtils.getTimeAgo(timestampMillis);
            tvTime.setText(timeAgo);
        }

        // Load images if any
        if (comment.getMediaUrls() != null && !comment.getMediaUrls().isEmpty()) {
            rvImages.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            ImageAdapter imageAdapter = new ImageAdapter(this, new ArrayList<>(comment.getMediaUrls()));
            rvImages.setAdapter(imageAdapter);
            rvImages.setVisibility(View.VISIBLE);
        } else {
            rvImages.setVisibility(View.GONE);
        }
    }

    private void loadCommentReplies(Long commentId) {
        ApiService apiService = RetrofitClient.getApiService();
        apiService.getCommentReplies(commentId).enqueue(new Callback<List<CommentResponse>>() {
            @Override
            public void onResponse(Call<List<CommentResponse>> call, Response<List<CommentResponse>> response) {
                if (response.isSuccessful()) {
                    commentList = response.body();
                    commentAdapter = new CommentAdapter(CommentDetailActivity.this, commentList);
                    recyclerView.setAdapter(commentAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<CommentResponse>> call, Throwable t) {
                Toast.makeText(CommentDetailActivity.this,
                        "Lỗi khi tải phản hồi", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupListeners() {
        ivBack.setOnClickListener(v -> finish());

        ImageButton ibImage = findViewById(R.id.ibImage);
        ibImage.setOnClickListener(v -> openGallery());

        ibSend.setOnClickListener(v -> {
            Long commentId = getIntent().getLongExtra("commentId", -1);
            if (commentId != -1) {
                uploadReply(commentId);
            }
        });
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setType("image/*");
        pickImagesLauncher.launch(intent);
    }

    private ActivityResultLauncher<Intent> pickImagesLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    handleGalleryResult(result.getData());
                }
            }
    );

    private void handleGalleryResult(Intent data) {
        ClipData clipData = data.getClipData();
        if (clipData != null) {
            for (int i = 0; i < clipData.getItemCount(); i++) {
                selectedImages.add(clipData.getItemAt(i).getUri());
            }
        } else {
            Uri imageUri = data.getData();
            if (imageUri != null) {
                selectedImages.add(imageUri);
            }
        }
        imageAdapter.notifyDataSetChanged();
        rvSelectedImages.setVisibility(View.VISIBLE);
    }

    private void uploadReply(Long parentCommentId) {
        String content = etComment.getText().toString().trim();
        if (content.isEmpty() && selectedImages.isEmpty()) {
            Toast.makeText(this, "Nội dung hoặc ảnh không được để trống!", Toast.LENGTH_SHORT).show();
            return;
        }

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Đang tải lên...");
        progressDialog.show();

        // Upload images and create reply
        uploadImagesAndCreateReply(content, parentCommentId, progressDialog);
    }

    private void uploadImagesAndCreateReply(String content, Long parentCommentId, ProgressDialog progressDialog) {
        ArrayList<String> mediaUrls = new ArrayList<>();
        CountDownLatch latch = new CountDownLatch(selectedImages.size());

        for (Object obj : selectedImages) {
            if (obj instanceof Uri) {
                uploadImageToCloudinary((Uri) obj, mediaUrls, latch);
            }
        }

        new Thread(() -> {
            try {
                latch.await();
                runOnUiThread(() -> createReplyWithUrls(content, parentCommentId, mediaUrls, progressDialog));
            } catch (InterruptedException e) {
                e.printStackTrace();
                runOnUiThread(() -> handleUploadError(progressDialog));
            }
        }).start();
    }

    private void uploadImageToCloudinary(Uri imageUri, ArrayList<String> mediaUrls, CountDownLatch latch) {
        ImageUploadService.uploadImage(this, imageUri, new ImageUploadService.UploadCallback() {
            @Override
            public void onSuccess(String imageUrl) {
                mediaUrls.add(imageUrl);
                latch.countDown();
            }

            @Override
            public void onFailure(String error) {
                latch.countDown();
                runOnUiThread(() -> Toast.makeText(CommentDetailActivity.this,
                        "Lỗi upload ảnh: " + error, Toast.LENGTH_SHORT).show());
            }
        });
    }

    private void createReplyWithUrls(String content, Long parentCommentId,
                                     ArrayList<String> mediaUrls, ProgressDialog progressDialog) {
        UserSessionManager sessionManager = new UserSessionManager(this);
        User user = sessionManager.getUser();

        if (user == null) {
            progressDialog.dismiss();
            Toast.makeText(this, "Không tìm thấy thông tin người dùng!",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        CommentRequest request = new CommentRequest(content, mediaUrls, "public",
                user.getUserId(), postId, parentCommentId);

        ApiService apiService = RetrofitClient.getApiService();
        apiService.createComment(request).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    handleSuccessfulReply();
                } else {
                    Toast.makeText(CommentDetailActivity.this,
                            "Lỗi khi tạo phản hồi!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(CommentDetailActivity.this,
                        "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handleSuccessfulReply() {
        Toast.makeText(this, "Phản hồi thành công!", Toast.LENGTH_SHORT).show();

        // Clear form
        etComment.setText("");
        selectedImages.clear();
        imageAdapter.notifyDataSetChanged();
        rvSelectedImages.setVisibility(View.GONE);

        // Reload replies
        Long commentId = getIntent().getLongExtra("commentId", -1);
        if (commentId != -1) {
            loadCommentReplies(commentId);
        }
    }

    private void handleUploadError(ProgressDialog progressDialog) {
        progressDialog.dismiss();
        Toast.makeText(CommentDetailActivity.this,
                "Lỗi xử lý ảnh", Toast.LENGTH_SHORT).show();
    }
}