package com.example.theadsproject;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.example.theadsproject.activityCommon.LoginActivity;

public class MyApp extends Application {
    private static MyApp instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        // Đăng ký BroadcastReceiver để nhận thông báo hết hạn token
        IntentFilter filter = new IntentFilter("ACTION_TOKEN_EXPIRED");
        registerReceiver(tokenExpiredReceiver, filter, Context.RECEIVER_NOT_EXPORTED);
    }

    // BroadcastReceiver để nhận thông báo token hết hạn
    private final BroadcastReceiver tokenExpiredReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Khi token hết hạn, chuyển hướng đến màn hình đăng nhập
            Intent loginIntent = new Intent(context, LoginActivity.class);
            loginIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear stack
            context.startActivity(loginIntent);
        }
    };

    @Override
    public void onTerminate() {
        super.onTerminate();
        // Hủy đăng ký receiver khi ứng dụng bị hủy
        unregisterReceiver(tokenExpiredReceiver);
    }

    public static Context getAppContext() {
        return instance.getApplicationContext();
    }
}

