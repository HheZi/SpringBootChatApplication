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

import com.chat_app.model.Group;
import com.chat_app.model.Message;
import com.chat_app.model.projection.GroupReadDTO;
import com.chat_app.model.projection.GroupWriteDTO;
import com.chat_app.model.projection.MessageReadDTO;
import com.chat_app.model.projection.MessageWriteDTO;
import com.chat_app.repository.GroupRepository;
import com.chat_app.repository.MessageRepository;
import com.chat_app.service.mapper.GroupMapper;
import com.chat_app.service.mapper.MessageMapper;

@Service
public class ChatService {

	@Autowired
	private GroupRepository groupRepository;
	
	@Autowired
	private MessageRepository messageRepository;
	
	@Autowired
	private GroupMapper groupMapper;
	
	@Autowired
	private MessageMapper messageMapper;
	
	@Transactional	
	public void createGroup(GroupWriteDTO dto) {
		groupRepository.save(groupMapper.writeDtoToGroup(dto));
	}
	
	@Transactional
	public MessageReadDTO saveMessageAndReturnDto(MessageWriteDTO message) {
		return messageMapper.messageToReadDto(messageRepository
				.save(messageMapper.writeDtoToMessage(message)));
	}

	@Transactional(readOnly = true)
	public List<GroupReadDTO> getAllGroupsByUsername(String username) {
		return groupRepository
				.findByUsersNameWithLastMessage(username)
				.stream()
				.map(groupMapper::groupToReadDto)
				.toList();
	}
	
	@Transactional(readOnly = true)
	public List<MessageReadDTO> findMessagesByGroupName(String groupName) {
		return messageRepository
				.findByGroupName(groupName)
				.stream()
				.map(messageMapper::messageToReadDto)
				.toList();
	}

}
