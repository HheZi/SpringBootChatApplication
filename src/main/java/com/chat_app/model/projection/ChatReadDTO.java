package com.chat_app.model.projection;

import java.util.List;

import com.chat_app.model.enums.ChatType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Value;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Builder
public class ChatReadDTO {
	
	private String chatName;
	
	private String groupSocketUrl;
	
	private String chatId;
	
	@Builder.Default
	private String lastMessage = "";
	
	private List<Integer> usersInGroup;
	
	@JsonFormat(shape = Shape.STRING)
	private ChatType chatType;
}
