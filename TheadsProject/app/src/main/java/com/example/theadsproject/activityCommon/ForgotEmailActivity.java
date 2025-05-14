package com.example.theadsproject.activityCommon;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.theadsproject.R;
import com.example.theadsproject.dto.UserRequest;
import com.example.theadsproject.retrofit.ApiService;
import com.example.theadsproject.retrofit.RetrofitClient;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotEmailActivity extends AppCompatActivity {
    Button btnSendOTP;
    TextInputEditText etEmailInput;
    String email;
    TextView tvBackLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forgotemail);

        tvBackLogin = findViewById(R.id.tvBackLogin);
        etEmailInput = findViewById(R.id.etEmailInput);
        btnSendOTP = findViewById(R.id.btnSendOTP);

        tvBackLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ForgotEmailActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnSendOTP.setOnClickListener(v -> {
            email = Objects.requireNonNull(etEmailInput.getText()).toString();
            sendOtpReset(email);
        });
    }

    public void sendOtpReset(String email) {
        UserRequest userRequest = new UserRequest(email);
        ApiService apiService = RetrofitClient.getApiService();
        Call<Boolean> call = apiService.resetOtp(userRequest);
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful() && Boolean.TRUE.equals(response.body())) {
                    Intent intent= new Intent(ForgotEmailActivity.this, OTPResetPW.class);
                    intent.putExtra("email", email);
                    startActivity(intent);
                    finish();
                }
//                else {
//                    Toast.makeText(ForgotEmailActivity.this, "Lỗi gửi email, vui lòng thử lại!", Toast.LENGTH_SHORT).show();
//                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Toast.makeText(ForgotEmailActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}