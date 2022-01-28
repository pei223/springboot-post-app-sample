package com.example.postapp.domain.repositories;

import com.example.postapp.domain.models.Favorite;
import com.example.postapp.domain.models.Post;
import org.springframework.beans.factory.annotation.Autowired;
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
}

class FavoriteRepositoryImpl implements FavoriteRepositoryCustom {
    @Autowired
    EntityManager entityManager;

    @Override
    public List<Favorite> findAllByPostList(OptionalLong userId, List<Post> postList) {
        var postIdList = postList.stream().map((post) ->
                post.id
        ).collect(Collectors.toList());
        String jpql = "select f from Favorite f where f.user.id = (:userId) AND f.post.id IN (:postIdList)";
        return entityManager.createQuery(jpql, Favorite.class)
                .setParameter("postIdList", postIdList)
                .setParameter("userId",
                        userId.isEmpty() ? -1 : userId.getAsLong()).getResultList();
    }
}