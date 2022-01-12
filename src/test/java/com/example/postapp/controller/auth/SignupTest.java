package com.example.postapp.controller.auth;

import com.example.postapp.controllers.auth.JwtResponse;
import com.example.postapp.controllers.auth.SignupRequest;
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
}
