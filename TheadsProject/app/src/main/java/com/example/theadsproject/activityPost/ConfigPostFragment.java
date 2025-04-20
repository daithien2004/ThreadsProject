package com.example.theadsproject.activityPost;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;

import com.example.theadsproject.R;
import com.example.theadsproject.retrofit.ApiService;
import com.example.theadsproject.retrofit.RetrofitClient;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConfigPostFragment extends BottomSheetDialogFragment {
    private Long postId;
    private static final Long loggedInUserId = 1L; // Giả định đã đăng nhập
    private OnPostDeletedListener deleteListener;
    private boolean isSaved = false;

    private LinearLayout saveOption;
    private TextView tvSave;
    private ImageView iconSave;

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

        saveOption = view.findViewById(R.id.saveOption);
        tvSave = view.findViewById(R.id.tvSave);
        iconSave = view.findViewById(R.id.iconSave);

        // Kiểm tra quyền sở hữu bài viết
        checkIfUserIsOwner(postId);


        // Gán sự kiện click
        saveOption.setOnClickListener(v -> {
            if (isSaved) {
                unsavePost();
            } else {
                savePost();
            }
        });

        return view;
    }

    private void checkIfUserIsOwner(Long postId) {
        if (postId == -1) return;

        ApiService apiService = RetrofitClient.getApiService();
        apiService.isPostOwner(postId, loggedInUserId).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful() && Boolean.TRUE.equals(response.body())) {
                    showUserPostOptions();
                } else {
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
        View view = getView();
        if (view != null) {
            view.findViewById(R.id.deleteOption).setVisibility(View.VISIBLE);
            view.findViewById(R.id.deleteOption).setOnClickListener(v -> showDeleteDialog());
        }
    }

    private void showGuestPostOptions() {
        View view = getView();
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null) {
                parent.removeAllViews();
                LayoutInflater inflater = LayoutInflater.from(requireContext());
                View guestView = inflater.inflate(R.layout.fragment_config_post_of_guest, parent, false);
                parent.addView(guestView);
            }
        }
    }

    private void showDeleteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_delete_post, null);
        builder.setView(dialogView);

        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
        alertDialog.getWindow().setLayout(700, 600);

        dialogView.findViewById(R.id.btnDelete).setOnClickListener(view -> {
            deletePost();
            alertDialog.dismiss();
        });

        dialogView.findViewById(R.id.btnCancel).setOnClickListener(view -> alertDialog.dismiss());
    }

    private void deletePost() {
        ApiService apiService = RetrofitClient.getApiService();
        apiService.deletePost(postId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    if (deleteListener != null) deleteListener.onPostDeleted(postId);
                    if (getDialog() != null && getDialog().isShowing()) getDialog().dismiss();
                    dismiss();
                } else {
                    Log.e("ERROR", "Xóa thất bại: " + response.code());
                    Toast.makeText(requireContext(), "Lỗi xóa bài viết", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("ERROR", "Lỗi kết nối API: " + t.getMessage());
                Toast.makeText(requireContext(), "Lỗi kết nối khi xóa bài viết", Toast.LENGTH_SHORT).show();
            }
        });
    }

//    private void checkIfPostIsSaved() {
//        ApiService apiService = RetrofitClient.getApiService();
//        apiService.isPostSaved(loggedInUserId, postId).enqueue(new Callback<Boolean>() {
//            @Override
//            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
//                if (response.isSuccessful() && response.body() != null) {
//                    isSaved = response.body();
//                    updateSaveUI();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Boolean> call, Throwable t) {
//                Log.e("ERROR", "Lỗi khi kiểm tra trạng thái lưu: " + t.getMessage());
//            }
//        });
//    }

    private void savePost() {
        ApiService apiService = RetrofitClient.getApiService();
        apiService.savePost(loggedInUserId, postId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    isSaved = true;
                    updateSaveUI();
                    Toast.makeText(getContext(), "Đã lưu bài viết", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("ERROR", "Lỗi khi lưu bài viết: " + t.getMessage());
            }
        });
    }

    private void unsavePost() {
        ApiService apiService = RetrofitClient.getApiService();
        apiService.unsavePost(loggedInUserId, postId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    isSaved = false;
                    updateSaveUI();
                    Toast.makeText(getContext(), "Đã bỏ lưu bài viết", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("ERROR", "Lỗi khi bỏ lưu bài viết: " + t.getMessage());
            }
        });
    }

    private void updateSaveUI() {
        if (isSaved) {
            iconSave.setImageResource(R.drawable.bookmark); // Thay bằng icon bookmark đã lưu
            tvSave.setText("Bỏ lưu bài viết");
        } else {
            iconSave.setImageResource(R.drawable.save); // Icon bookmark chưa lưu
            tvSave.setText("Lưu bài viết");
        }

    }
}
