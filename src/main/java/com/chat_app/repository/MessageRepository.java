package com.chat_app.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.chat_app.model.Message;

import java.util.List;
import java.util.Optional;


@Repository
public interface MessageRepository extends MongoRepository<Message, String>{
	
	List<Message> findByChatName(String groupName);
	
	@Query(value = "{chatName: ?0}", sort = "{timestamp: -1}")
	Optional<Message> findLastMessageByTimestamp(String groupName);

}
