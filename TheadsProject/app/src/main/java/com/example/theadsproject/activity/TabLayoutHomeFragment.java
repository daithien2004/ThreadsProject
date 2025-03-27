package com.example.theadsproject.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.theadsproject.R;
import com.example.theadsproject.adapter.ViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class TabLayoutHomeFragment extends Fragment {
    private static final String TAG = "TabLayoutHomeFragment";
    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private ViewPagerAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: TabLayoutHomeFragment is created");
        return inflater.inflate(R.layout.fragment_tab_layout_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tabLayout = view.findViewById(R.id.tabLayout);
        viewPager = view.findViewById(R.id.viewPager);

        Log.d(TAG, "onViewCreated: Finding tabLayout and viewPager");

        // Gán Adapter cho ViewPager2
        adapter = new ViewPagerAdapter(requireActivity());
        viewPager.setAdapter(adapter);
        Log.d(TAG, "onViewCreated: Adapter set to ViewPager2");

        // Liên kết TabLayout với ViewPager2
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("Home");
                    break;
                case 1:
                    tab.setText("Following");
                    break;
            }
        }).attach();

        Log.d(TAG, "onViewCreated: TabLayoutMediator attached");
    }
}
