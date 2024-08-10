package com.chat_app.model.projection;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class MessageReadDTO {

	private String message;

	private String sender;
	
	private String groupName;
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
	private Instant timestamp;
	
}
