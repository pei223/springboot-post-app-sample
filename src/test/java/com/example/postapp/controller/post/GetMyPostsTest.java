package com.example.postapp.controller.post;

import com.example.postapp.TestUtil;
import com.example.postapp.controllers.post.PostsResponse;
import com.example.postapp.domain.models.Post;
import com.example.postapp.domain.models.User;
import com.example.postapp.domain.models.UserDetailsImpl;
import com.example.postapp.domain.repositories.PostRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

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


    @Test
    void testMyPosts() throws Exception {
        User user = testUtil.createTestUser("testMyPosts_me");
        User otherUser = testUtil.createTestUser("testMyPosts_other");
        Post post1 = testUtil.createTestPost("testMyPosts1", true, user);
        Post post2 = testUtil.createTestPost("testMyPosts2", true, user);
        testUtil.createTestPost("testMyPosts_other", true, otherUser);

        String expectedRes = new ObjectMapper().writeValueAsString(new PostsResponse(Arrays.asList(post1, post2)));

        mockMvc.perform(
                get("/api/posts/me")
                        .with(user(UserDetailsImpl.build(user)))
                        .header("content-type", "application/json")
        ).andExpect(status().is(200)).andExpect(content().json(expectedRes));
    }

}
