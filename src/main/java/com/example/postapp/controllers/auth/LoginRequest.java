package com.example.postapp.controllers.auth;

public class LoginRequest {
    public final String name;
    public final String password;

    LoginRequest(String name, String password) {
        this.name = name;
        this.password = password;
    }
}
