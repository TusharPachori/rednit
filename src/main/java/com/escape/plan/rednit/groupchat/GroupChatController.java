package com.escape.plan.rednit.groupchat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin(origins = {"http://localhost:5173" , "http://localhost:3002", "http://localhost:3001"})
public class GroupChatController {

    @Autowired
    private GroupChatService groupChatService;

    @Autowired
    private RedisMessagePublisher redisMessagePublisher;

    @GetMapping("/getAllChats")
    public List<Message> getAllChats() throws IOException {
        return groupChatService.getAllChats();
    }

    @MessageMapping("/sendMessage")
    public Message postMessage(@RequestBody Message message) throws IOException {
        try {
            System.out.println("📥 Received message: " + message.getMessage());
            groupChatService.save(message);
            redisMessagePublisher.publishMessage(message);

        } catch (Exception e) {
            System.out.println("📥 Error:  " + e);
        }
        return message;
    }



}
