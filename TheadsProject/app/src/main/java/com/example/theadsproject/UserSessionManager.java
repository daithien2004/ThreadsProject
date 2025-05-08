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
    private static final String KEY_TOKEN = "token";
    private static final String KEY_TOKEN_EXPIRY = "token_expiry";

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public UserSessionManager() {
        Context context = MyApp.getAppContext();
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    // Lưu thông tin user và token
    public void saveUser(UserResponse user) {
        editor.putLong(KEY_USER_ID, user.getUserId());
        editor.putString(KEY_BIO, user.getBio());
        editor.putString(KEY_EMAIL, user.getEmail());
        editor.putString(KEY_IMAGE, user.getImage());
        editor.putString(KEY_NICKNAME, user.getNickName());
        editor.putString(KEY_USERNAME, user.getUsername());
        editor.putString(KEY_PHONE, user.getPhone());

        // Lưu token nếu có
        if (user.getToken() != null) {
            editor.putString(KEY_TOKEN, user.getToken());
            // Có thể lưu thêm thời gian hết hạn nếu cần
            // editor.putLong(KEY_TOKEN_EXPIRY, calculateExpiryTime());
        }

        editor.apply();
    }

    // Lấy token
    public String getToken() {
        return sharedPreferences.getString(KEY_TOKEN, null);
    }

    // Kiểm tra token có hợp lệ không
    public boolean isValidToken() {
        String token = getToken();
        if (token == null || token.isEmpty()) {
            return false;
        }

        // Có thể thêm logic kiểm tra thời gian hết hạn ở đây
        // long expiryTime = sharedPreferences.getLong(KEY_TOKEN_EXPIRY, 0);
        // return System.currentTimeMillis() < expiryTime;

        return true;
    }

    // Kiểm tra user đã đăng nhập chưa (có token hợp lệ)
    public boolean isLoggedIn() {
        return sharedPreferences.contains(KEY_USER_ID) && isValidToken();
    }

    // Xóa thông tin user và token khi đăng xuất
    public void logout() {
        editor.clear();
        editor.apply();
    }

    // Cập nhật thông tin user
    public void updateUserInfo(UserResponse user) {
        if (user.getBio() != null) editor.putString(KEY_BIO, user.getBio());
        if (user.getEmail() != null) editor.putString(KEY_EMAIL, user.getEmail());
        if (user.getImage() != null) editor.putString(KEY_IMAGE, user.getImage());
        if (user.getNickName() != null) editor.putString(KEY_NICKNAME, user.getNickName());
        if (user.getPhone() != null) editor.putString(KEY_PHONE, user.getPhone());
        if (user.getToken() != null) editor.putString(KEY_TOKEN, user.getToken());

        editor.apply();
    }

    // Lấy thông tin user
    public User getUser() {
        long userId = sharedPreferences.getLong(KEY_USER_ID, -1);
        String bio = sharedPreferences.getString(KEY_BIO, null);
        String email = sharedPreferences.getString(KEY_EMAIL, null);
        String image = sharedPreferences.getString(KEY_IMAGE, null);
        String nickName = sharedPreferences.getString(KEY_NICKNAME, null);
        String username = sharedPreferences.getString(KEY_USERNAME, null);
        String phone = sharedPreferences.getString(KEY_PHONE, null);

        if (userId == -1 || username == null) {
            return null;
        }

        return new User(userId, email, nickName, image, username, bio, phone);
    }

    // Cập nhật bio
    public void updateBio(String newBio) {
        editor.putString(KEY_BIO, newBio);
        editor.apply();
    }

    // Lấy username
    public String getUsername() {
        return sharedPreferences.getString(KEY_USERNAME, null);
    }

    // Lấy user ID
    public long getUserId() {
        return sharedPreferences.getLong(KEY_USER_ID, -1);
    }

    // Refresh token (khi có token mới)
    public void refreshToken(String newToken) {
        editor.putString(KEY_TOKEN, newToken);
        // Cập nhật thời gian hết hạn nếu cần
        // editor.putLong(KEY_TOKEN_EXPIRY, calculateExpiryTime());
        editor.apply();
    }
}
