package com.chat_app.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;

import com.mongodb.client.MongoClients;

@Configuration
public class MongoDBConfig {

	@Bean
	MongoTemplate factoryBean() {
		return new MongoTemplate(new SimpleMongoClientDatabaseFactory(MongoClients.create(), "chatApplication"));
	}
	
}
