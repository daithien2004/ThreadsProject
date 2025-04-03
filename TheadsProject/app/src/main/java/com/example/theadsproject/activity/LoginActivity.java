package com.example.theadsproject.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.theadsproject.R;
import com.example.theadsproject.UserSessionManager;
import com.example.theadsproject.dto.UserRequest;
import com.example.theadsproject.dto.UserResponse;
import com.example.theadsproject.retrofit.ApiService;
import com.example.theadsproject.retrofit.RetrofitClient;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private TextInputEditText etUsernameInput, etPasswordInput;
    private Button btnLogin;
    private TextView tvForgotPassword, tvRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        initViews();

        btnLogin.setOnClickListener(v -> handleLogin());

        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });

        tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ForgotEmailActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void initViews() {
        TextInputLayout etUsername = findViewById(R.id.etUsername);
        TextInputLayout etPassword = findViewById(R.id.etPassword);
        etUsernameInput = etUsername.findViewById(R.id.etUsernameInput);
        etPasswordInput = etPassword.findViewById(R.id.etPasswordInput);
        btnLogin = findViewById(R.id.btnLogin);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);
        tvRegister = findViewById(R.id.tvRegister);
    }

    private void handleLogin() {
        String username = Objects.requireNonNull(etUsernameInput.getText()).toString().trim();
        String password = Objects.requireNonNull(etPasswordInput.getText()).toString().trim();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        UserRequest userRequest = new UserRequest(username, password);

        ApiService apiService = RetrofitClient.getApiService();
        Call<UserResponse> call = apiService.checkLogin(userRequest);
        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    UserResponse userResponse = response.body();

                    if (userResponse.getMessage() != null && !userResponse.getMessage().isEmpty()) {
                        Toast.makeText(LoginActivity.this, userResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    else {
                        // Lưu thông tin user vào SharedPreferences
                        UserSessionManager sessionManager = new UserSessionManager(LoginActivity.this);
                        sessionManager.saveUser(userResponse);

                        Intent intent = new Intent(LoginActivity.this, BarActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
                else {
                    Toast.makeText(LoginActivity.this, "Lỗi đăng nhập, vui lòng thử lại!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Log.e("LoginError", "Lỗi khi gọi API đăng nhập: " + t.getMessage());
                Toast.makeText(LoginActivity.this, "Lỗi kết nối, vui lòng kiểm tra mạng!", Toast.LENGTH_SHORT).show();
            }
        });

    }
}