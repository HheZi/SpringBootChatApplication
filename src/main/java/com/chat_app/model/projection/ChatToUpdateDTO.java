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

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Builder
public class ChatToUpdateDTO {
	
	private String chatName;

	private String chatId;
	
	private List<String> usersInChat;
	
	private String description;
	
	@JsonFormat(shape = Shape.STRING)
	private ChatType chatType;
}
