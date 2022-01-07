package com.example.postapp.controller.post;

import com.example.postapp.TestUtil;
import com.example.postapp.controllers.post.PostsResponse;
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
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles("test")
public class GetPostsTest {
    @Autowired
    private TestUtil testUtil;
    @Autowired
    private PostRepository postRepo;
    @Autowired
    private MockMvc mockMvc;

    @Test
    void testGetPosts() throws Exception {
        User user = testUtil.createTestUser("testGetPosts_me");
        User otherUser = testUtil.createTestUser("testGetPosts_other");
        testUtil.createTestPost("testGetPosts1", false, user);
        testUtil.createTestPost("testGetPosts2", true, user);
        testUtil.createTestPost("testGetPosts_other1", true, otherUser);
        testUtil.createTestPost("testGetPosts_other2", false, otherUser);


        MvcResult result = mockMvc.perform(
                get("/api/posts/?page=1")
                        .with(user(UserDetailsImpl.build(user)))
                        .header("content-type", "application/json")
        ).andExpect(status().is(200)).andReturn();

        String responseStr = result.getResponse().getContentAsString();
        PostsResponse response = new ObjectMapper().readValue(responseStr, PostsResponse.class);

        Assert.assertEquals(response.posts.stream().filter(post -> !post.expose).count(), 0);
        Assert.assertTrue(response.posts.stream().filter(post -> post.expose).count() > 0);
    }

    @Test
    void testTryingToGetPostsWithoutPaginationParam() throws Exception {
        User user = testUtil.createTestUser("testTryingToGetPostsWithoutPaginationParam");
        testUtil.createTestPost("testTryingToGetPostsWithoutPaginationParam", false, user);
        mockMvc.perform(
                get("/api/posts/")
                        .with(user(UserDetailsImpl.build(user)))
                        .header("content-type", "application/json")
        ).andExpect(status().is(400));
    }

    // TODO ページングが正常に動いているか確認できるようなテストケース
}
