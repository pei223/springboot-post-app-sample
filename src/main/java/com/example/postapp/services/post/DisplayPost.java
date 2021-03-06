package com.example.postapp.services.post;

import com.example.postapp.domain.models.Post;
import com.example.postapp.domain.models.User;
import com.example.postapp.domain.repositories.PostWithFavorite;

import java.time.LocalDateTime;


public class DisplayPost {
    public long id;
    public String title;
    public String content;
    public User author;
    public final boolean favorited;
    public final LocalDateTime createdAt;


    public DisplayPost(long id, String title, String content, User author, boolean favorited, LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.author = author;
        this.favorited = favorited;
        this.createdAt = createdAt;
    }

    public static DisplayPost fromPost(Post post) {
        return new DisplayPost(post.id, post.title, post.content, post.author, false, post.createdAt);
    }

    public static DisplayPost fromPostWithFavorite(PostWithFavorite post) {
        return new DisplayPost(post.id, post.title, post.content, post.author, post.favorite != null, post.createdAt);
    }

    public DisplayPost() {
        this.id = 0;
        this.title = "";
        this.content = "";
        this.author = null;
        this.favorited = false;
        this.createdAt = null;
    }
}
