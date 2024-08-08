package com.chat_app.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.mongodb.client.MongoClients;

@Configuration
@EnableMongoRepositories("com.chat_app.repository")
public class MongoDBConfig {

	@Bean
	MongoTemplate mongoTemplate() {
		return new MongoTemplate(new SimpleMongoClientDatabaseFactory(MongoClients.create(), "chatApplication"));
	}
	
}
