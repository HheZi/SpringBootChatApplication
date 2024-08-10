package com.chat_app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import com.chat_app.mapper.GroupMapper;
import com.chat_app.mapper.MessageMapper;
import com.chat_app.model.projection.GroupReadDTO;
import com.chat_app.model.projection.GroupWriteDTO;
import com.chat_app.model.projection.MessageReadDTO;
import com.chat_app.model.websocket.Group;
import com.chat_app.repository.GroupRepository;
import com.chat_app.repository.MessageRepository;

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

	@Transactional(readOnly = true)
	public List<GroupReadDTO> getAllByUsername(String username) {
		return groupRepository
				.findByUsersName(username)
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
