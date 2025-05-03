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
import com.example.theadsproject.R;
import com.example.theadsproject.activityPost.CommentDetailActivity;
import com.example.theadsproject.activityPost.ConfigPostFragment;
import com.example.theadsproject.activityPost.PostDetailActivity;
import com.example.theadsproject.commonClass.TimeUtils;
import com.example.theadsproject.dto.CommentResponse;
import com.example.theadsproject.dto.PostResponse;
import com.example.theadsproject.dto.UserResponse;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {
    private final Context context;
    private final List<CommentResponse> commentList;

    public CommentAdapter(Context context, List<CommentResponse> commentList) {
        this.context = context;
        this.commentList = commentList;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false);
        return new CommentAdapter.CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        CommentResponse comment = commentList.get(position);

        holder.txtNickName.setText(comment.getUser().getNickName());
        holder.txtTextPost.setText(comment.getContent());
        LocalDateTime createdAt = comment.getCreateAt();
        if (createdAt != null) {
            try {
                // Chuyển LocalDateTime thành timestamp (milliseconds)
                long timestamp = createdAt.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
                holder.txtTime.setText(TimeUtils.getTimeAgo(timestamp));
            } catch (Exception e) {
                e.printStackTrace();
                holder.txtTime.setText("Lỗi định dạng thời gian");
            }
        } else {
            holder.txtTime.setText("Không có dữ liệu");
        }


        UserResponse userResponse = comment.getUser(); // Lấy đối tượng user từ post
        Glide.with(context)
                .load(userResponse.getImage())
                .placeholder(R.drawable.user)
                .error(R.drawable.user)
                .apply(RequestOptions.circleCropTransform())
                .into(holder.imgAvatar);

        List<String> mediaUrls = comment.getMediaUrls();
        if (mediaUrls == null || mediaUrls.isEmpty()) {
            holder.recyclerViewImages.setVisibility(View.GONE);
        } else {
            holder.recyclerViewImages.setVisibility(View.VISIBLE);

            // Chuyển các URL Cloudinary thành đối tượng để ImageAdapter có thể xử lý
            // ImageAdapter sẽ xử lý String URL
            List<Object> imageObjects = new ArrayList<>(mediaUrls);

            ImageAdapter imageAdapter = new ImageAdapter(context, imageObjects);
            holder.recyclerViewImages.setAdapter(imageAdapter);
        }

        // Sửa lại click listener cho comment
        holder.clItemPost.setOnClickListener(v -> {
            Intent intent = new Intent(context, CommentDetailActivity.class);
            intent.putExtra("commentId", comment.getCommentId());
            intent.putExtra("postId", comment.getPost().getPostId());
            context.startActivity(intent);
        });


        ///// xử lí khi văn bản quá dài
        holder.txtTextPost.setText(comment.getContent());
        // Giới hạn số dòng ban đầu
        holder.txtTextPost.setMaxLines(2);
        holder.txtTextPost.setEllipsize(TextUtils.TruncateAt.END);

        // Xử lý khi người dùng nhấn vào để mở rộng nội dung
        holder.txtTextPost.setOnClickListener(v -> {
            if (holder.txtTextPost.getMaxLines() == 2) {
                holder.txtTextPost.setMaxLines(Integer.MAX_VALUE); // Mở rộng full văn bản
                holder.txtTextPost.setEllipsize(null);
            } else {
                holder.txtTextPost.setMaxLines(2); // Thu gọn lại
                holder.txtTextPost.setEllipsize(TextUtils.TruncateAt.END);
            }
        });

        //// Kiểm tra xem post.getContent() có rỗng không
        if (TextUtils.isEmpty(comment.getContent())) {
            holder.txtTextPost.setVisibility(View.GONE); // Ẩn TextView nếu không có nội dung
        } else {
            holder.txtTextPost.setVisibility(View.VISIBLE); // Hiển thị TextView nếu có nội dung
        }
    }

    @Override
    public int getItemCount() {
        return (commentList != null) ? commentList.size() : 0;
    }

    public static class CommentViewHolder extends RecyclerView.ViewHolder {
        TextView txtNickName, txtTextPost, txtTime;
        ImageView imgAvatar, imgDots;
        RecyclerView recyclerViewImages;
        ConstraintLayout clItemPost;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNickName = itemView.findViewById(R.id.tvNickname);
            txtTextPost = itemView.findViewById(R.id.tvTextPost);
            txtTime = itemView.findViewById(R.id.tvTimePost);
            imgAvatar = itemView.findViewById(R.id.ivUserAvatar);
            imgDots = itemView.findViewById(R.id.ivDots);
            recyclerViewImages = itemView.findViewById(R.id.rvImages);
            clItemPost = itemView.findViewById(R.id.clItemPost);

            recyclerViewImages.setLayoutManager(new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.HORIZONTAL, false));
        }

    }
}
