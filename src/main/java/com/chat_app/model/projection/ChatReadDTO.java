package com.chat_app.model.projection;

import java.util.List;

import com.chat_app.model.enums.ChatType;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ChatReadDTO {
	
	private String groupName;
	
	private String groupSocketUrl;
	
	@Builder.Default
	private String lastMessage = "";
	
	private List<String> usersInGroup;
	
	private ChatType chatType;
}
