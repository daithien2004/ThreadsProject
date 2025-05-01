package com.example.theadsproject.service;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;

import com.example.theadsproject.commonClass.FileUtil;
import com.example.theadsproject.config.CloudinaryConfig;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ImageUploadService {
    public static void uploadImage(Context context, Uri imageUri, UploadCallback callback) {
        try {
            // Chuyển Uri thành File
            File imageFile = FileUtil.from(context, imageUri);

            // Tạo upload params
            Map<String, String> uploadParams = new HashMap<>();
            uploadParams.put("folder", "threads_app"); // folder trên Cloudinary

            // Upload ảnh trong background thread
            new AsyncTask<Void, Void, String>() {
                @Override
                protected String doInBackground(Void... voids) {
                    try {
                        // Upload to Cloudinary
                        Map uploadResult = CloudinaryConfig.getInstance()
                                .uploader()
                                .upload(imageFile, uploadParams);

                        // Lấy URL của ảnh
                        return uploadResult.get("url").toString();
                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }
                }

                @Override
                protected void onPostExecute(String imageUrl) {
                    if (imageUrl != null) {
                        callback.onSuccess(imageUrl);
                    } else {
                        callback.onFailure("Upload failed");
                    }
                }
            }.execute();

        } catch (IOException e) {
            e.printStackTrace();
            callback.onFailure("File conversion failed");
        }
    }

    public interface UploadCallback {
        void onSuccess(String imageUrl);
        void onFailure(String error);
    }
}