package com.example.postapp.controllers.favorite;

import com.example.postapp.domain.models.Favorite;

import java.util.List;
import java.util.stream.Collectors;

public class FavoritesResponse {

    public final List<FavoriteResource> favorites;

    FavoritesResponse(List<FavoriteResource> favorites) {
        this.favorites = favorites;
    }

    public static FavoritesResponse build(List<Favorite> favorites) {
        return new FavoritesResponse(favorites.stream().map(FavoriteResource::build).collect(Collectors.toList()));
    }
}
