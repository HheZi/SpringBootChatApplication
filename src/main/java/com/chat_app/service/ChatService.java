package com.chat_app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chat_app.mapper.GroupMapper;
import com.chat_app.model.projection.GroupWriteDTO;
import com.chat_app.repository.GroupRepository;

@Service
public class ChatService {

	@Autowired
	private GroupRepository groupRepository;
	
	@Autowired
	private GroupMapper groupMapper;
	
	public void createGroup(GroupWriteDTO dto) {
		groupRepository.save(groupMapper.dtoToGroup(dto));
	}
	
}
