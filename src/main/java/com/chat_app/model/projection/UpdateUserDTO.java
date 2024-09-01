package com.chat_app.model.projection;

import org.hibernate.validator.constraints.Length;
import org.springframework.lang.Nullable;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.Value;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UpdateUserDTO {
	
	@NotBlank(message = "Username must be not blank")
	@Length(min = 5, message = "Username need have at least 5 characters")
	private String username;
	
	@NotNull
	private String description;

//	@NotNull(message = "Avatar is required ")
	private MultipartFile avatar;
	
}
