package com.chat_app.controller.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.chat_app.exception.ErrorAPIException;
import com.chat_app.model.User;
import com.chat_app.model.projection.GroupWriteDTO;
import com.chat_app.service.ChatService;

@Controller
public class ChatController {
	
	@Autowired
	private ChatService service;
	
	@PostMapping("/chat")
	public void createGroup(@ModelAttribute @Validated GroupWriteDTO group, BindingResult result) {
		if (result.hasErrors()) {
			throw new ErrorAPIException(HttpStatus.NOT_ACCEPTABLE, result.getFieldError().getDefaultMessage());
		}
		service.createGroup(group);
	}
	
	
	
}
