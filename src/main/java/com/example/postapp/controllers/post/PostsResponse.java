package com.example.postapp.controllers.post;

import com.example.postapp.domain.models.Post;

import java.util.List;

public class PostsResponse {
    public final List<Post> posts;

    PostsResponse(List<Post> posts) {
        this.posts = posts;
    }
}
