package com.example.postapp.domain.repositories;

import com.example.postapp.domain.models.Favorite;
import com.example.postapp.domain.models.User;

import java.time.LocalDateTime;

public class PostWithFavorite {
    public final long id;
    public final String title;
    public final String content;
    public final LocalDateTime createdAt;
    public final User author;
    public final Favorite favorite;

    public PostWithFavorite(long id, String title, String content,
                            LocalDateTime createdAt, User author,
                            Favorite favorite) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
        this.author = author;
        this.favorite = favorite;
    }

    public PostWithFavorite() {
        id = 0;
        title = "";
        content = "";
        createdAt = null;
        author = null;
        favorite = null;
    }
}
