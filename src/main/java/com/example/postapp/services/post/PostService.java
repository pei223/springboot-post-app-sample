package com.example.postapp.services.post;

import com.example.postapp.domain.models.Post;
import com.example.postapp.domain.repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Transactional
@Service
public class PostService {
    private static final int DATA_NUM_PER_PAGE = 10;
    @Autowired
    PostRepository postRepo;

    public Page<Post> getMyPosts(long userId, int pageNum) {
        Pageable paging = PageRequest.of(pageNum, DATA_NUM_PER_PAGE, Sort.by("id").descending());
        return postRepo.findAllByAuthorId(userId, paging);
    }

    public Page<Post> getPosts(int pageNum) {
        Pageable paging = PageRequest.of(pageNum, DATA_NUM_PER_PAGE, Sort.by("id").descending());
        return postRepo.findAllByExpose(true, paging);
    }
}
