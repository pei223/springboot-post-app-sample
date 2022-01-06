package com.example.postapp.controllers.post;


public class RegisterPostBody {

    public final String title;
    public final String content;
    public final boolean expose;

    public RegisterPostBody(String title, String content, boolean expose) {
        this.title = title;
        this.content = content;
        this.expose = expose;
    }
}
