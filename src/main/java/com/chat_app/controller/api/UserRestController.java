package com.chat_app.controller.api;

import java.security.Principal;
import java.util.List;

import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.chat_app.exception.ErrorAPIException;
import com.chat_app.model.User;
import com.chat_app.model.projection.UpdateUserDTO;
import com.chat_app.model.projection.UserReadDTO;
import com.chat_app.model.projection.UserWriteDTO;
import com.chat_app.service.UserService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/users")
public class UserRestController {

	@Autowired
	private UserService userService;

// 	TODO Complete code
//	@MessageMapping("/app.addUser")
//	@SendTo("/user/topic")
//	public User addUser(@Payload User user) {
//		userService.makeUserOnline(user);
//		return user;
//	}
//	
//	@MessageMapping("/app.disconnectUser")
//	@SendTo("/user/topic")
//	public User disconnectUser(@Payload User user) {
//		userService.makeUserOffline(user);
//		return user;
//	}

	@GetMapping("/{username}")
	public UserReadDTO getUser(@PathVariable("username") String username) {
		return userService.getUserByUsername(username);
	}

	@GetMapping("/auth")
	public String getAuthUsername() {
		return UserService.getAuth().getUsername();
	}

	@GetMapping
	public List<UserReadDTO> UsersByUsername(@RequestParam(name = "username") String username) {
		return userService.getUsersByUsername(username);
	}

	@PutMapping("/{username}")
	public ResponseEntity<?> updateUser(@RequestBody @Validated UpdateUserDTO dto, BindingResult rs,
			@PathVariable("username") String username, @AuthenticationPrincipal User user) {
		if (rs.hasErrors()) {
			throw new ErrorAPIException(HttpStatus.NOT_ACCEPTABLE, rs.getFieldError().getDefaultMessage());
		}
		userService.updateUserByUsername(username, dto, user);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@PostMapping("/new")
	public ResponseEntity<?> registerUser(@ModelAttribute @Validated UserWriteDTO dto, BindingResult rs) {
		if (rs.hasErrors()) {
			throw new ErrorAPIException(HttpStatus.NOT_ACCEPTABLE, rs.getFieldError().getDefaultMessage());
		}
		userService.saveUser(dto);

		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

}
