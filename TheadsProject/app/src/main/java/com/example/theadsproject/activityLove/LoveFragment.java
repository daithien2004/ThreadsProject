package com.example.theadsproject.activityLove;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.theadsproject.NotificationListener;
import com.example.theadsproject.R;
import com.example.theadsproject.SocketManager;
import com.example.theadsproject.UserSessionManager;
import com.example.theadsproject.activityHome.BarActivity;
import com.example.theadsproject.adapter.NotificationAdapter;
import com.example.theadsproject.dto.NotificationResponse;
import com.example.theadsproject.entity.User;
import com.example.theadsproject.retrofit.ApiService;
import com.example.theadsproject.retrofit.RetrofitClient;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import io.socket.client.Socket;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoveFragment extends Fragment implements NotificationListener {

    private RecyclerView rcvNotification;
    private NotificationAdapter adapter;
    private List<NotificationResponse> fullList = new ArrayList<>();
    private Button btnAll, btnFollow, btnReply;
    private Long currentUserId; // Lấy từ login thực tế hoặc SharedPreferences

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_love, container, false);

        UserSessionManager sessionManager = new UserSessionManager(getContext());
        User user = sessionManager.getUser();
        if (user != null) {
            currentUserId = user.getUserId();
        } else {
            Toast.makeText(getContext(), "Không tìm thấy thông tin người dùng!", Toast.LENGTH_SHORT).show();
        }

        // Ánh xạ view
        rcvNotification = view.findViewById(R.id.rcvNotification);
        btnAll = view.findViewById(R.id.btnAll);
        btnFollow = view.findViewById(R.id.btnFollow);
        btnReply = view.findViewById(R.id.btnReply);

        // Setup RecyclerView
        adapter = new NotificationAdapter(new ArrayList<>());
        rcvNotification.setLayoutManager(new LinearLayoutManager(getContext()));
        rcvNotification.setAdapter(adapter);

        // Khởi tạo kết nối WebSocket
        SocketManager.connect(requireContext().getApplicationContext(), currentUserId);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SocketManager.setListener(this);

        // Lấy dữ liệu ban đầu
        fetchNotifications();

        // Bắt sự kiện lọc
        btnAll.setOnClickListener(v -> showFiltered("all"));
        btnFollow.setOnClickListener(v -> showFiltered("follow"));
        btnReply.setOnClickListener(v -> showFiltered("comment"));
    }

    private void fetchNotifications() {
        ApiService apiService = RetrofitClient.getApiService();
        apiService.getUserNotifications(currentUserId).enqueue(new Callback<List<NotificationResponse>>() {
            @Override
            public void onResponse(Call<List<NotificationResponse>> call, Response<List<NotificationResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    fullList.clear();
                    fullList.addAll(response.body());
                    showFiltered("all");
                } else {
                    Toast.makeText(getContext(), "Không lấy được thông báo", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<NotificationResponse>> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi kết nối server", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showFiltered(String type) {
        List<NotificationResponse> filtered;
        if (type.equals("all")) {
            filtered = fullList;
        } else {
            filtered = fullList.stream()
                    .filter(n -> type.equals(n.getType()))
                    .collect(Collectors.toList());
        }
        adapter.updateData(filtered);
    }

    @Override
    public void onPause() {
        super.onPause();
        SocketManager.removeListener(this); // Hủy listener khi fragment không hoạt động
    }

    @Override
    public void onResume() {
        super.onResume();
        SocketManager.setListener(this); // Đăng ký lại listener khi fragment quay lại
    }

    @Override
    public void onNotificationReceived(NotificationResponse notification) {
        if (notification == null) return;
        requireActivity().runOnUiThread(() -> {
            fullList.add(0, notification);
            adapter.notifyItemInserted(0); // Chỉ cập nhật item mới
            rcvNotification.scrollToPosition(0); // Cuộn lên đầu
        });
    }

    // Thêm phương thức hủy đăng ký listener
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        SocketManager.removeListener(this); // Thêm method này trong SocketManager
    }
}
