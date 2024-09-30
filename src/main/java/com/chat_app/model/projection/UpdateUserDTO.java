package com.chat_app.model.projection;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UpdateUserDTO {
	
	private String username;
	
	private String description;

//	@NotNull(message = "Avatar is required ")
	private MultipartFile avatar;
	
}
