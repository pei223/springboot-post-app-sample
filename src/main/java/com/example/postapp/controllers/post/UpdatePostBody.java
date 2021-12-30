package com.example.postapp.controllers.post;

public class UpdatePostBody {
    String title;
    String content;
    boolean expose;

    public UpdatePostBody(String title, String content, boolean expose) {
        this.title = title;
        this.content = content;
        this.expose = expose;
    }
}
