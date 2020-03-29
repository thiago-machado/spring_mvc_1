package br.com.casacodigo.model;

import java.util.Calendar;
import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
public class Produto {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	private String titulo;
	private String descricao;
	private int paginas;

	/*
	 * Irá fazer a conversão da String dd/MM/yyyy para um Calendar No caso, estamos
	 * usando agora um conversor universal definido em AppWebConfiguration. E por
	 * isso essa anotação comentada não é mais necessária
	 */
	// @DateTimeFormat(pattern = "dd/MM/yyyy")
	@DateTimeFormat
	private Calendar dataLancamento;

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

	/*
	 * Existem várias estratégias para guardar arquivos nas aplicações. Uma delas
	 * seria guardar o arquivo no banco de dados, mas esta seria muito trabalhosa e
	 * precisaríamos converter o arquivo para um formato aceito pelo banco,
	 * geralmente bytes. Outra opção seria guardar nas pastas do sistema de arquivos
	 * do servidor. Optaremos por esta segunda opção, por isso, o atributo
	 * sumarioPath é do tipo String. Nele será guardado apenas o caminho (path) do
	 * arquivo.
	 */
	private String sumarioPath;

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

	public Calendar getDataLancamento() {
		return dataLancamento;
	}

	public void setDataLancamento(Calendar dataLancamento) {
		this.dataLancamento = dataLancamento;
	}

	@Override
	public String toString() {
		return "Produto [titulo=" + titulo + ", descricao=" + descricao + ", paginas=" + paginas + "]";
	}

	public String getSumarioPath() {
		return sumarioPath;
	}

	public void setSumarioPath(String sumarioPath) {
		this.sumarioPath = sumarioPath;
	}

	/*
	 * Estes dois métodos foram criados somente para utilização no Carrinho de Compras
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Produto other = (Produto) obj;
		if (id != other.id)
			return false;
		return true;
	}
	
}
