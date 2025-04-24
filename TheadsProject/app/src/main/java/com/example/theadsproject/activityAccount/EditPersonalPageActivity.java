package com.example.theadsproject.activityAccount;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.theadsproject.R;

public class EditPersonalPageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_personal_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ImageView turnBack = findViewById(R.id.imClose);
        turnBack.setOnClickListener(v -> {
            finish();  // kết thúc activity này để quay về màn hình trước đó
        });

        LinearLayout bioLayout = findViewById(R.id.lnBio);
        bioLayout.setOnClickListener(v -> {
            Intent intent = new Intent(EditPersonalPageActivity.this, EditBioActivity.class);
            startActivity(intent);
        });
    }
}