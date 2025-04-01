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
    private Long loggedInUserId;
    private OnPostDeletedListener deleteListener; // Interface lắng nghe sự kiện xóa

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

        view.findViewById(R.id.deleteOption).setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            LayoutInflater inflater1 = getLayoutInflater();
            View dialogView = inflater1.inflate(R.layout.dialog_delete_post, null);
            builder.setView(dialogView);

            AlertDialog alertDialog = builder.create();
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            // Hiển thị hộp thoại trước khi chỉnh kích thước
            alertDialog.show();

            // Chỉnh kích thước hộp thoại
            alertDialog.getWindow().setLayout(700, 600); // Điều chỉnh chiều rộng và chiều cao theo px

            Button btnDelete = dialogView.findViewById(R.id.btnDelete);
            Button btnCancel = dialogView.findViewById(R.id.btnCancel);

            btnDelete.setOnClickListener(view1 -> {
                if (postId == -1) {
                    Toast.makeText(requireContext(), "Invalid post ID", Toast.LENGTH_SHORT).show();
                    return;
                }

                ApiService apiService = RetrofitClient.getApiService();
                apiService.deletePost(postId).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            Log.d("DEBUG", "Xóa bài viết thành công");

                            if (deleteListener != null) {
                                deleteListener.onPostDeleted(postId); // Gửi thông báo cập nhật danh sách
                            }

                            alertDialog.dismiss();
                            dismiss();
                        } else {
                            Log.e("ERROR", "Xóa thất bại: " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Log.e("ERROR", "Lỗi kết nối API: " + t.getMessage());
                    }
                });
            });

            btnCancel.setOnClickListener(view1 -> alertDialog.dismiss());
        });

        return view;
    }
}
