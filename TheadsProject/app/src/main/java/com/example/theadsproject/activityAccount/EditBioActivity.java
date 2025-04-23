package com.example.theadsproject.activityAccount;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.example.theadsproject.R;
import com.example.theadsproject.UserSessionManager;
import com.example.theadsproject.entity.User;
import com.example.theadsproject.retrofit.ApiService;
import com.example.theadsproject.retrofit.RetrofitClient;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditBioActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_bio);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        EditText edtPostContent = findViewById(R.id.edtPostContent);
        ImageView imTick = findViewById(R.id.imTick);

        imTick.setOnClickListener(view -> {
            String bio = edtPostContent.getText().toString().trim();

            if (bio.isEmpty()) {
                Toast.makeText(this, "Tiểu sử không được để trống", Toast.LENGTH_SHORT).show();
                return;
            }
            UserSessionManager sessionManager = new UserSessionManager(this);
            User user = sessionManager.getUser();

            Map<String, String> body = new HashMap<>();
            body.put("bio", bio);

            ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
            apiService.updateBio(user.getUserId(), body).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), "Cập nhật tiểu sử thành công", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Thất bại khi cập nhật", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "Lỗi kết nối server", Toast.LENGTH_SHORT).show();
                }
            });
        });

    }
}