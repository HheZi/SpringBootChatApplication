package com.chat_app.model.projection;

import java.util.List;

import com.mongodb.lang.Nullable;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Value;

@Value
@Builder
public class GroupWriteDTO {
	
	@Nullable
	private String groupName;
	
	@NotEmpty(message = "Users name's can't be empty")
	private String[] usersName;
}
