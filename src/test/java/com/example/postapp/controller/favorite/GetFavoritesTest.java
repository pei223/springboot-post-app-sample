package com.example.postapp.controller.favorite;

import com.example.postapp.TestUtil;
import com.example.postapp.domain.models.Post;
import com.example.postapp.domain.models.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles("test")
public class GetFavoritesTest {
    @Autowired
    private TestUtil testUtil;
    @Autowired
    private MockMvc mockMvc;

    @Test
    void testAddFavorite() {
        User me = testUtil.createTestUser("testAddFavorite_me");
        User other = testUtil.createTestUser("testAddFavorite_other");
        Post myPost1 = testUtil.createTestPost("testAddFavorite_me1", true, me);
        Post myPost2 = testUtil.createTestPost("testAddFavorite_me2", true, me);


    }

    @Test
    void testAddFavoriteOfOtherUsersPost() {
        User user = testUtil.createTestUser("testAddFavorite");
        Post post1 = testUtil.createTestPost("testAddFavorite", true, user);

    }

    @Test
    void testTryingToAddFavoriteOfNoExistPost() {

    }

    @Test
    void testTryingToAddFavoriteAlreadyExists() {

    }
}
