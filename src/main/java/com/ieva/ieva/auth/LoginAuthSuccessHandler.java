package com.ieva.ieva.auth;

import java.io.IOException;
import java.util.Collection;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.FlashMap;
import org.springframework.web.servlet.support.SessionFlashMapManager;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class LoginAuthSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        SessionFlashMapManager flashMapManager = new SessionFlashMapManager();
        FlashMap flashMap = new FlashMap();

        if (authentication != null) {
            String username = authentication.getName();
            flashMap.put("success", username + " se ha autenticado de forma correcta");
            flashMapManager.saveOutputFlashMap(flashMap, request, response);
        }

        // Obtener roles del usuario
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        String redirectUrl = "/"; // Redirección por defecto

        for (GrantedAuthority authority : authorities) {
            String role = authority.getAuthority();

            // Redirección según rol
            if (role.equals("ROLE_ADMIN")) {
                redirectUrl = "/ieva/egresadolistar";
                break;
            } else if (role.equals("ROLE_USER")) {
                redirectUrl = "/ieva/egresadolistar";
                break;
            } else if (role.equals("ROLE_MMTO")) {
                redirectUrl = "/ieva/egresadolistar";
                break;
            }
        }

        //Establecer URL de redirección
        setDefaultTargetUrl(redirectUrl);

        super.onAuthenticationSuccess(request, response, authentication);
    }
}
