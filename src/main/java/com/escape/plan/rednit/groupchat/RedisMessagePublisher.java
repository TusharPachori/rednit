package com.escape.plan.rednit.groupchat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisMessagePublisher {
    private final StringRedisTemplate redisTemplate;
    private final String CHANNEL_NAME = "chat-messages";
    private final ObjectMapper objectMapper;

    public RedisMessagePublisher(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = new ObjectMapper();
    }

    public void publishMessage(Message message) throws JsonProcessingException {
        String messageJson = objectMapper.writeValueAsString(message);
        System.out.println("In Redis {}" +  messageJson);
        redisTemplate.convertAndSend(CHANNEL_NAME, messageJson);
    }
}
