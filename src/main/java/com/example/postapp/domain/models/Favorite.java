package com.example.postapp.domain.models;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;


@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "post_id"})
})
@Entity
public class Favorite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @OneToOne(cascade = CascadeType.ALL)
    public User user;

    @OneToOne
    public Post post;

    @NotBlank
    public String postTitle;
    @NotBlank
    public String authorName;

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
