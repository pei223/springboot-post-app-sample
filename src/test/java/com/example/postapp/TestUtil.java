package com.example.postapp;

import com.example.postapp.domain.models.Post;
import com.example.postapp.domain.models.User;
import com.example.postapp.domain.repositories.PostRepository;
import com.example.postapp.domain.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

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

    public void deletePost(long id) {
        postRepo.deleteById(id);
    }

    public List<Post> createManyTestPost(int count, String baseName, User user) {
        List<Post> result = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Post post = this.createTestPost(String.format("%s_%d", baseName, count), i % 2 == 0, user);
            result.add(post);
        }
        return result;
    }
}
