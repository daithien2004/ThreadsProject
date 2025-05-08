package com.example.theadsproject.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.theadsproject.activityAccount.RepostsFragment;
import com.example.theadsproject.activityAccount.ThreadsFragment;

public class ViewPagerAdapterPersonalDetail extends FragmentStateAdapter {
        private final long userId;  // ✅ Biến này để lưu userId được truyền vào

        public ViewPagerAdapterPersonalDetail(@NonNull Fragment fragment, long userId) {
            super(fragment);
            this.userId = userId;  // ✅ Gán userId vào biến nội bộ
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            if (position == 0) {
                return ThreadsFragment.newInstance(userId);  // Fragment chứa danh sách Threads
            } else if (position == 1) {
                return RepostsFragment.newInstance(userId);  // Fragment chứa danh sách Reposts
            } else {
                return ThreadsFragment.newInstance(userId); // Mặc định nếu có lỗi
            }
        }

        @Override
        public int getItemCount() {
            return 2; // Chỉ có 2 tab: Threads và Reposts
        }
    }