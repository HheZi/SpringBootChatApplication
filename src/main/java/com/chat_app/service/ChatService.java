package com.chat_app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chat_app.exception.ErrorAPIException;
import com.chat_app.model.Chat;
import com.chat_app.model.Message;
import com.chat_app.model.User;
import com.chat_app.model.enums.ChatType;
import com.chat_app.model.projection.ChatWriteDTO;
import com.chat_app.repository.ChatRepository;
import com.chat_app.repository.MessageRepository;

@Service
public class ChatService {

	@Autowired
	private ChatRepository chatRepository;

	@Autowired
	private MessageRepository messageRepository;
	
	@Autowired
	private UserService userService;

	@Transactional
	public Chat createOrUpdateChat(Chat chat) {
		return chatRepository.save(chat);
	}

	@Transactional
	public void deleteChat(Chat chat) {
		chatRepository.delete(chat);
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

	@Transactional(readOnly = true)
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
	public Boolean isPrivateChatExists(Integer firstUserId, Integer secondUserId) {
		return chatRepository.existsByUsersIdInAndChatType(new Integer[] {firstUserId, secondUserId}, ChatType.PRIVATE);
	}
	
	public Chat updateChatByDto(Chat chat, ChatWriteDTO dto, List<Integer> list) {
		chat.setDescription(dto.getDescription());
		if (chat.getChatType() == ChatType.GROUP) {
			chat.setUsersId(list);
			chat.setChatName(dto.getChatName());
		}
		
		return chat;
	}
	
	@Transactional
	public String deleteMessageById(String id) {
		messageRepository.deleteById(id);
		return id;
	}

	public void deleteAllMessagesByChatId(String chatId) {
		messageRepository.deleteByChatId(chatId);
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
