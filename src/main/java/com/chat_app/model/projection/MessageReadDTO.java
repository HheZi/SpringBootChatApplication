package com.chat_app.model.projection;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Value;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageReadDTO {

	private String chatId;
	
	private String content;

	private String sender;
	
	private String messageId;
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "UTC",  shape = Shape.STRING)
	private Instant timestamp;
}
