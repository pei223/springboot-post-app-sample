package com.example.postapp.controllers.auth;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class SignupRequest extends LoginRequest {
    @NotBlank
    @Size(max = 255)
    public final String email;

    public SignupRequest(String name, String password, String email) {
        super(name, password);
        this.email = email;
    }

    public SignupRequest() {
        super();
        this.email = "";
    }
}
