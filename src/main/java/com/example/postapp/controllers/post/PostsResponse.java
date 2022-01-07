package com.example.postapp.controllers.post;

import com.example.postapp.domain.models.Post;
import org.springframework.data.domain.Page;

import java.util.Collections;
import java.util.List;

public class PostsResponse {
    public final List<Post> posts;
    public final int totalPage;

    public PostsResponse(Page<Post> postsPage) {
        this.posts = postsPage.getContent();
        this.totalPage = postsPage.getTotalPages();
    }

    public PostsResponse() {
        this.posts = Collections.emptyList();
        this.totalPage = 0;
    }
}
