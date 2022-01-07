package com.example.postapp.controller.post;

import com.example.postapp.TestUtil;
import com.example.postapp.controllers.post.RegisterPostBody;
import com.example.postapp.domain.models.Post;
import com.example.postapp.domain.models.User;
import com.example.postapp.domain.models.UserDetailsImpl;
import com.example.postapp.domain.repositories.PostRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles("test")
public class UpdatePostTest {
    @Autowired
    private TestUtil testUtil;
    @Autowired
    private PostRepository postRepo;
    @Autowired
    private MockMvc mockMvc;

    @Test
    void testUpdatePost() throws Exception {
        User user1 = testUtil.createTestUser("testUpdatePost");
        Post prePost = testUtil.createTestPost("testUpdatePost", true, user1);

        String body = new ObjectMapper().writeValueAsString(new RegisterPostBody("updated title", "updated content", false));
        mockMvc.perform(
                put("/api/posts/" + prePost.id)
                        .with(user(UserDetailsImpl.build(user1)))
                        .header("content-type", "application/json")
                        .content(body)
        ).andExpect(status().is(200));

        Post post = postRepo.findById(prePost.id).orElseThrow(() -> new AssertionError("Post is not exist."));
        Assert.assertEquals(post.title, "updated title");
        Assert.assertEquals(post.content, "updated content");
        Assert.assertFalse(post.expose);
    }

    @Test
    void testTryingToUpdateNoExistentPost() throws Exception {
        User user1 = testUtil.createTestUser("testNotExistsPostUpdating");
        String body = new ObjectMapper().writeValueAsString(new RegisterPostBody("test title", "test content", false));
        mockMvc.perform(
                put("/api/posts/100")
                        .with(user(UserDetailsImpl.build(user1)))
                        .header("content-type", "application/json")
                        .content(body)
        ).andExpect(status().is(400));
    }

    @Test
    void testTryingToUpdateOtherPost() throws Exception {
        User otherUser = testUtil.createTestUser("testTryOtherPostUpdating_Other");
        User me = testUtil.createTestUser("testTryOtherPostUpdating_Me");
        Post otherPost = testUtil.createTestPost("testTryOtherPostUpdating_Other", true, otherUser);
        String body = new ObjectMapper().writeValueAsString(new RegisterPostBody("test title", "test content", false));

        mockMvc.perform(
                put("/api/posts/" + otherPost.id)
                        .with(user(UserDetailsImpl.build(me)))
                        .header("content-type", "application/json")
                        .content(body)
        ).andExpect(status().is(403));

        Post postAfterTryUpdating = postRepo.findById(otherPost.id).orElseThrow(() -> new AssertionError("Post is not exist."));
        Assert.assertEquals(postAfterTryUpdating.title, otherPost.title);
        Assert.assertEquals(postAfterTryUpdating.content, otherPost.content);
        Assert.assertEquals(postAfterTryUpdating.expose, otherPost.expose);
    }
}
