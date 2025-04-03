package com.example.theadsproject.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.theadsproject.activityAccount.MediaFragment;
import com.example.theadsproject.activityAccount.RepliesFragment;
import com.example.theadsproject.activityAccount.ReportsFragment;
import com.example.theadsproject.activityAccount.ThreadsFragment;

public class ViewPagerAdapterPersonalDetail extends FragmentStateAdapter {
public ViewPagerAdapterPersonalDetail(@NonNull Fragment fragment) {
    super(fragment);
}

@NonNull
@Override
public Fragment createFragment(int position) {
    if (position == 0) {
        return new ThreadsFragment();  // Fragment chứa danh sách Threads
    } else if (position == 1) {
        return new RepliesFragment();  // Fragment chứa danh sách Replies
    } else if (position == 2) {
        return new MediaFragment();    // Fragment chứa danh sách Media
    } else if (position == 3) {
        return new ReportsFragment();  // Fragment chứa danh sách Reports
    } else {
        return new ThreadsFragment(); // Mặc định nếu có lỗi
    }
}

@Override
public int getItemCount() {
    return 4; // 4 tabs
}

}