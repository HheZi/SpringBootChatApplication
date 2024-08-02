package com.chat_app.controller.websocket;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.chat_app.model.User;

@Controller
public class ChatController {
	
	@GetMapping("/chat/{groupName}")
	public void createGroup(@PathVariable("groupName") String groupName) {
		
	}
	
}
