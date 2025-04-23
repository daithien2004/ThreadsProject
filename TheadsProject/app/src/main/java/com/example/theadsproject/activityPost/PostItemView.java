package com.example.theadsproject.activityPost;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.theadsproject.R;
import com.example.theadsproject.adapter.ImageAdapter;
import com.example.theadsproject.commonClass.TimeUtils;
import com.example.theadsproject.dto.PostResponse;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class PostItemView {
    private View rootView;
    private TextView tvNickname, tvTextPost, tvTimePost;
    private ImageView imgAvatar;
    private RecyclerView rvImages;

    public PostItemView(View view) {
        rootView = view;
        tvNickname = view.findViewById(R.id.tvNickname);
        tvTextPost = view.findViewById(R.id.tvTextPost);
        tvTimePost = view.findViewById(R.id.tvTimePost);
        imgAvatar = view.findViewById(R.id.ivUserAvatar);
        rvImages = view.findViewById(R.id.rvImages);
    }

    public void bind(PostResponse post, Context context) {
        tvNickname.setText(post.getUser().getNickName());
        tvTextPost.setText(post.getContent());

        if (post.getContent() != null && !post.getContent().isEmpty()) {
            tvTextPost.setVisibility(View.VISIBLE);
        } else {
            tvTextPost.setVisibility(View.GONE);
        }

        //long timestamp = post.getCreatedAt().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        long timestamp = post.getCreatedAt()
                .atZone(ZoneId.of("UTC")) // Dữ liệu từ server là UTC
                .toInstant()
                .toEpochMilli() ; // Cộng thêm 7 tiếng

        tvTimePost.setText(TimeUtils.getTimeAgo(timestamp));
        // Ép nó là UTC



        // Truyền vào để tính thời gian
        tvTimePost.setText(TimeUtils.getTimeAgo(timestamp));

        Glide.with(context)
                .load(post.getUser().getImage())
                .placeholder(R.drawable.user)
                .error(R.drawable.user)
                .apply(RequestOptions.circleCropTransform())
                .into(imgAvatar);

        if (post.getMediaUrls() != null && !post.getMediaUrls().isEmpty()) {
            rvImages.setVisibility(View.VISIBLE);
            rvImages.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            rvImages.setAdapter(new ImageAdapter(context, new ArrayList<>(post.getMediaUrls())));
        } else {
            rvImages.setVisibility(View.GONE);
        }
    }

    public View getRootView() {
        return rootView;
    }
}

