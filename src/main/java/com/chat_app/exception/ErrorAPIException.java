package com.chat_app.exception;

import java.time.Instant;

import org.springframework.core.NestedRuntimeException;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.StandardException;

@AllArgsConstructor
@Getter
public class ErrorAPIException extends RuntimeException{

	private static final long serialVersionUID = -9193466483723035316L;

	private HttpStatus httpStatus;
	
	private String message;
	
	private Instant timestamp;
	
}
