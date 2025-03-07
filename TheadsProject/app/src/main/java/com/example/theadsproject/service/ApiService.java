package com.example.theadsproject.service;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {
    @GET("/")
    Call<List<String>> getUsers();
}
