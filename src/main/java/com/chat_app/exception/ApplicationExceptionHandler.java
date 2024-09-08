package com.chat_app.exception;

import java.time.Instant;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@ControllerAdvice
public class ApplicationExceptionHandler {

	@ExceptionHandler(ErrorAPIException.class)
	@ResponseBody
	public ResponseEntity<ErrorAPIResponse> exception(ErrorAPIException ex){
		return new ResponseEntity<ErrorAPIResponse>(new ErrorAPIResponse(ex.getHttpStatus().value(), ex.getMessage(), 
					ex.getHttpStatus().getReasonPhrase(), ex.getTimestamp()), ex.getHttpStatus());
	}
	
	@ExceptionHandler(NoResourceFoundException.class)
	public String noStaticResource(NoResourceFoundException exception) {
		return "error";
	}
	
}
