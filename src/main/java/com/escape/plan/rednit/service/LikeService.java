package com.escape.plan.rednit.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class LikeService {
    @Autowired
    private StringRedisTemplate redisTemplate;

    private static final String LIKES_KEY_PREFIX = "blog:likes:";

    public String incrementLike(String blogId) {
        String key = LIKES_KEY_PREFIX + blogId;
        redisTemplate.opsForValue().increment(key, 1); // Atomic increment
        return "likes updated successfully";
    }

    public int getLikes(String blogId) {
        String key = LIKES_KEY_PREFIX + blogId;
        String value = redisTemplate.opsForValue().get(key);
        return (value != null) ? Integer.parseInt(value) : 0;
    }
}
