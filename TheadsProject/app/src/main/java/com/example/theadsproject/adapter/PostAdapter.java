package com.example.theadsproject.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.theadsproject.DTO.PostResponse;
import com.example.theadsproject.DTO.UserResponse;
import com.example.theadsproject.R;

import java.util.List;

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
        TextView txtUsername, txtTextPost;
        ImageView imgAvatar;
        RecyclerView recyclerViewImages;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            txtUsername = itemView.findViewById(R.id.txtUsername);
            txtTextPost = itemView.findViewById(R.id.txtTextPost);
            imgAvatar = itemView.findViewById(R.id.imgUser);
            recyclerViewImages = itemView.findViewById(R.id.rvImages);

            recyclerViewImages.setLayoutManager(new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.HORIZONTAL, false));
        }
    }
}
