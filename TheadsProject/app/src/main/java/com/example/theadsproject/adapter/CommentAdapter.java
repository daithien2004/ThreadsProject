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
import com.example.theadsproject.commonClass.BindingUtils;
import com.example.theadsproject.commonClass.TimeUtils;
import com.example.theadsproject.dto.BindableContent;
import com.example.theadsproject.dto.CommentResponse;
import com.example.theadsproject.dto.UserResponse;
import com.example.theadsproject.handler.CommentLikeHandler;

import org.w3c.dom.Comment;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommonViewHolder> {
    private final Context context;
    private final List<CommentResponse> commentList;
    private CommentLikeHandler commentLikeHandler = new CommentLikeHandler();

    public CommentAdapter(Context context, List<CommentResponse> commentList) {
        this.context = context;
        this.commentList = commentList;
    }

    @NonNull
    @Override
    public CommonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false);
        return new CommonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommonViewHolder holder, int position) {
        BindableContent item = commentList.get(position);
        CommentResponse comment = commentList.get(position);
        BindingUtils.bindCommonData(holder, item, context, commentLikeHandler);

        // Xử lý nút menu ba chấm (cấu hình bài viết)
        holder.imgDots.setOnClickListener(v -> {
            ConfigPostFragment bottomSheet = ConfigPostFragment.newInstance(ConfigPostFragment.ConfigType.COMMENT, comment.getCommentId(), this::removeComment);
            bottomSheet.show(((AppCompatActivity) context).getSupportFragmentManager(), bottomSheet.getTag());
        });

        holder.llRepost.setVisibility(View.INVISIBLE);

        // Sửa lại click listener cho comment
        holder.clItemPost.setOnClickListener(v -> {
            Intent intent = new Intent(context, CommentDetailActivity.class);
            intent.putExtra("commentId", comment.getId());
            intent.putExtra("postId", comment.getPost().getPostId());
            context.startActivity(intent);
        });
    }

    public void removeComment(ConfigPostFragment.ConfigType type, Long commentId) {
        int indexToRemove = -1;
        for (int i = 0; i < commentList.size(); i++) {
            if (commentList.get(i).getCommentId().equals(commentId)) {
                indexToRemove = i;
                break;
            }
        }
        if (indexToRemove != -1) {
            commentList.remove(indexToRemove);
            notifyItemRemoved(indexToRemove);
        }
    }

    @Override
    public int getItemCount() {
        return (commentList != null) ? commentList.size() : 0;
    }

}
