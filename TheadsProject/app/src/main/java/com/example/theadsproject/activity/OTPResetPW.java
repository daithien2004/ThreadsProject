package com.example.theadsproject.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.theadsproject.R;
import com.example.theadsproject.dto.OtpRequest;
import com.example.theadsproject.retrofit.ApiService;
import com.example.theadsproject.retrofit.RetrofitClient;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OTPResetPW extends AppCompatActivity {
    Button btnConfirmPassword;
    TextInputEditText etPasswordReset, etCPasswordReset, etOtpReset;
    String email, otp, password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_otpresetpw);

        email = getIntent().getStringExtra("email");
        btnConfirmPassword = findViewById(R.id.btnConfirmPassword);
        etPasswordReset = findViewById(R.id.etPasswordReset);
        etCPasswordReset = findViewById(R.id.etCPasswordReset);
        etOtpReset = findViewById(R.id.etOtpReset);


        btnConfirmPassword.setOnClickListener(v -> {
            otp = etOtpReset.getText().toString().trim();
            password = etPasswordReset.getText().toString().trim();

            if (otp.length() < 6) {
                Toast.makeText(OTPResetPW.this, "Vui lòng nhập đầy đủ OTP!", Toast.LENGTH_SHORT).show();
                return;
            }
            resetPassword(email, otp, password);
        });
    }

    private void resetPassword(String email, String otp, String password) {
        OtpRequest otpRequest = new OtpRequest(email, otp, password);

        ApiService apiService = RetrofitClient.getApiService();
        Call<Boolean> call = apiService.resetPassword(otpRequest);
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful()) {
                    Intent intent = new Intent(OTPResetPW.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
                else {
                    Toast.makeText(OTPResetPW.this, "Đã có lỗi!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Toast.makeText(OTPResetPW.this, "Đã có lỗi!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}