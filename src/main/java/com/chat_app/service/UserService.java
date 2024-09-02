package com.chat_app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Limit;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chat_app.exception.ErrorAPIException;
import com.chat_app.model.Chat;
import com.chat_app.model.User;
import com.chat_app.model.enums.Status;
import com.chat_app.model.projection.UpdateUserDTO;
import com.chat_app.model.projection.UserReadDTO;
import com.chat_app.model.projection.UserWriteDTO;
import com.chat_app.repository.UserReposiory;
import com.chat_app.service.mapper.UserMapper;

@Service
public class UserService implements UserDetailsService{

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private UserMapper userMapper;
	
	@Autowired
	private UserReposiory userReposiory;
	
	@Override
	@Transactional(readOnly = true)	
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return userReposiory.findByUsername(username)
				.orElseThrow((() -> new UsernameNotFoundException("User is not found")));
	}
	
	@Transactional(readOnly = true)	
	public List<User> getUserById(List<Integer> id) {
		return userReposiory.findAllById(id);
	}
	
	@Transactional
	public void updateUserByUsername(String username, UpdateUserDTO dto) {
		User user = userReposiory.findByUsername(username)
		.orElseThrow(() -> new ErrorAPIException(HttpStatus.NOT_FOUND, "The user is not found"));
		
//		user.setAvatar(dto.getAvatar().get);
		user.setDescription(dto.getDescription());
		user.setUsername(dto.getUsername());
		
		userReposiory.save(user);
	}
	
	@Transactional(readOnly = true)	
	public UserReadDTO getUserByUsername(String username) {
		return userReposiory.findByUsername(username)
				.map(userMapper::userToReadDTO)
				.orElseThrow(() -> new ErrorAPIException(HttpStatus.NOT_FOUND, "The user is not found"));
	}
	
	@Transactional(readOnly = true)	
	public List<Integer> getUserIdByUsername(List<String> usernames) {
		return userReposiory
				.getByUsernameIn(usernames)
				.stream()
				.map(t -> t.getId())
				.toList();
	}
	
	@Transactional(readOnly = true)	
	public List<UserReadDTO> getUsersByUsername(String username){
		return userReposiory
				.findByUsernameIsStartingWithIgnoreCase(username, Limit.of(5))
				.stream()
				.map(userMapper::userToReadDTO)
				.toList();
	}
	
	@Transactional(readOnly = true)	
	public Integer getIdByUsername(String username) {
		return userReposiory
				.findByUsername(username)
				.map(t -> t.getId())
				.orElseThrow(() -> new ErrorAPIException(HttpStatus.NOT_FOUND, "The user is not found"));
	}
	
	@Transactional
	public void saveUser(UserWriteDTO dto) {
		dto.setPassword(passwordEncoder.encode(dto.getPassword()));
		
		userReposiory.save(userMapper.writeDTOToUser(dto));
	}

	public void makeUserOnline(User user) {
		user.setStatus(Status.ONLINE);
		userReposiory.save(user);
	}

	public void makeUserOffline(User user) {
		user.setStatus(Status.OFFLINE);
		userReposiory.save(user);
	}
	
	public boolean isUsernameIsTheSameAsAuth(String username) {
		return SecurityContextHolder.getContext()
				.getAuthentication()
				.getName()
				.equals(username);	
	}
}
