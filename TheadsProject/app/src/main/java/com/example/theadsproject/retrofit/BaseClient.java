//Nguyễn Lý Hùng - 22110337
package com.example.theadsproject.retrofit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BaseClient {
    private static final HttpLoggingInterceptor sLogging =
            new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);

    private static final OkHttpClient.Builder sHttpClient =
            new OkHttpClient.Builder().addInterceptor(sLogging);

    protected static <S> S createService(Class<S> serviceClass, String baseUrl) {
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create());

        builder.client(sHttpClient.build());

        Retrofit retrofit = builder.build();
        return retrofit.create(serviceClass);
    }
}
