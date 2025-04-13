package com.androidpj.threads.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OtpRequest {
    private String email;
    private String otp;
    private String password;
}
