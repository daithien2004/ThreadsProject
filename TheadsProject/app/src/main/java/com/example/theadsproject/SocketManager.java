    package com.example.theadsproject;

    import android.content.Context;
    import android.os.Handler;
    import android.os.Looper;
    import android.util.Log;
    import android.widget.Toast;

    import com.example.theadsproject.dto.NotificationResponse;

    import org.json.JSONException;
    import org.json.JSONObject;

    import java.net.URISyntaxException;
    import java.util.ArrayList;
    import java.util.List;

    import io.reactivex.disposables.CompositeDisposable;
    import io.reactivex.disposables.Disposable;
    import io.socket.client.IO;
    import io.socket.client.Socket;

    //public class SocketManager {
    //    private static Socket socket;
    //    private static List<NotificationListener> listeners = new ArrayList<>();
    //    private static Context context;
    //    private static final String SOCKET_URL = "http://192.168.1.2:3000"; // IP server của bạn
    //
    //    // Cập nhật phương thức connect để nhận context
    //    public static void connect(Context appContext, Long userId) {
    //        context = appContext; // Lưu Context
    //
    //        if (socket == null) {
    //            try {
    //                IO.Options opts = new IO.Options();
    //                socket = IO.socket(SOCKET_URL, opts);
    //            } catch (URISyntaxException e) {
    //                e.printStackTrace();
    //                return;
    //            }
    //        }
    //
    //        if (!socket.connected()) {
    //            socket.connect();
    //
    //            // Gửi userId để server ghi nhớ
    //            socket.emit("register", userId);
    //
    //            // Nhận thông báo real-time
    //            socket.on("receive_notification", args -> {
    //                JSONObject data = (JSONObject) args[0];
    //                try {
    //                    NotificationResponse noti = new NotificationResponse();
    //                    noti.setType(data.getString("type"));
    //                    noti.setSenderId(data.getLong("senderId"));
    //                    noti.setSenderName(data.getString("senderName"));
    //                    noti.setSenderAvatar(data.getString("senderAvatar"));
    //                    noti.setCreatedAt(data.getString("createdAt"));
    //
    //                    // Hiển thị Toast trên thread chính
    //                    new Handler(context.getMainLooper()).post(() -> {
    //                        Toast.makeText(context, "Bạn có thông báo mới từ: " + noti.getSenderName(), Toast.LENGTH_SHORT).show();
    //                    });
    //
    //                    Log.d("SOCKET", "Noti: " + noti.getType());
    //
    //                    for (NotificationListener listener : listeners) {
    //                        listener.onNotificationReceived(noti);
    //                    }
    //
    //                } catch (Exception e) {
    //                    e.printStackTrace();
    //                }
    //            });
    //
    //            Log.d("SOCKET", "Socket đã kết nối");
    //        } else {
    //            Log.d("SOCKET", "Socket đã được kết nối, bỏ qua");
    //        }
    //    }
    //
    //    public static void setListener(NotificationListener listener) {
    //        if (!listeners.contains(listener)) {
    //            listeners.add(listener);
    //        }
    //    }
    //
    //    public static void disconnect() {
    //        if (socket != null && socket.connected()) {
    //            socket.disconnect();
    //        }
    //    }
    //
    //    public static Socket getSocket() {
    //        return socket;
    //    }
    //
    //    public static void removeListener(NotificationListener listener) {
    //        listeners.remove(listener);
    //    }
    //
    //}

    import android.content.Context;
    import android.os.Handler;
    import android.util.Log;
    import android.widget.Toast;

    import com.example.theadsproject.dto.NotificationResponse;

    import java.util.ArrayList;
    import java.util.List;
    import java.util.concurrent.CopyOnWriteArrayList;
    import io.reactivex.functions.Consumer;

    import ua.naiksoftware.stomp.Stomp;
    import ua.naiksoftware.stomp.StompClient;
    import ua.naiksoftware.stomp.dto.StompMessage;

    public class SocketManager {
        private static final String TAG = "SocketManager";
        private static final String SOCKET_URL = "ws://192.168.8.114:8080/ws/websocket";
//        private static final String SOCKET_URL = "wss://threadsproject.onrender.com/ws/websocket"; // Thay bằng IP server Spring Boot

        private static volatile StompClient stompClient;
        private static final List<NotificationListener> listeners = new CopyOnWriteArrayList<>();
        private static Context context;
        private static CompositeDisposable disposables = new CompositeDisposable();

        private SocketManager() {
            // Ngăn khởi tạo instance
        }

        public static void connect(Context appContext, Long userId) {
            if (appContext == null || userId == null) {
                Log.e(TAG, "Invalid context or userId");
                return;
            }

            context = appContext.getApplicationContext(); // Sử dụng Application Context để tránh memory leak

            if (stompClient != null && stompClient.isConnected()) {
                Log.d(TAG, "WebSocket already connected");
                return;
            }

            stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, SOCKET_URL);
            stompClient.withClientHeartbeat(10000) // Giữ kết nối sống
                    .withServerHeartbeat(10000);

            disposables.add(stompClient.lifecycle().subscribe(
                    event -> {
                        switch (event.getType()) {
                            case OPENED:
                                Log.d(TAG, "WebSocket connected");
                                registerUser(userId);
                                subscribeToTopics(userId);
                                break;
                            case CLOSED:
                                Log.d(TAG, "WebSocket closed");
                                break;
                            case ERROR:
                                Log.e(TAG, "WebSocket error: ", event.getException());
                                break;
                        }
                    },
                    throwable -> Log.e(TAG, "Lifecycle error: ", throwable)
            ));

            stompClient.connect();
        }

        private static void registerUser(Long userId) {
            disposables.add(stompClient.send("/app/register", userId.toString())
                    .subscribe(
                            () -> Log.d(TAG, "User registered: " + userId),
                            throwable -> Log.e(TAG, "Error registering user: ", throwable)
                    ));
        }

        private static void subscribeToTopics(Long userId) {
            // Đăng ký các topic
            subscribeToTopic("/user/queue/register", message -> Log.d(TAG, "Registration response: " + message.getPayload()));
            subscribeToTopic("/user/queue/errors", message -> Log.e(TAG, "Error received: " + message.getPayload()));
            subscribeToNotifications("/topic/notifications/" + userId);
        }

        private static void subscribeToTopic(String topic, Consumer<StompMessage> consumer) {
            disposables.add(stompClient.topic(topic).subscribe(consumer, throwable ->
                    Log.e(TAG, "Error subscribing to " + topic + ": ", throwable)));
        }

        private static void subscribeToNotifications(String topic) {
            disposables.add(stompClient.topic(topic).subscribe(message -> {
                NotificationResponse noti = parseNotification(message.getPayload());
                if (noti != null) {
                    new Handler(Looper.getMainLooper()).post(() -> {
                        Toast.makeText(context, "Thông báo từ: " + noti.getSenderName(), Toast.LENGTH_SHORT).show();
                        listeners.forEach(listener -> listener.onNotificationReceived(noti));
                    });
                    Log.d(TAG, "Notification: " + noti.getType());
                }
            }, throwable -> Log.e(TAG, "Error subscribing to notifications: ", throwable)));
        }

        private static NotificationResponse parseNotification(String json) {
            try {
                JSONObject data = new JSONObject(json);
                NotificationResponse noti = new NotificationResponse();
                noti.setType(data.optString("type"));
                noti.setSenderId(data.optLong("senderId"));
                noti.setSenderName(data.optString("senderName"));
                noti.setSenderAvatar(data.optString("senderAvatar"));
                noti.setCreatedAt(data.optString("createdAt"));
                if (data.has("postId")) {
                    noti.setPostId(data.optLong("postId"));
                }
                return noti;
            } catch (JSONException e) {
                Log.e(TAG, "Error parsing notification: ", e);
                return null;
            }
        }

        public static void setListener(NotificationListener listener) {
            if (listener != null && !listeners.contains(listener)) {
                listeners.add(listener);
            }
        }

        public static void removeListener(NotificationListener listener) {
            listeners.remove(listener);
        }

        public static void disconnect() {
            if (stompClient != null && stompClient.isConnected()) {
                disposables.add(stompClient.send("/app/disconnect", "")
                        .subscribe(
                                () -> Log.d(TAG, "Disconnect sent"),
                                throwable -> Log.e(TAG, "Error sending disconnect: ", throwable)
                        ));
                disposables.clear();
                stompClient.disconnect();
                stompClient = null;
            }
        }
    }

