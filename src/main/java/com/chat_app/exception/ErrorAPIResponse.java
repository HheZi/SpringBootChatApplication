package com.chat_app.exception;

import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class ErrorAPIResponse {
	
	private Integer httpStatus;
	
	private String message;
	
	private String error;
	
	@JsonFormat(shape = Shape.STRING)
	private Instant timestamp;
		
}
