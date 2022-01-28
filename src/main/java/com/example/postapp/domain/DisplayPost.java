package com.example.postapp.domain;

import com.example.postapp.domain.models.Post;
import com.example.postapp.domain.models.User;


public class DisplayPost {
    public long id;
    public String title;
    public String content;
    public User author;
    public final boolean favorited;

    public static DisplayPost toDisplayPost(Post post, boolean favorited) {
        return new DisplayPost(post.id, post.title, post.content, post.author, favorited);
    }

    public DisplayPost(long id, String title, String content, User author, boolean favorited) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.author = author;
        this.favorited = favorited;
    }

    public DisplayPost() {
        this.id = 0;
        this.title = "";
        this.content = "";
        this.author = null;
        this.favorited = false;
    }
}
