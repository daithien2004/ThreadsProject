package com.example.theadsproject;

import android.os.Bundle;

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


public class PersonalDetailFragment extends Fragment {
    private RecyclerView recyclerView;
    private List<Post> postList;
    private PostAdapter postAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_personal_detail, container, false);
        recyclerView = view.findViewById(R.id.rvPosts);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        postList = new ArrayList<>();
        postList.add(new Post("lyhung_04", "5h", "Định mệnh rau muống xào măn vcl ra", "https://beptruong.edu.vn/wp-content/uploads/2021/04/rau-muong-xao-toi-1.jpg", "me.png"));
        postList.add(new Post("lyhung_04", "5h", "Định mệnh rau muống xào măn vcl ra", "https://beptruong.edu.vn/wp-content/uploads/2021/04/rau-muong-xao-toi-1.jpg", "me.png"));
        postList.add(new Post("lyhung_04", "5h", "Định mệnh rau muống xào măn vcl ra", "https://beptruong.edu.vn/wp-content/uploads/2021/04/rau-muong-xao-toi-1.jpg", "me.png"));

        postAdapter = new PostAdapter(getContext(), postList);
        recyclerView.setAdapter(postAdapter);

        return view;
    }
}