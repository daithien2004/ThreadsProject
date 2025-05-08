package com.example.theadsproject.activityAccount;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.theadsproject.R;
import com.example.theadsproject.UserSessionManager;
import com.example.theadsproject.dto.UserRequest;
import com.example.theadsproject.dto.UserResponse;
import com.example.theadsproject.entity.User;
import com.example.theadsproject.retrofit.ApiService;
import com.example.theadsproject.retrofit.RetrofitClient;
import com.example.theadsproject.service.ImageUploadService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditPersonalPageActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private EditText edtName, edtNickname;
    private TextView tvBio;
    private ImageView ivAvatar;
    private Uri selectedImageUri;
    private UserSessionManager sessionManager;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_personal_page);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViews();
        setupListeners();
        loadUserData();
    }

    private void initViews() {
        edtName = findViewById(R.id.edtName);
        edtNickname = findViewById(R.id.edtNickname);
        tvBio = findViewById(R.id.tvBio);
        ivAvatar = findViewById(R.id.imgAvatar);

        sessionManager = new UserSessionManager();
        user = sessionManager.getUser();
    }

    private void setupListeners() {
        findViewById(R.id.imClose).setOnClickListener(v -> finish());

        findViewById(R.id.lnBio).setOnClickListener(v -> {
            Intent intent = new Intent(this, EditBioActivity.class);
            startActivity(intent);
        });

        ivAvatar.setOnClickListener(v -> openImageChooser());

        findViewById(R.id.tvDone).setOnClickListener(v -> saveChanges());
    }

    private void loadUserData() {
        if (user != null) {
            edtName.setText(user.getUsername());
            edtNickname.setText(user.getNickName());
            tvBio.setText(user.getBio());

            if (user.getImage() != null && !user.getImage().isEmpty()) {
                Glide.with(this)
                        .load(user.getImage())
                        .apply(RequestOptions.circleCropTransform())
                        .into(ivAvatar);
            }
        } else {
            Toast.makeText(this, "Không tìm thấy thông tin người dùng!", Toast.LENGTH_SHORT).show();
        }
    }

    private void openImageChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Chọn ảnh đại diện"), PICK_IMAGE_REQUEST);
    }

    private void saveChanges() {
        if (user != null) {
            String newName = edtName.getText().toString().trim();
            String newNickname = edtNickname.getText().toString().trim();
            String newBio = tvBio.getText().toString().trim();

            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Đang cập nhật...");
            progressDialog.show();

            if (selectedImageUri != null) {
                // Upload ảnh lên Cloudinary trước
                ImageUploadService.uploadImage(this, selectedImageUri, new ImageUploadService.UploadCallback() {
                    @Override
                    public void onSuccess(String imageUrl) {
                        updateUserOnServer(newName, newNickname, newBio, imageUrl, progressDialog);
                    }

                    @Override
                    public void onFailure(String error) {
                        progressDialog.dismiss();
                        Toast.makeText(EditPersonalPageActivity.this, "Lỗi upload ảnh: " + error, Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                // Không có ảnh mới
                updateUserOnServer(newName, newNickname, newBio, user.getImage(), progressDialog);
            }
        }
    }

    private void updateUserOnServer(String name, String nickname, String bio, String imageUrl, ProgressDialog progressDialog) {
        UserRequest request = new UserRequest(name, nickname, bio, imageUrl);

        ApiService apiService = RetrofitClient.getApiService();
        Call<Void> call = apiService.updateUser(user.getUserId(), request);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    // Lưu local session sau khi cập nhật
                    user.setUsername(name);
                    user.setNickName(nickname);
                    user.setBio(bio);
                    user.setImage(imageUrl);

                    UserResponse updatedUser = new UserResponse(user.getUserId(), name, nickname, bio, imageUrl);
                    sessionManager.saveUser(updatedUser);

                    Toast.makeText(EditPersonalPageActivity.this, "Đã cập nhật thành công!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(EditPersonalPageActivity.this, "Cập nhật thất bại!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(EditPersonalPageActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            selectedImageUri = data.getData();
            Glide.with(this)
                    .load(selectedImageUri)
                    .apply(RequestOptions.circleCropTransform())
                    .into(ivAvatar);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        user = sessionManager.getUser();
        if (user != null) {
            edtName.setText(user.getUsername());
            edtNickname.setText(user.getNickName());
            tvBio.setText(user.getBio());
        }
    }
}
