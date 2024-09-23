package com.chat_app.service;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chat_app.exception.ErrorAPIException;
import com.chat_app.model.Chat;
import com.chat_app.model.Message;
import com.chat_app.model.User;
import com.chat_app.model.projection.MessageReadDTO;
import com.chat_app.model.projection.MessageWriteDTO;
import com.chat_app.repository.MessageRepository;
import com.chat_app.service.mapper.MessageMapper;

import lombok.extern.slf4j.Slf4j;

@Service
public class MessageService {
	
	@Autowired
	private MessageMapper messageMapper;
	
	@Autowired
	private MessageRepository messageRepository;

	@Autowired
	private UserService userService;
	
	@Transactional
	public MessageReadDTO sendMessage(MessageWriteDTO dto, String chatId) {
		return messageMapper.messageToReadDto(
				messageRepository.save(
						messageMapper.writeDtoToMessage(dto, userService.getIdByUsername(dto.getSender()), chatId)),
				dto.getSender(), chatId);
	}
	
	@Transactional(readOnly = true)
	public List<MessageReadDTO> getMessages(String chatId) {
		List<Message> messages = messageRepository.findByChatId(new ObjectId(chatId));
		List<User> usersById = userService.getUserById(messages.stream()
				.map(Message::getSenderId)
				.distinct()
				.toList());

		
		return messages.stream()
			   .map(t -> messageMapper.messageToReadDto(t, usersById, chatId))
			   .toList();
	}
	
	@Transactional
	public String deleteMessageById(String id) {
		Message message = messageRepository.findById(id)
				.orElseThrow(() -> new ErrorAPIException(HttpStatus.NOT_FOUND, "The message is not found"));
		
		if (message.getSenderId() != UserService.getAuth().getId()) {
			throw new ErrorAPIException(HttpStatus.CONFLICT, "Not enough rights to delete the message");
		}
		
		messageRepository.delete(message);
		return id;
	}

	protected void deleteAllMessagesByChatId(String chatId) {
		messageRepository.deleteByChatId(chatId);
	}
}
