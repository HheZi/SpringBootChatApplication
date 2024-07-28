package com.chat_app.model.projection;

import org.hibernate.validator.constraints.Length;
import org.springframework.lang.Nullable;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Value;

@NoArgsConstructor
@Getter
@Setter
public class UserWriteDTO {

	@Email(message = "Email must have email pattern")
	private String email;
	
	@NotBlank(message = "Username must be not blank")
	@Length(min = 5, message = "Username need have at least 5 characters")
	private String username;

	@NotBlank(message = "Password must be not blank")
	@Length(min = 5, message = "Password need have at least 5 characters")
	private String password;
	
	@Nullable
	private MultipartFile avatar;
}
