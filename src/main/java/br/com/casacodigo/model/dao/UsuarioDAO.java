package br.com.casacodigo.model.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

import br.com.casacodigo.model.Usuario;

/**
 * UserDetailsService que é uma interface do Spring que realmente trabalha com
 * as configurações de autenticação.
 * 
 * 
 * 
 * @author thiago.machado
 *
 */
@Repository
public class UsuarioDAO implements UserDetailsService {

	@PersistenceContext
	private EntityManager manager;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

		/*
		 * Usuario deve implementar UserDetails.
		 */
		List<Usuario> usuarios = manager.createQuery("SELECT u FROM Usuario u WHERE u.email = :email", Usuario.class)
				.setParameter("email", email).getResultList();

		if (usuarios.isEmpty()) {
			throw new RuntimeException(email + " não encontrado");
		}

		return usuarios.get(0);
	}

}
