package com.chat_app.controller.api;

import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.http.ResponseEntity.status;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
import com.chat_app.service.ChatService;
import com.chat_app.service.UserService;
import com.chat_app.service.mapper.ChatMapper;
import com.chat_app.service.mapper.MessageMapper;

@RestController
public class ChatController {

	private static final String CHAT_CREATION_URI = "/chat/creation";
	
	@Autowired
	private SimpMessagingTemplate messagingTemplate;

	@Autowired
	private ChatService chatService;

	@Autowired
	private UserService userService;

	@Autowired
	private ChatMapper chatMapper;

	@Autowired
	private MessageMapper messageMapper;

	@GetMapping("/chat")
	public List<ChatReadDTO> getAllGroups(@AuthenticationPrincipal User authUser) {
		return chatService.getAllChatsByUsername(authUser.getId())
				.stream()
				.map(chatMapper::groupToReadDto)
				.toList();
	}

	@GetMapping("/chat/{chatId}")
	public ChatToUpdateDTO getChatForUpdate(@PathVariable("chatId") String chatId) {
		Chat chat = chatService.findChatByChatId(chatId);
		List<String> list = userService.getUserById(chat.getUsersId())
		.stream()
		.map(User::getUsername)
		.toList();
		return chatMapper.chatToChatUpdateDTO(chat, list);
	}
	
	@PutMapping("/chat/{chatId}")
	public ResponseEntity<?> updateChat(@PathVariable("chatId") String chatId, @RequestBody ChatWriteDTO dto){
		Chat chatByDto = chatService.createOrUpdateChat(
				chatService.updateChatByDto(
						chatService.findChatByChatId(chatId), dto, userService.getUserIdByUsername(dto.getUsersName())));
		
		sendMessageAboutChatToUsers(chatByDto, dto.getUsersName());
		
		return  ResponseEntity.status(HttpStatus.CREATED).build();
	}
	
	private void sendMessageAboutChatToUsers(Chat chat, List<String> users) {
		users.forEach(t -> {
			messagingTemplate.convertAndSendToUser(t, CHAT_CREATION_URI, chat.getChatType() == ChatType.PRIVATE ?
					chatMapper.groupToReadDto(chatService.calcualteChatName(chat, t)) : chatMapper.groupToReadDto(chat));
		});
	}
	
	private void sendMessageAboutChatToUsers(String message, List<String> users) {
		users.forEach(t -> {
			messagingTemplate.convertAndSendToUser(t, CHAT_CREATION_URI, message);
		});
	}
	
	@MessageMapping("/chat/creation/group")
	public void createGroupChat(@Payload ChatWriteDTO group) {
		Chat chat = chatService.createOrUpdateChat(
				chatMapper.writeDtoToGroup(group, userService.getUserIdByUsername(group.getUsersName()), ChatType.GROUP));
		
		sendMessageAboutChatToUsers(chat, group.getUsersName());
	}
	
	@MessageMapping("/chat/creation/private")
	public void createPrivateChat(@Payload ChatWriteDTO privateChat) {
		List<Integer> usersId = userService.getUserIdByUsername(privateChat.getUsersName());
		
		if(!chatService.isPrivateChatExists(usersId.get(0), usersId.get(1))) {
			Chat chat = chatService.createOrUpdateChat(chatMapper.writeDtoToGroup(privateChat, usersId, ChatType.PRIVATE));
			
			sendMessageAboutChatToUsers(chat, privateChat.getUsersName());
			
		}
		
	}
	
	@DeleteMapping("/chat/{chatId}")
	public ResponseEntity<?> deleteChat(@PathVariable("chatId") String chatId, @AuthenticationPrincipal User user){
		Chat chat = chatService.findChatByChatId(chatId);
		
		
		if (!chat.getUsersId().contains(user.getId())) {
			throw new ErrorAPIException(HttpStatus.PROXY_AUTHENTICATION_REQUIRED, "Not enough rights to delete chat");
		}
		chatService.deleteChat(chat);

		chatService.deleteAllMessagesByChatId(chatId);
		
		sendMessageAboutChatToUsers(chat.getChatId(), userService.getUserById(chat.getUsersId()).stream().map(User::getUsername).toList());
		
		return ok().build();
	}
	

	@GetMapping("/chat/messages/{chatId}")
	public List<MessageReadDTO> getMessages(@PathVariable("chatId") String chatId) {
		Chat chat = chatService.findChatByChatId(chatId);
		List<Message> messages = chatService.findMessagesByChatId(chat.getChatId());
		List<User> usersById = userService.getUserById(chat.getUsersId());

		return messages.stream()
			   .map(t -> messageMapper.messageToReadDto(t, usersById, chatId))
			   .toList();
	}

	@MessageMapping("/messages/{chatId}/{messageId}")
	@SendTo("/messages/{chatId}")
	public String deleteMessageAndReturnId(@DestinationVariable("messageId") String messageId) {
		return chatService.deleteMessageById(messageId);
	}
	
	@MessageMapping("/messages/{chatId}")
	@SendTo("/messages/{chatId}")
	public MessageReadDTO sendMessages(@Payload MessageWriteDTO dto, @DestinationVariable("chatId") String chatId) {
		return messageMapper.messageToReadDto(
				chatService.saveMessage(
						messageMapper.writeDtoToMessage(dto, userService.getIdByUsername(dto.getSender()), chatId)),
				dto.getSender(), chatId);
	}
}
