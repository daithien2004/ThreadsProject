package com.example.theadsproject.config;

import com.cloudinary.Cloudinary;

import java.util.HashMap;
import java.util.Map;

public class CloudinaryConfig {
    private static final String CLOUD_NAME = "dmcg6uu1f";
    private static final String API_KEY = "629166749329541";
    private static final String API_SECRET = "Z-YwseZDMnV1EZxLb3Fry-CKGFU";

    private static Cloudinary cloudinary;

    public static Cloudinary getInstance() {
        if (cloudinary == null) {
            Map<String, String> config = new HashMap<>();
            config.put("cloud_name", CLOUD_NAME);
            config.put("api_key", API_KEY);
            config.put("api_secret", API_SECRET);
            cloudinary = new Cloudinary(config);
        }
        return cloudinary;
    }
}
