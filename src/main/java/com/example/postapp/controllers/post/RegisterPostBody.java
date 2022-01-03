package com.example.postapp.controllers.post;

public class RegisterPostBody {
    String title;
    String content;
    boolean expose;

    public RegisterPostBody(String title, String content, boolean expose) {
        this.title = title;
        this.content = content;
        this.expose = expose;
    }
}
