package com.example.theadsproject.commonClass;

import android.util.Log;

import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class TimeUtils {
    public static String getTimeAgo(long pastTimeMillis) {
        pastTimeMillis = convertUtcToLocalTime(pastTimeMillis); // Chuyển về giờ địa phương
        long now = System.currentTimeMillis();

        // Kiểm tra nếu thời gian quá khứ lớn hơn hiện tại, tránh lỗi hiển thị âm
        if (pastTimeMillis > now) {
            Log.e("TimeError", "pastTimeMillis lớn hơn thời gian hiện tại! pastTimeMillis = " + pastTimeMillis + ", now = " + now);
            return "now";
        }

        long diff = now - pastTimeMillis;

        if (diff < TimeUnit.MINUTES.toMillis(1)) {
            return TimeUnit.MILLISECONDS.toSeconds(diff) + "s";
        } else if (diff < TimeUnit.HOURS.toMillis(1)) {
            return TimeUnit.MILLISECONDS.toMinutes(diff) + "m";
        } else if (diff < TimeUnit.DAYS.toMillis(1)) {
            return TimeUnit.MILLISECONDS.toHours(diff) + "h";
        } else if (diff < TimeUnit.DAYS.toMillis(7)) {
            return TimeUnit.MILLISECONDS.toDays(diff) + "d";
        } else if (diff < TimeUnit.DAYS.toMillis(30)) {
            return (TimeUnit.MILLISECONDS.toDays(diff) / 7) + "w";
        } else if (diff < TimeUnit.DAYS.toMillis(365)) {
            return (TimeUnit.MILLISECONDS.toDays(diff) / 30) + "mo";
        } else {
            return (TimeUnit.MILLISECONDS.toDays(diff) / 365) + "y";
        }
    }
    // Chuyển từ UTC về giờ địa phương
    public static long convertUtcToLocalTime(long utcTimeMillis) {
        return utcTimeMillis - TimeZone.getDefault().getRawOffset();
    }

}
