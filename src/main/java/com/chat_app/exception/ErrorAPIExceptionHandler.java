package com.chat_app.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ErrorAPIExceptionHandler {

	@ExceptionHandler(ErrorAPIException.class)
	public ResponseEntity<ErrorAPIResponse> exception(ErrorAPIException ex){
		return new ResponseEntity<ErrorAPIResponse>(new ErrorAPIResponse(ex.getHttpStatus(), ex.getMessage(), ex.getTimestamp()), 
				ex.getHttpStatus());
	}
	
}
