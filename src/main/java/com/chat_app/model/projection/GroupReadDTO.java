package com.chat_app.model.projection;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class GroupReadDTO {
	
	private String groupName;
	
	private String groupSocketUrl;
	
	@Builder.Default
	private String lastMessage = "";
	
	private String[] usersInGroup;
}
