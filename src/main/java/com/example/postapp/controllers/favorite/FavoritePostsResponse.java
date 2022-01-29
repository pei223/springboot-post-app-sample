package com.example.postapp.controllers.favorite;

import com.example.postapp.domain.repositories.FavoritePost;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

public class FavoritePostsResponse {

    public final List<FavoritePostResource> favorites;
    public final int totalPage;

    FavoritePostsResponse(List<FavoritePostResource> favorites, int totalPage) {
        this.favorites = favorites;
        this.totalPage = totalPage;
    }

    public static FavoritePostsResponse build(Page<FavoritePost> favorites) {
        return new FavoritePostsResponse(favorites.stream().map(FavoritePostResource::build).collect(Collectors.toList()),
                favorites.getTotalPages());
    }
}
