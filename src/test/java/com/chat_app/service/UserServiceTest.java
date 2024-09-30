package com.chat_app.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import com.chat_app.model.User;
import com.chat_app.model.projection.UserReadDTO;
import com.chat_app.model.projection.UserWriteDTO;
import com.chat_app.service.mapper.UserMapper;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
@Sql(scripts = "classpath:sql/init_db.sql")
class UserServiceTest {

	@Mock
	private UserService userService;
	
	@Mock
	private UserMapper userMapper;
	
	@Test
	public void createUserAndAssertReadDto() {
		UserReadDTO expected = new UserReadDTO("email.com", "HheZi", null);
		
		UserWriteDTO dto = new UserWriteDTO("email.com", "HheZi", "12345");
		User savedUser = userService.saveUser(dto);
		
		log.warn("User is {} ", savedUser);
		
		assertEquals(expected, userMapper.userToReadDTO(savedUser));
	}
}
