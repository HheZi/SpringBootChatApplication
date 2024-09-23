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
import org.springframework.web.bind.annotation.RequestMapping;
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
import com.chat_app.service.MessageService;
import com.chat_app.service.UserService;
import com.chat_app.service.mapper.ChatMapper;
import com.chat_app.service.mapper.MessageMapper;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/chat")
public class ChatController {

	@Autowired
	private ChatService chatService;

	@Autowired
	private MessageService messageService;
	
	@GetMapping()
	public List<ChatReadDTO> getAllGroups(@AuthenticationPrincipal User authUser) {
		return chatService.getAllChatsByUsername(authUser.getId());
	}

	@GetMapping("/{chatId}")
	public ChatToUpdateDTO getChatForUpdate(@PathVariable("chatId") String chatId) {
		return chatService.getChatToUpdate(chatId);
	}
	
	@PutMapping("/{chatId}")
	public ResponseEntity<?> updateChat(@PathVariable("chatId") String chatId, @RequestBody ChatWriteDTO dto){
		
		chatService.updateChat(chatId, dto);
		
		return  ResponseEntity.status(HttpStatus.CREATED).build();
	}

	
	@PutMapping("/{chatId}/{username}")
	public ResponseEntity<?> kickUserFromChat(@PathVariable("chatId") String chatId, @PathVariable("username") String username){
		
		chatService.kickUserFromChat(chatId, username);
		
		return ok().build();
	}
	
	@DeleteMapping("/{chatId}")
	public ResponseEntity<?> deleteChat(@PathVariable("chatId") String chatId){
		chatService.deleteChat(chatId);
		
		return ok().build();
	}
	

	@GetMapping("/messages/{chatId}")
	public List<MessageReadDTO> getMessages(@PathVariable("chatId") String chatId) {
		return messageService.getMessages(chatId);
	}
}
