package com.chat_app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chat_app.mapper.UserMapper;
import com.chat_app.model.User;
import com.chat_app.model.enums.Status;
import com.chat_app.model.projection.UserReadDTO;
import com.chat_app.model.projection.UserWriteDTO;
import com.chat_app.repository.UserReposiory;

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
	public List<UserReadDTO> getUsersByUsername(String username){
		return userReposiory
				.findByUsernameIsStartingWithIgnoreCase(username)
				.stream()
				.map(userMapper::userToReadDTO)
				.toList();
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
		return SecurityContextHolder.getContext().getAuthentication().getName().equals(username);	
	}
}
