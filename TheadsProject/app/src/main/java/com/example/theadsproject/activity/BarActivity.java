package com.example.theadsproject.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.theadsproject.R;
import com.example.theadsproject.databinding.ActivityBarBinding;
import com.google.android.material.behavior.HideBottomViewOnScrollBehavior;
import com.google.android.material.bottomappbar.BottomAppBar;

public class BarActivity extends AppCompatActivity {
    ActivityBarBinding binding;
    private GestureDetector gestureDetector;
    private View bottomAppBar;
    private View bottomNavigationView;

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

        // Khởi tạo GestureDetector và BottomAppBar
        bottomAppBar = findViewById(R.id.bottomAppBar);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        gestureDetector = new GestureDetector(this, new GestureListener());

        // Thiết lập touch listener cho FrameLayout để phát hiện vuốt
        findViewById(R.id.frame_layout).setOnTouchListener(new View.OnTouchListener() {
            ///// tat cả đều có thể ẩn
            //            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                return gestureDetector.onTouchEvent(event);  // Đảm bảo GestureDetector nhận diện touch event
//            }

            // chỉ home fragment có thể ẩn  hiện
            public boolean onTouch(View v, MotionEvent event) {
                // Kiểm tra xem hiện tại có phải là HomeFragment không
                if (isHomeFragmentVisible()) {
                    return gestureDetector.onTouchEvent(event);
                }
                return false;
            }
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

    // Lớp GestureListener để phát hiện vuốt lên và vuốt xuống
    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            // Kiểm tra vuốt lên (e1.getY() > e2.getY()) và vuốt xuống (e2.getY() > e1.getY())
            if (e1.getY() - e2.getY() > 100) {  // Vuốt lên
                Log.d("Gesture", "Vuốt lên - Ẩn BottomAppBar");
                hideBottomAppBar();
            } else if (e2.getY() - e1.getY() > 100) {  // Vuốt xuống
                Log.d("Gesture", "Vuốt xuống - Hiện BottomAppBar");
                showBottomAppBar();
            }
            return true;
        }
    }

    // Ẩn BottomAppBar và BottomNavigationView
    private void hideBottomAppBar() {
        if (bottomAppBar != null) {
            bottomAppBar.setVisibility(View.GONE);
        }
        if (bottomNavigationView != null) {
            bottomNavigationView.setVisibility(View.GONE);
        }
        // Cập nhật margin của FrameLayout để chiếm toàn bộ không gian
        FrameLayout frameLayout = findViewById(R.id.frame_layout);
        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) frameLayout.getLayoutParams();
        layoutParams.bottomMargin = 0; // Đặt margin dưới là 0 để FrameLayout chiếm hết không gian
        frameLayout.setLayoutParams(layoutParams);
    }

    private void showBottomAppBar() {
        if (bottomAppBar != null) {
            bottomAppBar.setVisibility(View.VISIBLE);
        }
        if (bottomNavigationView != null) {
            bottomNavigationView.setVisibility(View.VISIBLE);
        }
        // Cập nhật margin của FrameLayout khi BottomAppBar hiển thị lại
        FrameLayout frameLayout = findViewById(R.id.frame_layout);
        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) frameLayout.getLayoutParams();
        layoutParams.bottomMargin = (int) (60 * getResources().getDisplayMetrics().density); // Đặt lại margin dưới cho FrameLayout
        frameLayout.setLayoutParams(layoutParams);
    }
    private boolean isHomeFragmentVisible() {
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.frame_layout);
        return currentFragment instanceof TabLayoutHomeFragment;
    }

}
