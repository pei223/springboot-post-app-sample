package com.example.postapp.domain.models;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Table
@Entity
public class Album {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(referencedColumnName = "id")
    public User author;

    @NotBlank
    @Size(max = 200)
    public String title;
    @NotBlank
    @Size(max = 500)
    public String description;

    @OneToMany
    public List<Post> posts;

    public void setAuthor(User author) {
        this.author = author;
    }
}
