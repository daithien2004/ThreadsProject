package com.example.theadsproject.activity;

import android.content.Intent;
import android.os.Bundle;
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
import com.example.theadsproject.dto.UserRequest;
import com.example.theadsproject.retrofit.ApiService;
import com.example.theadsproject.retrofit.RetrofitClient;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    Button btnRegister;
    TextInputEditText etUsernameInputRe, etEmailInput, etPhoneInput, etPasswordInputRe, etCPasswordInput, etNickNameInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        initViews();
//        btnRegister.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent= new Intent(RegisterActivity.this, OTPRegisterActivity.class);
//                startActivity(intent);
//                finish();
//            }
//        });
        btnRegister.setOnClickListener(v ->handleRegister());
    }

    public void initViews() {
        etUsernameInputRe = findViewById(R.id.etUsernameInputRe);
        etEmailInput = findViewById(R.id.etEmailInput);
        etPhoneInput = findViewById(R.id.etPhoneInput);
        etPasswordInputRe = findViewById(R.id.etPasswordInputRe);
        etCPasswordInput = findViewById(R.id.etCPasswordInput);
        btnRegister = findViewById(R.id.btnRegister);
        etNickNameInput = findViewById(R.id.etNickNameInput);
    }

    public void handleRegister() {
        String nickName = etNickNameInput.getText().toString().trim();
        String username = etUsernameInputRe.getText().toString().trim();
        String email = etEmailInput.getText().toString().trim();
        String phone = etPhoneInput.getText().toString().trim();
        String password = etPasswordInputRe.getText().toString().trim();
        String cPassword = etCPasswordInput.getText().toString().trim();

        if (username.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty() || cPassword.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(cPassword)) {
            Toast.makeText(this, "Mật khẩu xác nhận không khớp", Toast.LENGTH_SHORT).show();
            return;
        }

        UserRequest userRequest = new UserRequest(nickName, username, email, phone, password);

        ApiService apiService = RetrofitClient.getApiService();
        Call<Boolean> call = apiService.register(userRequest);
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful()) {
                    Intent intent = new Intent(RegisterActivity.this, OTPRegisterActivity.class);
                    intent.putExtra("email", email);
                    startActivity(intent);
                    finish();
                }
                else {
                    Toast.makeText(RegisterActivity.this, "Đã có lỗi đăng ký!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, "Đã có lỗi!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}