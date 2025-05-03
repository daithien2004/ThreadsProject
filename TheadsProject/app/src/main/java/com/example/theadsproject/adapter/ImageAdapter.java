package com.example.theadsproject.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
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
import com.example.theadsproject.commonClass.FullScreenImageActivity;

import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {
    private final Context context;
    private final List<Object> imageList; // Có thể là Uri hoặc String
    private OnDataChangedListener onDataChangedListener;

    // Constructor
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
        int imageCount = imageList.size();
        int screenWidth = getScreenWidth();

        // Khởi tạo LayoutParams cho ImageView
        ViewGroup.LayoutParams params = holder.imageView.getLayoutParams();

        // Xử lý kích thước ảnh
        if (imageCount == 1) {
            params.width = dpToPx(300);
            params.height = dpToPx(400);
        } else {
            int itemWidth = screenWidth / Math.min(imageCount, 3);
            params.width = itemWidth;
            float aspectRatio = 1.5f;
            params.height = Math.min(dpToPx(207), (int) (itemWidth * aspectRatio));
        }

        holder.imageView.setLayoutParams(params);
        int cornerRadius = dpToPx(10);

        // Xử lý loading ảnh
        String imageUrl;
        if (image instanceof String) {
            imageUrl = (String) image; // Đây là Cloudinary URL từ database
        } else if (image instanceof Uri) {
            imageUrl = image.toString(); // Đây là URI local khi user chọn ảnh mới
        } else {
            imageUrl = null;
        }

        // Load ảnh với Glide
        if (imageUrl != null) {
            Glide.with(context)
                    .load(imageUrl)
                    .transform(new CenterCrop(), new RoundedCorners(cornerRadius))
                    .into(holder.imageView);
        }

        // Thêm sự kiện click vào ảnh để mở toàn màn hình
        holder.imageView.setOnClickListener(v -> {
            Intent intent = new Intent(context, FullScreenImageActivity.class);

            if (image instanceof String) {
                intent.putExtra("IMAGE_URL", (String) image);
            } else if (image instanceof Uri) {
                intent.putExtra("IMAGE_URL", image.toString()); // Chuyển Uri thành String
            }

            context.startActivity(intent);
        });
    }
    private int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    private int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
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
    // ViewHolder
    public static class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.ivPost);
        }
    }
}
