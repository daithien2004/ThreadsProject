package com.example.theadsproject.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.theadsproject.R;

import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {
    private Context context;
    private List<Object> imageList; // Chứa cả Uri và String
    private OnDataChangedListener onDataChangedListener;

    public ImageAdapter(Context context, List<Object> imageList) {
        this.context = context;
        this.imageList = imageList;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_image, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        Object image = imageList.get(position);

        if (image instanceof String) {
            // Load ảnh từ URL
            Glide.with(context)
                    .load((String) image)
                    .placeholder(R.drawable.loading)
                    .error(R.drawable.warning)
                    .into(holder.imageView);
        } else if (image instanceof Uri) {
            // Load ảnh từ Uri (ảnh local)
            Glide.with(context)
                    .load((Uri) image)
                    .placeholder(R.drawable.loading)
                    .error(R.drawable.warning)
                    .into(holder.imageView);
        }
    }
    public void setOnDataChangedListener(OnDataChangedListener listener) {
        this.onDataChangedListener = listener;
    }

    public void addImage(String imagePath) {
        imageList.add(imagePath);
        notifyDataSetChanged();
        if (onDataChangedListener != null) {
            onDataChangedListener.onDataChanged();
        }
    }
    @Override
    public int getItemCount() {
        return imageList.size();
    }
    public interface OnDataChangedListener {
        void onDataChanged();
    }
    public static class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imgPost);
        }
    }
}
