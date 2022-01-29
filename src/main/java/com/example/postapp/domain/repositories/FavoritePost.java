package com.example.postapp.domain.repositories;

import com.example.postapp.domain.models.Post;

import java.io.Serializable;

public class FavoritePost implements Serializable {
    public long id;
    public Post post;
    public String postTitle;
    public String authorName;

    public FavoritePost(long id, Post post, String postTitle, String authorName) {
        this.id = id;
        this.post = post;
        this.postTitle = postTitle;
        this.authorName = authorName;
    }

    public FavoritePost() {
    }
}
