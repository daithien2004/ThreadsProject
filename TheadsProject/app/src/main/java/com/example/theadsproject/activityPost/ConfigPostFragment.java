package com.example.theadsproject.activityPost;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.example.theadsproject.R;
import com.example.theadsproject.UserSessionManager;
import com.example.theadsproject.entity.User;
import com.example.theadsproject.retrofit.ApiService;
import com.example.theadsproject.retrofit.RetrofitClient;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConfigPostFragment extends BottomSheetDialogFragment {
    public enum ConfigType { POST, COMMENT }

    private ConfigType type;
    private Long id;
    private Long userId;
    private UserSessionManager sessionManager;
    private OnDeleteListener deleteListener;
    private boolean isSaved = false;

    private LinearLayout saveOption;
    private TextView tvSave;
    private ImageView iconSave;

    public interface OnDeleteListener {
        void onDelete(ConfigType type, long id);
    }

    public static ConfigPostFragment newInstance(ConfigType type, Long id, OnDeleteListener listener) {
        ConfigPostFragment fragment = new ConfigPostFragment();
        Bundle args = new Bundle();
        args.putSerializable("type", type);
        args.putLong("id", id);
        fragment.setArguments(args);
        fragment.setDeleteListener(listener);
        return fragment;
    }

    public void setDeleteListener(OnDeleteListener listener) {
        this.deleteListener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_config_post_of_user, container, false);

        sessionManager = new UserSessionManager();
        User user = sessionManager.getUser();
        if (user != null) {
            userId = user.getUserId();
        } else {
            Toast.makeText(getContext(), "Không tìm thấy thông tin người dùng", Toast.LENGTH_SHORT).show();
            dismiss();
            return view;
        }

        if (getArguments() != null) {
            type = (ConfigType) getArguments().getSerializable("type");
            id = getArguments().getLong("id", -1);
        }

        // Thiết lập sự kiện saveOption ban đầu (cho user layout)
        setupUI(view);

        if (type == ConfigType.POST) {
            // Kiểm tra quyền sở hữu bài viết
            checkIfUserIsOwnerPost(id);
            // Kiểm tra trạng thái lưu
            checkIfPostIsSaved();
        } else if (type == ConfigType.COMMENT) {
            // Kiểm tra quyền sở hữu bài viết
            checkIfUserIsOwnerComment(id);
        }

        return view;
    }

    private void setupUI(View view) {
        if (!isAdded() || getContext() == null) return;

        saveOption = view.findViewById(R.id.saveOption);
        tvSave = view.findViewById(R.id.tvSave);
        iconSave = view.findViewById(R.id.iconSave);

        // Ẩn tính năng save cho comment
        if (type == ConfigType.COMMENT && saveOption != null) {
            saveOption.setVisibility(View.GONE);
        }

        // Xử lý sự kiện save cho post
        if (type == ConfigType.POST && saveOption != null) {
            saveOption.setOnClickListener(v -> {
                if (isSaved) unsavePost();
                else savePost();
            });
        }
    }

    private void checkIfUserIsOwnerPost(Long postId) {
        if (postId == -1 || !isAdded()) return;

        ApiService apiService = RetrofitClient.getApiService();
        apiService.isPostOwner(postId, userId).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (!isAdded()) return;

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

    private void checkIfUserIsOwnerComment(Long commentId) {
        if (commentId == -1 || !isAdded()) return;

        ApiService apiService = RetrofitClient.getApiService();
        apiService.isCommentOwner(commentId, userId).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (!isAdded()) return;

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
        if (!isAdded()) return;

        View view = getView();
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null) {
                parent.removeAllViews();
                LayoutInflater inflater = LayoutInflater.from(requireContext());
                View guestView = inflater.inflate(R.layout.fragment_config_post_of_guest, parent, false);
                parent.addView(guestView);

                // Gán lại sự kiện saveOption cho guest layout
                setupUI(guestView);
                checkIfPostIsSaved();
            }
        }
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Hủy các API request tại đây (nếu dùng Retrofit Call)
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
            if (type == ConfigType.POST) {
                deletePost();
            } else if (type == ConfigType.COMMENT) {
                deleteComment();
            }

            alertDialog.dismiss();
        });

        dialogView.findViewById(R.id.btnCancel).setOnClickListener(view -> alertDialog.dismiss());
    }

    private void deletePost() {
        ApiService apiService = RetrofitClient.getApiService();
        apiService.deletePost(id, userId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (!isAdded()) return;

                if (response.isSuccessful()) {
                    if (deleteListener != null)
                        deleteListener.onDelete(type, id);
                    dismiss();
                } else {
                    String errorMessage;
                    if (response.code() == 403) {
                        errorMessage = "Bạn không có quyền xóa bài viết này";
                    } else {
                        errorMessage = "Không thể xóa bài viết";
                    }
                    Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                if (!isAdded()) return;
                Toast.makeText(requireContext(), "Lỗi kết nối", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteComment() {
        ApiService apiService = RetrofitClient.getApiService();
        apiService.deleteComment(id, userId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (!isAdded()) return;

                if (response.isSuccessful()) {
                    if (deleteListener != null)
                        deleteListener.onDelete(type, id);
                    dismiss();
                } else {
                    String errorMessage;
                    if (response.code() == 403) {
                        errorMessage = "Bạn không có quyền xóa bình luận này";
                    } else {
                        errorMessage = "Không thể xóa bình luận";
                    }
                    Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                if (!isAdded()) return;
                Toast.makeText(requireContext(), "Lỗi kết nối", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkIfPostIsSaved() {
        ApiService apiService = RetrofitClient.getApiService();
        apiService.isPostSaved(userId, id).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful() && response.body() != null) {
                    isSaved = response.body();
                    updateSaveUI();
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Log.e("ERROR", "Lỗi khi kiểm tra trạng thái lưu: " + t.getMessage());
            }
        });
    }

    private void savePost() {
        ApiService apiService = RetrofitClient.getApiService();
        apiService.savePost(userId, id).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (!isAdded()) return;

                if (response.isSuccessful()) {
                    isSaved = true;
                    updateSaveUI();
                    if (getContext() != null) {
                        Toast.makeText(getContext(), "Đã lưu bài viết", Toast.LENGTH_SHORT).show();
                    }

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
        apiService.unsavePost(userId, id).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (!isAdded()) return;

                if (response.isSuccessful()) {
                    isSaved = false;
                    updateSaveUI();
                    if (getContext() != null) {
                        Toast.makeText(getContext(), "Đã bỏ lưu bài viết", Toast.LENGTH_SHORT).show();
                    }
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
            iconSave.setImageResource(R.drawable.bookmark); // Icon đã lưu
            tvSave.setText("Unsaved");
        } else {
            iconSave.setImageResource(R.drawable.save); // Icon chưa lưu
            tvSave.setText("Save");
        }
    }
}