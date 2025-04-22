package com.example.theadsproject;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.example.theadsproject.dto.NotificationResponse;

import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import io.socket.client.IO;
import io.socket.client.Socket;


public class SocketManager {
    private static Socket socket;
    private static List<NotificationListener> listeners = new ArrayList<>();
    private static Context context;
    private static final String SOCKET_URL = "http://192.168.1.2:3000"; // IP server của bạn

    // Cập nhật phương thức connect để nhận context
    public static void connect(Context appContext, Long userId) {
        context = appContext; // Lưu Context

        if (socket == null) {
            try {
                IO.Options opts = new IO.Options();
                socket = IO.socket(SOCKET_URL, opts);
            } catch (URISyntaxException e) {
                e.printStackTrace();
                return;
            }
        }

        if (!socket.connected()) {
            socket.connect();

            // Gửi userId để server ghi nhớ
            socket.emit("register", userId);

            // Nhận thông báo real-time
            socket.on("receive_notification", args -> {
                JSONObject data = (JSONObject) args[0];
                try {
                    NotificationResponse noti = new NotificationResponse();
                    noti.setType(data.getString("type"));
                    noti.setSenderId(data.getLong("senderId"));
                    noti.setSenderName(data.getString("senderName"));
                    noti.setSenderAvatar(data.getString("senderAvatar"));
                    noti.setCreatedAt(data.getString("createdAt"));

                    // Hiển thị Toast trên thread chính
                    new Handler(context.getMainLooper()).post(() -> {
                        Toast.makeText(context, "Bạn có thông báo mới từ: " + noti.getSenderName(), Toast.LENGTH_SHORT).show();
                    });

                    Log.d("SOCKET", "Noti: " + noti.getType());

                    for (NotificationListener listener : listeners) {
                        listener.onNotificationReceived(noti);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            Log.d("SOCKET", "Socket đã kết nối");
        } else {
            Log.d("SOCKET", "Socket đã được kết nối, bỏ qua");
        }
    }

    public static void setListener(NotificationListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    public static void disconnect() {
        if (socket != null && socket.connected()) {
            socket.disconnect();
        }
    }

    public static Socket getSocket() {
        return socket;
    }

    public static void removeListener(NotificationListener listener) {
        listeners.remove(listener);
    }

}

