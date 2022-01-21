package com.example.postapp.controllers.favorite;

import com.example.postapp.controllers.common.UpdateResultResponse;
import com.example.postapp.domain.models.Favorite;
import com.example.postapp.domain.models.UserDetailsImpl;
import com.example.postapp.services.common.AlreadyExistsException;
import com.example.postapp.services.common.NotAuthorizedException;
import com.example.postapp.services.common.NotFoundException;
import com.example.postapp.services.favorite.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/favorites")
public class FavoriteController {
    @Autowired
    private FavoriteService service;

    @GetMapping("/")
    public ResponseEntity<?> myFavorites(@AuthenticationPrincipal UserDetailsImpl user) {
        List<Favorite> favorites = service.getMyFavorites(user.getId());
        return ResponseEntity.ok().body(FavoritesResponse.build(favorites));
    }

    @PostMapping("/add/{postId}")
    public ResponseEntity<?> addFavorite(@AuthenticationPrincipal UserDetailsImpl user, @PathVariable long postId)
            throws NotAuthorizedException, NotFoundException, AlreadyExistsException {
        service.register(user, postId);
        return ResponseEntity.ok().body(new UpdateResultResponse("created"));
    }

    @PostMapping("/delete/{postId}")
    public ResponseEntity<?> deleteFavorite(@AuthenticationPrincipal UserDetailsImpl user, @PathVariable long postId)
            throws NotFoundException {
        service.delete(user, postId);
        return ResponseEntity.ok().body("");
    }
}
