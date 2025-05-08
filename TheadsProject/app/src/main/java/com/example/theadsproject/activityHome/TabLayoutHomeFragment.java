package com.example.theadsproject.activityHome;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.theadsproject.LoginRequiredDialogFragment;
import com.example.theadsproject.R;
import com.example.theadsproject.UserSessionManager;
import com.example.theadsproject.activityCommon.LoginActivity;
import com.example.theadsproject.adapter.ViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class TabLayoutHomeFragment extends Fragment {
    private TabLayout tabLayout;
    private ViewPager2 viewPager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("TabLayoutHomeFragment", "onCreateView được gọi");

        View view = inflater.inflate(R.layout.fragment_tab_layout_home, container, false);

        tabLayout = view.findViewById(R.id.tabLayout);
        viewPager = view.findViewById(R.id.viewPager);

        if (tabLayout == null || viewPager == null) {
            Log.e("TabLayoutHomeFragment", "Lỗi: TabLayout hoặc ViewPager là null");
        }
        ViewPagerAdapter adapter = new ViewPagerAdapter(this);
        viewPager.setAdapter(adapter);


        // Chặn thao tác vuốt
        viewPager.setUserInputEnabled(false);

        TabLayoutMediator mediator = new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            if (position == 0) {
                tab.setText("Home");
            } else {
                tab.setText("Following");
            }
        });
        mediator.attach();

        // Kiểm tra login khi chọn tab
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 1) {
                    UserSessionManager sessionManager = new UserSessionManager();
                    if (!sessionManager.isLoggedIn()) {
                        // Hiển thị dialog yêu cầu đăng nhập
                        new LoginRequiredDialogFragment().show(getParentFragmentManager(), "login_required_dialog");

                        // Quay lại tab Home mà không gây vòng lặp
                        viewPager.post(() -> viewPager.setCurrentItem(0, false));
                    }
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        return view;
    }
}