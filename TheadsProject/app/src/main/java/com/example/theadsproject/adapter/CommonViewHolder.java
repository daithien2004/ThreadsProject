package com.example.theadsproject.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.theadsproject.R;

public class CommonViewHolder extends RecyclerView.ViewHolder {
    public TextView txtNickName, txtTextPost, txtTime, tvLove, tvConversation;
    public ImageView imgAvatar, imgDots, ivLove;
    public RecyclerView recyclerViewImages;
    public ConstraintLayout clItemPost;

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
        tvLove = itemView.findViewById(R.id.tvLove);
        tvConversation = itemView.findViewById(R.id.tvConversation);
        recyclerViewImages.setLayoutManager(new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.HORIZONTAL, false));
    }
}
