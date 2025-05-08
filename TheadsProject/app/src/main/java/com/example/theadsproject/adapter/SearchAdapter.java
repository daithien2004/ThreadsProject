package com.example.theadsproject.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.theadsproject.R;
import com.example.theadsproject.UserSessionManager;
import com.example.theadsproject.activityAccount.PersonalDetailFragment;
import com.example.theadsproject.activityHome.BarActivity;
import com.example.theadsproject.dto.UserResponse;
import com.example.theadsproject.retrofit.ApiService;
import com.example.theadsproject.retrofit.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {
    private Context context;
    private List<UserResponse> userList;
    private Long currentUserId;
    private UserSessionManager sessionManager;

    public SearchAdapter(Context context, List<UserResponse> userList, Long currentUserId) {
        this.context = context;
        this.userList = userList;
        this.currentUserId = currentUserId;
    }

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_user_search, parent, false);
        return new SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
        UserResponse user = userList.get(position);
        holder.txtUsername.setText(user.getUsername());
        holder.txtNickName.setText(user.getNickName());
        holder.txtCountFollowers.setText(String.valueOf(user.getFollowerCount()));
        Glide.with(context).load(user.getImage()).placeholder(R.drawable.user)
                .error(R.drawable.user).apply(RequestOptions.circleCropTransform()).into(holder.ivUserAvatar);

        //////// xem trang cá nhân của user khi click vào avatar
        holder.ivUserAvatar.setOnClickListener(v -> {
            long authorId = user.getUserId();
            if (context instanceof BarActivity) {
                ((BarActivity) context).replaceFragment(
                        PersonalDetailFragment.newInstance(authorId)
                );
            }
        });

        sessionManager = new UserSessionManager();

        if (sessionManager.isLoggedIn()) {
            if (user.getUserId().equals(currentUserId)) {
                holder.btnFollow.setVisibility(View.GONE);
            } else {
                holder.btnFollow.setVisibility(View.VISIBLE);
                updateFollowButtonUI(holder, user.isFollowing());

                holder.btnFollow.setOnClickListener(v -> {
                    if (user.isFollowing()) {
                        unfollowUser(currentUserId, user.getUserId(), holder.getAdapterPosition());
                    } else {
                        followUser(currentUserId, user.getUserId(), holder.getAdapterPosition());
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    private void updateFollowButtonUI(SearchViewHolder holder, boolean isFollowing) {
        holder.btnFollow.setText(isFollowing ? "Bỏ theo dõi" : "Theo dõi");
        holder.btnFollow.setBackgroundTintList(ContextCompat.getColorStateList(context,
                isFollowing ? R.color.unfollow_background : R.color.follow_background));
        holder.btnFollow.setTextColor(ContextCompat.getColor(context,
                isFollowing ? R.color.unfollow_text : R.color.follow_text));
    }

    private void followUser(Long followerId, Long followingId, int position) {
        ApiService apiService = RetrofitClient.getApiService();
        apiService.followUser(followerId, followingId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    userList.get(position).setFollowing(true);
                    notifyItemChanged(position);
                } else {
                    Toast.makeText(context, "Theo dõi thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(context, "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void unfollowUser(Long followerId, Long followingId, int position) {
        ApiService apiService = RetrofitClient.getApiService();
        apiService.unfollowUser(followerId, followingId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    userList.get(position).setFollowing(false);
                    notifyItemChanged(position);
                } else {
                    Toast.makeText(context, "Bỏ theo dõi thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(context, "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public class SearchViewHolder extends RecyclerView.ViewHolder {
        TextView txtUsername, txtNickName, txtCountFollowers;
        ImageView ivUserAvatar;
        Button btnFollow;

        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);
            txtUsername = itemView.findViewById(R.id.txtUsername);
            txtNickName = itemView.findViewById(R.id.txtNickName);
            txtCountFollowers = itemView.findViewById(R.id.txtCountFollowers);
            ivUserAvatar = itemView.findViewById(R.id.ivUserAvatar);
            btnFollow = itemView.findViewById(R.id.btnFollow);
        }
    }
}
