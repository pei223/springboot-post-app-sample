package com.example.postapp.controller.post;

import com.example.postapp.TestUtil;
import com.example.postapp.domain.models.Post;
import com.example.postapp.domain.models.User;
import com.example.postapp.domain.models.UserDetailsImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
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
public class FindPostTest {
    @Autowired
    private TestUtil testUtil;
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    void testFindMyPost() throws Exception {
        User user1 = testUtil.createTestUser("testFindMyPost");
        Post exposedPost = testUtil.createTestPost("testFindMyPost_exposed", true, user1);
        Post notExposedPost = testUtil.createTestPost("testFindMyPost_not_exposed", false, user1);

        mockMvc.perform(
                get("/api/posts/" + exposedPost.id)
                        .with(user(UserDetailsImpl.build(user1)))
                        .header("content-type", "application/json")
        ).andExpect(status().is(200));
        // TODO 取得したデータの中身も見たい

        mockMvc.perform(
                get("/api/posts/" + notExposedPost.id)
                        .with(user(UserDetailsImpl.build(user1)))
                        .header("content-type", "application/json")
        ).andExpect(status().is(200));
        // TODO 取得したデータの中身も見たい
    }

    @Test
    void testFindOtherUsersPost() throws Exception {
        User me = testUtil.createTestUser("testFindOtherUsersPost_me");
        User other = testUtil.createTestUser("testFindOtherUsersPost_other");

        Post exposedPost = testUtil.createTestPost("testFindOtherUsersPost_exposed", true, other);
        Post notExposedPost = testUtil.createTestPost("testFindOtherUsersPost_not_exposed", false, other);

        mockMvc.perform(
                get("/api/posts/" + exposedPost.id)
                        .with(user(UserDetailsImpl.build(me)))
                        .header("content-type", "application/json")
        ).andExpect(status().is(200));
        // TODO 取得したデータの中身も見たい

        mockMvc.perform(
                get("/api/posts/" + notExposedPost.id)
                        .with(user(UserDetailsImpl.build(me)))
                        .header("content-type", "application/json")
        ).andExpect(status().is(404));
    }

    @Test
    void testFindOthersPostWithoutLogin() throws Exception {
        User other = testUtil.createTestUser("testFindOthersPostWithoutLogin");
        Post exposedPost = testUtil.createTestPost("testFindOthersPostWithoutLogin_exposed", true, other);
        Post notExposedPost = testUtil.createTestPost("testFindOthersPostWithoutLogin_not_exposed", false, other);

        mockMvc.perform(
                get("/api/posts/" + exposedPost.id)
                        .header("content-type", "application/json")
        ).andExpect(status().is(200));
        // TODO 取得したデータの中身も見たい

        mockMvc.perform(
                get("/api/posts/" + notExposedPost.id)
                        .header("content-type", "application/json")
        ).andExpect(status().is(404));
    }


}
