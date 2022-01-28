package com.example.postapp.services.post;

import com.example.postapp.domain.DisplayPost;

import java.util.List;

public class PostPageInfo {
    public int totalPage;
    public List<DisplayPost> posts;

    public PostPageInfo(int totalPage, List<DisplayPost> posts) {
        this.totalPage = totalPage;
        this.posts = posts;
    }

    public PostPageInfo() {
    }
}
