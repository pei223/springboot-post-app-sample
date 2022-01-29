package com.example.postapp.domain.models;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;


@Table
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Favorite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @NotNull
    public long postId;

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

    @CreatedDate
    public LocalDateTime createdAt;

    public Favorite() {
    }

    public Favorite(User user, long postId, String postTitle, String authorName) {
        this.user = user;
        this.postId = postId;
        this.postTitle = postTitle;
        this.authorName = authorName;
    }

    public static Favorite build(User user, Post post) {
        return new Favorite(user, post.id, post.title, post.author.getName());
    }
}
