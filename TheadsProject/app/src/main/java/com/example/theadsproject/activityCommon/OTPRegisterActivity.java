package com.example.theadsproject.activityCommon;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.theadsproject.R;
import com.example.theadsproject.dto.OtpRequest;
import com.example.theadsproject.retrofit.ApiService;
import com.example.theadsproject.retrofit.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OTPRegisterActivity extends AppCompatActivity {
    Button btnConfirmOTP;
    private EditText otp1, otp2, otp3, otp4, otp5, otp6;
    private String email;
    TextView tvEmailSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_otpregister);

        email = getIntent().getStringExtra("email");
        btnConfirmOTP = findViewById(R.id.btnConfirmOTP);
        tvEmailSend = findViewById(R.id.tvEmailSend);

        tvEmailSend.setText(email);

        otp1 = findViewById(R.id.otp1);
        otp2 = findViewById(R.id.otp2);
        otp3 = findViewById(R.id.otp3);
        otp4 = findViewById(R.id.otp4);
        otp5 = findViewById(R.id.otp5);
        otp6 = findViewById(R.id.otp6);


        btnConfirmOTP.setOnClickListener(view -> {
            String otp = otp1.getText().toString() +
                    otp2.getText().toString() +
                    otp3.getText().toString() +
                    otp4.getText().toString() +
                    otp5.getText().toString() +
                    otp6.getText().toString();

            if (otp.length() < 6) {
                Toast.makeText(OTPRegisterActivity.this, "Vui lòng nhập đầy đủ OTP!", Toast.LENGTH_SHORT).show();
                return;
            }
            verifyOtp(email, otp);
        });
    }

    private void verifyOtp(String email, String otp) {
        OtpRequest otpRequest = new OtpRequest(email, otp);

        ApiService apiService = RetrofitClient.getApiService();
        Call<Boolean> call = apiService.activate(otpRequest);
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful() && Boolean.TRUE.equals(response.body())) {
                    Intent intent = new Intent(OTPRegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
                else {
                    Toast.makeText(OTPRegisterActivity.this, "Lỗi đăng ký, vui lòng thử lại!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Toast.makeText(OTPRegisterActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}