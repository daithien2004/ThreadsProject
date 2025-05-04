package com.example.theadsproject.activityPost;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.theadsproject.R;
import com.example.theadsproject.adapter.ImageAdapter;
import com.example.theadsproject.adapter.PostAdapter;
import com.example.theadsproject.commonClass.TimeUtils;
import com.example.theadsproject.dto.BindableContent;
import com.example.theadsproject.dto.PostResponse;
import com.example.theadsproject.handler.CommentLikeHandler;
import com.example.theadsproject.handler.LikeHandler;
import com.example.theadsproject.handler.PostLikeHandler;
import com.example.theadsproject.retrofit.ApiService;
import com.example.theadsproject.retrofit.RetrofitClient;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostItemView {
    private View rootView;
    private Context context;
    private TextView tvNickname, tvTextPost, tvTimePost, tvLove, tvConversation;
    private ImageView imgAvatar, ivLove, ivConversation, imgDots;
    private RecyclerView rvImages;
    private final ApiService apiService = RetrofitClient.getApiService();

    private LikeHandler likeHandler;
    private ConfigPostFragment.ConfigType type;

    public PostItemView(View view, Context context, LikeHandler likeHandler) {
        rootView = view;
        tvNickname = view.findViewById(R.id.tvNickname);
        tvTextPost = view.findViewById(R.id.tvTextPost);
        tvTimePost = view.findViewById(R.id.tvTimePost);
        imgAvatar = view.findViewById(R.id.ivUserAvatar);
        rvImages = view.findViewById(R.id.rvImages);
        ivLove = view.findViewById(R.id.ivLove);
        ivConversation = view.findViewById(R.id.ivConversation);
        tvLove = view.findViewById(R.id.tvLove);
        tvConversation = view.findViewById(R.id.tvConversation);
        imgDots = view.findViewById(R.id.ivDots);
        this.context = context;
        this.likeHandler = likeHandler;
    }

    public void bind(BindableContent item, Context context) {

        if (likeHandler instanceof CommentLikeHandler) {
            type = ConfigPostFragment.ConfigType.COMMENT;
        } else if (likeHandler instanceof PostLikeHandler) {
            type = ConfigPostFragment.ConfigType.COMMENT;
        }

        Long userId = item.getUser().getUserId();
        tvNickname.setText(item.getUser().getNickName());
        tvTextPost.setText(item.getContent());

        if (item.getContent() != null && !item.getContent().isEmpty()) {
            tvTextPost.setVisibility(View.VISIBLE);
        } else {
            tvTextPost.setVisibility(View.GONE);
        }

        if (item.getCreatedAt() != null) {
            long timestamp = item.getCreatedAt()
                    .atZone(ZoneId.of("UTC")) // Dữ liệu từ server là UTC
                    .toInstant()
                    .toEpochMilli() ; // Cộng thêm 7 tiếng

            tvTimePost.setText(TimeUtils.getTimeAgo(timestamp));
        }


        likeHandler.checkIfLiked(userId, item.getId(), new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                boolean isLiked = response.body() != null && response.body();
                item.setIsLoved(isLiked);
                ivLove.setImageResource(isLiked ? R.drawable.heart_red : R.drawable.heart);
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) { }
        });

        // Bấm thả tim
        ivLove.setOnClickListener(v -> {
            boolean isLoved = item.getIsLoved();

            if (isLoved) {
                likeHandler.unlike(userId, item.getId(), new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        ivLove.setImageResource(R.drawable.heart);
                        item.setIsLoved(false);
                        updateLikeCount(item.getId());
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) { }
                });
            } else {
                likeHandler.like(userId, item.getId(), new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        ivLove.setImageResource(R.drawable.heart_red);
                        item.setIsLoved(true);
                        updateLikeCount(item.getId());
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) { }
                });
            }
        });

        // Hiển thị số lượng like
        likeHandler.countLikes(item.getId(), new Callback<Long>() {
            @Override
            public void onResponse(Call<Long> call, Response<Long> response) {
                if (response.isSuccessful() && response.body() != null) {
                    tvLove.setText(String.valueOf(response.body()));
                }
            }

            @Override
            public void onFailure(Call<Long> call, Throwable t) {
                tvLove.setText("0");
            }
        });

        // Hiển thị số lượng comment
        likeHandler.countComments(item.getId(), new Callback<Long>() {
            @Override
            public void onResponse(Call<Long> call, Response<Long> response) {
                if (response.isSuccessful() && response.body() != null) {
                    tvConversation.setText(String.valueOf(response.body()));
                }
            }

            @Override
            public void onFailure(Call<Long> call, Throwable t) {
                tvConversation.setText("0");
            }
        });

        // Thu gọn / mở rộng nội dung
        tvTextPost.setMaxLines(2);
        tvTextPost.setEllipsize(TextUtils.TruncateAt.END);
        tvTextPost.setOnClickListener(v -> {
            boolean expanded = tvTextPost.getMaxLines() != 2;
            tvTextPost.setMaxLines(expanded ? 2 : Integer.MAX_VALUE);
            tvTextPost.setEllipsize(expanded ? TextUtils.TruncateAt.END : null);
        });

        if (TextUtils.isEmpty(item.getContent())) {
            tvTextPost.setVisibility(View.GONE);
        } else {
            tvTextPost.setVisibility(View.VISIBLE);
        }

        // Xử lý nút menu ba chấm (cấu hình bài viết)
        imgDots.setOnClickListener(v -> {
            ConfigPostFragment bottomSheet = ConfigPostFragment.newInstance(ConfigPostFragment.ConfigType.COMMENT, item.getId(), this::remove);
            bottomSheet.show(((AppCompatActivity) context).getSupportFragmentManager(), bottomSheet.getTag());
        });

        Glide.with(context)
                .load(item.getUser().getImage())
                .placeholder(R.drawable.user)
                .error(R.drawable.user)
                .apply(RequestOptions.circleCropTransform())
                .into(imgAvatar);

        if (item.getMediaUrls() != null && !item.getMediaUrls().isEmpty()) {
            rvImages.setVisibility(View.VISIBLE);
            rvImages.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            rvImages.setAdapter(new ImageAdapter(context, new ArrayList<>(item.getMediaUrls())));
        } else {
            rvImages.setVisibility(View.GONE);
        }
    }

    public View getRootView() {
        return rootView;
    }

    // Cập nhật lại số lượng like
    private void updateLikeCount(Long itemId) {
        likeHandler.countLikes(itemId, new Callback<Long>() {
            @Override
            public void onResponse(Call<Long> call, Response<Long> response) {
                if (response.body() != null) {
                    tvLove.setText(String.valueOf(response.body()));
                }
            }

            @Override
            public void onFailure(Call<Long> call, Throwable t) { }
        });
    }

    public void remove(ConfigPostFragment.ConfigType type, Long id) {
        if (context != null && context instanceof AppCompatActivity) {
            ((AppCompatActivity) context).finish();
        }
    }
}

