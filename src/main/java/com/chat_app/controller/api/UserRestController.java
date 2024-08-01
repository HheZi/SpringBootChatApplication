package com.chat_app.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.chat_app.exception.ResponseAPIException;
import com.chat_app.model.User;
import com.chat_app.model.projection.UserWriteDTO;
import com.chat_app.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserRestController {

	@Autowired
	private UserService userService;

	@MessageMapping("/app.addUser")
	@SendTo("/user/topic")
	public User addUser(@Payload User user) {
		userService.makeUserOnline(user);
		return user;
	}
	
	@MessageMapping("/app.disconnectUser")
	@SendTo("/user/topic")
	public User disconnectUser(@Payload User user) {
		userService.makeUserOffline(user);
		return user;
	}
	
	@PostMapping("/new")
	public ResponseEntity<?> registerUser(@ModelAttribute @Validated UserWriteDTO dto, BindingResult rs){	
		if (rs.hasErrors()) {
			throw new ResponseAPIException(HttpStatus.NOT_ACCEPTABLE, rs.getFieldError().getDefaultMessage());
		}
		userService.saveUser(dto);
		
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
	
}
