package com.chat_app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Limit;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;

import com.chat_app.exception.ErrorAPIException;
import com.chat_app.model.Chat;
import com.chat_app.model.Message;
import com.chat_app.model.User;
import com.chat_app.model.enums.ChatType;
import com.chat_app.model.projection.ChatReadDTO;
import com.chat_app.model.projection.ChatWriteDTO;
import com.chat_app.model.projection.MessageReadDTO;
import com.chat_app.model.projection.MessageWriteDTO;
import com.chat_app.repository.ChatRepository;
import com.chat_app.repository.MessageRepository;
import com.chat_app.service.mapper.ChatMapper;
import com.chat_app.service.mapper.MessageMapper;

import ch.qos.logback.core.rolling.helper.IntegerTokenConverter;

@Service
public class ChatService {

	@Autowired
	private ChatRepository chatRepository;

	@Autowired
	private MessageRepository messageRepository;
	
	@Autowired
	private UserService userService;

	@Transactional
	public Chat createChat(Chat chat) {
		return chatRepository.save(chat);
	}

	@Transactional
	public Message saveMessage(Message message) {
		return messageRepository.save(message);
	}

	@Transactional(readOnly = true)
	public List<Chat> getAllChatsByUsername(Integer usernameId) {
		return chatRepository.findByUsersIdWithLastMessage(usernameId)
				.stream()
				.peek(this::calcualteChatName)
				.toList();
	}
	
	@Transactional(readOnly = true)
	public List<Message> findMessagesByChatId(String chatId) {
		return messageRepository.findByChatId(chatId);
	}

	public Chat findChatByChatId(String chatId) {
		return calcualteChatName(chatRepository.findByChatId(chatId)
				.orElseThrow(() -> new ErrorAPIException(HttpStatus.NOT_FOUND, "Chat is not found!")));
	}
	
	@Transactional(readOnly = true)
	public String findChatByChatName(String chatName) {
		return chatRepository.findByChatName(chatName)
				.map(t -> t.getId())
				.orElseThrow(() -> new ErrorAPIException(HttpStatus.NOT_FOUND, "Chat is not found!"));
	}
	
	@Transactional(readOnly = true)
	public void isPrivateChatExists(Integer firstUserId, Integer secondUserId) {
		boolean b = chatRepository.existsByChatNameInAndChatType(new String[] { String.format("%d_%d", firstUserId, secondUserId),
				String.format("%d_%d", secondUserId, firstUserId) }, ChatType.PRIVATE);
		if (b) {
			throw new ErrorAPIException(HttpStatus.CONFLICT, "The group already exists");
		}
		
	}
	
	public Chat calcualteChatName(Chat chat) {
		return calcualteChatName(chat, ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername());
	}
	
	public Chat calcualteChatName(Chat chat, String authUser) {
		if (chat.getChatType() == ChatType.PRIVATE) {
			chat.setChatName(userService.getUserById(chat.getUsersId())
					.stream()
					.filter(u -> !u.getUsername().equals(authUser))
					.findAny()
					.orElseThrow(() -> new ErrorAPIException(HttpStatus.NOT_FOUND, "The user is not found"))
					.getUsername());
		}
		return chat;
	}
}
