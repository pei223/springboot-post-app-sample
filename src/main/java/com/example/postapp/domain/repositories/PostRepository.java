package com.example.postapp.domain.repositories;

import com.example.postapp.domain.models.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findAllByAuthorId(long authorId, Pageable pageable);

    Page<Post> findAllByExpose(boolean expose, Pageable pageable);
}
