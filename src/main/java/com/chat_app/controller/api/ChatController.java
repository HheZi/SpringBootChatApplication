package com.chat_app.controller.api;

import static org.springframework.http.ResponseEntity.ok;

import java.security.Principal;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.quartz.JobStoreType;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.chat_app.exception.ErrorAPIException;
import com.chat_app.model.Group;
import com.chat_app.model.User;
import com.chat_app.model.projection.GroupReadDTO;
import com.chat_app.model.projection.GroupWriteDTO;
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
	public List<GroupReadDTO> getAllGroups(
				@RequestParam(name = "username") String username
			){
		return service.getAllGroupsByUsername(username);
	}
	
	@PostMapping(value = "/chat/groups", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> createGroup(@Validated @RequestBody GroupWriteDTO group, BindingResult result) {
		if (result.hasErrors()) {
			throw new ErrorAPIException(HttpStatus.NOT_ACCEPTABLE, result.getFieldError().getDefaultMessage());
		}
		GroupReadDTO dto = service.createGroup(group);
		Stream.of(dto.getUsersInGroup())
		.forEach(t -> messagingTemplate.convertAndSendToUser(t, "/queue", dto));
		return ok().build();
	}
	
	@GetMapping("/chat/messages")
	public List<MessageReadDTO> getMessages(
				@RequestParam(name = "groupName") String groupName
			) {
		return service.findMessagesByGroupName(groupName);
	}
	
	@MessageMapping("/chat/{groupName}/queue/messages")
	@SendTo("/chat/{groupName}/queue/messages")
	public MessageReadDTO sendMessages(@Payload  MessageWriteDTO message) {
		return service.saveMessageAndReturnDto(message);
	}
}
