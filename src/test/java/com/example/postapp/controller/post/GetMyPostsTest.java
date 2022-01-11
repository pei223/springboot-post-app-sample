package com.example.postapp.controller.post;

import com.example.postapp.TestUtil;
import com.example.postapp.controllers.post.PostsResponse;
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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.util.NestedServletException;

import javax.validation.ConstraintViolationException;
import java.util.Arrays;
import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles("test")
public class GetMyPostsTest {
    @Autowired
    private TestUtil testUtil;
    @Autowired
    private PostRepository postRepo;
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testMyPosts() throws Exception {
        User user = testUtil.createTestUser("testMyPosts_me");
        User otherUser = testUtil.createTestUser("testMyPosts_other");
        Post post1 = testUtil.createTestPost("testMyPosts1", true, user);
        Post post2 = testUtil.createTestPost("testMyPosts2", true, user);
        testUtil.createTestPost("testMyPosts_other", true, otherUser);

        List<Post> expectedPosts = Arrays.asList(post2, post1);

        MvcResult result = mockMvc.perform(
                get("/api/posts/me?page=1")
                        .with(user(UserDetailsImpl.build(user)))
                        .header("content-type", "application/json")
        ).andExpect(status().is(200)).andReturn();

        String responseStr = result.getResponse().getContentAsString();
        PostsResponse response = objectMapper.readValue(responseStr, PostsResponse.class);

        Assert.assertEquals(expectedPosts.size(), response.posts.size());
        for (int i = 0; i < response.posts.size(); i++) {
            if (!response.posts.get(i).equals(expectedPosts.get(i))) {
                Assert.fail();
            }
        }
    }

    @Test
    void testGetMyPostPagination() throws Exception {
        User user = testUtil.createTestUser("testGetMyPostPagination_me");
        User otherUser = testUtil.createTestUser("testGetMyPostPagination_other");
        testUtil.createTestPost("testGetMyPostPagination_other", true, otherUser);
        testUtil.createManyTestPost(20, "testGetMyPostPagination_me", user);

        MvcResult result = mockMvc.perform(
                get("/api/posts/me?page=1")
                        .with(user(UserDetailsImpl.build(user)))
                        .header("content-type", "application/json")
        ).andExpect(status().is(200)).andReturn();

        String responseStr = result.getResponse().getContentAsString();
        PostsResponse response = objectMapper.readValue(responseStr, PostsResponse.class);

        Assert.assertEquals(10, response.posts.size());
        Assert.assertEquals(2, response.totalPage);
    }

    @Test
    void testTryingToGetMyPostWithNoPageParam() throws Exception {
        User user = testUtil.createTestUser("testTryingToGetMyPostWithNoPageParam");
        testUtil.createManyTestPost(20, "testTryingToGetMyPostWithNoPageParam", user);

        mockMvc.perform(
                get("/api/posts/me")
                        .with(user(UserDetailsImpl.build(user)))
                        .header("content-type", "application/json")
        ).andExpect(status().is(400));
    }
}
