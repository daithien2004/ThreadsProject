package com.example.theadsproject;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.databinding.DataBindingComponent;
import androidx.databinding.DataBindingUtil;

import com.example.theadsproject.databinding.ActivityMainBinding;
import com.example.theadsproject.entity.User;
import com.example.theadsproject.network.RetrofitClient;
import com.example.theadsproject.service.ApiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private User user;
    private ActivityMainBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        user = new User();
        binding.setUser(user);
    }

    public void Test() {
        TextView editText = (TextView) findViewById(R.id.tv1);

        ApiService apiService = RetrofitClient.getClient("http:///172.172.8.127:8080/").create(ApiService.class);
        Call<List<String>> call = apiService.getUsers();

        call.enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                if (response.isSuccessful()) {
                    List<String> users = response.body(); // Lấy dữ liệu
                    editText.setText(users.toString());
                } else {
                    Log.e("ALD", "Error: " + response.code()); // Xử lý lỗi HTTP (404, 500, v.v.)
                }
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                Log.e("ALD", "Failure: " + t.getMessage());
            }
        });
    }
}