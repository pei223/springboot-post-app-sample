package com.example.postapp.domain.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalTime;
import java.util.Objects;

@Table
@Entity
@EntityListeners(AuditingEntityListener.class)
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

    @CreatedDate
    public LocalTime createdAt;
    @LastModifiedDate
    public LocalTime updatedAt;
    @JsonIgnore
    @CreatedBy
    public String createdByIpAddress;
    @JsonIgnore
    @LastModifiedBy
    public String updatedByIpAddress;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "author_id")
    public User author;

    public Post(@NotBlank @NotNull @Size(max = 200) String title, @NotBlank @NotNull @Size(max = 10000) String content, @NotNull boolean expose) {
        this.title = title;
        this.content = content;
        this.expose = expose;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Post() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Post post = (Post) o;
        return id == post.id &&
                expose == post.expose &&
                Objects.equals(title, post.title) &&
                Objects.equals(content, post.content) &&
                Objects.equals(author.getId(), post.author.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, content, expose, author);
    }
}
