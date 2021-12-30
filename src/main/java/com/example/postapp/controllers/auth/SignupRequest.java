package com.example.postapp.controllers.auth;

public class SignupRequest {
    public final String name;
    public final String password;
    public final String email;

    SignupRequest(String name, String password, String email) {
        this.name = name;
        this.password = password;
        this.email = email;
    }
}
