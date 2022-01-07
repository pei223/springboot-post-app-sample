package com.example.postapp.controller.post;

import com.example.postapp.TestUtil;
import com.example.postapp.controllers.post.RegisterPostBody;
import com.example.postapp.domain.models.Post;
import com.example.postapp.domain.models.User;
import com.example.postapp.domain.models.UserDetailsImpl;
import com.example.postapp.domain.repositories.PostRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.util.NestedServletException;

import javax.validation.ConstraintViolationException;
import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles("test")
public class CreatePostTest {
    @Autowired
    private TestUtil testUtil;
    @Autowired
    private PostRepository postRepo;
    @Autowired
    private WebApplicationContext context;
    @Autowired
    private MockMvc mockMvc;


    @Test
    @DisplayName("記事作成")
    void testCreatePost() throws Exception {
        User user1 = testUtil.createTestUser("testCreatePost");
        String body = new ObjectMapper().writeValueAsString(new RegisterPostBody("test title", "test content", false));

        mockMvc.perform(
                post("/api/posts/")
                        .with(user(UserDetailsImpl.build(user1)))
                        .header("content-type", "application/json")
                        .content(body)
        ).andExpect(status().is(200));
        List<Post> post = postRepo.findAllByAuthorId(user1.getId());
        Assert.assertEquals(post.size(), 1);
    }

    @Test
    @DisplayName("境界値付近のタイトル・本文の記事作成")
    void testCreatePostWithBoundaryValues() throws Exception {
        User user1 = testUtil.createTestUser("testCreatePostWithBoundaryValues");
        String longTitleAndContentBody = new ObjectMapper().writeValueAsString(new RegisterPostBody("1".repeat(200), "1".repeat(10000), false));

        mockMvc.perform(
                post("/api/posts/")
                        .with(user(UserDetailsImpl.build(user1)))
                        .header("content-type", "application/json")
                        .content(longTitleAndContentBody)
        ).andExpect(status().is(200));

        String shortTitleAndContentBody = new ObjectMapper().writeValueAsString(new RegisterPostBody("1", "1", false));

        mockMvc.perform(
                post("/api/posts/")
                        .with(user(UserDetailsImpl.build(user1)))
                        .header("content-type", "application/json")
                        .content(shortTitleAndContentBody)
        ).andExpect(status().is(200));

        List<Post> post = postRepo.findAllByAuthorId(user1.getId());
        Assert.assertEquals(post.size(), 2);
    }

    @Test
    @DisplayName("無効なタイトルで記事作成を試行")
    void testTryToCreatePostWithInvalidTitle() throws Exception {
        User user1 = testUtil.createTestUser("testTryToCreatePostWithInvalidTitle");
        String tooLongTitleBody =
                new ObjectMapper().writeValueAsString(new RegisterPostBody("1".repeat(201), "test content", false));
        try {
            mockMvc.perform(
                    post("/api/posts/")
                            .with(user(UserDetailsImpl.build(user1)))
                            .header("content-type", "application/json")
                            .content(tooLongTitleBody)
            ).andExpect(status().is(400));
            Assert.assertEquals(postRepo.findAllByAuthorId(user1.getId()).size(), 0);
        } catch (NestedServletException e) {
            // TODO 本当はレスポンスの中身を見るようにしたい
            // 実装を@Validatedではないやり方にするか、ValidationのExceptionは起こさずに400が返る方法を調べる(なさそう)
            Assert.assertTrue(e.getCause() instanceof ConstraintViolationException);
        }

        try {
            String emptyTitleBody =
                    new ObjectMapper().writeValueAsString(new RegisterPostBody("", "test content", false));
            mockMvc.perform(
                    post("/api/posts/")
                            .with(user(UserDetailsImpl.build(user1)))
                            .header("content-type", "application/json")
                            .content(emptyTitleBody)
            ).andExpect(status().is(400));
        } catch (NestedServletException e) {
            // TODO 本当はレスポンスの中身を見るようにしたい
            // 実装を@Validatedではないやり方にするか、ValidationのExceptionは起こさずに400が返る方法を調べる(なさそう)
            Assert.assertTrue(e.getCause() instanceof ConstraintViolationException);
        }

        Assert.assertEquals(postRepo.findAllByAuthorId(user1.getId()).size(), 0);
    }

    @Test
    @DisplayName("無効な本文で記事作成を試行")
    void testTryToCreatePostWithInvalidContent() throws Exception {
        User user1 = testUtil.createTestUser("testTryToCreatePostWithInvalidContent");
        String tooLongContentBody =
                new ObjectMapper().writeValueAsString(new RegisterPostBody("test content", "1".repeat(10001), false));

        try {
            mockMvc.perform(
                    post("/api/posts/")
                            .with(user(UserDetailsImpl.build(user1)))
                            .header("content-type", "application/json")
                            .content(tooLongContentBody)
            ).andExpect(status().is(400));
        } catch (NestedServletException e) {
            // TODO 本当はレスポンスの中身を見るようにしたい
            // 実装を@Validatedではないやり方にするか、ValidationのExceptionは起こさずに400が返る方法を調べる(なさそう)
            Assert.assertTrue(e.getCause() instanceof ConstraintViolationException);
        }

        try {
            String emptyContentBody =
                    new ObjectMapper().writeValueAsString(new RegisterPostBody("test title", "1".repeat(10001), false));

            mockMvc.perform(
                    post("/api/posts/")
                            .with(user(UserDetailsImpl.build(user1)))
                            .header("content-type", "application/json")
                            .content(emptyContentBody)
            ).andExpect(status().is(400));
        } catch (NestedServletException e) {
            // TODO 本当はレスポンスの中身を見るようにしたい
            // 実装を@Validatedではないやり方にするか、ValidationのExceptionは起こさずに400が返る方法を調べる(なさそう)
            Assert.assertTrue(e.getCause() instanceof ConstraintViolationException);
        }
        Assert.assertEquals(postRepo.findAllByAuthorId(user1.getId()).size(), 0);
    }
}
