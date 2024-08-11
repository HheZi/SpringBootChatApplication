package com.chat_app.service.mapper;

import java.time.Instant;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.chat_app.model.Message;
import com.chat_app.model.projection.MessageReadDTO;
import com.chat_app.model.projection.MessageWriteDTO;

@Service
public class MessageMapper implements ModelMapper<Message, MessageReadDTO, MessageWriteDTO>{

	@Override
	public Message writeDtoToModel(MessageWriteDTO dto) {
		return Message.builder()
				.content(dto.getContent())
				.groupName(dto.getGroupName())
				.sender(dto.getSender())
				.timestamp(Instant.now())
				.build();
	}

	@Override
	public MessageReadDTO modelToReadDto(Message entity) {
		return MessageReadDTO.builder()
				.groupName(entity.getGroupName())
				.content(entity.getContent())
				.timestamp(entity.getTimestamp())
				.sender(entity.getSender())
				.build();
	}
}
