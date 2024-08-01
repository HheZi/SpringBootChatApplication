package com.chat_app.exception;

import java.time.Instant;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class ErrorAPIExceptionHandler {

	@ExceptionHandler(ResponseAPIException.class)
	public ResponseEntity<ErrorAPIResponse> exception(ResponseAPIException ex){
		return new ResponseEntity<ErrorAPIResponse>(new ErrorAPIResponse(ex.getHttpStatus().value(), ex.getMessage(), ex.getTimestamp()), 
				ex.getHttpStatus());
	}
	
}
