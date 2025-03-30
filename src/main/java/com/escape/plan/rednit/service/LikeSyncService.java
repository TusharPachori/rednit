package com.escape.plan.rednit.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Set;

@Service
public class LikeSyncService {
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private BlogPostService blogPostService;

    private static final String LIKES_KEY_PREFIX = "blog:likes:";

    @Scheduled(fixedRate = 5000)  // Run every 5 seconds
    public void syncLikesToElasticsearch() throws IOException {
        Set<String> keys = redisTemplate.keys(LIKES_KEY_PREFIX + "*");

        if (keys == null || keys.isEmpty()) return;

        for (String key : keys) {
            String blogId = key.replace(LIKES_KEY_PREFIX, "");
            String value = redisTemplate.opsForValue().get(key);

            if (value != null) {
                int likes = Integer.parseInt(value);

                // Update Elasticsearch
                blogPostService.incrementLike(blogId, likes);

                // Reset Redis counter after syncing
                redisTemplate.delete(key);
            }
        }
    }
}
