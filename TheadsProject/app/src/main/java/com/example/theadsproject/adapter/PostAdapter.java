package com.example.theadsproject.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.theadsproject.R;
import com.example.theadsproject.entity.Post;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {
    private Context context;
    private List<Post> postList;

    public PostAdapter(Context context, List<Post> postList) {
        this.context = context;
        this.postList = postList;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_post, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post post = postList.get(position);

        holder.txtUsername.setText(post.getUsername());
        holder.txtTimePost.setText(post.getTime());
        holder.txtTextPost.setText(post.getContent());

        // Kiểm tra nếu có ảnh => Hiển thị, nếu không => Ẩn
        if (post.getImageUrl() != null && !post.getImageUrl().isEmpty()) {
            holder.imgPosts.setVisibility(View.VISIBLE);
            Glide.with(context).load(post.getImageUrl()).into(holder.imgPosts);
        } else {
            holder.imgPosts.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder {
        TextView txtUsername, txtTimePost, txtTextPost;
        ImageView imgPosts;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            txtUsername = itemView.findViewById(R.id.txtUsername);
            txtTimePost = itemView.findViewById(R.id.txtTimePost);
            txtTextPost = itemView.findViewById(R.id.txtTextPost);
            imgPosts = itemView.findViewById(R.id.imgPosts);
        }
    }
}
