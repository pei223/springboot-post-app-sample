package com.example.postapp.controllers.favorite;

import com.example.postapp.controllers.common.ErrorResponse;
import com.example.postapp.controllers.common.UpdateResultResponse;
import com.example.postapp.domain.models.UserDetailsImpl;
import com.example.postapp.domain.repositories.FavoriteRepository;
import com.example.postapp.services.common.AlreadyExistsException;
import com.example.postapp.services.common.NotAuthorizedException;
import com.example.postapp.services.common.NotFoundException;
import com.example.postapp.services.favorite.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/favorites")
public class FavoriteController {
    @Autowired
    private FavoriteRepository favRepo;
    @Autowired
    private FavoriteService service;

    @GetMapping("/")
    public ResponseEntity<?> myFavorites(@AuthenticationPrincipal UserDetailsImpl user) {
        return ResponseEntity.ok().body(FavoritesResponse.build(favRepo.findAllByUserId(user.getId())));
    }

    @PostMapping("/add/{postId}")
    public ResponseEntity<?> addFavorite(@AuthenticationPrincipal UserDetailsImpl user, @PathVariable long postId) {
        try {
            service.register(user, postId);
            return ResponseEntity.ok().body(new UpdateResultResponse("created"));
        } catch (NotAuthorizedException e) {
            // TODO エラーコード
            return ResponseEntity.status(401).body(new ErrorResponse(e.getMessage(), ""));
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (AlreadyExistsException e) {
            // TODO エラーコード
            return ResponseEntity.badRequest().body(new ErrorResponse("post is already added.", ""));
        }
    }

    @PostMapping("/delete/{postId}")
    public ResponseEntity<?> deleteFavorite(@AuthenticationPrincipal UserDetailsImpl user, @PathVariable long postId) {
        try {
            service.delete(user, postId);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body("");
    }
}
