package com.example.theadsproject.commonClass;

import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;

import androidx.activity.result.ActivityResultLauncher;

import java.util.ArrayList;
import java.util.List;

public class ImagePickerHelper {

    public interface OnImagesPickedListener {
        void onImagesPicked(List<Uri> imageUris);
    }

    public static void openGallery(Activity activity, ActivityResultLauncher<Intent> launcher) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setType("image/*");
        launcher.launch(intent);
    }

    public static List<Uri> handleGalleryResult(Intent data) {
        List<Uri> imageUris = new ArrayList<>();
        if (data == null) return imageUris;

        ClipData clipData = data.getClipData();
        if (clipData != null) {
            for (int i = 0; i < clipData.getItemCount(); i++) {
                imageUris.add(clipData.getItemAt(i).getUri());
            }
        } else {
            Uri singleUri = data.getData();
            if (singleUri != null) {
                imageUris.add(singleUri);
            }
        }
        return imageUris;
    }
}
