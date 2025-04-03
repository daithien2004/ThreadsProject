package com.example.theadsproject.adapter;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.theadsproject.R;

public class FullScreenImageActivity extends AppCompatActivity {

    private ImageView fullScreenImageView;
    private GestureDetector gestureDetector;
    private float initialTouchX;
    private float initialTouchY;
    private float dx, dy; // Delta X, Y for movement
    private boolean isImageMoving = false;

    private static final float SWIPE_THRESHOLD = 300f; // Kích thước ngưỡng để ảnh quay lại
    private static final long ANIMATION_DURATION = 300; // Thời gian quay lại

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen_image);

        fullScreenImageView = findViewById(R.id.ivfullscreen);

        // Lấy đường dẫn ảnh từ intent
        String imageUrl = getIntent().getStringExtra("IMAGE_URL");

        // Tải ảnh vào ImageView
        Glide.with(this)
                .load(imageUrl)
                .placeholder(R.drawable.loading)
                .error(R.drawable.warning)
                .into(fullScreenImageView);

        // GestureDetector để phát hiện vuốt
        gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                // Di chuyển ảnh khi vuốt
                if (isImageMoving) {
                    float newX = fullScreenImageView.getX() - distanceX;
                    float newY = fullScreenImageView.getY() - distanceY;

                    // Cập nhật vị trí ảnh
                    fullScreenImageView.setX(newX);
                    fullScreenImageView.setY(newY);

                    // Lưu các giá trị delta
                    dx = newX - initialTouchX;
                    dy = newY - initialTouchY;
                }
                return true;
            }
        });

        // Bắt sự kiện chạm vào ảnh
        fullScreenImageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gestureDetector.onTouchEvent(event);
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();
                        isImageMoving = true;
                        return true;

                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        isImageMoving = false;

                        // Kiểm tra nếu ảnh vượt qua phạm vi
                        if (Math.abs(dx) > SWIPE_THRESHOLD || Math.abs(dy) > SWIPE_THRESHOLD) {
                            closeImage();  // Quay lại khi vuốt quá phạm vi
                        } else {
                            resetImagePosition(); // Quay về vị trí ban đầu
                        }
                        return true;

                    default:
                        return false;
                }
            }
        });
    }

    // Hàm reset ảnh về vị trí ban đầu khi thả tay
    private void resetImagePosition() {
        ObjectAnimator animatorX = ObjectAnimator.ofFloat(fullScreenImageView, "translationX", fullScreenImageView.getX(), 0);
        ObjectAnimator animatorY = ObjectAnimator.ofFloat(fullScreenImageView, "translationY", fullScreenImageView.getY(), 0);

        animatorX.setDuration(ANIMATION_DURATION); // Thời gian chuyển động
        animatorY.setDuration(ANIMATION_DURATION);

        animatorX.start();
        animatorY.start();
    }

    // Hàm để đóng ảnh khi vuốt quá phạm vi
    private void closeImage() {
        finish();  // Đóng activity khi ảnh vượt qua phạm vi
    }
}
