package com.chat_app.model;

import java.time.Instant;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
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
@Builder
public class Message {
	
	@Id
	private String id;

	private String content;

	private String sender;
	
	private String chatName;
	
	private Instant timestamp;
	
}
 