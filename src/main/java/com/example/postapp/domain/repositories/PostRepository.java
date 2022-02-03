package com.example.postapp.domain.repositories;

import com.example.postapp.domain.models.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.List;
import java.util.Optional;
import java.util.OptionalLong;


@Repository
public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom {
    Page<Post> findAllByAuthorId(long authorId, Pageable pageable);

    Page<Post> findAllByExposeOrderByCreatedAtDesc(boolean expose, Pageable pageable);
}

interface PostRepositoryCustom {
    Optional<Post> findPost(long id, OptionalLong authorId);

    Page<PostWithFavorite> findAllPostsWithFavorite(long userId, Pageable pageable);
}

class PostRepositoryImpl implements PostRepositoryCustom {

    @Autowired
    EntityManager entityManager;

    @Override
    public Optional<Post> findPost(long id, OptionalLong authorId) {
        String jpql = "select p from Post p where id = :postId and (expose = true or author.id = :authorId)";
        try {
            return Optional.of(entityManager.createQuery(jpql, Post.class).setParameter("postId", id).setParameter(
                    "authorId",
                    authorId.isEmpty() ? -1 : authorId.getAsLong()).getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public Page<PostWithFavorite> findAllPostsWithFavorite(long userId, Pageable page) {
        String countJpql = "SELECT COUNT(p.id) FROM Post p WHERE p.expose = true";
        long count = (long) entityManager.createQuery(countJpql).getSingleResult();

        String jpql = "SELECT NEW com.example.postapp.domain.repositories.PostWithFavorite(p.id, p.title, p.content, " +
                "p.createdAt, p.author, f) " +
                "FROM Post p " +
                "LEFT JOIN Favorite f ON p.id=f.post.id AND f.user.id=:userId " +
                "WHERE p.expose=true " +
                "ORDER BY p.createdAt DESC";
        List<PostWithFavorite> posts = entityManager.createQuery(jpql, PostWithFavorite.class)
                .setParameter("userId", userId)
                .setFirstResult(page.getPageNumber() * page.getPageSize())
                .setMaxResults(page.getPageSize())
                .getResultList();
        return new PageImpl<>(posts, page, count);
    }
}