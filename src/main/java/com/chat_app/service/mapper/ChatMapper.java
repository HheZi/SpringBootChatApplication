package com.chat_app.service.mapper;

import java.lang.reflect.Array;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.chat_app.model.Chat;
import com.chat_app.model.enums.ChatType;
import com.chat_app.model.projection.ChatReadDTO;
import com.chat_app.model.projection.ChatWriteDTO;
import com.chat_app.model.projection.MessageReadDTO;

@Service
public class ChatMapper {

	private final String SOCKET_URL_PATTERN = "/messages/%s";
	
	private final String LAST_MESSAGE_FORMAT = "%s: %s";

	public Chat writeDtoToGroup(ChatWriteDTO dto) {
		return Chat.builder()
				.chatName(dto.getChatName())
				.usersName(List.of(dto.getUsersName()))
				.chatType(dto.getChatType())
				.build();
	}

	public ChatReadDTO groupToReadDto(Chat entity) {
		return ChatReadDTO.builder()
				.chatName(entity.getChatName())
				.groupSocketUrl(String.format(SOCKET_URL_PATTERN, entity.hashCode()))
				.lastMessage(entity.getLastMessage() == null || entity.getLastMessage().isEmpty() 
							?  "" : String.format(LAST_MESSAGE_FORMAT, entity.getLastMessage().get(0).getSender(),
							entity.getLastMessage().get(0).getContent()))
				.usersInGroup(entity.getUsersName())
				.chatType(entity.getChatType())
				.build();
	}
}
