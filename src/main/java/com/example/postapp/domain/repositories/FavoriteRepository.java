package com.example.postapp.domain.repositories;

import com.example.postapp.domain.models.Favorite;
import com.example.postapp.domain.models.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.OptionalLong;
import java.util.stream.Collectors;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long>, FavoriteRepositoryCustom {
    List<Favorite> findAllByUserId(long userId);

    boolean existsByUserIdAndPostId(long userId, long postId);

    void deleteByUserIdAndPostId(long userId, long postId);
}


interface FavoriteRepositoryCustom {
    List<Favorite> findAllByPostList(OptionalLong userId, List<Post> postList);

    Page<FavoritePost> findAllMyFavoritePost(long userId, Pageable page);
}

class FavoriteRepositoryImpl implements FavoriteRepositoryCustom {
    @Autowired
    EntityManager entityManager;

    @Override
    public List<Favorite> findAllByPostList(OptionalLong userId, List<Post> postList) {
        var postIdList = postList.stream().map((post) ->
                post.id
        ).collect(Collectors.toList());
        String jpql = "select f from Favorite f where f.user.id = (:userId) AND f.postId IN (:postIdList)";
        return entityManager.createQuery(jpql, Favorite.class)
                .setParameter("postIdList", postIdList)
                .setParameter("userId",
                        userId.isEmpty() ? -1 : userId.getAsLong()).getResultList();
    }

    public Page<FavoritePost> findAllMyFavoritePost(long userId, Pageable page) {
        String countJpql = "SELECT COUNT(f.id) FROM Favorite f WHERE f.user.id = :userId";
        long count = (long) entityManager.createQuery(countJpql).setParameter("userId", userId).getSingleResult();

        String jpql = "SELECT NEW com.example.postapp.domain.repositories.FavoritePost(f.id, p, f.postTitle, f" +
                ".authorName) FROM Favorite f LEFT OUTER " +
                "JOIN Post p ON" +
                " f" +
                ".postId" +
                " = p.id AND f.user.id = :userId ORDER BY f.createdAt DESC";
        List<FavoritePost> favoritePosts = entityManager.createQuery(jpql, FavoritePost.class)
                .setParameter("userId", userId)
                .setFirstResult(page.getPageNumber() * page.getPageSize())
                .setMaxResults(page.getPageSize())
                .getResultList();
        return new PageImpl<FavoritePost>(favoritePosts, page, count);
    }
}