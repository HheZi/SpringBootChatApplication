package com.chat_app.controller.websocket;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.chat_app.exception.ErrorAPIException;
import com.chat_app.model.User;
import com.chat_app.model.projection.GroupReadDTO;
import com.chat_app.model.projection.GroupWriteDTO;
import com.chat_app.model.projection.MessageReadDTO;
import com.chat_app.service.ChatService;

@Controller
public class ChatController {
	
	@Autowired
	private ChatService service;

	@GetMapping("/chat")
	@ResponseBody
	public List<GroupReadDTO> getAllGroups(
				@RequestParam(name = "username") String username
			){
		return service.getAllByUsername(username);
	}
	
	@PostMapping("/chat")
	public void createGroup(@ModelAttribute @Validated GroupWriteDTO group, BindingResult result) {
		if (result.hasErrors()) {
			throw new ErrorAPIException(HttpStatus.NOT_ACCEPTABLE, result.getFieldError().getDefaultMessage());
		}
		service.createGroup(group);
	}
	
	@GetMapping("chat/messages")
	@ResponseBody
	public List<MessageReadDTO> getMessages(
				@RequestParam(name = "groupName") String groupName
			) {
		return service.findMessagesByGroupName(groupName);
	}
}
