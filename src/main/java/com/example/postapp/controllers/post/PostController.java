package com.example.postapp.controllers.post;

import com.example.postapp.controllers.common.ErrorResponse;
import com.example.postapp.controllers.common.UpdateResultResponse;
import com.example.postapp.domain.models.Post;
import com.example.postapp.domain.models.User;
import com.example.postapp.domain.models.UserDetailsImpl;
import com.example.postapp.domain.repositories.PostRepository;
import com.example.postapp.domain.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping("/me")
    public ResponseEntity<PostsResponse> myPosts(@AuthenticationPrincipal UserDetailsImpl user) {
        return ResponseEntity.ok().body(new PostsResponse(
                postRepo.findAllByAuthorId(user.getId())
        ));
    }

    @PostMapping("/")
    public ResponseEntity<?> create(@AuthenticationPrincipal UserDetailsImpl userDetail,
                                    @Validated @RequestBody Post post) {
        Optional<User> user = userRepo.findById(userDetail.getId());
        if (user.isEmpty()) {
            // TODO エラーレスポンス
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("", ""));
        }
        post.setAuthor(user.get());
        postRepo.save(post);
        return ResponseEntity.ok().body(new UpdateResultResponse("Ok"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@AuthenticationPrincipal UserDetailsImpl userDetail,
                                    @PathVariable long id, @Validated @RequestBody UpdatePostBody body) {
        Optional<Post> post = postRepo.findById(id);
        if (post.isEmpty()) {
            // TODO エラーレスポンス
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("", ""));
        }
        System.out.println(String.format("%d, %d", post.get().author.getId(), userDetail.getId()));
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


    @GetMapping("/")
    public ResponseEntity<PostsResponse> posts() {
        System.out.println(postRepo.findAllByExpose(true).get(0));
        return ResponseEntity.ok().body(new PostsResponse(postRepo.findAllByExpose(true)));
    }

}
