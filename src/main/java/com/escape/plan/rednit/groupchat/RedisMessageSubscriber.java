package com.escape.plan.rednit.groupchat;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisMessageSubscriber implements MessageListener {
    private final SimpMessagingTemplate messagingTemplate;
    private final ObjectMapper objectMapper;

    public RedisMessageSubscriber(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            String messageJson = new String(message.getBody());
            System.out.println("✅ [Server 2] Received raw message from Redis: " + messageJson);
            com.escape.plan.rednit.groupchat.Message receivedMessage = objectMapper.readValue(messageJson, com.escape.plan.rednit.groupchat.Message.class);
            System.out.println("Received message from Redis: " + receivedMessage);
            // Forward to WebSocket clients
            messagingTemplate.convertAndSend("/topic/messages", receivedMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
