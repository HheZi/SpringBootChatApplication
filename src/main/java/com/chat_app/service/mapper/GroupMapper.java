package com.chat_app.service.mapper;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.chat_app.model.Group;
import com.chat_app.model.projection.GroupReadDTO;
import com.chat_app.model.projection.GroupWriteDTO;

@Service
public class GroupMapper implements ModelMapper<Group, GroupReadDTO, GroupWriteDTO>{

	private final String SOCKET_URL_PATTERN = "/user/%s/queue/messages";

	@Override
	public Group writeDtoToModel(GroupWriteDTO dto) {
		return Group.builder()
				.groupName(dto.getGroupName())
				.usersName(List.of(dto.getUsersName()))
				.build();
	}

	@Override
	public GroupReadDTO modelToReadDto(Group entity) {
		return GroupReadDTO.builder()
				.groupName(entity.getGroupName())
				.groupSocketUrl(String.format(SOCKET_URL_PATTERN, entity.getId().substring(0, 5)))
				.build();
	}
}
