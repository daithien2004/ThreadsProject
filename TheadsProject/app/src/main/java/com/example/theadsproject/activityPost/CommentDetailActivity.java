package com.example.theadsproject.activityPost;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.theadsproject.R;
import com.example.theadsproject.UserSessionManager;
import com.example.theadsproject.adapter.CommentAdapter;
import com.example.theadsproject.adapter.ImageAdapter;
import com.example.theadsproject.commonClass.ImagePickerHelper;
import com.example.theadsproject.commonClass.TimeUtils;
import com.example.theadsproject.dto.CommentRequest;
import com.example.theadsproject.dto.CommentResponse;
import com.example.theadsproject.entity.User;
import com.example.theadsproject.handler.CommentLikeHandler;
import com.example.theadsproject.handler.PostLikeHandler;
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
    private RecyclerView recyclerView;          // RecyclerView cho danh sách phản hồi
    private ImageView ivBack;                   // Nút quay lại
    private RecyclerView rvSelectedImages;      // RecyclerView hiển thị ảnh đã chọn
    private EditText etComment;                 // Ô nhập nội dung bình luận
    private ImageButton ibSend;                 // Nút gửi bình luận
    private CommentAdapter commentAdapter;      // Adapter cho danh sách phản hồi
    private List<CommentResponse> commentList;  // Danh sách các phản hồi
    private List<Object> selectedImages = new ArrayList<>(); // Danh sách ảnh đã chọn
    private ImageAdapter imageAdapter;          // Adapter cho ảnh đã chọn
    private Long postId;                        // ID bài viết
    private ActivityResultLauncher<Intent> pickImagesLauncher;

    private ConstraintLayout commentBar;
    CommentLikeHandler commentLikeHandler = new CommentLikeHandler();

    private UserSessionManager sessionManager;

    // Khai báo ActivityResultLauncher trong Activity hoặc Fragment
    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    long deletedId = result.getData().getLongExtra("deleted_comment_id", -1);
                    if (deletedId != -1) {
                        // Gọi hàm xóa comment khỏi RecyclerView adapter
                        removeCommentById(deletedId);
                    }
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_detail);


        // Khởi tạo các thành phần UI
        initViews();

        // Nhận dữ liệu từ Intent
        Long commentId = getIntent().getLongExtra("commentId", -1);
        postId = getIntent().getLongExtra("postId", -1);

        // Kiểm tra tính hợp lệ của commentId
        if (commentId != -1) {
            loadCommentDetail(commentId);     // Tải chi tiết bình luận
            loadCommentReplies(commentId);    // Tải các phản hồi của bình luận
        } else {
            Toast.makeText(this, "Không tìm thấy bình luận", Toast.LENGTH_SHORT).show();
            finish();
        }

        // Thiết lập các sự kiện click
        ivBack.setOnClickListener(v -> finish());

        findViewById(R.id.ibImage).setOnClickListener(v -> ImagePickerHelper.openGallery(this, pickImagesLauncher));

        ibSend.setOnClickListener(v -> {
            if (commentId != -1) {
                uploadReply(commentId);
            }
        });

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

    private void initViews() {
        UserSessionManager sessionManager = new UserSessionManager();
        commentBar = findViewById(R.id.comment_bar);
        if (!sessionManager.isLoggedIn()) {
            commentBar.setVisibility(View.INVISIBLE);
        }
        // Ánh xạ các thành phần UI
        ivBack = findViewById(R.id.ivBack);
        recyclerView = findViewById(R.id.rvReplies);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        rvSelectedImages = findViewById(R.id.rvSelectedImages);
        etComment = findViewById(R.id.etComment);
        ibSend = findViewById(R.id.ibSend);

        // Thiết lập layout manager và adapter cho RecyclerView
        rvSelectedImages.setLayoutManager(new LinearLayoutManager(
                this,
                LinearLayoutManager.HORIZONTAL,
                false));
        imageAdapter = new ImageAdapter(this, selectedImages);
        rvSelectedImages.setAdapter(imageAdapter);
    }

    private void loadCommentDetail(Long commentId) {
        ApiService apiService = RetrofitClient.getApiService();
        apiService.getCommentById(commentId).enqueue(new Callback<CommentResponse>() {
            @Override
            public void onResponse(Call<CommentResponse> call, Response<CommentResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    CommentResponse comment = response.body();
                    View view = findViewById(R.id.commentDetail);
                    PostItemView commentItemView = new PostItemView(
                            view,
                            CommentDetailActivity.this,
                            commentLikeHandler,
                            (type, id) -> {
                                if (type == ConfigPostFragment.ConfigType.COMMENT && !isFinishing()) {
                                    Toast.makeText(CommentDetailActivity.this, "Bình luận đã được xóa", Toast.LENGTH_SHORT).show();
                                    Intent resultIntent = new Intent();
                                    resultIntent.putExtra("deleted_comment_id", id);  // Truyền id
                                    setResult(RESULT_OK, resultIntent);
                                    finish();
                                }
                            });
                    commentItemView.bind(comment, CommentDetailActivity.this);
                }
            }

            @Override
            public void onFailure(Call<CommentResponse> call, Throwable t) {
                Toast.makeText(CommentDetailActivity.this,
                        "Lỗi khi tải bình luận", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadCommentReplies(Long commentId) {
        ApiService apiService = RetrofitClient.getApiService();
        apiService.getCommentReplies(commentId).enqueue(new Callback<List<CommentResponse>>() {
            @Override
            public void onResponse(Call<List<CommentResponse>> call, Response<List<CommentResponse>> response) {
                if (response.isSuccessful()) {
                    commentList = response.body();
                    commentAdapter = new CommentAdapter(CommentDetailActivity.this, commentList, activityResultLauncher);
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
        ArrayList<String> mediaUrls = new ArrayList<>();
        CountDownLatch latch = new CountDownLatch(selectedImages.size());

        // Tải ảnh lên cloud
        for (Object obj : selectedImages) {
            if (obj instanceof Uri) {
                ImageUploadService.uploadImage(this, (Uri) obj, new ImageUploadService.UploadCallback() {
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
        }

        new Thread(() -> {
            try {
                latch.await();
                runOnUiThread(() -> createReplyWithUrls(content, parentCommentId, mediaUrls, progressDialog));
            } catch (InterruptedException e) {
                e.printStackTrace();
                runOnUiThread(() ->
                {
                    progressDialog.dismiss();
                    Toast.makeText(CommentDetailActivity.this, "Lỗi xử lý ảnh", Toast.LENGTH_SHORT).show();
                });
            }
        }).start();
    }

    private void createReplyWithUrls(String content, Long parentCommentId, ArrayList<String> mediaUrls, ProgressDialog progressDialog) {
        UserSessionManager sessionManager = new UserSessionManager();
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
                    Toast.makeText(CommentDetailActivity.this, "Phản hồi thành công!", Toast.LENGTH_SHORT).show();
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

    public void removeCommentById(long commentId) {
        int indexToRemove = -1;

        // Duyệt qua commentList để tìm bình luận có commentId khớp
        for (int i = 0; i < commentList.size(); i++) {
            if (commentList.get(i).getCommentId() == commentId) { // So sánh id để tìm bình luận cần xóa
                indexToRemove = i;
                break;
            }
        }

        // Nếu tìm thấy bình luận cần xóa, thực hiện xóa và cập nhật RecyclerView
        if (indexToRemove != -1) {
            commentList.remove(indexToRemove);  // Xóa bình luận khỏi danh sách
            commentAdapter.notifyItemRemoved(indexToRemove);   // Cập nhật RecyclerView để hiển thị lại danh sách
        }
    }
}