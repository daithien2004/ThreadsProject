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
    private long userId;

    public static TabPersonalDetailFragment newInstance(long userId) {
        TabPersonalDetailFragment fragment = new TabPersonalDetailFragment();
        Bundle args = new Bundle();
        args.putLong("user_id", userId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userId = getArguments().getLong("user_id");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab_personal_detail, container, false);

        tabLayout = view.findViewById(R.id.tabLayoutPersonal);
        viewPager = view.findViewById(R.id.viewPagerPersonal);

        // ✅ Truyền userId vào adapter
        ViewPagerAdapterPersonalDetail adapter = new ViewPagerAdapterPersonalDetail(this, userId);
        viewPager.setAdapter(adapter);

        // Kết nối TabLayout với ViewPager2
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            if (position == 0) {
                tab.setText("Threads");
            } else if (position == 1) {
                tab.setText("Reposts");
            }
        }).attach();

        // Chặn thao tác vuốt
        viewPager.setUserInputEnabled(false);

        return view;
    }
}
