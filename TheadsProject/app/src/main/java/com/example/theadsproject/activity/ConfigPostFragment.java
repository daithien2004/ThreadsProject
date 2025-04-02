package com.example.theadsproject.activity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.example.theadsproject.R;
import com.example.theadsproject.retrofit.ApiService;
import com.example.theadsproject.retrofit.RetrofitClient;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConfigPostFragment extends BottomSheetDialogFragment {
    private Long postId;
    private static final Long loggedInUserId = 1L; // Đặt userId mặc định là 9L
    private OnPostDeletedListener deleteListener;

    public interface OnPostDeletedListener {
        void onPostDeleted(long postId);
    }

    public static ConfigPostFragment newInstance(Long postId, OnPostDeletedListener listener) {
        ConfigPostFragment fragment = new ConfigPostFragment();
        Bundle args = new Bundle();
        args.putLong("postId", postId);
        fragment.setArguments(args);
        fragment.setDeleteListener(listener);
        return fragment;
    }

    public void setDeleteListener(OnPostDeletedListener listener) {
        this.deleteListener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_config_post_of_user, container, false);

        if (getArguments() != null) {
            postId = getArguments().getLong("postId", -1);
        }

        // Kiểm tra nếu người dùng là chủ sở hữu bài viết
        checkIfUserIsOwner(postId);

        return view;
    }

    private void checkIfUserIsOwner(Long postId) {
        if (postId == -1) {
            return; // Không có postId hợp lệ
        }

        ApiService apiService = RetrofitClient.getApiService();
        apiService.isPostOwner(postId, loggedInUserId).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful() && response.body() != null && response.body()) {
                    // Nếu người dùng là chủ sở hữu, hiển thị giao diện của người dùng
                    showUserPostOptions();
                } else {
                    // Nếu không phải chủ sở hữu, hiển thị giao diện của khách
                    showGuestPostOptions();
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Log.e("ERROR", "Lỗi kết nối API: " + t.getMessage());
                Toast.makeText(requireContext(), "Lỗi kiểm tra quyền sở hữu", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showUserPostOptions() {
        // Hiển thị giao diện của người dùng (tùy chọn xóa bài viết)
        View view = getView();
        if (view != null) {
            view.findViewById(R.id.deleteOption).setVisibility(View.VISIBLE);
            view.findViewById(R.id.deleteOption).setOnClickListener(v -> showDeleteDialog());
        }
    }

    private void showGuestPostOptions() {
        // Hiển thị giao diện của khách (ẩn tùy chọn xóa)
        View view = getView();
        if (view != null) {
            view.findViewById(R.id.deleteOption).setVisibility(View.GONE);
            Toast.makeText(requireContext(), "Bạn không phải là chủ sở hữu bài viết này", Toast.LENGTH_SHORT).show();
        }
    }

    private void showDeleteDialog() {
        // Hiển thị hộp thoại xác nhận xóa bài viết
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_delete_post, null);
        builder.setView(dialogView);

        final AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
        alertDialog.getWindow().setLayout(700, 600);

        Button btnDelete = dialogView.findViewById(R.id.btnDelete);
        Button btnCancel = dialogView.findViewById(R.id.btnCancel);

        btnDelete.setOnClickListener(view -> {
            // Gọi phương thức xóa bài viết khi nhấn "Delete"
            deletePost();
            // Đóng hộp thoại sau khi đã xử lý xóa
            alertDialog.dismiss();
        });

        btnCancel.setOnClickListener(view -> alertDialog.dismiss()); // Đóng hộp thoại khi nhấn "Cancel"
    }


    private void deletePost() {
        ApiService apiService = RetrofitClient.getApiService();
        apiService.deletePost(postId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // Xóa bài viết thành công
                    if (deleteListener != null) {
                        deleteListener.onPostDeleted(postId); // Gửi thông báo cập nhật danh sách
                    }

                    // Đóng BottomSheetDialogFragment sau khi xóa thành công
                    if (getDialog() != null && getDialog().isShowing()) {
                        getDialog().dismiss(); // Đảm bảo đóng BottomSheetDialogFragment
                    }

                    dismiss(); // Đảm bảo đóng Fragment nếu cần thiết
                } else {
                    // Xử lý lỗi nếu có
                    Log.e("ERROR", "Xóa thất bại: " + response.code());
                    Toast.makeText(requireContext(), "Lỗi xóa bài viết", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // Xử lý lỗi kết nối
                Log.e("ERROR", "Lỗi kết nối API: " + t.getMessage());
                Toast.makeText(requireContext(), "Lỗi kết nối khi xóa bài viết", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
