package br.com.casacodigo.model;

import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Produto {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	private String titulo;
	private String descricao;
	private int paginas;

	/*
	 * Podemos fazer uma relação de produtos com seus preços em duas tabelas
	 * diferentes no banco de dados, usando o id do produto para estabelecer essa
	 * relação OneToMany, ou seja, um produto para vários preços. Mas neste
	 * contexto, isso não faria muito sentido, porque teríamos um id para o preço e
	 * não precisamos disso, pois não vamos reutilizar o preço do produto.
	 * 
	 * Faremos essa relação de uma outra forma, marcaremos o atributo List<Preco>
	 * precos da classe Produto com a anotação @ElementCollection indicando que este
	 * atributo é uma coleção de elementos.
	 * 
	 * E para que o Spring possa relacionar e portar os elementos de preço para
	 * dentro desta coleção, devemos marcar a classe Preco com uma a anotação
	 * Embeddable.
	 * 
	 * Portante, para indicar que você irá armazenar uma lista de Preco, de uma
	 * classe @Embeddable, utilize a anotação @ElementCollection.
	 */
	@ElementCollection
	private List<Preco> precos;

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public int getPaginas() {
		return paginas;
	}

	public void setPaginas(int paginas) {
		this.paginas = paginas;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public List<Preco> getPrecos() {
		return precos;
	}

	public void setPrecos(List<Preco> precos) {
		this.precos = precos;
	}

	@Override
	public String toString() {
		return "Produto [titulo=" + titulo + ", descricao=" + descricao + ", paginas=" + paginas + "]";
	}
}
