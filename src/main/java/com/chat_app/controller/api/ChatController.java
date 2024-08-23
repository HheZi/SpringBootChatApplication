package com.chat_app.controller.api;

import static org.springframework.http.ResponseEntity.status;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.chat_app.exception.ErrorAPIException;
import com.chat_app.model.projection.GroupReadDTO;
import com.chat_app.model.projection.GroupWriteDTO;
import com.chat_app.model.projection.MessageReadDTO;
import com.chat_app.model.projection.MessageWriteDTO;
import com.chat_app.service.ChatService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class ChatController {
	
	@Autowired
	private SimpMessagingTemplate messagingTemplate;
	
	@Autowired
	private ChatService service;

	@GetMapping("/chat/groups")
	public List<GroupReadDTO> getAllGroups(Principal principal){
		return service.getAllGroupsByUsername(principal.getName());
	}
	
	@MessageMapping("/group/creation")
	public ResponseEntity<?> createGroup(@Payload GroupWriteDTO group) {
		GroupReadDTO dto = service.createGroup(group);
		dto.getUsersInGroup()
		.forEach(t -> {
			messagingTemplate.convertAndSendToUser(t, "/group/creation", dto);
		});
		return status(HttpStatus.CREATED).build();
	}
	
	@GetMapping("/chat/messages")
	public List<MessageReadDTO> getMessages(
				@RequestParam(name = "groupName") String groupName
			) {
		return service.findMessagesByGroupName(groupName);
	}
	
	@MessageMapping("/messages/{groupName}")
	@SendTo("/messages/{groupName}")
	public MessageReadDTO sendMessages(@Payload  MessageWriteDTO message) {
		return service.saveMessageAndReturnDto(message);
	}
}
