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
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.theadsproject.R;

import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {
    private Context context;
    private List<Object> imageList; // Chứa cả Uri và String

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
                    .transform(new CenterCrop(), new RoundedCorners(10))
                    .into(holder.imageView);
        } else if (image instanceof Uri) {
            // Load ảnh từ Uri (ảnh local)
            Glide.with(context)
                    .load((Uri) image)
                    .placeholder(R.drawable.loading)
                    .error(R.drawable.warning)
                    .transform(new CenterCrop(), new RoundedCorners(10))
                    .into(holder.imageView);
        }
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.ivPost);
        }
    }
}
