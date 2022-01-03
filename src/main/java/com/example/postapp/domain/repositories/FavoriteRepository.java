package com.example.postapp.domain.repositories;

import com.example.postapp.domain.models.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    List<Favorite> findAllByUserId(long userId);

    boolean existsByUserIdAndPostId(long userId, long postId);

    List<Favorite> deleteByUserIdAndPostId(long userId, long postId);
}
