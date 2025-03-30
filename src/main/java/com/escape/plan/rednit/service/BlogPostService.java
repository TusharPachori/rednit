package com.escape.plan.rednit.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.UpdateRequest;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.json.JsonData;
import com.escape.plan.rednit.dao.BlogPost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BlogPostService {

    @Autowired
    private ElasticsearchClient elasticsearchClient;

    public BlogPost save(BlogPost blogPost) throws IOException {
        UUID uuid = UUID.randomUUID();
        blogPost.setId(uuid.toString());
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

    public String incrementLike(String id, int likes) throws IOException {

            UpdateRequest<Object, Object> updateRequest =
                    UpdateRequest.of(u -> u
                    .index("blog")
                    .id(id)
                    .retryOnConflict(5)
                    .script(s -> s.inline(i -> i
                            .source("ctx._source.likes += params.count")
                            .lang("painless")
                                    .params(Map.of("count", JsonData.of(likes))
                            // Increment the 'views' field
                    ))
            ));

            elasticsearchClient.update(updateRequest, Object.class);

        return "Views updated!";
    }
}
