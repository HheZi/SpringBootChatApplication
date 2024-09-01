package com.chat_app.service.mapper;

import java.time.Instant;
import java.util.Optional;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.chat_app.model.Message;
import com.chat_app.model.projection.MessageReadDTO;
import com.chat_app.model.projection.MessageWriteDTO;

@Service
public class MessageMapper{

	public Message writeDtoToMessage(MessageWriteDTO dto, Integer senderId) {
		return Message.builder()
				.content(dto.getContent())
				.chatId(dto.getChatName())
				.senderId(senderId)
				.timestamp(Instant.now())
				.build();
	}

	public MessageReadDTO messageToReadDto(Message entity, String chatName, String sender) {
		return MessageReadDTO.builder()
				.chatName(chatName)
				.content(entity.getContent())
				.timestamp(entity.getTimestamp())
				.sender(sender)
				.build();
	}
	
	public String mapToSendeAndContentString(Optional<Message> message) {
		return message.map(t -> String.format("%s: %s", t.getSenderId(), t.getContent()))
				.orElse("");
	}
}
