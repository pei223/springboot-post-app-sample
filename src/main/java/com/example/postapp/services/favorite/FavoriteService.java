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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Transactional
@Service
public class FavoriteService {
    private static final int DATA_NUM_PER_PAGE = 10;

    @Autowired
    FavoriteRepository favRepo;
    @Autowired
    PostRepository postRepo;
    @Autowired
    UserRepository userRepo;

    public Page<Favorite> getMyFavorites(long userId, int page) {
        return favRepo.findAllByUserId(userId, PageRequest.of(page, DATA_NUM_PER_PAGE));
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
        Favorite fav = new Favorite(user, post);
        favRepo.save(fav);
    }

    public void delete(UserDetailsImpl userDetails, long postId) throws NotFoundException {
        if (!favRepo.existsByUserIdAndPostId(userDetails.getId(), postId)) {
            // TODO エラーコード
            throw new NotFoundException(
                    String.format("Not exist userId: %d, postId: %d", userDetails.getId(), postId), "");
        }
        favRepo.deleteByUserIdAndPostId(userDetails.getId(), postId);
    }
}
