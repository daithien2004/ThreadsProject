package com.example.theadsproject.activityAccount;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.theadsproject.R;
import com.example.theadsproject.adapter.ViewPagerAdapterPersonalDetail;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;


public class TabPersonalDetailFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager2 viewPager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab_personal_detail, container, false);

        tabLayout = view.findViewById(R.id.tabLayoutPersonal);
        viewPager = view.findViewById(R.id.viewPagerPersonal);

        // Cấu hình Adapter cho ViewPager2
        ViewPagerAdapterPersonalDetail adapter = new ViewPagerAdapterPersonalDetail(this);
        viewPager.setAdapter(adapter);

        // Kết nối TabLayout với ViewPager2
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            if (position == 0) {
                tab.setText("Threads");
            } else if (position == 1) {
                tab.setText("Replies");
            } else if (position == 2) {
                tab.setText("Media");
            } else if (position == 3) {
                tab.setText("Reports");
            }
        }).attach();


        return view;
    }
}