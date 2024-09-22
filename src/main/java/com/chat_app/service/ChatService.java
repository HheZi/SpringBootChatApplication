package com.chat_app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.aggregation.ArrayOperators.IsArray;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chat_app.controller.message.WebSocketController;
import com.chat_app.exception.ErrorAPIException;
import com.chat_app.model.Chat;
import com.chat_app.model.Message;
import com.chat_app.model.User;
import com.chat_app.model.enums.ChatType;
import com.chat_app.model.projection.ChatReadDTO;
import com.chat_app.model.projection.ChatToUpdateDTO;
import com.chat_app.model.projection.ChatWriteDTO;
import com.chat_app.model.projection.MessageReadDTO;
import com.chat_app.model.projection.MessageWriteDTO;
import com.chat_app.repository.ChatRepository;
import com.chat_app.repository.MessageRepository;
import com.chat_app.service.mapper.ChatMapper;
import com.chat_app.service.mapper.MessageMapper;

@Service
public class ChatService {

	@Autowired
	private ChatRepository chatRepository;

	@Autowired
	private MessageRepository messageRepository;

	@Autowired
	private UserService userService;

	@Autowired
	private ChatMapper chatMapper;

	@Autowired
	private MessageMapper messageMapper;
	
	@Autowired
	private WebSocketController webSocketController;
	
	@Transactional(readOnly = true)
	public List<ChatReadDTO> getAllChatsByUsername(Integer usernameId) {
		return chatRepository.findByUsersIdWithLastMessage(usernameId)
				.stream()
				.peek(this::calcualteChatName)
				.map(chatMapper::chatToReadDto)
				.toList();
	}

	@Transactional(readOnly = true)
	public List<MessageReadDTO> getMessages(String chatId) {
		Chat chat = chatRepository.findById(chatId)
				.orElseThrow(() -> new ErrorAPIException(HttpStatus.NOT_FOUND, "Chat is not found"));
		List<Message> messages = messageRepository.findByChatId(chat.getId());
		List<User> usersById = userService.getUserById(chat.getUsersId());

		return messages.stream()
			   .map(t -> messageMapper.messageToReadDto(t, usersById, chatId))
			   .toList();
	}

	public ChatToUpdateDTO getChatToUpdate(String chatId) {
		Chat chat = chatRepository.findById(chatId)
				.orElseThrow(() -> new ErrorAPIException(HttpStatus.NOT_FOUND, "Chat is not found"));
		return chatMapper.chatToChatUpdateDTO(chat,
				userService.getUserById(chat.getUsersId()).stream().map(User::getUsername).toList());
	}

	@Transactional
	public void updateChat(String chatId, ChatWriteDTO dto) {
		webSocketController.sendMessageAboutChatToUsers(chatMapper
				.chatToReadDto(calcualteChatName(
						chatRepository.save(updateChatByDto(findChatByChatId(chatId), dto,
								userService.getUserIdByUsername(dto.getUsersName()))))), dto.getUsersName());
	}
	
	@Transactional
	public void kickUserFromChat(String chatId, String userToKick) { 
		Chat chat = chatRepository.findById(chatId)
				.orElseThrow(() -> new ErrorAPIException(HttpStatus.NOT_FOUND, "Chat is not found"));

		Integer userId = userService.getIdByUsername(userToKick);
		
		if (!chat.getUsersId().contains(UserService.getAuth().getId())) {
			throw new ErrorAPIException(HttpStatus.PROXY_AUTHENTICATION_REQUIRED, "Not enough rights to kick user");
		}
		
		chat.getUsersId().remove(userId);
		
		chatRepository.save(chat);
		
		webSocketController.sendMessageAboutChatToUsers(chatId, List.of(userToKick));
	}
	
	@Transactional
	public ChatReadDTO createChat(ChatWriteDTO dto, ChatType chatType) {
		Chat chat = chatMapper.writeDtoToChat(dto, userService.getUserIdByUsername(dto.getUsersName()), chatType);
		
		if (chatType == ChatType.PRIVATE) {
			isPrivateChatExists(chat.getUsersId().get(0), chat.getUsersId().get(1));
		}
		
		return chatMapper.chatToReadDto(chatRepository.save(chat));
	}
	
	@Transactional
	public void deleteChat(String chatId) {
		Chat chat = chatRepository.findById(chatId)
				.orElseThrow(() -> new ErrorAPIException(HttpStatus.NOT_FOUND, "Chat is not found"));
		
		if (!chat.getUsersId().contains(UserService.getAuth().getId())) {
			throw new ErrorAPIException(HttpStatus.PROXY_AUTHENTICATION_REQUIRED, "Not enough rights to delete the chat");
		}
		chatRepository.delete(chat);

		messageRepository.deleteByChatId(chatId);
		
		webSocketController.sendMessageAboutChatToUsers(chat.getId().toString(), userService.getUserById(chat.getUsersId()).stream().map(User::getUsername).toList());
	}
	
	@Transactional(readOnly = true)
	public Chat findChatByChatId(String chatId) {
		return calcualteChatName(chatRepository.findById(chatId)
				.orElseThrow(() -> new ErrorAPIException(HttpStatus.NOT_FOUND, "Chat is not found!")));
	}

	@Transactional(readOnly = true)
	public void isPrivateChatExists(Integer firstUserId, Integer secondUserId) {
		 if (chatRepository.existsByUsersIdInAndChatType(new Integer[] { firstUserId, secondUserId },
					ChatType.PRIVATE)) {
			 throw new ErrorAPIException(HttpStatus.CONFLICT, "The chat already exists");
		 } 
	}
	
	@Transactional
	public MessageReadDTO sendMessage(MessageWriteDTO dto, String chatId) {
		return messageMapper.messageToReadDto(
				messageRepository.save(
						messageMapper.writeDtoToMessage(dto, userService.getIdByUsername(dto.getSender()), chatId)),
				dto.getSender(), chatId);
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
		Message message = messageRepository.findById(id)
				.orElseThrow(() -> new ErrorAPIException(HttpStatus.NOT_FOUND, "The message is not found"));
		
		if (message.getSenderId() != UserService.getAuth().getId()) {
			throw new ErrorAPIException(HttpStatus.PROXY_AUTHENTICATION_REQUIRED, "Not enough rights to delete the message");
		}
		
		messageRepository.delete(message);
		return id;
	}

	private Chat calcualteChatName(Chat chat) {
		if (chat.getChatType() == ChatType.PRIVATE) {
			chat.setChatName(
					userService.getUserById(chat.getUsersId()).stream().filter(u -> !u.getUsername().equals(UserService.getAuth().getUsername()))
							.findAny().orElseThrow(() -> new ErrorAPIException(HttpStatus.INTERNAL_SERVER_ERROR,
									"Can't calculate name of the private chat"))
							.getUsername());
		}
		return chat;
	}
}
