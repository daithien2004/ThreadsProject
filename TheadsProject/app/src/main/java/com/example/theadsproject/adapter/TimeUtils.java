package com.example.theadsproject.adapter;

import java.util.concurrent.TimeUnit;

public class TimeUtils {
    public static String getTimeAgo(long pastTimeMillis) {
        long now = System.currentTimeMillis();
        long diff = now - pastTimeMillis; // Khoảng cách thời gian

        if (diff < TimeUnit.MINUTES.toMillis(1)) {
            return TimeUnit.MILLISECONDS.toSeconds(diff) + "s"; // Dưới 1 phút: Giây
        } else if (diff < TimeUnit.HOURS.toMillis(1)) {
            return TimeUnit.MILLISECONDS.toMinutes(diff) + "m"; // Dưới 1 giờ: Phút
        } else if (diff < TimeUnit.DAYS.toMillis(1)) {
            return TimeUnit.MILLISECONDS.toHours(diff) + "h"; // Dưới 1 ngày: Giờ
        } else if (diff < TimeUnit.DAYS.toMillis(7)) {
            return TimeUnit.MILLISECONDS.toDays(diff) + "d"; // Dưới 1 tuần: Ngày
        } else if (diff < TimeUnit.DAYS.toMillis(30)) {
            return (TimeUnit.MILLISECONDS.toDays(diff) / 7) + "w"; // Dưới 1 tháng: Tuần
        } else if (diff < TimeUnit.DAYS.toMillis(365)) {
            return (TimeUnit.MILLISECONDS.toDays(diff) / 30) + "mo"; // Dưới 1 năm: Tháng
        } else {
            return (TimeUnit.MILLISECONDS.toDays(diff) / 365) + "y"; // Trên 1 năm: Năm
        }
    }
}
