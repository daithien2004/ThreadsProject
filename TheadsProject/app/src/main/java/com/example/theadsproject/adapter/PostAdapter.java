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
    private final ApiService apiService = RetrofitClient.getApiService();
    private PostLikeHandler postLikeHandler = new PostLikeHandler();

    public PostAdapter(Context context, List<PostResponse> postList) {
        this.context = context;
        this.postList = postList;
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


        // Xử lý nút menu ba chấm (cấu hình bài viết)
        holder.imgDots.setOnClickListener(v -> {
            ConfigPostFragment bottomSheet = ConfigPostFragment.newInstance(ConfigPostFragment.ConfigType.POST, post.getPostId(), this::removePost);
            bottomSheet.show(((AppCompatActivity) context).getSupportFragmentManager(), bottomSheet.getTag());
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

