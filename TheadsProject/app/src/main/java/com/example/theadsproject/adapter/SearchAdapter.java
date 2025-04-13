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
import com.example.theadsproject.R;
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
    private Long currentUserId; // ID của người dùng hiện tại (follower)

    public SearchAdapter(Context context, List<UserResponse> userList, Long currentUserId) {
        this.context = context;
        this.userList = userList;
        this.currentUserId = currentUserId;  // Cần truyền thêm ID của người dùng hiện tại (follower)
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
        Glide.with(context).load(user.getImage()).into(holder.ivUserAvatar);

        // Thiết lập nút Follow/Unfollow
        holder.btnFollow.setText(user.isFollowing() ? "Bỏ theo dõi" : "Theo dõi");
        holder.btnFollow.setBackgroundTintList(ContextCompat.getColorStateList(context,
                user.isFollowing() ? R.color.unfollow_background : R.color.follow_background));
        holder.btnFollow.setTextColor(ContextCompat.getColor(context,
                user.isFollowing() ? R.color.unfollow_text : R.color.follow_text));

        holder.btnFollow.setOnClickListener(v -> {
            boolean currentlyFollowing = user.isFollowing();
            user.setFollowing(!currentlyFollowing);

            // Gọi API để thực hiện theo dõi hoặc bỏ theo dõi
            if (user.isFollowing()) {
                followUser(currentUserId, user.getUserId(), holder); // Call follow
            } else {
                unfollowUser(currentUserId, user.getUserId(), holder); // Call unfollow
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
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

    // Gọi API để theo dõi
    private void followUser(Long followerId, Long followingId, SearchViewHolder holder) {
        ApiService apiService = RetrofitClient.getApiService();
        apiService.followUser(followerId, followingId).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    // Cập nhật UI, thay đổi văn bản nút từ "Follow" thành "Unfollow"
                    holder.btnFollow.setText("Bỏ theo dõi");
                    holder.btnFollow.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.unfollow_background));
                    holder.btnFollow.setTextColor(ContextCompat.getColor(context, R.color.unfollow_text));
                } else {
                    // Xử lý lỗi nếu có
                    Toast.makeText(context, "Follow failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                // Xử lý lỗi kết nối
                Toast.makeText(context, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Gọi API để bỏ theo dõi
    private void unfollowUser(Long followerId, Long followingId, SearchViewHolder holder) {
        ApiService apiService = RetrofitClient.getApiService();
        apiService.unfollowUser(followerId, followingId).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    // Cập nhật UI, thay đổi văn bản nút từ "Unfollow" thành "Follow"
                    holder.btnFollow.setText("Theo dõi");
                    holder.btnFollow.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.follow_background));
                    holder.btnFollow.setTextColor(ContextCompat.getColor(context, R.color.follow_text));
                } else {
                    // Xử lý lỗi nếu có
                    Toast.makeText(context, "Unfollow failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                // Xử lý lỗi kết nối
                Toast.makeText(context, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
