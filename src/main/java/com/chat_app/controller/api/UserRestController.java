package com.chat_app.controller.api;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chat_app.exception.ErrorAPIException;
import com.chat_app.model.projection.UserWriteDTO;
import com.chat_app.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserRestController {

	@Autowired
	private UserService userService;
	
	@PostMapping("/new")
	public ResponseEntity<?> registerUser(@ModelAttribute @Validated UserWriteDTO dto, BindingResult rs){	
		if (rs.hasErrors()) {
			throw new ErrorAPIException(HttpStatus.NOT_ACCEPTABLE, rs.getFieldError().getDefaultMessage(), Instant.now());
		}
		userService.saveUser(dto);
		
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
}
