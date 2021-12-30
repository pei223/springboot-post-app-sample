package com.example.postapp.domain.models;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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
    @Size(max = 200)
    public String title;
    @NotBlank
    @Size(max = 10000)
    public String content;
    @NotNull
    public boolean expose;

    @ManyToOne
    @JoinColumn(referencedColumnName = "id")
    public User author;

    public void setAuthor(User author) {
        this.author = author;
    }
}
