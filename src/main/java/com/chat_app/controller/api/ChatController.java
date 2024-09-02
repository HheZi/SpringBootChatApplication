package com.chat_app.controller.api;

import static org.springframework.http.ResponseEntity.status;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.chat_app.exception.ErrorAPIException;
import com.chat_app.model.Chat;
import com.chat_app.model.Message;
import com.chat_app.model.User;
import com.chat_app.model.enums.ChatType;
import com.chat_app.model.projection.ChatReadDTO;
import com.chat_app.model.projection.ChatWriteDTO;
import com.chat_app.model.projection.MessageReadDTO;
import com.chat_app.model.projection.MessageWriteDTO;
import com.chat_app.service.ChatService;
import com.chat_app.service.UserService;
import com.chat_app.service.mapper.ChatMapper;
import com.chat_app.service.mapper.MessageMapper;

@RestController
public class ChatController {

	@Autowired
	private SimpMessagingTemplate messagingTemplate;

	@Autowired
	private ChatService chatService;

	@Autowired
	private UserService userService;

	@Autowired
	private ChatMapper chatMapper;

	@Autowired
	private MessageMapper messageMapper;

	@GetMapping("/chat/groups")
	public List<ChatReadDTO> getAllGroups(@AuthenticationPrincipal User user) {
		return chatService.getAllGroupsByUsername(user.getId()).stream().peek(t -> {
			if (t.getChatType() == ChatType.PRIVATE) {
				t.setChatName(userService.getUserById(t.getUsersId()).stream().map(u -> u.getUsername())
						.collect(Collectors.joining("_")));
			}
		}).map(chatMapper::groupToReadDto).toList();
	}

	@MessageMapping("/chat/creation")
	public ResponseEntity<?> createGroup(@Payload ChatWriteDTO group) {
		List<Integer> userIdByUsername = userService.getUserIdByUsername(group.getUsersName());

		ChatReadDTO dto = chatMapper
				.groupToReadDto(chatService.createGroup(chatMapper.writeDtoToGroup(group, userIdByUsername)));

		if (dto.getChatType() == ChatType.PRIVATE) {
			dto.setChatName(userIdByUsername.stream().map(t -> t.toString()).collect(Collectors.joining("_")));
		}

		group.getUsersName().forEach(t -> {
			messagingTemplate.convertAndSendToUser(t, "/chat/creation", dto);
		});
		return status(HttpStatus.CREATED).build();
	}

	@GetMapping("/chat/{user}")
	public ResponseEntity<?> isPrivatChatExists(@PathVariable("user") String nameOfUser,
			@AuthenticationPrincipal User user) {
		if (!chatService.isPrivatChatExists(user.getId(), userService.getIdByUsername(nameOfUser))) {
			return ResponseEntity.ok().build();
		} else {
			throw new ErrorAPIException(HttpStatus.CONFLICT, "The group already exists");
		}
	}

	@GetMapping("/chat/messages/{groupName}")
	public List<MessageReadDTO> getMessages(@PathVariable("groupName") String chatName) {
		Chat chat = chatService.findChatByChatName(chatName);
		List<Message> messages = chatService.findMessagesByChatId(chat.getId());
		List<User> usersById = userService.getUserById(chat.getUsersId());

		return messages.stream().map(t -> messageMapper.messageToReadDto(t, usersById, chatName)).toList();
	}

	@MessageMapping("/messages/{groupName}")
	@SendTo("/messages/{groupName}")
	public MessageReadDTO sendMessages(@Payload MessageWriteDTO dto) {
		return messageMapper.messageToReadDto(
				chatService
						.saveMessage(messageMapper.writeDtoToMessage(dto, userService.getIdByUsername(dto.getSender()),
								chatService.findIdOfChatByChatName(dto.getChatName()))),
				dto.getSender(), dto.getChatName());
	}
}
