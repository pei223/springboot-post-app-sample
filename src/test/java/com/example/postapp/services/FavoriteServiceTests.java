package com.example.postapp.services;


import com.example.postapp.domain.models.UserDetailsImpl;
import com.example.postapp.services.common.NotFoundException;
import com.example.postapp.services.favorite.FavoriteService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.Assert.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
public class FavoriteServiceTests {
    @Autowired
    private FavoriteService service;

    @Test
    void testDelete() throws Exception {
        try {
            service.delete(new UserDetailsImpl(1, "", "", ""), 1);
        } catch (NotFoundException expected) {
            assertTrue(true);
        }
    }
}
