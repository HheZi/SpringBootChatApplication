package com.chat_app.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.chat_app.model.Chat;

@SpringBootTest
class ChatServiceTest {

	@Mock
	private ChatService chatService;
	
	@Mock
	private MongoTemplate mongoTemplate;
	
	@Test
	public void createPrivateChatAndCheckIfReadDtoAsserts() {
	}
	
	@AfterAll
	public void afterAll() {
		mongoTemplate.getDb().drop();
	}
}
