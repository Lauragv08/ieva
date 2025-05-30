package com.ieva.ieva.controller;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class LoginController {
	
	@GetMapping("/login")
	public String login(Model model, Principal principal, RedirectAttributes flash,
			@RequestParam(name = "error", required = false) String error, 
			@RequestParam(name = "logout", required = false) String logout) {
		
		if (principal != null) {
			flash.addFlashAttribute("info", "El usuario " + principal.getName() + " ya tiene una sesión activa");
			return "redirect:/";
		}
		
		if (error != null) {
			model.addAttribute("error", "Usuario o clave inválido, trate de nuevo");
		}
		
		if (logout != null) {
			model.addAttribute("info", "El usuario ha cerrado su sesión de forma correcta");
		}
		
		return "login/login";
	}
}
