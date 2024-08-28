package com.chat_app.service.mapper;

import java.lang.reflect.Array;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.chat_app.model.Chat;
import com.chat_app.model.enums.ChatType;
import com.chat_app.model.projection.ChatReadDTO;
import com.chat_app.model.projection.ChatWriteDTO;
import com.chat_app.model.projection.MessageReadDTO;

@Service
public class GroupMapper {

	private final String SOCKET_URL_PATTERN = "/messages/%s";
	
	private final String LAST_MESSAGE_FORMAT = "%s: %s";

	public Chat writeDtoToGroup(ChatWriteDTO dto) {
		return Chat.builder()
				.groupName(dto.getGroupName())
				.usersName(List.of(dto.getUsersName()))
				.chatType(dto.getChatType())
				.build();
	}

	public ChatReadDTO groupToReadDto(Chat entity) {
		return ChatReadDTO.builder()
				.groupName(entity.getGroupName())
				.groupSocketUrl(String.format(SOCKET_URL_PATTERN, entity.getId().substring(0, 5)))
				.lastMessage(entity.getLastMessage() == null || entity.getLastMessage().isEmpty() 
							?  "" : String.format(LAST_MESSAGE_FORMAT, entity.getLastMessage().get(0).getSender(),
							entity.getLastMessage().get(0).getContent()))
				.usersInGroup(entity.getUsersName())
				.chatType(entity.getChatType())
				.build();
	}
}
