package com.androidpj.threads.service;

import com.androidpj.threads.entity.Post;
import com.androidpj.threads.entity.Repost;
import com.androidpj.threads.entity.User;
import com.androidpj.threads.repository.PostRepository;
import com.androidpj.threads.repository.RepostRepository;
import com.androidpj.threads.repository.UserRepository;
import com.androidpj.threads.dto.RepostResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RepostService {

    @Autowired
    private RepostRepository repostRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    // Repost bài viết
    public void repost(Long postId, String username) {
        User currentUser = userRepository.findByUsername(username);
        Post originalPost = postRepository.findById(postId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));

        if (repostRepository.existsByUserAndPost(currentUser, originalPost)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Bạn đã repost bài viết này rồi");
        }

        Repost repost = new Repost();
        repost.setUser(currentUser);
        repost.setPost(originalPost);
        repost.setRepostedAt(LocalDateTime.now());
        repostRepository.save(repost);
    }

    // Lấy danh sách bài repost của người dùng
    public List<RepostResponse> getMyReposts(Long userId) {
        User currentUser = userRepository.findByUserId(userId);
        List<Repost> reposts = repostRepository.findByUser(currentUser);
        return reposts.stream()
                .map(repost -> new RepostResponse(repost.getPost(), currentUser)) // Tạo RepostResponse với thông tin người repost
                .collect(Collectors.toList());
    }
    public Long countReposts(Long postId) {
        return repostRepository.countByPostPostId(postId);
    }
    public boolean hasUserReposted(Long postId, String username) {
        User currentUser = userRepository.findByUsername(username);
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));

        return repostRepository.existsByUserAndPost(currentUser, post);
    }

}
