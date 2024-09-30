package com.chat_app.model.projection;

import java.util.List;

import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@NoArgsConstructor
@Setter
@AllArgsConstructor
@Builder
@ToString
public class ChatWriteDTO {
	
	@Nullable
	private String chatName;
	
	private List<String> usersName;
	
	@Nullable
	private String description;
	
}
