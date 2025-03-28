package com.escape.plan.rednit.dao;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BlogPostRepository extends ElasticsearchRepository<BlogPost, String> {

    List<BlogPost> findByTitleContaining(String title);
}
