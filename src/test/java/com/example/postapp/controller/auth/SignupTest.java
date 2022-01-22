package com.example.postapp.controller.auth;

import com.example.postapp.controllers.auth.JwtResponse;
import com.example.postapp.controllers.auth.SignupRequest;
import com.example.postapp.controllers.common.ErrorResponse;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class SignupTest {
    private final TestRestTemplate restTemplate = new TestRestTemplate();

    @LocalServerPort
    int port;

    @Test
    void testSignup() {
        SignupRequest request = new SignupRequest("testname1", "testpass1", "test1@email.com");
        ResponseEntity<JwtResponse> response =
                restTemplate.postForEntity("http://localhost:" + port + "/api/auth/signup", request, JwtResponse.class);
        Assert.assertEquals(200, response.getStatusCode().value());
        if (response.getBody() == null) {
            Assert.fail("Body is null.");
        }
        Assert.assertEquals(request.email, response.getBody().email);
        Assert.assertEquals(request.name, response.getBody().name);
        Assert.assertNotNull(response.getBody().token);
    }


    @Test
    void testSignupWithUpperBoundaryValues() {
        SignupRequest request = new SignupRequest("1".repeat(128), "1".repeat(20), "1".repeat(255));
        ResponseEntity<JwtResponse> response =
                restTemplate.postForEntity("http://localhost:" + port + "/api/auth/signup", request, JwtResponse.class);
        Assert.assertEquals(200, response.getStatusCode().value());
        if (response.getBody() == null) {
            Assert.fail("Body is null.");
        }
        Assert.assertEquals(request.email, response.getBody().email);
        Assert.assertEquals(request.name, response.getBody().name);
        Assert.assertNotNull(response.getBody().token);
    }


    @Test
    void testSignupWithLowerBoundaryValues() {
        SignupRequest request = new SignupRequest("1".repeat(1), "1".repeat(5), "1".repeat(1));
        ResponseEntity<JwtResponse> response =
                restTemplate.postForEntity("http://localhost:" + port + "/api/auth/signup", request, JwtResponse.class);
        Assert.assertEquals(200, response.getStatusCode().value());
        if (response.getBody() == null) {
            Assert.fail("Body is null.");
        }
        Assert.assertEquals(request.email, response.getBody().email);
        Assert.assertEquals(request.name, response.getBody().name);
        Assert.assertNotNull(response.getBody().token);
    }

    @Test
    void testTryToSignupWithTooLongName() {
        SignupRequest tooLongNameRequest = new SignupRequest("1".repeat(129), "testpass1", "test1@email.com");
        ResponseEntity<ErrorResponse> tooLongNameRes = restTemplate.postForEntity("http://localhost:" + port + "/api/auth/signup",
                tooLongNameRequest, ErrorResponse.class);
        if (tooLongNameRes.getBody() == null) {
            Assert.fail();
        }
        Assert.assertEquals(400, tooLongNameRes.getStatusCode().value());
        Assert.assertTrue(tooLongNameRes.getBody().data.containsKey("name"));
    }

    @Test
    void testTryToSignupWithEmptyName() {
        SignupRequest emptyNameRequest = new SignupRequest("", "testpass1", "test1@email.com");
        ResponseEntity<ErrorResponse> emptyNameRes = restTemplate.postForEntity("http://localhost:" + port + "/api" +
                        "/auth" +
                        "/signup",
                emptyNameRequest, ErrorResponse.class);
        if (emptyNameRes.getBody() == null) {
            Assert.fail();
        }
        Assert.assertEquals(400, emptyNameRes.getStatusCode().value());
        Assert.assertTrue(emptyNameRes.getBody().data.containsKey("name"));
    }


    @Test
    void testTryToSignupWithTooLongPassword() {
        SignupRequest request = new SignupRequest("ユーザー1", "p".repeat(21), "test1@email.com");
        ResponseEntity<ErrorResponse> response = restTemplate.postForEntity("http://localhost:" + port + "/api/auth/signup",
                request, ErrorResponse.class);
        if (response.getBody() == null) {
            Assert.fail();
        }
        Assert.assertEquals(400, response.getStatusCode().value());
        Assert.assertTrue(response.getBody().data.containsKey("password"));
    }

    @Test
    void testTryToSignupWithEmptyPassword() {
        SignupRequest request = new SignupRequest("ユーザー1", "", "test1@email.com");
        ResponseEntity<ErrorResponse> response = restTemplate.postForEntity("http://localhost:" + port + "/api/auth/signup",
                request, ErrorResponse.class);
        if (response.getBody() == null) {
            Assert.fail();
        }
        Assert.assertEquals(400, response.getStatusCode().value());
        Assert.assertTrue(response.getBody().data.containsKey("password"));
    }


    @Test
    void testTryToSignupWithTooLongEmail() {
        SignupRequest request = new SignupRequest("ユーザー1", "password1", "test1@" + "1".repeat(251));
        ResponseEntity<ErrorResponse> response = restTemplate.postForEntity("http://localhost:" + port + "/api/auth/signup",
                request, ErrorResponse.class);
        if (response.getBody() == null) {
            Assert.fail();
        }
        Assert.assertEquals(400, response.getStatusCode().value());
        Assert.assertTrue(response.getBody().data.containsKey("email"));
    }


    @Test
    void testTryToSignupWithEmptyEmail() {
        SignupRequest request = new SignupRequest("ユーザー1", "password1", "");
        ResponseEntity<ErrorResponse> response = restTemplate.postForEntity("http://localhost:" + port + "/api/auth/signup",
                request, ErrorResponse.class);
        if (response.getBody() == null) {
            Assert.fail();
        }
        Assert.assertEquals(400, response.getStatusCode().value());
        Assert.assertTrue(response.getBody().data.containsKey("email"));
    }

    // TODO 同じユーザー名のデータ登録のテストケース
}
