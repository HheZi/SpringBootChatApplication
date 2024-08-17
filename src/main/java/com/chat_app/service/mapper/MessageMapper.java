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

	public Message writeDtoToMessage(MessageWriteDTO dto) {
		return Message.builder()
				.content(dto.getContent())
				.groupName(dto.getGroupName())
				.sender(dto.getSender())
				.timestamp(Instant.now())
				.build();
	}

	public MessageReadDTO messageToReadDto(Message entity) {
		return MessageReadDTO.builder()
				.groupName(entity.getGroupName())
				.content(entity.getContent())
				.timestamp(entity.getTimestamp())
				.sender(entity.getSender())
				.build();
	}
	
	public String mapToSendeAndContentString(Optional<Message> message) {
		return message.map(t -> String.format("%s: %s", t.getSender(), t.getContent()))
				.orElse("");
	}
}
