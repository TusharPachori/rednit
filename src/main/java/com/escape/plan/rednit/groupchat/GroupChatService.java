package com.escape.plan.rednit.groupchat;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class GroupChatService {

    @Autowired
    private ElasticsearchClient elasticsearchClient;

    public Message save(Message message) throws IOException {
        UUID uuid = UUID.randomUUID();
        System.out.println("Recieved message in save" + message);
        IndexResponse response = elasticsearchClient.index(i -> i
                .index("message")
                .id(uuid.toString())
                .document(message)
        );
        System.out.println("✅ Elasticsearch Response: " + response.result());
        return message;
    }

    public List<Message> getAllChats() throws IOException {
        SearchResponse<Message> response = elasticsearchClient.search(s -> s
                        .index("message") // Index name
                        .query(q -> q.matchAll(m -> m))
                        .size(300)
                        // Ensure a query is included
                , Message.class // Specify the document class
        );

        return response.hits().hits().stream()
                .map(Hit::source) // Extract actual documents
                .collect(Collectors.toList());
    }

}
