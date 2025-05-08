package com.example.theadsproject.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.theadsproject.UserSessionManager;
import com.example.theadsproject.activityAccount.PersonalDetailFragment;
import com.example.theadsproject.activityHome.BarActivity;
import com.example.theadsproject.activityPost.PostDetailActivity;
import com.example.theadsproject.commonClass.BindingUtils;
import com.example.theadsproject.commonClass.TimeUtils;
import com.example.theadsproject.dto.BindableContent;
import com.example.theadsproject.dto.PostResponse;
import com.example.theadsproject.R;
import com.example.theadsproject.activityPost.ConfigPostFragment;
import com.example.theadsproject.entity.User;
import com.example.theadsproject.handler.PostLikeHandler;
import com.example.theadsproject.retrofit.ApiService;
import com.example.theadsproject.retrofit.RetrofitClient;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostAdapter extends RecyclerView.Adapter<CommonViewHolder> {
    private final Context context;
    private final List<PostResponse> postList;
    private PostLikeHandler postLikeHandler = new PostLikeHandler();
    private boolean isLoggedIn;
    private final User currentUser;
    ApiService apiService = RetrofitClient.getApiService();

    public PostAdapter(Context context, List<PostResponse> postList) {
        this.context = context;
        this.postList = postList;

        // Kiểm tra trạng thái đăng nhập khi khởi tạo adapter
        UserSessionManager sessionManager = new UserSessionManager();
        this.isLoggedIn = sessionManager.isLoggedIn();
        this.currentUser = isLoggedIn ? sessionManager.getUser() : null;
    }

    @NonNull
    @Override
    public CommonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false);
        return new CommonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommonViewHolder holder, int position) {
        PostResponse post = postList.get(position);
        BindableContent item = postList.get(position);
        BindingUtils.bindCommonData(holder, item, context, postLikeHandler);

        if (!isLoggedIn) {
            // Vô hiệu hóa các chức năng khi chưa đăng nhập
            holder.imgDots.setEnabled(false);
            holder.ivRepost.setEnabled(false);
        } else {
            if (currentUser != null) {
                holder.imgDots.setVisibility(View.VISIBLE);
                holder.imgDots.setOnClickListener(v -> {
                    ConfigPostFragment bottomSheet = ConfigPostFragment.newInstance(
                            ConfigPostFragment.ConfigType.POST,
                            post.getPostId(),
                            this::removePost
                    );
                    bottomSheet.show(((AppCompatActivity) context).getSupportFragmentManager(), bottomSheet.getTag());
                });

                String username = currentUser.getUsername();
                apiService.isPostReposted(post.getPostId(), username).enqueue(new Callback<Boolean>() {
                    @Override
                    public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                        if (response.isSuccessful() && Boolean.TRUE.equals(response.body())) {
                            holder.ivRepost.setImageResource(R.drawable.reposted);
                        } else {
                            holder.ivRepost.setImageResource(R.drawable.retweet);
                        }
                    }
                    @Override
                    public void onFailure(Call<Boolean> call, Throwable t) {
                        holder.ivRepost.setImageResource(R.drawable.retweet);
                    }
                });

                holder.ivRepost.setOnClickListener(v -> {
                    Long postId = post.getPostId();

                    // 1. Kiểm tra đã repost chưa
                    apiService.isPostReposted(postId, username).enqueue(new Callback<Boolean>() {
                        @Override
                        public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                            if (Boolean.TRUE.equals(response.body())) {
                                Toast.makeText(context, "Bạn đã repost bài viết này rồi!", Toast.LENGTH_SHORT).show();
                            } else {
                                // 2. Hiển thị dialog xác nhận repost
                                new AlertDialog.Builder(context)
                                        .setTitle("Repost bài viết?")
                                        .setMessage("Bạn có chắc chắn muốn repost bài viết này?")
                                        .setPositiveButton("Đồng ý", (dialog, which) -> {
                                            // 3. Gọi API thực hiện repost
                                            apiService.repost(postId, username).enqueue(new Callback<Void>() {
                                                @Override
                                                public void onResponse(Call<Void> call, Response<Void> response) {
                                                    if (response.isSuccessful()) {
                                                        Toast.makeText(context, "Repost thành công!", Toast.LENGTH_SHORT).show();

                                                        // Đổi icon sau khi repost
                                                        holder.ivRepost.setImageResource(R.drawable.reposted);

                                                        // 4. Cập nhật lại số lượng repost
                                                        updateRepostCount(postId, holder);
                                                    } else {
                                                        Toast.makeText(context, "Repost thất bại!", Toast.LENGTH_SHORT).show();
                                                    }
                                                }

                                                @Override
                                                public void onFailure(Call<Void> call, Throwable t) {
                                                    Toast.makeText(context, "Lỗi kết nối!", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        })
                                        .setNegativeButton("Hủy", null)
                                        .show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Boolean> call, Throwable t) {
                            Toast.makeText(context, "Không thể kiểm tra trạng thái repost!", Toast.LENGTH_SHORT).show();
                        }
                    });
                });
            }
        }

        // Xử lý khi click vào post để xem chi tiết
        holder.clItemPost.setOnClickListener(v -> {
            Intent intent = new Intent(context, PostDetailActivity.class);
            intent.putExtra("postId", post.getPostId());
            context.startActivity(intent);
        });


        // Gọi riêng ở ngoài để luôn hiển thị đúng số lượng repost khi load item
        updateRepostCount(post.getPostId(), holder);

        //////// xem trang cá nhân của user khi click vào avatar
        holder.imgAvatar.setOnClickListener(v -> {
            long authorId = post.getUser().getUserId();
            if (context instanceof BarActivity) {
                ((BarActivity) context).replaceFragment(
                        PersonalDetailFragment.newInstance(authorId)
                );
            }
        });
    }

    private void updateRepostCount(Long postId, CommonViewHolder holder) {
        ApiService apiService = RetrofitClient.getApiService();

        apiService.countReposts(postId).enqueue(new Callback<Long>() {
            @Override
            public void onResponse(Call<Long> call, Response<Long> response) {
                if (response.isSuccessful() && response.body() != null) {
                    holder.tvRepost.setText(String.valueOf(response.body()));
                } else {
                    holder.tvRepost.setText("0");
                }
            }

            @Override
            public void onFailure(Call<Long> call, Throwable t) {
                holder.tvRepost.setText("0");
            }
        });
    }

    @Override
    public int getItemCount() {
        return postList != null ? postList.size() : 0;
    }

    // Xóa bài viết khỏi danh sách
    public void removePost(ConfigPostFragment.ConfigType type, Long postId) {
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
}
