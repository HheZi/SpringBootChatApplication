package com.chat_app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.chat_app.mapper.UserMapper;
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
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return userReposiory.findByUsername(username)
				.orElseThrow((() -> new UsernameNotFoundException("User is not found")));
	}

	public void saveUser(UserWriteDTO dto) {
		dto.setPassword(passwordEncoder.encode(dto.getPassword()));
		
		userReposiory.save(userMapper.dtoToUser(dto));
	}
	
	
}
