package com.chat_app.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.chat_app.model.websocket.Message;
import java.util.List;


@Repository
public interface MessageRepository extends MongoRepository<Message, String>{
	
	List<Message> findByGroupName(String groupName);
	
}
