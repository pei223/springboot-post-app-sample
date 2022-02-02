package com.example.postapp.services.post;

import com.example.postapp.domain.DisplayPost;
import com.example.postapp.domain.models.Favorite;
import com.example.postapp.domain.models.Post;
import com.example.postapp.domain.models.User;
import com.example.postapp.domain.models.UserDetailsImpl;
import com.example.postapp.domain.repositories.FavoriteRepository;
import com.example.postapp.domain.repositories.PostRepository;
import com.example.postapp.domain.repositories.UserRepository;
import com.example.postapp.services.common.NotAuthorizedException;
import com.example.postapp.services.common.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.OptionalLong;
import java.util.stream.Collectors;


@Transactional
@Service
public class PostService {
    private static final int DATA_NUM_PER_PAGE = 10;
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private PostRepository postRepo;
    @Autowired
    private FavoriteRepository favRepo;

    public Page<Post> getMyPosts(long userId, int pageNum) {
        Pageable paging = PageRequest.of(pageNum, DATA_NUM_PER_PAGE, Sort.by("id").descending());
        return postRepo.findAllByAuthorId(userId, paging);
    }

    public PostPageInfo getPosts(OptionalLong userId, int pageNum) {
        Pageable paging = PageRequest.of(pageNum, DATA_NUM_PER_PAGE, Sort.by("id").descending());
        Page<Post> postPage = postRepo.findAllByExpose(true, paging);
        List<Favorite> favoriteList = favRepo.findAllByPostList(userId, postPage.toList());
        List<DisplayPost> displayPosts = postPage.toList().stream().map(post -> DisplayPost.toDisplayPost(post,
                favoriteList.stream().anyMatch(fav -> fav.postId == post.id)
        )).collect(Collectors.toList());
        return new PostPageInfo(postPage.getTotalPages(), displayPosts);
    }

    public Post findPost(long id, OptionalLong userId) throws NotFoundException {
        return postRepo.findPost(id, userId).orElseThrow(() -> new NotFoundException("", ""));
    }

    public void registerPost(UserDetailsImpl userDetail,
                             String title, String content, boolean expose) throws NotAuthorizedException {
        if (userDetail == null) {
            throw new NotAuthorizedException("No user", "");
        }
        User user = userRepo.findById(userDetail.getId()).orElseThrow(() -> new NotAuthorizedException("No exist " +
                "user: " + userDetail.getId(), ""));
        Post post = new Post(title, content, expose);
        post.setAuthor(user);
        postRepo.save(post);
    }

    public void updatePost(UserDetailsImpl userDetail, long postId,
                           String title, String content, boolean expose) throws NotFoundException, NotAuthorizedException {
        if (userDetail == null) {
            throw new NotAuthorizedException("No user", "");
        }
        Post post = postRepo.findById(postId).orElseThrow(() -> new NotFoundException("No exist Post: " + postId, ""));
        if (post.author.getId() != userDetail.getId()) {
            throw new NotAuthorizedException("Trying to update others post", "");
        }
        post.title = title;
        post.content = content;
        post.expose = expose;
        postRepo.save(post);
    }

    public void deletePost(UserDetailsImpl userDetail, long postId) throws NotFoundException, NotAuthorizedException {
        if (userDetail == null) {
            throw new NotAuthorizedException("No user", "");
        }
        Post post = postRepo.findById(postId).orElseThrow(() -> new NotFoundException("No exist Post: " + postId, ""));
        if (post.author.getId() != userDetail.getId()) {
            throw new NotAuthorizedException("Trying to delete others post", "");
        }
        postRepo.deleteById(postId);
    }
}
