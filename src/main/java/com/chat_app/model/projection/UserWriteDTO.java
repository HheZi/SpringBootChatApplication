package com.chat_app.model.projection;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class UserWriteDTO {

	private String email;
	
	private String username;

	private String password;
}
