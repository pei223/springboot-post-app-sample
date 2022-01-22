package com.example.postapp.controller.auth;

import com.example.postapp.controllers.auth.JwtResponse;
import com.example.postapp.controllers.auth.LoginRequest;
import com.example.postapp.controllers.auth.SignupRequest;
import com.example.postapp.controllers.common.ErrorResponse;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class LoginTest {
    private boolean init = false;

    private final TestRestTemplate restTemplate = new TestRestTemplate();

    @LocalServerPort
    private int port;

    @BeforeEach
    public void setupUser() {
        if (init) {
            return;
        }
        init = true;
        // TODO 本当はRepositoryでやった方が楽だがなぜかエラー起きる
        SignupRequest request = new SignupRequest("ログインテストユーザー", "password123", "test1@email.com");
        restTemplate.postForEntity("http://localhost:" + port + "/api/auth/signup", request, JwtResponse.class);
    }

    @Test
    void testLogin() {
        LoginRequest request = new LoginRequest("ログインテストユーザー", "password123");
        ResponseEntity<JwtResponse> response =
                restTemplate.postForEntity("http://localhost:" + port + "/api/auth/login", request, JwtResponse.class);
        Assert.assertEquals(200, response.getStatusCode().value());
        if (response.getBody() == null) {
            Assert.fail("Body is null.");
        }
        Assert.assertEquals("test1@email.com", response.getBody().email);
        Assert.assertEquals(request.name, response.getBody().name);
        Assert.assertNotNull(response.getBody().token);
    }

    @Test
    void testTryToLoginWithTooLongName() {
        LoginRequest request = new LoginRequest("1".repeat(256), "password1");
        ResponseEntity<ErrorResponse> response = restTemplate.postForEntity("http://localhost:" + port + "/api/auth/signup",
                request, ErrorResponse.class);
        if (response.getBody() == null) {
            Assert.fail();
        }
        Assert.assertEquals(400, response.getStatusCode().value());
        Assert.assertTrue(response.getBody().data.containsKey("name"));
    }


    @Test
    void testTryToLoginWithEmptyName() {
        LoginRequest request = new LoginRequest(null, "password1");
        ResponseEntity<ErrorResponse> response = restTemplate.postForEntity("http://localhost:" + port + "/api/auth/signup",
                request, ErrorResponse.class);
        if (response.getBody() == null) {
            Assert.fail();
        }
        Assert.assertEquals(400, response.getStatusCode().value());
        Assert.assertTrue(response.getBody().data.containsKey("name"));
    }


    @Test
    void testTryToLoginWithTooLongPassword() {
        LoginRequest request = new LoginRequest("ログインテストユーザー", "1".repeat(21));
        ResponseEntity<ErrorResponse> response = restTemplate.postForEntity("http://localhost:" + port + "/api/auth/signup",
                request, ErrorResponse.class);
        if (response.getBody() == null) {
            Assert.fail();
        }
        Assert.assertEquals(400, response.getStatusCode().value());
        Assert.assertTrue(response.getBody().data.containsKey("password"));
    }


    @Test
    void testTryToLoginWithEmptyPassword() {
        LoginRequest request = new LoginRequest("ログインテストユーザー", "");
        ResponseEntity<ErrorResponse> response = restTemplate.postForEntity("http://localhost:" + port + "/api/auth/signup",
                request, ErrorResponse.class);
        if (response.getBody() == null) {
            Assert.fail();
        }
        Assert.assertEquals(400, response.getStatusCode().value());
        Assert.assertTrue(response.getBody().data.containsKey("password"));
    }
}
