package com.ieva.ieva.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorController {
	
	@GetMapping("/error_403")
	public String error403() {
		return "error/error_403";
	}
}
