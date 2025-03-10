package com.example.theadsproject;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.theadsproject.adapter.PostAdapter;
import com.example.theadsproject.entity.Post;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {
    private RecyclerView recyclerView;
    private PostAdapter postAdapter;
    private List<Post> postList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = view.findViewById(R.id.rcViewhome);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Thêm dữ liệu mẫu
        postList = new ArrayList<>();
        postList.add(new Post("lyhung_04", "5h", "Định mệnh rau muống xào măn vcl ra", "https://beptruong.edu.vn/wp-content/uploads/2021/04/rau-muong-xao-toi-1.jpg"));
        postList.add(new Post("user123", "3h", "Hôm nay trời đẹp quá!", null)); // Không có ảnh
        postList.add(new Post("anhdep_trenmang", "1h", "Chụp ảnh ở Đà Lạt", "https://baovemoitruong.org.vn/wp-content/uploads/2023/06/da-lat-1-n.jpg"));

        postAdapter = new PostAdapter(getContext(), postList);
        recyclerView.setAdapter(postAdapter);

        return view;
    }
}
