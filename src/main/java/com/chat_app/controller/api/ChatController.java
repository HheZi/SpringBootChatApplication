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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.chat_app.exception.ErrorAPIException;
import com.chat_app.model.projection.ChatReadDTO;
import com.chat_app.model.projection.ChatWriteDTO;
import com.chat_app.model.projection.MessageReadDTO;
import com.chat_app.model.projection.MessageWriteDTO;
import com.chat_app.service.ChatService;

import lombok.extern.slf4j.Slf4j;

@RestController
public class ChatController {

	@Autowired
	private SimpMessagingTemplate messagingTemplate;

	@Autowired
	private ChatService service;

	@GetMapping("/chat/groups")
	public List<ChatReadDTO> getAllGroups(Principal principal) {
		return service.getAllGroupsByUsername(principal.getName());
	}

	@MessageMapping("/chat/creation")
	public ResponseEntity<?> createGroup(@Payload ChatWriteDTO group) {
		ChatReadDTO dto = service.createGroup(group);
		dto.getUsersInGroup().forEach(t -> {
			messagingTemplate.convertAndSendToUser(t, "/chat/creation", dto);
		});
		return status(HttpStatus.CREATED).build();
	}

	@GetMapping("/chat/{user}")
	public ResponseEntity<?> isPrivatChatExists(@PathVariable("user") String nameOfUser, Principal principal) {
		if (!service.isPrivatChatExists(principal.getName(), nameOfUser)) {
			return ResponseEntity.ok().build();
		}
		else {
			throw new ErrorAPIException(HttpStatus.CONFLICT, "The group already exists");
		}
	}

	@GetMapping("/chat/messages")
	public List<MessageReadDTO> getMessages(@RequestParam(name = "groupName") String groupName) {
		return service.findMessagesByGroupName(groupName);
	}

	@MessageMapping("/messages/{groupName}")
	@SendTo("/messages/{groupName}")
	public MessageReadDTO sendMessages(@Payload MessageWriteDTO message) {
		return service.saveMessageAndReturnDto(message);
	}
}
