package com.chat_app.model.websocket;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ChatMessage {

	private String message;

	private String sender;
	
	private Instant timestamp;
	
}
 