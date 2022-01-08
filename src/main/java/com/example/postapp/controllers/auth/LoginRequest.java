package com.example.postapp.controllers.auth;

class LoginRequest {
    public final String name;
    public final String password;

    public LoginRequest(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public LoginRequest() {
        this.name = "";
        this.password = "";
    }
}
