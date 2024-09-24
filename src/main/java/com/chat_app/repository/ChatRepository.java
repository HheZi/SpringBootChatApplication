package com.chat_app.repository;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Limit;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.chat_app.model.Chat;
import com.chat_app.model.enums.ChatType;
import com.chat_app.model.projection.ChatReadDTO;

@Repository
public interface ChatRepository extends MongoRepository<Chat, ObjectId>{

	@Aggregation(pipeline = {
			"{$match: {usersId: ?0}}",
			"{$lookup: {"
			+ "from: 'message',"
			+ "localField: '_id',"
			+ "foreignField: 'chatId',"
			+ "as: 'lastMessage',"
			+ "pipeline: ["
			+ "{$sort: {timestamp: -1}},"
			+ "{$limit: 1}"
			+ "]}}",
			"{$sort: {'lastMessage.timestamp': -1}}"
	})
	List<Chat> findByUsersIdWithLastMessage(Integer usersId);
	
	public boolean existsByUsersIdInAndChatType(Integer[] chatId, ChatType chatType);
	
	public Optional<Chat> findByChatName(String chatName);
	
}
