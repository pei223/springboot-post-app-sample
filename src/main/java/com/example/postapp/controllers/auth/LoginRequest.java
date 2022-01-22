package com.example.postapp.controllers.auth;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class LoginRequest {
    @NotBlank
    @Size(min = 1, max = 128)
    public final String name;
    @NotBlank
    @Size(min = 5, max = 20)
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
