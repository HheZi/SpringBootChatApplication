package com.chat_app.exception;

import java.time.Instant;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class ErrorAPIResponse {
	
	private HttpStatus httpStatus;
	
	private String message;
	
	@JsonFormat(shape = Shape.STRING)
	private Instant timestamp;
		
}
