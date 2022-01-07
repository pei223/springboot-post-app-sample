package com.example.postapp.controllers.post;

import com.example.postapp.controllers.common.ErrorResponse;
import com.example.postapp.controllers.common.UpdateResultResponse;
import com.example.postapp.domain.models.Post;
import com.example.postapp.domain.models.User;
import com.example.postapp.domain.models.UserDetailsImpl;
import com.example.postapp.domain.repositories.PostRepository;
import com.example.postapp.domain.repositories.UserRepository;
import com.example.postapp.services.post.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/posts")
public class PostController {
    @Autowired
    private PostRepository postRepo;
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private PostService postService;

    @GetMapping("/me")
    public ResponseEntity<PostsResponse> myPosts(@AuthenticationPrincipal UserDetailsImpl user, @RequestParam int page) {
        Page<Post> postsPage = postService.getMyPosts(user.getId(), page - 1);
        return ResponseEntity.ok().body(new PostsResponse(postsPage));
    }

    @GetMapping("/")
    public ResponseEntity<PostsResponse> getPosts(@RequestParam int page) {
        return ResponseEntity.ok().body(new PostsResponse(postService.getPosts(page - 1)));
    }

    @PostMapping("/")
    public ResponseEntity<?> create(@AuthenticationPrincipal UserDetailsImpl userDetail,
                                    @Validated @RequestBody RegisterPostBody postBody) {
        Optional<User> user = userRepo.findById(userDetail.getId());
        if (user.isEmpty()) {
            // TODO エラーレスポンス
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("", ""));
        }
        Post post = new Post(postBody.title, postBody.content, postBody.expose);
        post.setAuthor(user.get());
        postRepo.save(post);
        return ResponseEntity.ok().body(new UpdateResultResponse("Ok"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@AuthenticationPrincipal UserDetailsImpl userDetail,
                                    @PathVariable long id, @Validated @RequestBody RegisterPostBody body) {
        Optional<Post> post = postRepo.findById(id);
        if (post.isEmpty()) {
            // TODO エラーレスポンス
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("", ""));
        }
        if (post.get().author.getId() != userDetail.getId()) {
            // TODO エラーレスポンス
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorResponse("", ""));
        }

        post.get().title = body.title;
        post.get().content = body.content;
        post.get().expose = body.expose;

        postRepo.save(post.get());

        return ResponseEntity.ok().body(new UpdateResultResponse("Ok"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@AuthenticationPrincipal UserDetailsImpl userDetail,
                                    @PathVariable long id) {
        Optional<Post> post = postRepo.findById(id);
        if (post.isEmpty()) {
            // TODO エラーレスポンス
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("", ""));
        }
        if (post.get().author.getId() != userDetail.getId()) {
            // TODO エラーレスポンス
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorResponse("", ""));
        }

        postRepo.deleteById(id);

        return ResponseEntity.ok().body(new UpdateResultResponse("Ok"));
    }
}
