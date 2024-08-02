package com.chat_app.model.projection;

import com.chat_app.model.enums.Status;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class UserReadDTO {

	private String email;

	private String username;
	
	@JsonFormat(shape = Shape.STRING)
	private Status status;

	private String avatarUrl;
}
