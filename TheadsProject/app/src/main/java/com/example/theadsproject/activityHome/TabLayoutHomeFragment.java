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

import com.example.theadsproject.R;
import com.example.theadsproject.activityCommon.LoginActivity;
import com.example.theadsproject.adapter.ViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class TabLayoutHomeFragment extends Fragment {
    private TabLayout tabLayout;
    private ViewPager2 viewPager;

    private Button btnLoginHome;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("TabLayoutHomeFragment", "onCreateView được gọi");

        View view = inflater.inflate(R.layout.fragment_tab_layout_home, container, false);

        btnLoginHome = view.findViewById(R.id.btnLoginHome);

        btnLoginHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });

        tabLayout = view.findViewById(R.id.tabLayout);
        viewPager = view.findViewById(R.id.viewPager);
        if (tabLayout == null || viewPager == null) {
            Log.e("TabLayoutHomeFragment", "Lỗi: tabLayout hoặc viewPager là null");
        }
        ViewPagerAdapter adapter = new ViewPagerAdapter(this);
        viewPager.setAdapter(adapter);


        // Chặn thao tác vuốt
        viewPager.setUserInputEnabled(false);

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            if (position == 0) {
                tab.setText("Home");
            } else {
                tab.setText("Following");
            }
        }).attach();

        return view;
    }
}