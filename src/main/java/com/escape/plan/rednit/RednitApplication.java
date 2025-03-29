package com.escape.plan.rednit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@EnableMongoRepositories("com.escape.plan.rednit.repository.mongo")
@EnableElasticsearchRepositories("com.escape.plan.rednit.repository.elastic")
@SpringBootApplication
public class RednitApplication {

	public static void main(String[] args) {
		SpringApplication.run(RednitApplication.class, args);
	}

}
