package com.chat_app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chat_app.mapper.GroupMapper;
import com.chat_app.model.projection.GroupReadDTO;
import com.chat_app.model.projection.GroupWriteDTO;
import com.chat_app.repository.GroupRepository;

@Service
public class ChatService {

	@Autowired
	private GroupRepository groupRepository;
	
	@Autowired
	private GroupMapper groupMapper;
	
	public void createGroup(GroupWriteDTO dto) {
		groupRepository.save(groupMapper.writeDtoToGroup(dto));
	}

	public List<GroupReadDTO> getAllByUsername(String username) {
		return groupRepository
				.findByUsersName(List.of(username))
				.stream()
				.map(groupMapper::groupToReadDto)
				.toList();
	}
	
}
