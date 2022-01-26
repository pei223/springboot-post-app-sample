package com.example.postapp.controllers.favorite;

import com.example.postapp.domain.models.Favorite;

public class FavoriteResource {
    private static final long DELETED_ID = -1;
    public final long id;
    public final String title;
    public final String author;
    public final long authorId;
    public final long postId;

    public FavoriteResource(long id, String title, String author, long authorId, long postId) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.authorId = authorId;
        this.postId = postId;
    }

    public static FavoriteResource build(Favorite favorite) {
        if (favorite.post == null) {
            return new FavoriteResource(favorite.id, favorite.postTitle,
                    favorite.authorName, DELETED_ID, DELETED_ID);
        }
        return new FavoriteResource(favorite.id, favorite.postTitle,
                favorite.authorName, favorite.post.author.getId(), favorite.post.id);
    }
}
