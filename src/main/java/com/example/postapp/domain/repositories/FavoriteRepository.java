package com.example.postapp.domain.repositories;

import com.example.postapp.domain.models.Favorite;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    Page<Favorite> findAllByUserId(long userId, Pageable page);

    boolean existsByUserIdAndPostId(long userId, long postId);

    void deleteByUserIdAndPostId(long userId, long postId);
}

