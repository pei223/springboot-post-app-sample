package com.example.postapp.domain.models;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Table
@Entity
public class Favorite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @Nullable
    @OnDelete(action = OnDeleteAction.CASCADE)
    @ManyToOne
    public Post post;

    @NotNull
    @OnDelete(action = OnDeleteAction.CASCADE)
    @ManyToOne
    public User user;

    @NotNull
    @NotBlank
    public String postTitle;
    @NotNull
    @NotBlank
    public String authorName;

    public Favorite() {
    }

    public Favorite(User user, Post post, String postTitle, String authorName) {
        this.user = user;
        this.post = post;
        this.postTitle = postTitle;
        this.authorName = authorName;
    }

    public static Favorite build(User user, Post post) {
        return new Favorite(user, post, post.title, post.author.getName());
    }
}
