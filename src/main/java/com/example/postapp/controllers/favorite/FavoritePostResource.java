package com.example.postapp.controllers.favorite;

import com.example.postapp.domain.models.Favorite;

import java.time.LocalDateTime;

public class FavoritePostResource {
    public final String title;
    public final String author;
    public final long authorId;
    public final long postId;
    public final LocalDateTime createdAt;

    public FavoritePostResource(String title, String author, long authorId, long postId, LocalDateTime createdAt) {
        this.title = title;
        this.author = author;
        this.authorId = authorId;
        this.postId = postId;
        this.createdAt = createdAt;
    }

    public static FavoritePostResource build(Favorite favorite) {
        return new FavoritePostResource(favorite.post.title,
                favorite.post.author.getName(), favorite.post.author.getId(), favorite.post.id, favorite.post.createdAt);
    }
}
