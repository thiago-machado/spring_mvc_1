package br.com.casacodigo.model.dao;

import java.math.BigDecimal;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import br.com.casacodigo.conf.DataSourceConfiguracaoTeste;
import br.com.casacodigo.configuracao.JPAConfiguracao;
import br.com.casacodigo.model.Produto;
import br.com.casacodigo.model.TipoPreco;
import br.com.casacodigo.model.builder.ProdutoBuilder;

/**
 * Para que o JUnit possa reconhecer o contexto dos objetos do Spring,
 * precisamos adicionar uma nova biblioteca em nosso projeto, sendo esta a
 * Spring Test por meio da declaração da mesma no arquivo pom.xml
 * 
 * Mas apenas isso não é o suficiente. Precisamos dizer agora que o JUnit deverá
 * carregar configurações do Spring Test para poder executar os testes e fazemos
 * isso realizando duas configurações por anotação.
 * 
 * A anotação @RunWith do próprio JUnit nos permite dizer que classe irá
 * executar os testes encontrados na nossa suite de testes. Já a
 * anotação @ContextConfiguration nos permite configurar quais são as classes de
 * configurações para execução dos testes. Como estamos usando conexão com o
 * banco de dados, precisamos da classe que configura a JPA e o ProdutoDAO neste
 * caso.
 * 
 * Após este passo, podemos transformar o objeto produtoDAO em um atributo da
 * classe ProdutoDAOTest, anota-lo com @Autowired e renomea-lo para produtoDao
 * para que fique mais claro a mudança. Por último, precisamos que o método seja
 * anotado com @Transactional porque o mesmo precisa de uma transação com o
 * banco de dados para que tudo funcione corretamente. Assim teremos:
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { JPAConfiguracao.class, ProdutoDAO.class, DataSourceConfiguracaoTeste.class })
@ActiveProfiles("teste")
public class ProdutoDAOTeste {

	@Autowired
	private ProdutoDAO produtoDAO;

	@Test
	@Transactional
	public void deveSomarTodosOsPrecosPorTipoLivro() {

		List<Produto> livrosImpressos = ProdutoBuilder.newProduto(TipoPreco.IMPRESSO, BigDecimal.TEN).more(3)
				.buildAll();
		List<Produto> livrosEbook = ProdutoBuilder.newProduto(TipoPreco.EBOOK, BigDecimal.TEN).more(3).buildAll();

		// livrosImpressos.stream().forEach(produtoDAO::gravar);
		// livrosEbook.stream().forEach(produtoDAO::gravar);
		livrosImpressos.stream().forEach((produto) -> produtoDAO.gravar(produto));
		livrosEbook.stream().forEach((produto) -> produtoDAO.gravar(produto));

		BigDecimal valor = produtoDAO.somaPrecosPorTipo(TipoPreco.EBOOK);
		Assert.assertEquals(new BigDecimal(40).setScale(2), valor);
		
		/*
		 * IMPORTANTE: as informações inseridas serão deletadas pelo Spring a cada fim de execução.
		 * Ou seja, se tivermos 10 métodos de testes que fazem inserção na base, nos 10 métodos a base 
		 * será limpa para que um método não pegue a "sujeira" deixada pelo anterior.
		 */
	}

}
