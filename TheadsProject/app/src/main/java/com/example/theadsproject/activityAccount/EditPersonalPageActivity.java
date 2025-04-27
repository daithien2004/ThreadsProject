package com.example.theadsproject.activityAccount;

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
import com.example.theadsproject.dto.UserResponse;
import com.example.theadsproject.entity.User;

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

        sessionManager = new UserSessionManager(this);
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

            user.setUsername(newName);
            user.setNickName(newNickname);
            user.setBio(newBio);

            // Nếu có chọn ảnh mới
            if (selectedImageUri != null) {
                user.setImage(selectedImageUri.toString());
            }

            // Convert User -> UserResponse
            UserResponse userResponse = new UserResponse(user.getUserId(), user.getUsername(), user.getNickName(), user.getBio(), user.getImage());
            sessionManager.saveUser(userResponse); // lưu đúng kiểu UserResponse

            Toast.makeText(this, "Đã cập nhật thành công!", Toast.LENGTH_SHORT).show();
            finish();
        }
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
