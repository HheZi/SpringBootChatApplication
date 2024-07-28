package com.chat_app.mapper;

import org.springframework.stereotype.Component;

import com.chat_app.model.User;
import com.chat_app.model.projection.UserWriteDTO;

@Component
public class UserMapper {

	public User dtoToUser(UserWriteDTO dto) {
		return User.builder()
				.email(dto.getEmail())
				.username(dto.getUsername())
				.password(dto.getPassword())
				.avatar(dto.getAvatar().getOriginalFilename())
				.build();
	}
	
}
