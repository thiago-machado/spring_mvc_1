package br.com.casacodigo.model.builder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import br.com.casacodigo.model.Preco;
import br.com.casacodigo.model.Produto;
import br.com.casacodigo.model.TipoPreco;

public class ProdutoBuilder {

	private List<Produto> produtos = new ArrayList<>();

	private ProdutoBuilder(Produto produto) {
		produtos.add(produto);

	}

	public static ProdutoBuilder newProduto(TipoPreco tipoPreco, BigDecimal valor) {
		Produto livro = create("Livro 1", tipoPreco, valor);
		return new ProdutoBuilder(livro);
	}

	private static Produto create(String nomeLivro, TipoPreco tipoPreco, BigDecimal valor) {
		Produto livro = new Produto();
		livro.setTitulo(nomeLivro);
		livro.setDataLancamento(Calendar.getInstance());
		livro.setPaginas(150);
		livro.setDescricao("Livro top sobre testes");
		Preco preco = new Preco();
		preco.setTipo(tipoPreco);
		preco.setValor(valor);
		livro.getPrecos().add(preco);
		return livro;
	}

	public ProdutoBuilder more(int number) {
		Produto base = produtos.get(0);
		Preco preco = base.getPrecos().get(0);
		for (int i = 0; i < number; i++) {
			produtos.add(create("Livro " + i, preco.getTipo(), preco.getValor()));
		}
		return this;
	}

	public Produto buildOne() {
		return produtos.get(0);
	}

	public List<Produto> buildAll() {
		return produtos;
	}
}
