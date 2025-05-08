package com.example.theadsproject.commonClass;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.theadsproject.R;
import com.example.theadsproject.UserSessionManager;
import com.example.theadsproject.activityPost.ConfigPostFragment;
import com.example.theadsproject.activityPost.PostDetailActivity;
import com.example.theadsproject.adapter.CommonViewHolder;
import com.example.theadsproject.adapter.ImageAdapter;
import com.example.theadsproject.adapter.PostAdapter;
import com.example.theadsproject.dto.BindableContent;
import com.example.theadsproject.dto.PostResponse;
import com.example.theadsproject.entity.User;
import com.example.theadsproject.handler.LikeHandler;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BindingUtils {
    public static void bindCommonData(CommonViewHolder holder, BindableContent item, Context context, LikeHandler likeHandler) {
        // Hiển thị thông tin cơ bản
        holder.txtNickName.setText(item.getUser().getNickName());
        holder.txtTextPost.setText(item.getContent());

        // Hiển thị thời gian
        LocalDateTime createdAt = item.getCreatedAt();
        if (createdAt != null) {
            try {
                long timestamp = createdAt.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
                holder.txtTime.setText(TimeUtils.getTimeAgo(timestamp));
            } catch (Exception e) {
                e.printStackTrace();
                holder.txtTime.setText("Lỗi định dạng thời gian");
            }
        } else {
            holder.txtTime.setText("Không có dữ liệu");
        }

        // Xử lý avatar
        Glide.with(context)
                .load(item.getUser().getImage())
                .placeholder(R.drawable.user)
                .error(R.drawable.user)
                .apply(RequestOptions.circleCropTransform())
                .into(holder.imgAvatar);

        // Hiển thị ảnh nếu có
        List<String> mediaUrls = item.getMediaUrls();
        if (mediaUrls == null || mediaUrls.isEmpty()) {
            holder.recyclerViewImages.setVisibility(View.GONE);
        } else {
            holder.recyclerViewImages.setVisibility(View.VISIBLE);
            holder.recyclerViewImages.setAdapter(new ImageAdapter(context, new ArrayList<>(mediaUrls)));
        }

        // Thu gọn / mở rộng nội dung
        holder.txtTextPost.setMaxLines(2);
        holder.txtTextPost.setEllipsize(TextUtils.TruncateAt.END);
        holder.txtTextPost.setOnClickListener(v -> {
            boolean expanded = holder.txtTextPost.getMaxLines() != 2;
            holder.txtTextPost.setMaxLines(expanded ? 2 : Integer.MAX_VALUE);
            holder.txtTextPost.setEllipsize(expanded ? TextUtils.TruncateAt.END : null);
        });

        if (TextUtils.isEmpty(item.getContent())) {
            holder.txtTextPost.setVisibility(View.GONE);
        } else {
            holder.txtTextPost.setVisibility(View.VISIBLE);
        }

        // Lấy thông tin người dùng hiện tại
        UserSessionManager sessionManager = new UserSessionManager(context);
        User currentUser = sessionManager.getUser();
        Long currentUserId = currentUser.getUserId();

        // Bấm thả tim
        holder.llLove.setOnClickListener(v -> {
            boolean isLoved = item.getIsLoved();

            if (isLoved) {
                likeHandler.unlike(currentUserId, item.getId(), new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        holder.ivLove.setImageResource(R.drawable.heart);
                        item.setIsLoved(false);
                        updateLikeCount(holder, item.getId(), likeHandler);
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) { }
                });
            } else {
                likeHandler.like(currentUserId, item.getId(), new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        holder.ivLove.setImageResource(R.drawable.heart_red);
                        item.setIsLoved(true);
                        updateLikeCount(holder, item.getId(), likeHandler);
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) { }
                });
            }
        });

        likeHandler.checkIfLiked(item.getUser().getUserId(), item.getId(), new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                boolean isLiked = response.body() != null && response.body();
                item.setIsLoved(isLiked);
                holder.ivLove.setImageResource(isLiked ? R.drawable.heart_red : R.drawable.heart);
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) { }
        });

        // Hiển thị số lượng like
        likeHandler.countLikes(item.getId(), new Callback<Long>() {
            @Override
            public void onResponse(Call<Long> call, Response<Long> response) {
                if (response.isSuccessful() && response.body() != null) {
                    holder.tvLove.setText(String.valueOf(response.body()));
                }
            }

            @Override
            public void onFailure(Call<Long> call, Throwable t) {
                holder.tvLove.setText("0");
            }
        });

        // Hiển thị số lượng comment
        likeHandler.countComments(item.getId(), new Callback<Long>() {
            @Override
            public void onResponse(Call<Long> call, Response<Long> response) {
                if (response.isSuccessful() && response.body() != null) {
                    holder.tvConversation.setText(String.valueOf(response.body()));
                }
            }

            @Override
            public void onFailure(Call<Long> call, Throwable t) {
                holder.tvConversation.setText("0");
            }
        });
    }

    // Cập nhật lại số lượng like
    private static void updateLikeCount(CommonViewHolder holder, Long itemId, LikeHandler likeHandler) {
        likeHandler.countLikes(itemId, new Callback<Long>() {
            @Override
            public void onResponse(Call<Long> call, Response<Long> response) {
                if (response.body() != null) {
                    holder.tvLove.setText(String.valueOf(response.body()));
                }
            }

            @Override
            public void onFailure(Call<Long> call, Throwable t) { }
        });
    }
}
