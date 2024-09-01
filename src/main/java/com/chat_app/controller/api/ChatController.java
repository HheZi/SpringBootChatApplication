package com.chat_app.controller.api;

import static org.springframework.http.ResponseEntity.status;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.convert.DtoInstantiatingConverter;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.chat_app.exception.ErrorAPIException;
import com.chat_app.model.User;
import com.chat_app.model.enums.ChatType;
import com.chat_app.model.projection.ChatReadDTO;
import com.chat_app.model.projection.ChatWriteDTO;
import com.chat_app.model.projection.MessageReadDTO;
import com.chat_app.model.projection.MessageWriteDTO;
import com.chat_app.service.ChatService;
import com.chat_app.service.UserService;

import lombok.extern.slf4j.Slf4j;

@RestController
public class ChatController {

	@Autowired
	private SimpMessagingTemplate messagingTemplate;
	
	@Autowired
	private ChatService chatService;

	@Autowired
	private UserService userService;
		
	@GetMapping("/chat/groups")
	public List<ChatReadDTO> getAllGroups(@AuthenticationPrincipal User user) {
		return chatService.getAllGroupsByUsername(user.getId())
				.stream()
				.peek(t -> { 
					if (t.getChatType() == ChatType.PRIVATE) {
						t.setChatName(userService
								.getUserById(t.getUsersInGroup())
								.stream()
								.map(u -> u.getUsername())
								.collect(Collectors.joining("_")));
					}
				})
				.toList();
	}

	@MessageMapping("/chat/creation")
	public ResponseEntity<?> createGroup(@Payload ChatWriteDTO group) {

		if (group.getChatType() == ChatType.PRIVATE) {
			List<Integer> userIdByUsername = userService.getUserIdByUsername(group.getUsersName());
			group.setChatName(userIdByUsername
			.stream()
			.map(t -> t.toString())
			.collect(Collectors.joining("_")));
		}
		
		ChatReadDTO dto = chatService.createGroup(group, userService.getUserIdByUsername(group.getUsersName()));
		
		group.getUsersName().forEach(t -> {
			messagingTemplate.convertAndSendToUser(t, "/chat/creation", dto);
		});
		return status(HttpStatus.CREATED).build();
	}

	@GetMapping("/chat/{user}")
	public ResponseEntity<?> isPrivatChatExists(@PathVariable("user") String nameOfUser, Principal principal) {
		if (!chatService.isPrivatChatExists(principal.getName(), nameOfUser)) {
			return ResponseEntity.ok().build();
		}
		else {
			throw new ErrorAPIException(HttpStatus.CONFLICT, "The group already exists");
		}
	}

	@GetMapping("/chat/messages/{groupName}")
	public List<MessageReadDTO> getMessages(@PathVariable("groupName") String groupName) {
		return chatService.findMessagesByGroupName(groupName);
	}

	@MessageMapping("/messages/{groupName}")
	@SendTo("/messages/{groupName}")
	public MessageReadDTO sendMessages(@Payload MessageWriteDTO message) {
		return chatService.saveMessageAndReturnDto(message, userService.getIdByUsername(message.getSender()));
	}
}
