package com.example.postapp.controller.post;


import com.example.postapp.TestUtil;
import com.example.postapp.domain.models.Post;
import com.example.postapp.domain.models.User;
import com.example.postapp.domain.models.UserDetailsImpl;
import com.example.postapp.domain.repositories.PostRepository;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles("test")
public class DeletePostTest {

    @Autowired
    private TestUtil testUtil;
    @Autowired
    private PostRepository postRepo;
    @Autowired
    private MockMvc mockMvc;

    @Test
    void testDeletePost() throws Exception {
        User user = testUtil.createTestUser("testDeletePost");
        Post post = testUtil.createTestPost("testDeletePost", true, user);

        mockMvc.perform(
                delete("/api/posts/" + post.id)
                        .with(user(UserDetailsImpl.build(user)))
                        .header("content-type", "application/json")
        ).andExpect(status().is(200));

        Optional<Post> deletedPost = postRepo.findById(post.id);
        Assert.assertTrue(deletedPost.isEmpty());
    }

    @Test
    void testTryingToDeleteNoExistentPost() throws Exception {
        User user = testUtil.createTestUser("testNoExistPostDeleting");

        mockMvc.perform(
                delete("/api/posts/999")
                        .with(user(UserDetailsImpl.build(user)))
                        .header("content-type", "application/json")
        ).andExpect(status().is(404));
    }

    @Test
    void testTryingToDeleteOtherUsersPost() throws Exception {
        User otherUser = testUtil.createTestUser("testTryingToDeleteOtherUsersPost_Other");
        User me = testUtil.createTestUser("testTryingToDeleteOtherUsersPost_Me");
        Post otherPost = testUtil.createTestPost("testTryingToDeleteOtherUsersPost_Other", true, otherUser);

        mockMvc.perform(
                delete("/api/posts/" + otherPost.id)
                        .with(user(UserDetailsImpl.build(me)))
                        .header("content-type", "application/json")
        ).andExpect(status().is(401));

        Post postAfterTryingToDelete = postRepo.findById(otherPost.id).orElseThrow(() -> new AssertionError("Post is " +
                "not exist."));
        Assert.assertEquals(otherPost.title, postAfterTryingToDelete.title);
        Assert.assertEquals(otherPost.content, postAfterTryingToDelete.content);
        Assert.assertEquals(otherPost.expose, postAfterTryingToDelete.expose);
    }
}
