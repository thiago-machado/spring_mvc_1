package br.com.casacodigo.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * É necessário que implemente UserDetails
 * 
 * @author thiago.machado
 *
 */
@Entity
public class Usuario implements UserDetails {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	private String email;
	private String nome;
	private String senha;

	/*
	 * É necessário que Role implemente GrantedAuthority
	 * fetch=FetchType.EAGER = carrega o usuário e suas permissões
	 */
	@OneToMany(fetch = FetchType.EAGER)
	private List<Role> roles = new ArrayList<Role>();

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return roles;
	}

	/*
	 * Para os métodos isAccountNonExpired, isAccountNonLocked,
	 * isCredentialsNonExpired e isEnabled retornaremos true, por não termos nenhum
	 * controle para expiração, bloqueio de contas ou expiração de credenciais dos
	 * usuários e queremos que os mesmos sempre esteja ativos.
	 */
	@Override
	public String getPassword() {
		return senha;
	}

	@Override
	public String getUsername() {
		return email;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}
