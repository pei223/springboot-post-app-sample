package com.example.postapp.services.favorite;

import com.example.postapp.domain.models.Favorite;
import com.example.postapp.domain.models.Post;
import com.example.postapp.domain.models.User;
import com.example.postapp.domain.models.UserDetailsImpl;
import com.example.postapp.domain.repositories.FavoriteRepository;
import com.example.postapp.domain.repositories.PostRepository;
import com.example.postapp.domain.repositories.UserRepository;
import com.example.postapp.services.common.AlreadyExistsException;
import com.example.postapp.services.common.NotAuthorizedException;
import com.example.postapp.services.common.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class FavoriteService {
    @Autowired
    FavoriteRepository favRepo;
    @Autowired
    PostRepository postRepo;
    @Autowired
    UserRepository userRepo;

    @Transactional
    public void register(UserDetailsImpl userDetails, long postId)
            throws NotAuthorizedException, NotFoundException, AlreadyExistsException {
        Optional<User> user = userRepo.findById(userDetails.getId());
        if (user.isEmpty()) {
            throw new NotAuthorizedException(String.format("id: %d is not found.", userDetails.getId()));
        }
        Optional<Post> post = postRepo.findById(postId);
        if (post.isEmpty()) {
            throw new NotFoundException(String.format("Not exist postId: %d", postId));
        }
        if (favRepo.existsByUserIdAndPostId(user.get().getId(), postId)) {
            throw new AlreadyExistsException(
                    String.format("postId: %d, userId: %d data already exists", postId, user.get().getId()));
        }
        Favorite fav = Favorite.build(user.get(), post.get());
        favRepo.save(fav);
    }

    @Transactional
    public void delete(UserDetailsImpl userDetails, long postId) throws NotFoundException {
        if (!favRepo.existsByUserIdAndPostId(userDetails.getId(), postId)) {
            throw new NotFoundException(String.format("Not exist userId: %d, postId: %d", userDetails.getId(), postId));
        }
        favRepo.deleteByUserIdAndPostId(userDetails.getId(), postId);
    }
}
