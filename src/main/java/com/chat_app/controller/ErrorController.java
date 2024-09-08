package com.chat_app.controller;

import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("error")
public class ErrorController {
	
	public String error() {
		return "error";
	}
	
}
