package com.example.postapp.controllers.auth;

public class JwtResponse {
    public final String token;
    public final long id;
    public final String name;
    public final String email;

    public JwtResponse(String token, long id, String name, String email) {
        this.token = token;
        this.id = id;
        this.name = name;
        this.email = email;
    }
}
