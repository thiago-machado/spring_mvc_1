package br.com.casacodigo.model;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.springframework.security.core.GrantedAuthority;

/*
 * É necessário que Role implemente GrantedAuthority
 */
@Entity
public class Role implements GrantedAuthority {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
    private String nome;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

	@Override
	public String getAuthority() {
		return nome;
	}

}