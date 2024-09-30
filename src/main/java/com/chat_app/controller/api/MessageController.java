package com.chat_app.controller.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chat_app.model.projection.MessageReadDTO;
import com.chat_app.service.MessageService;

@RestController
@RequestMapping("/messages")
public class MessageController {
	
	@Autowired
	private MessageService messageService;
	
	
	@GetMapping("/{chatId}")
	public List<MessageReadDTO> getMessages(@PathVariable("chatId") String chatId) {
		return messageService.getMessages(chatId);
	}
	
}
