package com.chat_app.exception;

import java.time.Instant;

import org.springframework.http.HttpStatusCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class ErrorAPIException extends RuntimeException{

	private static final long serialVersionUID = 648060121129122216L;

	private HttpStatusCode httpStatus;
	
	private String message;
	
	private Instant timestamp;

	public ErrorAPIException(HttpStatusCode httpStatus, String message) {
		this.httpStatus = httpStatus;
		this.message = message;
		this.timestamp = Instant.now();
	}
	

}
