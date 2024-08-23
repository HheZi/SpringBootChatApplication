package com.chat_app.service.mapper;

import java.lang.reflect.Array;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.chat_app.model.Group;
import com.chat_app.model.projection.GroupReadDTO;
import com.chat_app.model.projection.GroupWriteDTO;
import com.chat_app.model.projection.MessageReadDTO;

@Service
public class GroupMapper {

	private final String SOCKET_URL_PATTERN = "/messages/%s";
	
	private final String LAST_MESSAGE_FORMAT = "%s: %s";

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
				.lastMessage(entity.getLastMessage() == null || entity.getLastMessage().isEmpty() 
							?  "" : String.format(LAST_MESSAGE_FORMAT, entity.getLastMessage().get(0).getSender(),
							entity.getLastMessage().get(0).getContent()))
				.usersInGroup(entity.getUsersName())
				.build();
	}
}
