package com.escape.plan.rednit.controller;

import com.escape.plan.rednit.dao.BlogPost;
import com.escape.plan.rednit.service.BlogPostService;
import com.escape.plan.rednit.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/blog")
public class BlogPostController {
    @Autowired
    private BlogPostService service;

    @Autowired
    private LikeService likeService;

    @PostMapping
    public BlogPost savePost(@RequestBody BlogPost blogPost) throws IOException {
        return service.save(blogPost);
    }

    @GetMapping("/search")
    public List<BlogPost> searchByTitle(@RequestParam String title) throws IOException {
        return service.searchByTitle(title);
    }

    @PostMapping("/updateLike")
    public String incrementLikes(@RequestBody String id) throws IOException {
        return likeService.incrementLike(id);
    }
}
