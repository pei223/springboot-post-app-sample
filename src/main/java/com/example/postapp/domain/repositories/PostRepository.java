package com.example.postapp.domain.repositories;

import com.example.postapp.domain.models.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByAuthorId(long authorId);

    List<Post> findAllByExpose(boolean expose);
}
