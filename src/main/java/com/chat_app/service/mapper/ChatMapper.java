package com.chat_app.service.mapper;

import java.lang.reflect.Array;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.chat_app.model.Chat;
import com.chat_app.model.User;
import com.chat_app.model.enums.ChatType;
import com.chat_app.model.projection.ChatReadDTO;
import com.chat_app.model.projection.ChatToUpdateDTO;
import com.chat_app.model.projection.ChatWriteDTO;
import com.chat_app.model.projection.MessageReadDTO;

@Service
public class ChatMapper {

	private final String NEW_MESSAGE_URL = "/messages/%s";
	
	public Chat writeDtoToGroup(ChatWriteDTO dto, List<Integer> usersId, ChatType chatType) {
		return Chat.builder()
				.chatName(chatType == ChatType.PRIVATE ? null : dto.getChatName())
				.chatId(UUID.randomUUID().toString())
				.usersId(usersId)
				.chatType(chatType)
				.description(dto.getDescription())
				.build();
	}

	public ChatReadDTO groupToReadDto(Chat entity) {
		return ChatReadDTO.builder()
				.chatName(entity.getChatName())
				.groupSocketUrl(String.format(NEW_MESSAGE_URL, entity.getChatId()))
				.lastMessage(entity.getLastMessage() == null || entity.getLastMessage().isEmpty() 
							? "" : entity.getLastMessage().get(0).getContent())
				.chatId(entity.getChatId())
				.build();
	}
	
	public ChatToUpdateDTO chatToChatUpdateDTO(Chat chat, List<String> users) {
		return ChatToUpdateDTO.builder()
				.chatName(chat.getChatName())
				.chatId(chat.getChatId())
				.chatType(chat.getChatType())
				.description(chat.getDescription())
				.usersInChat(users)
				.build();
	}
}
