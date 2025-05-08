package com.example.theadsproject.activityAccount;

import android.content.Intent;
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

    private EditText edtBio;
    private ImageView imTick, imClose;

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

        edtBio = findViewById(R.id.edtBio);
        imTick = findViewById(R.id.imTick);
        imClose = findViewById(R.id.imClose);

        imTick.setOnClickListener(view -> updateBio());
        imClose.setOnClickListener(view -> finish());

        UserSessionManager sessionManager = new UserSessionManager();
        User user = sessionManager.getUser();

        if (user != null && user.getBio() != null) {
            edtBio.setText(user.getBio());
        }

    }

    private void updateBio() {
        String bio = edtBio.getText().toString().trim();

        if (bio.isEmpty()) {
            Toast.makeText(this, "Tiểu sử không được để trống", Toast.LENGTH_SHORT).show();
            return;
        }

        UserSessionManager sessionManager = new UserSessionManager();
        User user = sessionManager.getUser();

        Map<String, String> body = new HashMap<>();
        body.put("bio", bio);

        ApiService apiService = RetrofitClient.getApiService();
        apiService.updateBio(user.getUserId(), body).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // Cập nhật lại UserSessionManager
                    UserSessionManager sessionManager = new UserSessionManager();
                    sessionManager.updateBio(bio);

                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("updated_bio", bio);
                    setResult(RESULT_OK, resultIntent);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Thất bại khi cập nhật", Toast.LENGTH_SHORT).show();
                }
            }


            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Lỗi kết nối server", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
