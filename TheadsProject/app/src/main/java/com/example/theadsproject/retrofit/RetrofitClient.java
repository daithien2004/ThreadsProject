// Nguyễn Lý Hùng --22110337
package com.example.theadsproject.retrofit;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.theadsproject.MyApp;
import com.example.theadsproject.UserSessionManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RetrofitClient extends BaseClient {

    private static Retrofit retrofit;
    private static OkHttpClient okHttpClient;

    public static void initialize() {
        if (retrofit == null) {
            // Interceptor để thêm token vào header
            Interceptor authInterceptor = new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request originalRequest = chain.request();
                    String token = new UserSessionManager().getToken();

                    if (token != null && !token.isEmpty()) {
                        Request newRequest = originalRequest.newBuilder()
                                .header("Authorization", "Bearer " + token)
                                .build();
                        return chain.proceed(newRequest);
                    }
                    return chain.proceed(originalRequest);
                }
            };

            // Interceptor xử lý lỗi 401
            Interceptor unauthorizedInterceptor = new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request request = chain.request();
                    Response response = chain.proceed(request);

                    if (response.code() == 401) {
                        // Gửi broadcast thông báo token hết hạn
                        Intent intent = new Intent("ACTION_TOKEN_EXPIRED");
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        MyApp.getAppContext().sendBroadcast(intent);

                        // Đăng xuất người dùng
                        new UserSessionManager().logout();
                    }

                    return response;
                }
            };

            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            okHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(authInterceptor)
                    .addInterceptor(unauthorizedInterceptor)
                    .addInterceptor(loggingInterceptor)
                    .build();

            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDateTime.class, new JsonDeserializer<LocalDateTime>() {
                        @Override
                        public LocalDateTime deserialize(JsonElement json, Type typeOfT,
                                                         com.google.gson.JsonDeserializationContext context)
                                throws JsonParseException {
                            return LocalDateTime.parse(json.getAsString());
                        }
                    })
                    .create();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
    }

    public static Retrofit getInstance() {
        if (retrofit == null) {
            initialize();
        }
        return retrofit;
    }

    public static ApiService getApiService() {
        return getInstance().create(ApiService.class);
    }
}