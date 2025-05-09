package com.example.theadsproject.adapter;

import static androidx.core.util.TypedValueCompat.dpToPx;

import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.theadsproject.R;
import com.example.theadsproject.dto.NotificationResponse;
import com.example.theadsproject.entity.Notification;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotiViewHolder> {
    private List<NotificationResponse> list;

    public NotificationAdapter(List<NotificationResponse> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public NotiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification, parent, false);
        return new NotiViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotiViewHolder holder, int position) {
        NotificationResponse noti = list.get(position);

        holder.tvName.setText(noti.getSenderName());
        holder.tvContent.setText(getContentFromType(noti.getType()));
        holder.tvTime.setText(getTimeAgo(noti.getCreatedAt()));

        int cornerRadius = dpToPx(20);

        Glide.with(holder.imgAvatar.getContext())
                .load(noti.getSenderAvatar())
                .transform(new CenterCrop(), new RoundedCorners((cornerRadius)))
                .placeholder(R.drawable.user)
                .into(holder.imgAvatar);
    }

    private int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    private String getContentFromType(String type) {
        switch (type) {
            case "like":
                return "đã thích bài viết của bạn";
            case "comment":
                return "đã bình luận bài viết của bạn";
            case "follow":
                return "đã theo dõi bạn";
            default:
                return "có hoạt động mới";
        }
    }

    private String getTimeAgo(String createdAt) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.getDefault());
            Date date = sdf.parse(createdAt);
            if (date == null) return "vừa xong";

            long diff = System.currentTimeMillis() - date.getTime();
            long seconds = diff / 1000;
            long minutes = seconds / 60;
            long hours = minutes / 60;
            long days = hours / 24;

            if (days > 0) return days + " ngày trước";
            else if (hours > 0) return hours + " giờ trước";
            else if (minutes > 0) return minutes + " phút trước";
            else return "vừa xong";
        } catch (ParseException e) {
            e.printStackTrace();
            return "vừa xong";
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    public void updateData(List<NotificationResponse> newList) {
        list.clear();
        list.addAll(newList);
        notifyDataSetChanged();
    }

    public static class NotiViewHolder extends RecyclerView.ViewHolder {
        ImageView imgAvatar;
        TextView tvName, tvContent, tvTime;

        public NotiViewHolder(@NonNull View itemView) {
            super(itemView);
            imgAvatar = itemView.findViewById(R.id.imgAvatar);
            tvName = itemView.findViewById(R.id.tvName);
            tvContent = itemView.findViewById(R.id.tvContent);
            tvTime = itemView.findViewById(R.id.tvTime);
        }
    }
}
