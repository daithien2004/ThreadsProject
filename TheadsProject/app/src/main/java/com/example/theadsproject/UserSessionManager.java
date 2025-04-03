package com.example.theadsproject;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.theadsproject.dto.UserResponse;
import com.example.theadsproject.entity.User;

public class UserSessionManager {
    private static final String PREF_NAME = "UserSession";
    private static final String KEY_USER_ID = "userId";
    private static final String KEY_BIO = "bio";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_IMAGE = "image";
    private static final String KEY_NICKNAME = "nickName";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PHONE = "phone";
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public UserSessionManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    // 📌 Lưu thông tin user
    public void saveUser(UserResponse user) {
        editor.putLong(KEY_USER_ID, user.getUserId());
        editor.putString(KEY_BIO, user.getBio());
        editor.putString(KEY_EMAIL, user.getEmail());
        editor.putString(KEY_IMAGE, user.getImage());
        editor.putString(KEY_NICKNAME, user.getNickName());
        editor.putString(KEY_USERNAME, user.getUsername());
        editor.putString(KEY_PHONE, user.getPhone());
        editor.apply(); // Lưu thay đổi
    }

    // 📌 Lấy thông tin user
    public User getUser() {
        long userId = sharedPreferences.getLong(KEY_USER_ID, -1);
        String bio = sharedPreferences.getString(KEY_BIO, null);
        String email = sharedPreferences.getString(KEY_EMAIL, null);
        String image = sharedPreferences.getString(KEY_IMAGE, null);
        String nickName = sharedPreferences.getString(KEY_NICKNAME, null);
        String username = sharedPreferences.getString(KEY_USERNAME, null);
        String phone = sharedPreferences.getString(KEY_PHONE, null);

        if (userId == -1 || username == null) {
            return null; // Không có user nào đăng nhập
        }

        return new User(userId, email, nickName, image, username, bio, phone);
    }

    // 📌 Kiểm tra user đã đăng nhập chưa
    public boolean isLoggedIn() {
        return sharedPreferences.contains(KEY_USER_ID);
    }

    // 📌 Xóa thông tin user khi đăng xuất
    public void logout() {
        editor.clear();
        editor.apply();
    }
}
