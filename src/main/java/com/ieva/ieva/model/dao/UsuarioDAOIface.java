package com.ieva.ieva.model.dao;
import com.ieva.ieva.model.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioDAOIface extends JpaRepository<Usuario, Long>{

	@Query("select u from Usuario u where u.nombre = ?1")
	public Usuario buscarUsuarioPorNombre(String nombre);
	
	public Usuario findByNombre(String nombre);
}
