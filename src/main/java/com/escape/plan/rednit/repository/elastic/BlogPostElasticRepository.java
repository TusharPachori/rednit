package com.escape.plan.rednit.repository.elastic;

import com.escape.plan.rednit.model.elastic.ElasticBlogPost;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface BlogPostElasticRepository extends ElasticsearchRepository<ElasticBlogPost, String> {

}
