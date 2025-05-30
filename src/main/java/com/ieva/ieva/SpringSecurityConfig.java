package com.ieva.ieva;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import com.ieva.ieva.auth.LoginAuthSuccessHandler;
import com.ieva.ieva.service.UsuarioDetailService;

@Configuration
public class SpringSecurityConfig {
    @Autowired
	LoginAuthSuccessHandler successHandler;
	
	@Autowired
	UsuarioDetailService usuarioDetailService;

    @Bean
	public static BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
    @Autowired
	public void userDetailsService(AuthenticationManagerBuilder build) throws Exception {
		build.userDetailsService(usuarioDetailService).passwordEncoder(passwordEncoder());
	}

    	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests()
		.requestMatchers("/", "/css/**", "/js/**", "/img/**", "/ieva/egresadolistar", "/ieva/egresadoconsultar/**").permitAll()
		.requestMatchers("/ieva/egresadoconsultar/**").hasAnyRole("USER")
		.requestMatchers( "/ieva/egresadonuevo/**",  "/ieva/egresadoeliminar/**","/ieva/egresadomodificar/**").hasAnyRole("ADMIN")
		.requestMatchers("/documento/documentonuevo/**", "/documento/documentoeliminar/**", "/documento/documentoconsultar/**").hasAnyRole("ADMIN")
//		.requestMatchers("/comercial/facturanueva/**").hasAnyRole("ADMIN")
		.requestMatchers("/mantenimiento").hasAnyRole("MMTO")
		.anyRequest().authenticated()
		.and()
		.formLogin().successHandler(successHandler)
		.loginPage("/login").permitAll()
		.and()
		.logout().permitAll()
		.and()
		.exceptionHandling().accessDeniedPage("/error_403");
		
		
		return http.build();
	}

}
