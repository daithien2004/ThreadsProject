package com.example.theadsproject.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import com.example.theadsproject.activityHome.HomeFragment;
import com.example.theadsproject.activityHome.FollowingFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {
    public ViewPagerAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 1) {
            return new FollowingFragment();
        }
        return new HomeFragment();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
