package com.androidpj.threads.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostRequest {
    private String content;
    private ArrayList<String> mediaUrls;
    private String visibility;
    private Long userId;
}