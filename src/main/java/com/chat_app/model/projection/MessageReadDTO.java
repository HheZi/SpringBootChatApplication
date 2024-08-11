package com.chat_app.model.projection;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class MessageReadDTO {

	private String content;

	private String sender;
	
	private String groupName;
	
	@JsonFormat(pattern = "HH:mm:ss", timezone = "UTC",  shape = Shape.STRING)
	private Instant timestamp;
	
}
