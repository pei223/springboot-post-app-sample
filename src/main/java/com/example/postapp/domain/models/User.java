package com.example.postapp.domain.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.List;

@Table
@Entity(name = "post_user")
public class User {
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false, length = 128)
    private String name;
    @JsonIgnore
    @Column(nullable = false, length = 255)
    private String password;
    @Column(nullable = false, unique = true, length = 255)
    private String email;

    @OneToMany(cascade = CascadeType.REMOVE, orphanRemoval = true, mappedBy = "author")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<Post> posts;

    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public User() {
    }
}
