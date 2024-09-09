package com.chat_app.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import com.chat_app.model.enums.ChatType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
@Document
public class Chat {
	
	@Id
	private String id;
	
	private String chatName;
	
	private String chatId;
		
	private List<Integer> usersId;
	
	private List<Message> lastMessage;
	
	private String description;
	
	private ChatType chatType;
}
