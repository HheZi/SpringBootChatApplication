package com.chat_app.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import com.chat_app.model.User;
import com.chat_app.model.projection.UserReadDTO;
import com.chat_app.model.projection.UserWriteDTO;
import com.chat_app.service.mapper.UserMapper;

import lombok.extern.slf4j.Slf4j;

@Profile("test")
@DataJpaTest(showSql = true)
@Sql(scripts = "classpath:sql/init_db.sql")
class UserServiceTest {

	@MockBean
	private UserService userService;
	
	@MockBean
	private UserMapper userMapper;
	
	@Test
	public void createUserAndAssertReadDto() {
		UserReadDTO expected = new UserReadDTO("email.com", "HheZi", null);
		
		UserWriteDTO dto = new UserWriteDTO("email.com", "HheZi", "12345");
		userService.saveUser(dto);
		
		
		
		assertEquals(expected, userService.getUserByUsername(expected.getUsername()));
	}
}
