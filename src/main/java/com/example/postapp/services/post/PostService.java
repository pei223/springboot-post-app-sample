package com.example.postapp.services.post;

import com.example.postapp.domain.models.Post;
import com.example.postapp.domain.models.User;
import com.example.postapp.domain.models.UserDetailsImpl;
import com.example.postapp.domain.repositories.PostRepository;
import com.example.postapp.domain.repositories.PostWithFavorite;
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

    public Page<Post> getMyPosts(long userId, int pageNum) {
        Pageable paging = PageRequest.of(pageNum, DATA_NUM_PER_PAGE, Sort.by("id").descending());
        return postRepo.findAllByAuthorId(userId, paging);
    }

    public PostPageInfo getPosts(OptionalLong userId, int pageNum) {
        Pageable paging = PageRequest.of(pageNum, DATA_NUM_PER_PAGE, Sort.by("id").descending());
        List<DisplayPost> displayPosts;
        int totalPage;
        if (userId.isEmpty()) {
            Page<Post> postPage = postRepo.findAllByExposeOrderByCreatedAtDesc(true, paging);
            totalPage = postPage.getTotalPages();
            displayPosts = postPage.get().map(DisplayPost::fromPost).collect(Collectors.toList());
        } else {
            Page<PostWithFavorite> postWithFavoritePage = postRepo.findAllPostsWithFavorite(userId.getAsLong(), paging);
            totalPage = postWithFavoritePage.getTotalPages();
            displayPosts =
                    postWithFavoritePage.get().map(DisplayPost::fromPostWithFavorite).collect(Collectors.toList());
        }
        return new PostPageInfo(totalPage, displayPosts);
    }

    public Post findPost(long id, OptionalLong userId) throws NotFoundException {
        return postRepo.findPost(id, userId).orElseThrow(() -> new NotFoundException("", ""));
    }

    public void registerPost(UserDetailsImpl userDetail,
                             String title, String content, boolean expose) throws NotAuthorizedException {
        User user = userRepo.findById(userDetail.getId()).orElseThrow(() -> new NotAuthorizedException("No exist " +
                "user: " + userDetail.getId(), ""));
        Post post = new Post(title, content, expose);
        post.setAuthor(user);
        postRepo.save(post);
    }

    public void updatePost(UserDetailsImpl userDetail, long postId,
                           String title, String content, boolean expose) throws NotFoundException, NotAuthorizedException {
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
        Post post = postRepo.findById(postId).orElseThrow(() -> new NotFoundException("No exist Post: " + postId, ""));
        if (post.author.getId() != userDetail.getId()) {
            throw new NotAuthorizedException("Trying to delete others post", "");
        }
        postRepo.deleteById(postId);
    }
}
