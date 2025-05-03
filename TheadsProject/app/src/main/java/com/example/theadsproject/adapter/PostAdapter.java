package com.example.theadsproject.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.theadsproject.UserSessionManager;
import com.example.theadsproject.activityPost.PostDetailActivity;
import com.example.theadsproject.commonClass.TimeUtils;
import com.example.theadsproject.dto.PostResponse;
import com.example.theadsproject.dto.UserResponse;
import com.example.theadsproject.R;
import com.example.theadsproject.activityPost.ConfigPostFragment;
import com.example.theadsproject.entity.User;
import com.example.theadsproject.retrofit.ApiService;
import com.example.theadsproject.retrofit.RetrofitClient;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {
    private final Context context;
    private final List<PostResponse> postList;
    private final ApiService apiService = RetrofitClient.getApiService();

    public PostAdapter(Context context, List<PostResponse> postList) {
        this.context = context;
        this.postList = postList;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        PostResponse post = postList.get(position);

        // Hiển thị thông tin cơ bản
        holder.txtNickName.setText(post.getUser().getNickName());
        holder.txtTextPost.setText(post.getContent());

        // Hiển thị thời gian
        LocalDateTime createdAt = post.getCreatedAt();
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
                .load(post.getUser().getImage())
                .placeholder(R.drawable.user)
                .error(R.drawable.user)
                .apply(RequestOptions.circleCropTransform())
                .into(holder.imgAvatar);

        // Hiển thị ảnh nếu có
        List<String> mediaUrls = post.getMediaUrls();
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

        if (TextUtils.isEmpty(post.getContent())) {
            holder.txtTextPost.setVisibility(View.GONE);
        } else {
            holder.txtTextPost.setVisibility(View.VISIBLE);
        }

        // Xử lý nút menu ba chấm (cấu hình bài viết)
        holder.imgDots.setOnClickListener(v -> {
            ConfigPostFragment bottomSheet = ConfigPostFragment.newInstance(post.getPostId(), this::removePost);
            bottomSheet.show(((AppCompatActivity) context).getSupportFragmentManager(), bottomSheet.getTag());
        });

        // Lấy thông tin người dùng hiện tại
        UserSessionManager sessionManager = new UserSessionManager(context);
        User currentUser = sessionManager.getUser();
        Long currentUserId = currentUser.getUserId();

        // Bấm thả tim
        holder.ivLove.setOnClickListener(v -> {
            boolean isLoved = post.isLoved();

            if (isLoved) {
                apiService.unlikePost(currentUserId, post.getPostId()).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        holder.ivLove.setImageResource(R.drawable.heart);
                        post.setLoved(false);
                        updateLikeCount(holder, post.getPostId());
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) { }
                });
            } else {
                apiService.likePost(currentUserId, post.getPostId()).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        holder.ivLove.setImageResource(R.drawable.heart_red);
                        post.setLoved(true);
                        updateLikeCount(holder, post.getPostId());
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) { }
                });
            }
        });

        // Kiểm tra trạng thái đã like
        apiService.isPostLiked(currentUserId, post.getPostId()).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                boolean isLiked = response.body() != null && response.body();
                post.setLoved(isLiked);
                holder.ivLove.setImageResource(isLiked ? R.drawable.heart_red : R.drawable.heart);
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) { }
        });

        // Hiển thị số lượng like
        apiService.countLikes(post.getPostId()).enqueue(new Callback<Long>() {
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
        apiService.countComments(post.getPostId()).enqueue(new Callback<Long>() {
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

        // Xử lý khi click vào post để xem chi tiết
        holder.clItemPost.setOnClickListener(v -> {
            Intent intent = new Intent(context, PostDetailActivity.class);
            intent.putExtra("postId", post.getPostId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return postList != null ? postList.size() : 0;
    }

    // ViewHolder class
    public static class PostViewHolder extends RecyclerView.ViewHolder {
        TextView txtNickName, txtTextPost, txtTime, tvLove, tvConversation;
        ImageView imgAvatar, imgDots, ivLove;
        RecyclerView recyclerViewImages;
        ConstraintLayout clItemPost;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNickName = itemView.findViewById(R.id.tvNickname);
            txtTextPost = itemView.findViewById(R.id.tvTextPost);
            txtTime = itemView.findViewById(R.id.tvTimePost);
            imgAvatar = itemView.findViewById(R.id.ivUserAvatar);
            imgDots = itemView.findViewById(R.id.ivDots);
            recyclerViewImages = itemView.findViewById(R.id.rvImages);
            clItemPost = itemView.findViewById(R.id.clItemPost);
            ivLove = itemView.findViewById(R.id.ivLove);
            tvLove = itemView.findViewById(R.id.tvLove);
            tvConversation = itemView.findViewById(R.id.tvConversation);
            recyclerViewImages.setLayoutManager(new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.HORIZONTAL, false));
        }
    }

    // Xóa bài viết khỏi danh sách
    public void removePost(Long postId) {
        int indexToRemove = -1;
        for (int i = 0; i < postList.size(); i++) {
            if (postList.get(i).getPostId().equals(postId)) {
                indexToRemove = i;
                break;
            }
        }
        if (indexToRemove != -1) {
            postList.remove(indexToRemove);
            notifyItemRemoved(indexToRemove);
        }
    }

    // Cập nhật lại số lượng like
    private void updateLikeCount(PostViewHolder holder, Long postId) {
        apiService.countLikes(postId).enqueue(new Callback<Long>() {
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

