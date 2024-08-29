package com.chat_app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Limit;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import com.chat_app.model.Chat;
import com.chat_app.model.Message;
import com.chat_app.model.projection.ChatReadDTO;
import com.chat_app.model.projection.ChatWriteDTO;
import com.chat_app.model.projection.MessageReadDTO;
import com.chat_app.model.projection.MessageWriteDTO;
import com.chat_app.repository.ChatRepository;
import com.chat_app.repository.MessageRepository;
import com.chat_app.service.mapper.ChatMapper;
import com.chat_app.service.mapper.MessageMapper;

@Service
public class ChatService {

	@Autowired
	private ChatRepository chatRepository;

	@Autowired
	private MessageRepository messageRepository;

	@Autowired
	private ChatMapper chatMapper;

	@Autowired
	private MessageMapper messageMapper;

	@Transactional
	public ChatReadDTO createGroup(ChatWriteDTO dto) {
		return chatMapper.groupToReadDto(chatRepository.save(chatMapper.writeDtoToGroup(dto)));
	}

	@Transactional
	public MessageReadDTO saveMessageAndReturnDto(MessageWriteDTO message) {
		return messageMapper.messageToReadDto(messageRepository.save(messageMapper.writeDtoToMessage(message)));
	}

	@Transactional(readOnly = true)
	public List<ChatReadDTO> getAllGroupsByUsername(String username) {
		return chatRepository.findByUsersNameWithLastMessage(username).stream().map(chatMapper::groupToReadDto)
				.toList();
	}

	@Transactional(readOnly = true)
	public List<MessageReadDTO> findMessagesByGroupName(String chatName) {
		return messageRepository.findByChatName(chatName).stream().map(messageMapper::messageToReadDto).toList();
	}

	@Transactional(readOnly = true)
	public Boolean isPrivatChatExists(String firstUser, String secondUser) {
		return chatRepository.existsByChatNameIn(new String[] { String.format("%s_%s", firstUser, secondUser),
				String.format("%s_%s", secondUser, firstUser) });
	}
}
