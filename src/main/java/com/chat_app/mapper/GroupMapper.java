package com.chat_app.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import com.chat_app.model.projection.GroupReadDTO;
import com.chat_app.model.projection.GroupWriteDTO;
import com.chat_app.model.websocket.Group;

@Component
public class GroupMapper {

	private final String socketUrlPattern = "/user/%s/queue/messages";
	
	public Group writeDtoToGroup(GroupWriteDTO dto) {
		return Group.builder()
				.groupName(dto.getGroupName())
				.usersName(List.of(dto.getUsersName()))
				.build();
	}
	
	public GroupReadDTO groupToReadDto(Group group) {
		return GroupReadDTO.builder()
				.groupName(group.getGroupName())
				.groupSocketUrl(String.format(socketUrlPattern, group.getGroupName()))
				.build();
	}
}
