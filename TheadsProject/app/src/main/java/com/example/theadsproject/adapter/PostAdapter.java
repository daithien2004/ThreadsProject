package com.example.theadsproject.adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.theadsproject.DTO.PostResponse;
import com.example.theadsproject.DTO.UserResponse;
import com.example.theadsproject.R;
import com.example.theadsproject.activity.ConfigPostFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {
    private final Context context;
    private final List<PostResponse> postList;

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

        holder.txtUsername.setText(post.getUser().getUsername());
        holder.txtTextPost.setText(post.getContent());
        String createdAt = post.getCreatedAt();
        if (createdAt != null && !createdAt.isEmpty()) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
                sdf.setTimeZone(TimeZone.getTimeZone("UTC")); // Nếu server trả về UTC
                Date date = sdf.parse(createdAt);

                if (date != null) {
                    long timestamp = date.getTime();
                    holder.txtTime.setText(TimeUtils.getTimeAgo(timestamp));
                } else {
                    holder.txtTime.setText("none");
                }
            } catch (ParseException e) {
                e.printStackTrace();
                holder.txtTime.setText("Lỗi định dạng");
            }
        } else {
            holder.txtTime.setText("Không có dữ liệu");
        }
        holder.imgDots.setOnClickListener(v -> {
            ConfigPostFragment bottomSheet = ConfigPostFragment.newInstance(post.getId(), postId -> {
                removePost(postId); // Xóa bài đăng khỏi RecyclerView ngay lập tức
            });
            bottomSheet.show(((AppCompatActivity) context).getSupportFragmentManager(), bottomSheet.getTag());
        });



        UserResponse userResponse = post.getUser(); // Lấy đối tượng user từ post
        Glide.with(context)
                .load(userResponse.getImage())
                .placeholder(R.drawable.user)
                .error(R.drawable.user)
                .into(holder.imgAvatar);

        List<String> mediaUrls = post.getMediaUrls();
        if (mediaUrls == null || mediaUrls.isEmpty()) {
            holder.recyclerViewImages.setVisibility(View.GONE);
        } else {
            holder.recyclerViewImages.setVisibility(View.VISIBLE);
            ImageAdapter imageAdapter = new ImageAdapter(context, mediaUrls);
            holder.recyclerViewImages.setAdapter(imageAdapter);
        }
    }

    @Override
    public int getItemCount() {
        return (postList != null) ? postList.size() : 0;
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder {
        TextView txtUsername, txtTextPost, txtTime;
        ImageView imgAvatar, imgDots;
        RecyclerView recyclerViewImages;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            txtUsername = itemView.findViewById(R.id.txtUsername);
            txtTextPost = itemView.findViewById(R.id.txtTextPost);
            txtTime = itemView.findViewById(R.id.txtTimePost);
            imgAvatar = itemView.findViewById(R.id.imgUser);
            imgDots = itemView.findViewById(R.id.imgDots);
            recyclerViewImages = itemView.findViewById(R.id.rvImages);

            recyclerViewImages.setLayoutManager(new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.HORIZONTAL, false));
        }
    }
    public void removePost(Long postId) {
        int indexToRemove = -1;
        for (int i = 0; i < postList.size(); i++) {
            if (postList.get(i).getId().equals(postId)) {
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
