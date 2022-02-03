package com.example.postapp.domain.models;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
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
    @OnDelete(action = OnDeleteAction.CASCADE)
    @ManyToOne
    public User user;

    @OnDelete(action = OnDeleteAction.CASCADE)
    @NotNull
    @ManyToOne
    public Post post;

    @CreatedDate
    public LocalDateTime createdAt;

    public Favorite() {
    }

    public Favorite(User user, Post post) {
        this.user = user;
        this.post = post;
    }
}
