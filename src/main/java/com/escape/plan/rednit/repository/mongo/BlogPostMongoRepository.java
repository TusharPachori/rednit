package com.escape.plan.rednit.repository.mongo;

import com.escape.plan.rednit.model.BlogPost;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BlogPostMongoRepository extends MongoRepository<BlogPost, String> {

    @Query("{title:'?0'}")
    BlogPost findBlogPostByTitle(String title);

    public long count();
}
