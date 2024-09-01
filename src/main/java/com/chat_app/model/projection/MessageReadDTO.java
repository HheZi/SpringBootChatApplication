package com.chat_app.model.projection;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class MessageReadDTO {

	private String chatName;
	
	private String content;

	private String sender;
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "UTC",  shape = Shape.STRING)
	private Instant timestamp;
}
