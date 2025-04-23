package com.example.theadsproject.commonClass;

import android.util.Log;

import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import android.util.Log;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.concurrent.TimeUnit;

public class TimeUtils {
    public static String getTimeAgo(long pastTimeMillis) {
        // Chuyển UTC thành giờ địa phương
        long now = Instant.now().toEpochMilli(); // Thời gian hiện tại
        Log.d("TimeUtils", "pastTimeMillis = " + pastTimeMillis + ", now = " + now);

        if (pastTimeMillis > now) {
            Log.e("TimeError", "pastTimeMillis lớn hơn thời gian hiện tại! pastTimeMillis = " + pastTimeMillis + ", now = " + now);
            return "now";
        }

        long diff = now - pastTimeMillis - TimeUnit.HOURS.toMillis(7);

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
}



