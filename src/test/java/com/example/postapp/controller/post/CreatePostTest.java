package com.example.postapp.controller.post;

import com.example.postapp.TestUtil;
import com.example.postapp.controllers.common.ErrorResponse;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.WebApplicationContext;

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

    @Autowired
    private ObjectMapper objectMapper;

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
        List<Post> post = postRepo.findAllByAuthorId(user1.getId(), PageRequest.of(0, 1000)).getContent();
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

        List<Post> post = postRepo.findAllByAuthorId(user1.getId(), PageRequest.of(0, 1000)).getContent();
        Assert.assertEquals(post.size(), 2);
    }

    @Test
    @DisplayName("無効なタイトルで記事作成を試行")
    void testTryToCreatePostWithInvalidTitle() throws Exception {
        User user1 = testUtil.createTestUser("testTryToCreatePostWithInvalidTitle");
        String tooLongTitleBody =
                new ObjectMapper().writeValueAsString(new RegisterPostBody("1".repeat(201), "test content", false));
        MvcResult tooLongTitleResult = mockMvc.perform(
                post("/api/posts/")
                        .with(user(UserDetailsImpl.build(user1)))
                        .header("content-type", "application/json")
                        .content(tooLongTitleBody)
        ).andExpect(status().is(400)).andReturn();
        ErrorResponse tooLongTitleResponse =
                objectMapper.readValue(tooLongTitleResult.getResponse().getContentAsString(), ErrorResponse.class);

        Assert.assertTrue("Validation error result contains title field.", tooLongTitleResponse.data.containsKey("title"));
        Assert.assertEquals("Post tried to create is not registered",
                postRepo.findAllByAuthorId(user1.getId(), PageRequest.of(0, 1000)).getContent().size()
                , 0);


        String emptyTitleBody =
                new ObjectMapper().writeValueAsString(new RegisterPostBody("", "test content", false));
        MvcResult emptyTitleResult = mockMvc.perform(
                post("/api/posts/")
                        .with(user(UserDetailsImpl.build(user1)))
                        .header("content-type", "application/json")
                        .content(emptyTitleBody)
        ).andExpect(status().is(400)).andReturn();
        ErrorResponse emptyTitleResponse =
                objectMapper.readValue(emptyTitleResult.getResponse().getContentAsString(), ErrorResponse.class);

        Assert.assertTrue("Validation error result contains title field.", emptyTitleResponse.data.containsKey("title"));
        Assert.assertEquals(postRepo.findAllByAuthorId(user1.getId(), PageRequest.of(0, 1000)).getContent().size(), 0);
    }

    @Test
    @DisplayName("無効な本文で記事作成を試行")
    void testTryToCreatePostWithInvalidContent() throws Exception {
        User user1 = testUtil.createTestUser("testTryToCreatePostWithInvalidContent");
        String tooLongContentBody =
                new ObjectMapper().writeValueAsString(new RegisterPostBody("test content", "1".repeat(10001), false));

        MvcResult tooLongContentResult = mockMvc.perform(
                post("/api/posts/")
                        .with(user(UserDetailsImpl.build(user1)))
                        .header("content-type", "application/json")
                        .content(tooLongContentBody)
        ).andExpect(status().is(400)).andReturn();
        ErrorResponse tooLongContentResponse =
                objectMapper.readValue(tooLongContentResult.getResponse().getContentAsString(), ErrorResponse.class);

        Assert.assertTrue("Validation error result contains content field.", tooLongContentResponse.data.containsKey(
                "content"));
        Assert.assertEquals("Post tried to create is not registered",
                postRepo.findAllByAuthorId(user1.getId(), PageRequest.of(0, 1000)).getContent().size()
                , 0);


        String emptyContentBody =
                new ObjectMapper().writeValueAsString(new RegisterPostBody("test title", "1".repeat(10001), false));

        MvcResult emptyContentResult = mockMvc.perform(
                post("/api/posts/")
                        .with(user(UserDetailsImpl.build(user1)))
                        .header("content-type", "application/json")
                        .content(emptyContentBody)
        ).andExpect(status().is(400)).andReturn();
        ErrorResponse emptyContentResponse =
                objectMapper.readValue(emptyContentResult.getResponse().getContentAsString(), ErrorResponse.class);
        Assert.assertTrue("Validation error result contains content field.", emptyContentResponse.data.containsKey(
                "content"));

        Assert.assertEquals(postRepo.findAllByAuthorId(user1.getId(), PageRequest.of(0, 1000)).getContent().size(), 0);
    }
}
