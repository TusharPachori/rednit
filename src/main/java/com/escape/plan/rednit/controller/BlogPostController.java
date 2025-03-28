package com.escape.plan.rednit.controller;

import com.escape.plan.rednit.dao.BlogPost;
import com.escape.plan.rednit.service.BlogPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/blog")
public class BlogPostController {
    @Autowired
    private BlogPostService service;

    @PostMapping
    public BlogPost savePost(@RequestBody BlogPost blogPost) throws IOException {
        return service.save(blogPost);
    }

    @GetMapping("/search")
    public List<BlogPost> searchByTitle(@RequestParam String title) {
        return service.searchByTitle(title);
    }
}
