package com.example.theadsproject.activityHome;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.theadsproject.R;
import com.example.theadsproject.activityPost.ConfigPostFragment;
import com.example.theadsproject.adapter.PostAdapter;
import com.example.theadsproject.dto.PostResponse;
import com.example.theadsproject.dto.UserResponse;
import com.example.theadsproject.retrofit.ApiService;
import com.example.theadsproject.retrofit.RetrofitClient;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {
    private RecyclerView recyclerView;
    private PostAdapter postAdapter;
    private List<PostResponse> displayedPosts;
    private boolean isLoading = false;
    private int currentPage = 0;
    private boolean isLastPage = false;
    private static final int PAGE_SIZE = 10;
    private ProgressBar progressBar;

    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    long deletedId = result.getData().getLongExtra("deleted_post_id", -1);
                    if (deletedId != -1) {
                        removePostById(deletedId);
                    }
                }
            }
    );

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = view.findViewById(R.id.rvPosts);
        progressBar = view.findViewById(R.id.progressBar);
        
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        
        displayedPosts = new ArrayList<>();
        postAdapter = new PostAdapter(getContext(), displayedPosts, activityResultLauncher);
        recyclerView.setAdapter(postAdapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                
                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

                if (!isLoading && !isLastPage) {
                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                            && firstVisibleItemPosition >= 0) {
                        loadMorePosts();
                    }
                }
            }
        });

        loadMorePosts();
        return view;
    }

    private void loadMorePosts() {
        if (isLoading || isLastPage) return;
        
        isLoading = true;
        if (progressBar != null) {
            progressBar.setVisibility(View.VISIBLE);
        }

        Log.d("API_DEBUG", "Loading page " + currentPage);
        ApiService apiService = RetrofitClient.getApiService();
        apiService.getPagedPosts(currentPage, PAGE_SIZE).enqueue(new Callback<Map<String, Object>>() {
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                Log.d("API_DEBUG", "Response code: " + response.code());
                if (!response.isSuccessful()) {
                    try {
                        Log.e("API_ERROR", "Error body: " + response.errorBody().string());
                    } catch (Exception e) {
                        Log.e("API_ERROR", "Could not read error body");
                    }
                }
                
                if (response.isSuccessful() && response.body() != null) {
                    Map<String, Object> data = response.body();
                    Log.d("API_DEBUG", "Response data: " + data.toString());
                    
                    List<?> rawPosts = (List<?>) data.get("posts");
                    List<PostResponse> newPosts = new ArrayList<>();
                    if (rawPosts != null) {
                        for (Object item : rawPosts) {
                            if (item instanceof Map) {
                                Map<String, Object> postMap = (Map<String, Object>) item;
                                PostResponse post = new PostResponse();
                                
                                if (postMap.get("postId") instanceof Number) {
                                    post.setPostId(((Number) postMap.get("postId")).longValue());
                                }
                                post.setContent((String) postMap.get("content"));
                                post.setVisibility((String) postMap.get("visibility"));
                                post.setMediaUrls((ArrayList<String>) postMap.get("mediaUrls"));
                                
                                // Parse createdAt
                                String createdAtStr = (String) postMap.get("createdAt");
                                if (createdAtStr != null) {
                                    try {
                                        LocalDateTime createdAt = LocalDateTime.parse(createdAtStr);
                                        post.setCreatedAt(createdAt);
                                    } catch (Exception e) {
                                        Log.e("API_DEBUG", "Error parsing createdAt: " + createdAtStr, e);
                                    }
                                }
                                
                                Map<String, Object> userMap = (Map<String, Object>) postMap.get("user");
                                if (userMap != null) {
                                    Long userId = userMap.get("userId") instanceof Number ? 
                                        ((Number) userMap.get("userId")).longValue() : null;
                                    String username = (String) userMap.get("username");
                                    String nickName = (String) userMap.get("nickName");
                                    String bio = (String) userMap.get("bio");
                                    String image = (String) userMap.get("image");
                                    
                                    UserResponse user = new UserResponse(userId, username, nickName, bio, image);
                                    
                                    // Set các trường bổ sung
                                    user.setEmail((String) userMap.get("email"));
                                    user.setPhone((String) userMap.get("phone"));
                                    if (userMap.get("followerCount") instanceof Number) {
                                        user.setFollowerCount(((Number) userMap.get("followerCount")).intValue());
                                    }
                                    post.setUser(user);
                                }
                                
                                newPosts.add(post);
                            }
                        }
                    }
                    
                    double totalPagesDouble = (double) data.get("totalPages");
                    int totalPages = (int) totalPagesDouble;
                    
                    if (!newPosts.isEmpty()) {
                        Log.d("API_DEBUG", "Received " + newPosts.size() + " new posts");
                        int startPosition = displayedPosts.size();
                        displayedPosts.addAll(newPosts);
                        postAdapter.notifyItemRangeInserted(startPosition, newPosts.size());
                        currentPage++;
                        isLastPage = currentPage >= totalPages;
                    } else {
                        Log.d("API_DEBUG", "No new posts received");
                        isLastPage = true;
                    }
                } else {
                    Log.e("API_ERROR", "Không lấy được dữ liệu");
                }
                
                isLoading = false;
                if (progressBar != null) {
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                Log.e("API_ERROR", "Lỗi kết nối: " + t.getMessage());
                Log.e("API_ERROR", "Stack trace: ", t);
                isLoading = false;
                if (progressBar != null) {
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }

    public void removePostById(long postId) {
        int indexInDisplayed = -1;
        for (int i = 0; i < displayedPosts.size(); i++) {
            if (displayedPosts.get(i).getPostId() == postId) {
                indexInDisplayed = i;
                break;
            }
        }
        if (indexInDisplayed != -1) {
            displayedPosts.remove(indexInDisplayed);
            postAdapter.notifyItemRemoved(indexInDisplayed);
        }
    }
}


