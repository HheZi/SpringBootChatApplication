package com.chat_app.mapper;

import java.util.List;

import com.chat_app.model.projection.GroupWriteDTO;
import com.chat_app.model.websocket.Group;

public class GroupMapper {

	public Group dtoToGroup(GroupWriteDTO dto) {
		return Group.builder()
				.groupName(dto.getGroupName())
				.usersName(List.of(dto.getUsersName()))
				.build();
	}
	
}
