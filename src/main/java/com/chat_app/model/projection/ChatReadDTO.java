package com.chat_app.model.projection;

import java.util.List;

import com.chat_app.model.enums.ChatType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ChatReadDTO {
	
	private String chatName;
	
	private String groupSocketUrl;
	
	@Builder.Default
	private String lastMessage = "";
	
	private List<String> usersInGroup;
	
	@JsonFormat(shape = Shape.STRING)
	private ChatType chatType;
}
