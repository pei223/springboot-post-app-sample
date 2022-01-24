package com.example.postapp.domain.repositories;

import com.example.postapp.domain.models.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.Optional;
import java.util.OptionalLong;


@Repository
public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom {
    Page<Post> findAllByAuthorId(long authorId, Pageable pageable);

    Page<Post> findAllByExpose(boolean expose, Pageable pageable);
}

interface PostRepositoryCustom {
    Optional<Post> findPost(long id, OptionalLong authorId);
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
}