package com.ieva.ieva.service;

import java.util.ArrayList;
import java.util.List;

import com.ieva.ieva.model.dao.UsuarioDAOIface;
import com.ieva.ieva.model.entity.Rol;
import com.ieva.ieva.model.entity.Usuario;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsuarioDetailService implements UserDetailsService{

	protected final Log logger = LogFactory.getLog(this.getClass());
	
	@Autowired
	private UsuarioDAOIface usuarioDAO;
	
	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		Usuario usuario = usuarioDAO.findByNombre(username);
		
		if (usuario == null) {
			logger.error("*** Error de autenticación, el usuario '" + username + "' no existe");
			throw new UsernameNotFoundException("*** Error de autenticación, el usuario '" + username + "' no existe");
		}
		
		List<GrantedAuthority> roles = new ArrayList<>();
		for(Rol rol : usuario.getRoles()) {
			logger.info("Rol: " + rol.getNombre());
			roles.add(new SimpleGrantedAuthority(rol.getNombre()));
		}
		
		if (roles.isEmpty()) {
			logger.warn("El usuario " + usuario.getNombre() + " no tiene roles asignados");
			throw new UsernameNotFoundException("El usuario " + usuario.getNombre() + " no tiene roles asignados");
		}

		return new User(usuario.getNombre(), usuario.getClave(), usuario.isActivo(), true, true, true, roles);
	}
}
