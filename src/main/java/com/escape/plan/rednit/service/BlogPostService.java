package com.escape.plan.rednit.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.escape.plan.rednit.dao.BlogPost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BlogPostService {

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

    public List<BlogPost> searchByTitle(String title) throws IOException {
        SearchResponse<BlogPost> response = elasticsearchClient.search(s -> s
                .index("blog") // Index name
                .query(q -> q
                        .match(m -> m
                                .field("title") // Field to search
                                .query(title) // Keyword to match
                        )
                ), BlogPost.class
        );

        return response.hits().hits().stream()
                .map(Hit::source) // Extract actual documents
                .collect(Collectors.toList());
    }
}
