package com.example.theadsproject.dto;

public class OtpRequest {
    String email;
    String otp;
    String password;

    public OtpRequest(String email, String otp) {
        this.email = email;
        this.otp = otp;
    }

    public OtpRequest(String email, String otp, String password) {
        this.email = email;
        this.otp = otp;
        this.password = password;
    }
}
