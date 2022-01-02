package com.example.postapp.domain.models;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Table
@Entity
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;
    @NotBlank
    @NotNull
    @Size(max = 200)
    public String title;
    @NotBlank
    @NotNull
    @Size(max = 10000)
    public String content;
    @NotNull
    public boolean expose;

    @NotNull
    @ManyToOne
    @JoinColumn(referencedColumnName = "id")
    public User author;

    public void setAuthor(User author) {
        this.author = author;
    }
}
