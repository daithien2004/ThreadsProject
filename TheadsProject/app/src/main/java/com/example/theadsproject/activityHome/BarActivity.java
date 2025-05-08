package com.example.theadsproject.activityHome;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.theadsproject.LoginRequiredDialogFragment;
import com.example.theadsproject.R;
import com.example.theadsproject.SocketManager;
import com.example.theadsproject.UserSessionManager;
import com.example.theadsproject.activityAccount.PersonalDetailFragment;
import com.example.theadsproject.activityLove.LoveFragment;
import com.example.theadsproject.activityPost.PostFragment;
import com.example.theadsproject.activitySearch.SearchFragment;
import com.example.theadsproject.databinding.ActivityBarBinding;
import com.example.theadsproject.entity.User;

public class BarActivity extends AppCompatActivity {
    ActivityBarBinding binding;
    private Fragment currentFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBarBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Thiết lập insets cho barBottom nếu có (đảm bảo tương thích các thiết bị có thanh điều hướng)
        View barBottom = findViewById(R.id.barBottom);
        if (barBottom != null) {
            ViewCompat.setOnApplyWindowInsetsListener(barBottom, (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
        }

        // Hiển thị fragment trang chủ mặc định khi vừa vào ứng dụng
        replaceFragment(new TabLayoutHomeFragment());

        // Xóa nền của thanh điều hướng (bottom nav)
        binding.bottomNavigationView.setBackground(null);

        // Xử lý sự kiện chọn các item trong thanh điều hướng
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.home) {
                replaceFragment(new TabLayoutHomeFragment());
            } else if(item.getItemId() == R.id.love) {
                replaceFragment(new LoveFragment());
            } else if(item.getItemId() == R.id.account) {
                // Lấy userId hiện tại
                UserSessionManager sessionManager = new UserSessionManager();
                if (sessionManager.isLoggedIn()) {
                    long currentUserId = sessionManager.getUser().getUserId();
                    // Hiển thị fragment với userId
                    replaceFragment(PersonalDetailFragment.newInstance(currentUserId));
                } else {
                    new LoginRequiredDialogFragment().show(getSupportFragmentManager(), "login_required_dialog");
                }
        } else if(item.getItemId() == R.id.search) {
                replaceFragment(new SearchFragment());
            } else if(item.getItemId() == R.id.addPost) {
                replaceFragment(new PostFragment());
            }
            return true;
        });

        // Lấy thông tin người dùng từ phiên đăng nhập
        UserSessionManager sessionManager = new UserSessionManager();
        User user = sessionManager.getUser();

        // Kết nối socket nếu tìm thấy người dùng
        if (user != null) {
            SocketManager.connect(BarActivity.this, user.getUserId());
        } else {
            Toast.makeText(BarActivity.this, "Bạn đang ở chế độ khách!", Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * Phương thức thay đổi fragment đang hiển thị
     * @param fragment Fragment cần hiển thị
     */

    public void replaceFragment(Fragment fragment) {
        this.currentFragment = fragment;
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}
