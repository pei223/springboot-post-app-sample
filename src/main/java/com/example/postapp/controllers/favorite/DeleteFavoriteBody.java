package com.example.postapp.controllers.favorite;

public class DeleteFavoriteBody {
    public final long postId;
    public final long favoriteId;

    public DeleteFavoriteBody(long postId, long favoriteId) {
        this.postId = postId;
        this.favoriteId = favoriteId;
    }

    public DeleteFavoriteBody() {
        this.postId = 0;
        this.favoriteId = 0;
    }
}
