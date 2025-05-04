    package com.example.theadsproject;

    import android.content.Context;
    import android.os.Handler;
    import android.os.Looper;
    import android.util.Log;
    import android.widget.Toast;
    import com.example.theadsproject.dto.NotificationResponse;
    import org.json.JSONException;
    import org.json.JSONObject;
    import java.util.List;
    import io.reactivex.disposables.CompositeDisposable;
    import java.util.concurrent.CopyOnWriteArrayList;
    import io.reactivex.functions.Consumer;

    import ua.naiksoftware.stomp.Stomp;
    import ua.naiksoftware.stomp.StompClient;
    import ua.naiksoftware.stomp.dto.StompMessage;

    public class SocketManager {
        private static final String TAG = "SocketManager";
        private static final String SOCKET_URL = "ws://192.168.1.9:8080/ws/websocket";
//      private static final String SOCKET_URL = "wss://threadsproject.onrender.com/ws/websocket";

        private static volatile StompClient stompClient;
        private static final List<NotificationListener> listeners = new CopyOnWriteArrayList<>();
        private static Context context;
        private static CompositeDisposable disposables = new CompositeDisposable();

        // Constructor private để không tạo được instance
        private SocketManager() {}

        // Hàm kết nối socket
        public static void connect(Context appContext, Long userId) {
            if (appContext == null || userId == null) {
                Log.e(TAG, "Context hoặc userId không hợp lệ!");
                return;
            }

            // Dùng Application Context để tránh rò rỉ bộ nhớ
            context = appContext.getApplicationContext();

            // Nếu đã kết nối rồi thì không cần kết nối lại
            if (stompClient != null && stompClient.isConnected()) {
                Log.d(TAG, "WebSocket đã được kết nối rồi");
                return;
            }

            // Tạo StompClient
            stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, SOCKET_URL);
            stompClient.withClientHeartbeat(10000).withServerHeartbeat(10000);  // Giữ kết nối sống

            // Theo dõi vòng đời kết nối
            disposables.add(stompClient.lifecycle().subscribe(
                    event -> {
                        switch (event.getType()) {
                            case OPENED:
                                Log.d(TAG, "WebSocket đã kết nối");
                                registerUser(userId); // Đăng ký user
                                subscribeToTopics(userId); // Lắng nghe các kênh
                                break;
                            case CLOSED:
                                Log.d(TAG, "WebSocket đã đóng");
                                break;
                            case ERROR:
                                Log.e(TAG, "Lỗi WebSocket: ", event.getException());
                                break;
                        }
                    },
                    throwable -> Log.e(TAG, "Lỗi vòng đời WebSocket: ", throwable)
            ));

            // Kết nối socket
            stompClient.connect();
        }

        // Gửi yêu cầu đăng ký user đến server
        private static void registerUser(Long userId) {
            disposables.add(stompClient.send("/app/register", userId.toString())
                .subscribe(
                        () -> Log.d(TAG, "Đã đăng ký user: " + userId),
                        throwable -> Log.e(TAG, "Lỗi khi đăng ký user: ", throwable)
                ));
        }

        // Đăng ký lắng nghe các topic
        private static void subscribeToTopics(Long userId) {
            // Đăng ký các topic nội bộ
            subscribeToTopic("/user/queue/register",
                    message -> Log.d(TAG, "Phản hồi đăng ký: " + message.getPayload()));
            subscribeToTopic("/user/queue/errors",
                    message -> Log.e(TAG, "Lỗi nhận từ server: " + message.getPayload()));

            // Lắng nghe thông báo real-time cho user
            subscribeToNotifications("/topic/notifications/" + userId);
        }

        // Lắng nghe topic thông thường
        private static void subscribeToTopic(String topic, Consumer<StompMessage> consumer) {
            disposables.add(stompClient.topic(topic).subscribe(consumer, throwable ->
                    Log.e(TAG, "Lỗi khi lắng nghe " + topic + ": ", throwable)));
        }

        // Lắng nghe topic thông báo gửi đến user
        private static void subscribeToNotifications(String topic) {
            disposables.add(stompClient.topic(topic).subscribe(message -> {
                NotificationResponse noti = parseNotification(message.getPayload());
                if (noti != null) {
                    // Gọi trên luồng chính để hiển thị Toast và thông báo cho các listener
                    new Handler(Looper.getMainLooper()).post(() -> {
                        Toast.makeText(context, "Thông báo từ: " + noti.getSenderName(), Toast.LENGTH_SHORT).show();
                        listeners.forEach(listener -> listener.onNotificationReceived(noti));
                    });
                    Log.d(TAG, "Nhận thông báo: " + noti.getType());
                }
            }, throwable -> Log.e(TAG, "Lỗi khi nhận thông báo: ", throwable)));
        }

        // Parse dữ liệu JSON từ WebSocket thành đối tượng NotificationResponse
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
                Log.e(TAG, "Lỗi parse thông báo: ", e);
                return null;
            }
        }

        // Đăng ký listener nhận thông báo
        public static void setListener(NotificationListener listener) {
            if (listener != null && !listeners.contains(listener)) {
                listeners.add(listener);
            }
        }

        // Hủy đăng ký listener
        public static void removeListener(NotificationListener listener) {
            listeners.remove(listener);
        }

        // Ngắt kết nối socket
        public static void disconnect() {
            if (stompClient != null && stompClient.isConnected()) {
                disposables.add(stompClient.send("/app/disconnect", "")
                        .subscribe(
                                () -> Log.d(TAG, "Đã gửi yêu cầu ngắt kết nối"),
                                throwable -> Log.e(TAG, "Lỗi khi gửi yêu cầu ngắt kết nối: ", throwable)
                        ));
                disposables.clear();
                stompClient.disconnect();
                stompClient = null;
            }
        }
    }

