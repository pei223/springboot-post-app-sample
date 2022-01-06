package com.example.postapp;

import com.example.postapp.domain.models.Post;
import com.example.postapp.domain.models.User;
import com.example.postapp.domain.repositories.PostRepository;
import com.example.postapp.domain.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TestUtil {
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private PostRepository postRepo;

    public User createTestUser(String baseName) {
        User user = new User(baseName + " user", baseName + "@email.com", "password");
        userRepo.save(user);
        return user;
    }

    public Post createTestPost(String baseName, boolean expose, User user) {
        Post post = new Post(baseName + " title", baseName + " content", expose);
        post.setAuthor(user);
        postRepo.save(post);
        return post;
    }
}
