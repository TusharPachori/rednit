package com.escape.plan.rednit.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import com.escape.plan.rednit.dao.BlogPost;
import com.escape.plan.rednit.dao.BlogPostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class BlogPostService {
    @Autowired
    private BlogPostRepository repository;

    @Autowired
    private ElasticsearchClient elasticsearchClient;

    public BlogPost save(BlogPost blogPost) throws IOException {
        IndexResponse response = elasticsearchClient.index(i -> i
                .index("blog")
                .id(blogPost.getId())
                .document(blogPost)
        );
        return blogPost;
    }

    public List<BlogPost> searchByTitle(String title) {
        return repository.findByTitleContaining(title);
    }
}
