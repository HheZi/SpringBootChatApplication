package com.chat_app.model.websocket;

import java.time.Instant;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Document
@EqualsAndHashCode
public class Message {
	
	@Id
	private String id;

	private String message;

	private String sender;
	
	private String groupName;
	
	private Instant timestamp;
	
}
 