package br.com.casacodigo.model.dao;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.casacodigo.model.Produto;
import br.com.casacodigo.model.TipoPreco;

/**
 * O Spring precisa "conhecer" o ProdutoDAO. Em outras palavras dizemos que
 * devemos definir que o ProdutoDAO será gerenciado pelo Spring. Para isso
 * devemos marcar o ProdutoDAO com a anotação @Repository.
 * 
 * Nosso ProdutoDAO é uma classe Transancional e fazemos isso através da
 * anotação @Transactional
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

	public List<Produto> listar() {
		return manager.createQuery("SELECT p FROM Produto p", Produto.class).getResultList();
	}

	/*
	 * Precisamos fazer com que o Hibernate relacione os produtos com seus preços.
	 * Pra isso, utilizamos o JOIN FETCH. Isso evitará o LazyInitializationException
	 * ao tentarmos chamar a lista de preços de produto.
	 * 
	 * Observe que estamos fazendo um SELECT comum, mas estamos usando o DISTINCT
	 * para que o Hibernate nos retorne apenas resultados diferentes. Estamos também
	 * fazendo um JOIN FETCH com a tabela Precos usando como relação o id do produto
	 * presente na tabela de preços.
	 * 
	 * Usaremos o sql através do método createQuery e passaremos o :id através do
	 * método setParameter. Queremos retornar apenas um resultado desta query
	 * através do método getSingleResult.
	 */
	public Produto find(long id) {
		return manager
				.createQuery("SELECT DISTINCT(p) FROM Produto p JOIN FETCH p.precos WHERE p.id = :id", Produto.class)
				.setParameter("id", id).getSingleResult();
	}
	
	
	public BigDecimal somaPrecosPorTipo(TipoPreco tipoPreco){
	    TypedQuery<BigDecimal> query = manager.createQuery(
	    		"SELECT sum(preco.valor) "
	    		+ "FROM Produto p JOIN p.precos preco "
	    		+ "WHERE preco.tipo = :tipoPreco", BigDecimal.class);
	    query.setParameter("tipoPreco", tipoPreco);
	    return query.getSingleResult();
	}
}
