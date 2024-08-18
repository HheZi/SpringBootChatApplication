package com.chat_app.model.projection;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mongodb.lang.Nullable;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Value;

@Getter
@NoArgsConstructor
@Setter
@AllArgsConstructor
@Builder
public class GroupWriteDTO {
	
	@Nullable
	private String groupName;
	
	@NotEmpty(message = "Users name's can't be empty")
	private String[] usersName;
	
	@Nullable
	private String description;
}
