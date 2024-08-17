package com.chat_app.model.projection;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class GroupReadDTO {
	
	private String groupName;
	
	private String groupSocketUrl;
	
	private String lastMessage;
}
