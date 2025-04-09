package com.example.theadsproject.activityHome;

import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.theadsproject.R;
import com.example.theadsproject.activityAccount.PersonalDetailFragment;
import com.example.theadsproject.activityLove.LoveFragment;
import com.example.theadsproject.activityPost.PostFragment;
import com.example.theadsproject.activitySearch.SearchFragment;
import com.example.theadsproject.databinding.ActivityBarBinding;


public class BarActivity extends AppCompatActivity {
    ActivityBarBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBarBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Đặt insets cho barBottom nếu tồn tại
        View barBottom = findViewById(R.id.barBottom);
        if (barBottom != null) {
            ViewCompat.setOnApplyWindowInsetsListener(barBottom, (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
        }

        // Set fragment mặc định
        replaceFragment(new TabLayoutHomeFragment());
        binding.bottomNavigationView.setBackground(null);
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            if(item.getItemId() == R.id.home) {
                replaceFragment(new TabLayoutHomeFragment());
            } else if(item.getItemId() == R.id.love) {
                replaceFragment(new LoveFragment());
            } else if(item.getItemId() == R.id.account) {
                replaceFragment(new PersonalDetailFragment());
            } else if(item.getItemId() == R.id.search) {
                replaceFragment(new SearchFragment());
            } else if(item.getItemId() == R.id.addPost) {
                replaceFragment(new PostFragment());
            }
            return true;
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}
