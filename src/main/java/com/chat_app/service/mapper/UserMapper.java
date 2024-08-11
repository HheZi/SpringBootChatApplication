package com.chat_app.service.mapper;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.chat_app.model.User;
import com.chat_app.model.projection.UserReadDTO;
import com.chat_app.model.projection.UserWriteDTO;

@Service
public class UserMapper {

	public User writeDTOToUser(UserWriteDTO dto) {
		return User.builder()
				.email(dto.getEmail())
				.username(dto.getUsername())
				.password(dto.getPassword())
				.avatar(dto.getAvatar() == null ? "" : dto.getAvatar().getOriginalFilename())
				.build();
	}
	
	public UserReadDTO userToReadDTO(User user) {
		return UserReadDTO.builder()
				.email(user.getEmail())
				.username(user.getUsername())
				.status(user.getStatus())
				.avatarUrl(user.getAvatar().isEmpty() ? "" : user.getAvatar())
				.build();
	}
}
