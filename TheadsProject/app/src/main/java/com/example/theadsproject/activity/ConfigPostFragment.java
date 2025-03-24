package com.example.theadsproject.activity;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.telecom.Call;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.example.theadsproject.R;
import com.example.theadsproject.network.RetrofitClient;
import com.example.theadsproject.retrofit.ApiService;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import retrofit2.Callback;
import retrofit2.Response;

public class ConfigPostFragment extends BottomSheetDialogFragment {

    private Context context;

    public ConfigPostFragment(Context context) {
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_config_post, container, false);

        view.findViewById(R.id.translateOption).setOnClickListener(v -> {
            Toast.makeText(context, "Translate clicked", Toast.LENGTH_SHORT).show();
            dismiss();
        });

        view.findViewById(R.id.saveOption).setOnClickListener(v -> {
            Toast.makeText(context, "Save clicked", Toast.LENGTH_SHORT).show();
            dismiss();
        });

        view.findViewById(R.id.pinOption).setOnClickListener(v -> {
            Toast.makeText(context, "Pin clicked", Toast.LENGTH_SHORT).show();
            dismiss();
        });

        view.findViewById(R.id.deleteOption).setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            LayoutInflater inflater1 = getLayoutInflater();
            View dialogView = inflater1.inflate(R.layout.dialog_delete_post, null);
            builder.setView(dialogView);

            AlertDialog alertDialog = builder.create();
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); // Nền trong suốt
            alertDialog.show();

            // Điều chỉnh kích thước
            alertDialog.getWindow().setLayout(600, ViewGroup.LayoutParams.WRAP_CONTENT); // 600dp chiều rộng

            // Lấy các thành phần từ layout
            Button btnDelete = dialogView.findViewById(R.id.btnDelete);
            Button btnCancel = dialogView.findViewById(R.id.btnCancel);

            // Xử lý sự kiện click
            btnDelete.setOnClickListener(view1 -> {
                Toast.makeText(requireContext(), "Post deleted", Toast.LENGTH_SHORT).show();
                alertDialog.dismiss();
                dismiss(); // Đóng BottomSheetDialogFragment
            });

            btnCancel.setOnClickListener(view1 -> alertDialog.dismiss());
        });
        return view;
    }
    // thực hiện API xóa post
    private void deletePost(int postId) {
        ApiService apiService = RetrofitClient.getClient();
        apiService.deletePost(postId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(requireContext(), "Post deleted successfully", Toast.LENGTH_SHORT).show();
                    dismiss(); // Đóng BottomSheetDialogFragment
                } else {
                    Toast.makeText(requireContext(), "Failed to delete post", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(requireContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
