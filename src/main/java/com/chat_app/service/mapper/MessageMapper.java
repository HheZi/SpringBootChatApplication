package com.chat_app.service.mapper;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.chat_app.model.Message;
import com.chat_app.model.User;
import com.chat_app.model.projection.MessageReadDTO;
import com.chat_app.model.projection.MessageWriteDTO;

@Service
public class MessageMapper{

	public Message writeDtoToMessage(MessageWriteDTO dto, Integer senderId, String chatId) {
		return Message.builder()
				.content(dto.getContent())
				.chatId(chatId)
				.senderId(senderId)
				.timestamp(Instant.now())
				.build();
	}

	public MessageReadDTO messageToReadDto(Message entity, List<User> userId, String chatName) {
		String username = userId.stream()
		.filter(u -> u.getId() == entity.getSenderId())
		.findFirst().get().getUsername();
		return messageToReadDto(entity, username, chatName);
	}
	
	public MessageReadDTO messageToReadDto(Message entity, String username, String chatName) {
		return MessageReadDTO.builder()
				.chatName(chatName)
				.content(entity.getContent())
				.timestamp(entity.getTimestamp())
				.sender(username)
				.build();
	}
	
	protected String mapToSendeAndContentString(Optional<Message> message,String username) {
		return message.map(t -> String.format("%s: %s", username, t.getContent()))
				.orElse("");
	}
}
