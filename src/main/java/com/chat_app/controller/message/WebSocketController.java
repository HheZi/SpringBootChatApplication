package com.chat_app.controller.message;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.chat_app.model.enums.ChatType;
import com.chat_app.model.projection.ChatWriteDTO;
import com.chat_app.model.projection.MessageReadDTO;
import com.chat_app.model.projection.MessageWriteDTO;
import com.chat_app.service.ChatService;
import com.chat_app.service.MessageService;

@Controller
public class WebSocketController {
	
	private static final String CHAT_CREATION_URI = "/chat/creation";
	
	@Autowired
	private SimpMessagingTemplate messagingTemplate;
	
	@Autowired
	@Lazy
	private ChatService chatService;
	
	@Autowired
	private MessageService messageService;
	
	public void sendMessageAboutChatToUsers(Object message, List<String> users) {
		users.forEach(t -> {
			messagingTemplate.convertAndSendToUser(t, CHAT_CREATION_URI, message);
		});
	}
	
	@MessageMapping("/chat/creation/group")
	public void createGroupChat(@Payload ChatWriteDTO group) {
		sendMessageAboutChatToUsers(chatService.createChat(group, ChatType.GROUP), group.getUsersName());
	}
	
	@MessageMapping("/chat/creation/private")
	public void createPrivateChat(@Payload ChatWriteDTO privateChat) {
		sendMessageAboutChatToUsers(chatService.createChat(privateChat, ChatType.PRIVATE), privateChat.getUsersName());
	}
	
	@MessageMapping("/messages/{chatId}/{messageId}")
	@SendTo("/messages/{chatId}")
	public String deleteMessageAndReturnId(@DestinationVariable("messageId") String messageId) {
		return messageService.deleteMessageById(messageId);
	}
	
	@MessageMapping("/messages/{chatId}")
	@SendTo("/messages/{chatId}")
	public MessageReadDTO sendMessages(@Payload MessageWriteDTO dto, @DestinationVariable("chatId") String chatId) {
		return messageService.sendMessage(dto, chatId);
	}
}
