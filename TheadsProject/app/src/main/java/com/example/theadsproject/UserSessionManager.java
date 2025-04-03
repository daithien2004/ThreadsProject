package com.example.theadsproject;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.theadsproject.dto.UserResponse;

public class UserSessionManager {
//    private static final String PREF_NAME = "UserSession";
//    private static final String KEY_USER_ID = "userId";
//    private static final String KEY_USERNAME = "username";
//    private SharedPreferences sharedPreferences;
//    private SharedPreferences.Editor editor;
//    public UserSessionManager(Context context) {
//        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
//        editor = sharedPreferences.edit();
//    }
//    // Lưu thông tin user
//    public void saveUser(UserResponse user) {
//        editor.putLong(KEY_USER_ID, user.getUserId());
//        editor.putString(KEY_USERNAME, user.getUsername());
//        editor.apply(); // Lưu thay đổi
//    }
//    // Lấy thông tin user
//    public UserResponse getUser() {
//        long userId = sharedPreferences.getLong(KEY_USER_ID, -1);
//        String username = sharedPreferences.getString(KEY_USERNAME, null);
//
//        if (userId == -1 || username == null) {
//            return null; // Không có user nào đăng nhập
//        }
//
//        return new UserResponse(userId, username);
//    }
//    // Kiểm tra user đã đăng nhập chưa
//    public boolean isLoggedIn() {
//        return sharedPreferences.contains(KEY_USER_ID);
//    }
//    // Xóa thông tin user khi đăng xuất
//    public void logout() {
//        editor.clear();
//        editor.apply();
//    }
}
