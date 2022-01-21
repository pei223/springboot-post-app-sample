package com.example.postapp.controllers.post;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class RegisterPostBody {
    @Size(max = 200, message = "{post.size}")
    @NotBlank
    @NotNull
    public final String title;
    @Size(max = 10000)
    @NotBlank
    @NotNull
    public final String content;
    @NotNull
    public final boolean expose;

    public RegisterPostBody(String title, String content, boolean expose) {
        this.title = title;
        this.content = content;
        this.expose = expose;
    }

    public RegisterPostBody() {
        this.title = "";
        this.content = "";
        this.expose = false;
    }
}
