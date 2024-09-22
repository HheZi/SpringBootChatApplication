package com.chat_app.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.chat_app.model.Message;

import java.util.List;
import java.util.Optional;
import org.bson.types.ObjectId;



@Repository
public interface MessageRepository extends MongoRepository<Message, String>{
	
	@Query(value = "{chatName: ?0}", sort = "{timestamp: -1}")
	Optional<Message> findLastMessageByTimestamp(String groupName);
	
	public void deleteByChatId(String chatId);
	
	public List<Message> findByChatId(ObjectId chatId);
}
