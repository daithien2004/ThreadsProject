package com.example.theadsproject.activityPost;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.theadsproject.LoginRequiredDialogFragment;
import com.example.theadsproject.UserSessionManager;
import com.example.theadsproject.activityHome.TabLayoutHomeFragment;
import com.example.theadsproject.dto.PostRequest;
import com.example.theadsproject.R;
import com.example.theadsproject.adapter.ImageAdapter;
import com.example.theadsproject.entity.User;
import com.example.theadsproject.retrofit.ApiService;
import com.example.theadsproject.retrofit.RetrofitClient;
import com.example.theadsproject.service.ImageUploadService;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostFragment extends Fragment {
    private List<Object> imageList = new ArrayList<>(); // Chứa cả Uri và String
    private ImageAdapter imageAdapter;
    private ActivityResultLauncher<Intent> pickImagesLauncher;
    private EditText edtPostContent;
    private Button btnPost;
    RecyclerView rvImages;
    private UserSessionManager sessionManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Xử lý chọn ảnh từ thư viện
        pickImagesLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        ClipData clipData = result.getData().getClipData();
                        if (clipData != null) {
                            for (int i = 0; i < clipData.getItemCount(); i++) {
                                imageList.add(clipData.getItemAt(i).getUri());
                            }
                        } else {
                            Uri imageUri = result.getData().getData();
                            if (imageUri != null) {
                                imageList.add(imageUri);
                            }
                        }
                        rvImages.setVisibility(View.VISIBLE);
                        imageAdapter.notifyDataSetChanged();
                        updatePostButtonState();
                    }
                }
        );
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        sessionManager = new UserSessionManager();

        // Kiểm tra đăng nhập trước khi load view
        if (!sessionManager.isLoggedIn()) {
            new LoginRequiredDialogFragment().show(getParentFragmentManager(), "login_required_dialog");
            return null; // Dừng việc load giao diện
        }

        View view = inflater.inflate(R.layout.fragment_post, container, false);

        TextView tvPostName = view.findViewById(R.id.tvPostName);
        ImageView ivPostAvatar = view.findViewById(R.id.ivPostAvatar);

        // Lấy thông tin user từ UserSessionManager
        UserSessionManager sessionManager = new UserSessionManager();
        User user = sessionManager.getUser();

        if (user != null) {
            tvPostName.setText(user.getUsername());

            // Load ảnh đại diện (Sử dụng Glide hoặc Picasso)
            if (user.getImage() != null && !user.getImage().isEmpty()) {
                Glide.with(this).load(user.getImage()).apply(RequestOptions.circleCropTransform()).into(ivPostAvatar);
            }
        } else {
            Toast.makeText(getContext(), "Không tìm thấy thông tin người dùng!", Toast.LENGTH_SHORT).show();
        }


        rvImages = view.findViewById(R.id.rvImages);
        rvImages.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view,
                                       @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                int spacing = 8; // 8dp khoảng cách giữa các item
                outRect.right = spacing; // Áp dụng padding cho item (trừ item cuối)
            }
        });
        rvImages.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));


        edtPostContent = view.findViewById(R.id.edtPostContent);
        btnPost = view.findViewById(R.id.btnPost);

        // Khởi tạo Adapter với danh sách ảnh hỗn hợp (Uri và String)
        imageAdapter = new ImageAdapter(requireContext(), imageList);
        rvImages.setAdapter(imageAdapter);

        view.findViewById(R.id.imGallery).setOnClickListener(v -> openGallery());
        btnPost.setOnClickListener(v -> uploadPost());

        // Xử lý khi nhấn nút đóng (ivClose)
        ImageView ivClose = view.findViewById(R.id.ivClose);
        ivClose.setOnClickListener(v -> {
            if (getActivity() != null) {
                BottomAppBar bottomAppBar = getActivity().findViewById(R.id.bottomAppBar);
                BottomNavigationView bottomNav = getActivity().findViewById(R.id.bottomNavigationView);

                if (bottomAppBar != null) bottomAppBar.setVisibility(View.VISIBLE);
                if (bottomNav != null) bottomNav.setVisibility(View.VISIBLE);
            }

            // Quay lại HomeFragment
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frame_layout, new TabLayoutHomeFragment())
                    .commit();
        });

        // Lắng nghe thay đổi trong EditText
        edtPostContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updatePostButtonState();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Lắng nghe thay đổi trong danh sách ảnh
        imageAdapter.setOnDataChangedListener(this::updatePostButtonState);

        return view;
    }
    private void updatePostButtonState() {
        boolean hasText = !edtPostContent.getText().toString().trim().isEmpty();
        boolean hasImages = !imageList.isEmpty(); // Kiểm tra có ảnh hay không

        if (hasText || hasImages) {
            btnPost.setEnabled(true);
            btnPost.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(), R.color.blue)); // Màu xanh khi có nội dung/ảnh
        } else {
            btnPost.setEnabled(false);
            btnPost.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(), R.color.gray)); // Màu xám khi không có gì
        }
    }

    // Mở thư viện ảnh để chọn nhiều ảnh
    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setType("image/*");
        pickImagesLauncher.launch(intent);
    }

    // Thêm ảnh từ web vào danh sách
    public void addImageFromWeb(String imageUrl) {
        imageList.add(imageUrl);
        imageAdapter.notifyDataSetChanged();
    }

    // Gửi bài viết lên server
    private void uploadPost() {
        String content = edtPostContent.getText().toString().trim();
        if (content.isEmpty() && imageList.isEmpty()) {
            Toast.makeText(requireContext(), "Nội dung hoặc ảnh không được để trống!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Hiển thị loading dialog
        ProgressDialog progressDialog = new ProgressDialog(requireContext());
        progressDialog.setMessage("Đang tải lên...");
        progressDialog.show();

        // Tạo list để lưu URL ảnh từ Cloudinary
        ArrayList<String> mediaUrls = new ArrayList<>();
        CountDownLatch latch = new CountDownLatch(imageList.size());

        // Upload từng ảnh lên Cloudinary
        for (Object obj : imageList) {
            if (obj instanceof Uri) {
                Uri imageUri = (Uri) obj;
                ImageUploadService.uploadImage(requireContext(), imageUri, new ImageUploadService.UploadCallback() {
                    @Override
                    public void onSuccess(String imageUrl) {
                        mediaUrls.add(imageUrl);
                        latch.countDown();
                    }

                    @Override
                    public void onFailure(String error) {
                        latch.countDown();
                        requireActivity().runOnUiThread(() -> {
                            Toast.makeText(requireContext(),
                                    "Lỗi upload ảnh: " + error, Toast.LENGTH_SHORT).show();
                        });
                    }
                });
            }
        }

        // Chờ tất cả ảnh upload xong rồi mới tạo post
        new Thread(() -> {
            try {
                latch.await();
                requireActivity().runOnUiThread(() -> {
                    // Lấy user từ session
                    UserSessionManager sessionManager = new UserSessionManager();
                    User user = sessionManager.getUser();

                    // Tạo request với URL ảnh từ Cloudinary
                    PostRequest postRequest = new PostRequest(content, mediaUrls, "public", user.getUserId());

                    // Log request JSON
                    Gson gson = new Gson();
                    Log.d("UPLOAD_POST", "Request JSON: " + gson.toJson(postRequest));

                    // Gửi request lên server
                    ApiService apiService = RetrofitClient.getApiService();
                    Call<Void> call = apiService.createPost(postRequest);
                    call.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            progressDialog.dismiss();
                            Log.d("UPLOAD_POST", "Response Code: " + response.code());

                            if (response.isSuccessful()) {
                                Toast.makeText(requireContext(),
                                        "Đăng bài thành công!", Toast.LENGTH_SHORT).show();
                                goToHomeFragment();
                            } else {
                                try {
                                    String errorBody = response.errorBody() != null ?
                                            response.errorBody().string() : "Không có lỗi cụ thể";
                                    Log.e("UPLOAD_POST", "Lỗi khi đăng bài: " + errorBody);
                                    Toast.makeText(requireContext(),
                                            "Lỗi khi đăng bài!", Toast.LENGTH_SHORT).show();
                                } catch (Exception e) {
                                    Log.e("UPLOAD_POST", "Lỗi khi đọc errorBody", e);
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            progressDialog.dismiss();
                            Log.e("UPLOAD_POST", "Lỗi kết nối: " + t.getMessage());
                            Toast.makeText(requireContext(),
                                    "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
                requireActivity().runOnUiThread(() -> {
                    progressDialog.dismiss();
                    Toast.makeText(requireContext(),
                            "Lỗi xử lý ảnh", Toast.LENGTH_SHORT).show();
                });
            }
        }).start();
    }

    // Chuyển về HomeFragment sau khi đăng bài thành công
    private void goToHomeFragment() {
        // Hiển thị lại BottomAppBar và BottomNavigationView
        if (getActivity() != null) {
            BottomAppBar bottomAppBar = getActivity().findViewById(R.id.bottomAppBar);
            BottomNavigationView bottomNav = getActivity().findViewById(R.id.bottomNavigationView);

            if (bottomAppBar != null) bottomAppBar.setVisibility(View.VISIBLE);
            if (bottomNav != null) bottomNav.setVisibility(View.VISIBLE);
        }

        // Chuyển về HomeFragment sau khi đăng bài thành công
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_layout, new TabLayoutHomeFragment()) // Thay thế hoàn toàn fragment
                .commit();
    }
}
