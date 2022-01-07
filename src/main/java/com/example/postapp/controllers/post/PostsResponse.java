package com.example.postapp.controllers.post;

import com.example.postapp.domain.models.Post;

import java.util.Collections;
import java.util.List;

public class PostsResponse {
    public final List<Post> posts;

    public PostsResponse(List<Post> posts) {
        this.posts = posts;
    }

    public PostsResponse() {
        this.posts = Collections.emptyList();
    }
}
