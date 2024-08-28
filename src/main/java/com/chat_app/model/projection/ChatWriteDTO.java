package com.chat_app.model.projection;

import java.util.List;

import com.chat_app.model.enums.ChatType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mongodb.lang.Nullable;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
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
	
	@NotBlank
	private String groupName;
	
	@NotEmpty(message = "At least one user need to be in group")
	private String[] usersName;
	
	@Nullable
	private String description;
	
	@NotNull(message = "Chat type is required")
	private ChatType chatType;
}
