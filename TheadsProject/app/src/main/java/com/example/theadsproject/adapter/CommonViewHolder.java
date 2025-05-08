package com.example.theadsproject.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.theadsproject.R;

public class CommonViewHolder extends RecyclerView.ViewHolder {
    public TextView txtNickName, txtTextPost, txtTime, tvLove, tvConversation, tvRepost;
    public ImageView imgAvatar, imgDots, ivLove, ivRepost;
    public RecyclerView recyclerViewImages;
    public ConstraintLayout clItemPost;
    public LinearLayout llRepost,llConversation,llLove;

    public CommonViewHolder(@NonNull View itemView) {
        super(itemView);
        txtNickName = itemView.findViewById(R.id.tvNickname);
        txtTextPost = itemView.findViewById(R.id.tvTextPost);
        txtTime = itemView.findViewById(R.id.tvTimePost);
        imgAvatar = itemView.findViewById(R.id.ivUserAvatar);
        imgDots = itemView.findViewById(R.id.ivDots);
        recyclerViewImages = itemView.findViewById(R.id.rvImages);
        clItemPost = itemView.findViewById(R.id.clItemPost);
        ivLove = itemView.findViewById(R.id.ivLove);
        ivRepost = itemView.findViewById(R.id.ivRepost);
        tvLove = itemView.findViewById(R.id.tvLove);
        tvRepost = itemView.findViewById(R.id.tvRepost);
        tvConversation = itemView.findViewById(R.id.tvConversation);
        llRepost = itemView.findViewById(R.id.llRepost);
        llConversation = itemView.findViewById(R.id.llConversation);
        llLove = itemView.findViewById(R.id.llLove);
        recyclerViewImages.setLayoutManager(new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.HORIZONTAL, false));
    }
}
