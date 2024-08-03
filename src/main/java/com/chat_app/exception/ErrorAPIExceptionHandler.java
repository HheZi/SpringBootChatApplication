package com.chat_app.exception;

import java.time.Instant;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class ErrorAPIExceptionHandler {

	@ExceptionHandler(ErrorAPIException.class)
	public ResponseEntity<ErrorAPIResponse> exception(ErrorAPIException ex){
		return new ResponseEntity<ErrorAPIResponse>(new ErrorAPIResponse(ex.getHttpStatus().value(), ex.getMessage(), 
					ex.getHttpStatus().getReasonPhrase(), ex.getTimestamp()), ex.getHttpStatus());
	}
	
}
