package com.chat_app.model.projection;

import java.time.Instant;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MessageWriteDTO {
	
	@NotBlank(message = "Message must not be blank")
	private String content;
	
	@NotBlank(message = "Sender must not be blank")
	private String sender;
	
}
