package com.example.postapp.services.favorite;

import com.example.postapp.domain.models.Favorite;
import com.example.postapp.domain.models.Post;
import com.example.postapp.domain.models.User;
import com.example.postapp.domain.models.UserDetailsImpl;
import com.example.postapp.domain.repositories.FavoriteRepository;
import com.example.postapp.domain.repositories.PostRepository;
import com.example.postapp.domain.repositories.UserRepository;
import com.example.postapp.services.common.AlreadyExistsException;
import com.example.postapp.services.common.ArgumentException;
import com.example.postapp.services.common.NotAuthorizedException;
import com.example.postapp.services.common.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Transactional
@Service
public class FavoriteService {
    private static long NO_SELECTED_ID = -1;

    @Autowired
    FavoriteRepository favRepo;
    @Autowired
    PostRepository postRepo;
    @Autowired
    UserRepository userRepo;

    public List<Favorite> getMyFavorites(long userId) {
        return favRepo.findAllByUserId(userId);
    }

    public void register(UserDetailsImpl userDetails, long postId)
            throws NotAuthorizedException, NotFoundException, AlreadyExistsException {
        User user = userRepo.findById(userDetails.getId())
                .orElseThrow(
                        // TODO エラーコード
                        () -> new NotAuthorizedException(String.format("id: %d is not found.", userDetails.getId()), "")
                );
        Post post = postRepo.findById(postId)
                // TODO エラーコード
                .orElseThrow(() -> new NotFoundException(String.format("Not exist postId: %d", postId), ""));
        if (favRepo.existsByUserIdAndPostId(user.getId(), postId)) {
            // TODO エラーコード
            throw new AlreadyExistsException(
                    String.format("postId: %d, userId: %d data already exists", postId, user.getId()), "");
        }
        Favorite fav = Favorite.build(user, post);
        favRepo.save(fav);
    }

    public void delete(UserDetailsImpl userDetails, long favoriteId, long postId) throws NotFoundException, ArgumentException {
        if (postId == NO_SELECTED_ID && favoriteId == NO_SELECTED_ID) {
            // TODO エラーコード
            throw new ArgumentException("", "");
        }
        if (postId != NO_SELECTED_ID) {
            if (!favRepo.existsByUserIdAndPostId(userDetails.getId(), postId)) {
                // TODO エラーコード
                throw new NotFoundException(
                        String.format("Not exist userId: %d, postId: %d", userDetails.getId(), postId), "");
            }
            favRepo.deleteByUserIdAndPostId(userDetails.getId(), postId);
            return;
        }
        if (!favRepo.existsById(favoriteId)) {
            // TODO エラーコード
            throw new NotFoundException(
                    String.format("Not exist userId: %d, postId: %d", userDetails.getId(), postId), "");
        }
        favRepo.deleteById(favoriteId);
    }
}
