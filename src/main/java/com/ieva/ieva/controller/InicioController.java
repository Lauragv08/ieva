package com.ieva.ieva.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class InicioController {

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@GetMapping("/")
	public String inicio() {
		System.out.println(passwordEncoder.encode("Abc123"));
		return "inicio.html";
	}
	
	@GetMapping("/mantenimiento")
	public String mantenimiento(Model model, Authentication authentication) {
		model.addAttribute("mensaje", "Esta página es sólo para el usuario " + authentication.getName());
		return "vista_mmto";
	}
}
