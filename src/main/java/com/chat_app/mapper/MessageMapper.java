package com.chat_app.mapper;

import org.springframework.stereotype.Component;

import com.chat_app.model.projection.MessageReadDTO;
import com.chat_app.model.websocket.Message;

@Component
public class MessageMapper {

	public MessageReadDTO messageToReadDto(Message message) {
		return MessageReadDTO.builder()
				.groupName(message.getGroupName())
				.message(message.getMessage())
				.timestamp(message.getTimestamp())
				.sender(message.getSender())
				.build();
	}
	
}
