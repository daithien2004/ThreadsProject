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
import com.bumptech.glide.load.resource.bitmap.CenterInside;
import com.bumptech.glide.load.resource.bitmap.FitCenter;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
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
        int imageCount = imageList.size(); // Số lượng ảnh
        int screenWidth = getScreenWidth(); // Lấy thông tin chiều rộng của màn hình

        // Khởi tạo LayoutParams cho ImageView
        ViewGroup.LayoutParams params = holder.imageView.getLayoutParams();

        if (imageCount == 1) {
            // Nếu chỉ có 1 ảnh, hiển thị lớn
            params.width = dpToPx(300);
            params.height = dpToPx(400);

            holder.imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);  // Giữ nguyên tỷ lệ ảnh
        } else {
            // Nếu có nhiều ảnh, giảm kích thước sao cho phù hợp
            int itemWidth = screenWidth / Math.min(imageCount, 3);  // Chia đều tối đa 3 ảnh

            params.width = itemWidth;

            // Giả sử tỷ lệ chiều rộng / chiều cao của ảnh là 1.5 (hoặc bạn có thể điều chỉnh tùy theo ảnh)
            float aspectRatio = 1.5f;  // Điều chỉnh tỷ lệ nếu cần

            // Tính chiều cao sao cho giữ tỷ lệ
            params.height = Math.min(dpToPx(207), (int) (itemWidth * aspectRatio));
        }


        holder.imageView.setLayoutParams(params);
        int cornerRadius = dpToPx(10);
        // Load ảnh từ URL hoặc Uri
        if (image instanceof String) {
            Glide.with(context)
                    .load((String) image)
                    .transform(new CenterCrop(), new RoundedCorners((cornerRadius)))
                    .into(holder.imageView);
        } else if (image instanceof Uri) {
            Glide.with(context)
                    .load((Uri) image)
                    .transform(new CenterCrop(), new RoundedCorners((cornerRadius)))
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
    public static class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.ivPost);
        }
    }
}
