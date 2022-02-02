package com.example.postapp.controllers.favorite;

import com.example.postapp.domain.repositories.FavoritePost;

import java.time.LocalDateTime;

public class FavoritePostResource {
    private static final long DELETED_ID = -1;
    public final long id;
    public final String title;
    public final String author;
    public final long authorId;
    public final long postId;
    public final LocalDateTime createdAt;

    public FavoritePostResource(long id, String title, String author, long authorId, long postId, LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.authorId = authorId;
        this.postId = postId;
        this.createdAt = createdAt;
    }

    public static FavoritePostResource build(FavoritePost favoritePost) {
        if (favoritePost.post == null) {
            return new FavoritePostResource(favoritePost.id, favoritePost.postTitle,
                    favoritePost.authorName, DELETED_ID, DELETED_ID, null);
        }
        return new FavoritePostResource(favoritePost.id, favoritePost.postTitle,
                favoritePost.authorName, favoritePost.post.author.getId(), favoritePost.post.id, favoritePost.post.createdAt);
    }
}
