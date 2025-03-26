package com.example.theadsproject.activity;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.theadsproject.dto.PostResponse;
import com.example.theadsproject.dto.UserResponse; // ✅ Import đúng
import com.example.theadsproject.R;
import com.example.theadsproject.adapter.PostAdapter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PersonalDetailFragment extends Fragment {
    private RecyclerView recyclerView;
    private List<PostResponse> postList; // ✅ Đổi từ List<Post> thành List<PostResponse>
    private PostAdapter postAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_personal_detail, container, false);
        recyclerView = view.findViewById(R.id.rvPosts);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        postList = new ArrayList<>();
        postList.add(new PostResponse(
                100L,
                "Định mệnh rau muống xào mặn vcl ra",
                new ArrayList<>(Arrays.asList("https://beptruong.edu.vn/wp-content/uploads/2021/04/rau-muong-xao-toi-1.jpg")),
                "public",
                LocalDateTime.of(2025, 3, 25, 12, 0, 0),
                new UserResponse(
                        101L, // userId
                        "lyhung_04", // username
                        "Lý Hùng", // nickName
                        "https://saigonbanme.vn/wp-content/uploads/2024/12/bo-99-anh-avatar-dep-cho-con-gai-ngau-chat-nhat-viet-nam-38.jpg"
                )
        ));

        postAdapter = new PostAdapter(getContext(), postList);
        recyclerView.setAdapter(postAdapter);

        return view;
    }
}
