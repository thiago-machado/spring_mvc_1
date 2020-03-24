package br.com.casacodigo.model.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.casacodigo.model.Produto;

/**
 * O Spring precisa "conhecer" o ProdutoDAO. Em outras palavras dizemos que
 * devemos definir que o ProdutoDAO será gerenciado pelo Spring. Para isso
 * devemos marcar o ProdutoDAO com a anotação @Repository.
 * 
 * Nosso ProdutoDAO é uma classe Transancional e fazemos isso através da anotação @Transactional
 */
@Repository
@Transactional
public class ProdutoDAO {

	/*
	 * Gerenciador de entidades. O Spring gerenciará nosso EntityManager
	 */
	@PersistenceContext
	private EntityManager manager;

	public void gravar(Produto produto) {
		manager.persist(produto);
	}

	public List<Produto> listar(){
	    return manager.createQuery("SELECT p FROM Produto p", Produto.class).getResultList();
	}
}
