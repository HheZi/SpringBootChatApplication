package com.chat_app.model.projection;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MessageWriteDTO {
	
	private String content;
	
	private String sender;
	
}
