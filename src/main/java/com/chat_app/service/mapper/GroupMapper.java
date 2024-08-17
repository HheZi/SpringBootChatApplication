package com.chat_app.service.mapper;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.chat_app.model.Group;
import com.chat_app.model.projection.GroupReadDTO;
import com.chat_app.model.projection.GroupWriteDTO;
import com.chat_app.model.projection.MessageReadDTO;

@Service
public class GroupMapper {

	private final String SOCKET_URL_PATTERN = "/user/%s/queue/messages";

	public Group writeDtoToGroup(GroupWriteDTO dto) {
		return Group.builder()
				.groupName(dto.getGroupName())
				.usersName(List.of(dto.getUsersName()))
				.build();
	}

	public GroupReadDTO groupToReadDto(Group entity) {
		return GroupReadDTO.builder()
				.groupName(entity.getGroupName())
				.groupSocketUrl(String.format(SOCKET_URL_PATTERN, entity.getId().substring(0, 5)))
				.lastMessage(entity.getLastMessage().get(0).getContent())
				.build();
	}
}
