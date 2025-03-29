package com.escape.plan.rednit.controller;

import com.escape.plan.rednit.model.BlogPost;
import com.escape.plan.rednit.model.elastic.ElasticBlogPost;
import com.escape.plan.rednit.repository.elastic.BlogPostElasticRepository;
import com.escape.plan.rednit.repository.mongo.BlogPostMongoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@RestController
@RequestMapping("/blog")
public class BlogPostController {
    @Autowired
    private BlogPostElasticRepository blogPostElasticRepository;

    @Autowired
    private BlogPostMongoRepository blogPostMongoRepository;

    @PostMapping("/savePost")
    public BlogPost savePost(@RequestBody BlogPost blogPost) throws IOException {
        blogPostElasticRepository.save(new ElasticBlogPost(blogPost));
        blogPostMongoRepository.save(blogPost);
        return blogPost;
    }

    @GetMapping("/getAllElastic")
    public List<ElasticBlogPost> getAllElastic() {
        Iterator<ElasticBlogPost> iterator = blogPostElasticRepository.findAll().iterator();
        List<ElasticBlogPost> elasticBlogPostList = new ArrayList<ElasticBlogPost>();
        while (iterator.hasNext())
            elasticBlogPostList.add(iterator.next());
        return elasticBlogPostList;
    }

    @GetMapping("/getAllMongo")
    public List<BlogPost> getAllMongo() {
        return blogPostMongoRepository.findAll();
    }
}
