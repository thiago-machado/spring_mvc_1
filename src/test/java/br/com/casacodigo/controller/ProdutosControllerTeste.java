package br.com.casacodigo.controller;

import javax.servlet.Filter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import br.com.casacodigo.conf.DataSourceConfiguracaoTeste;
import br.com.casacodigo.configuracao.AppWebConfiguration;
import br.com.casacodigo.configuracao.JPAConfiguracao;
import br.com.casacodigo.configuracao.SecurityConfiguration;

/**
 * 
 * Como podemos garantir que o HomeController em seu método index realmente esta
 * retornando como resposta a view home.jsp? E como podemos garantir que os
 * produtos estão sendo carregados para esta página? A resposta é simples! Por
 * meio dos testes automatizados!
 * 
 * O primeiro teste será fazer uma requisição para a página inicial da nossa
 * aplicação e verificar se a view home.jsp está realmente sendo retornada. O
 * primeiro passo ao criar a classe é fazer as devidas configurações, bem
 * parecidas com as da classe ProdutoDAOTeste.
 * 
 * As únicas diferenças são que na anotação @ContextConfiguration em vez de
 * carregarmos a classe ProdutoDAO, carregamos a classe que tem todas as
 * configurações de MVC da aplicação, AppWebConfiguration. A outra diferença é a
 * presença da anotação @WebAppConfiguration que faz o carregamento das demais
 * configurações de MVC do Spring.
 */
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { JPAConfiguracao.class, AppWebConfiguration.class, DataSourceConfiguracaoTeste.class,
		SecurityConfiguration.class })
@ActiveProfiles("teste")
public class ProdutosControllerTeste {

	/*
	 * Para que possamos fazer requisições sem o uso de um navegador, precisamos de
	 * um objeto capaz de simular este procedimento. Estes objetos que simulam
	 * comportamentos são conhecidos como Mock's. E para a criação de um mock no
	 * Spring, precisaremos definir um contexto. Por isso, criaremos mais dois
	 * atributos na classe ProdutosControllerTest.
	 * 
	 * O Spring já conhece o contexto da aplicação, por isso o atributo
	 * WebApplicationContext é anotado com @Autowired. O Objeto MockMvc será o
	 * objeto que fará as requisições para os controllers da nossa aplicação.
	 */
	@Autowired
	private WebApplicationContext wac;

	@Autowired
	private Filter springSecurityFilterChain;

	private MockMvc mockMvc;

	/*
	 * Apesar do Spring criar o objeto de contexto da aplicação de forma automática,
	 * este não cria o objeto Mock, mas facilita essa tarefa através de classes
	 * auxiliadoras. Para a criação do objeto MockMvc, definiremos um método que
	 * será executado antes dos testes e instanciaremos o objeto usando a classe
	 * MockMvcBuilders. Iremos fornecer para o método, que se chamará setup, o
	 * contexto webAppContextSetup.
	 * 
	 * A anotação @Before é quem faz com que o método seja chamado antes que
	 * qualquer teste seja executado. Métodos de setup são muito comuns para
	 * carregamento de recursos e definição de configurações que devem ser
	 * executadas antes de qualquer teste.
	 */
	@Before
	public void setup() {
		mockMvc = MockMvcBuilders.webAppContextSetup(wac).addFilter(springSecurityFilterChain).build();
	}

	/*
	 * Método perform() do objeto mockMvc simula uma requisição.
	 * 
	 * Precisaremos de algum outro objeto que construa um Request, objeto de
	 * requisição. Este objeto será fornecido pela classe MockMvcRequestBuilderspor
	 * meio do método GET para o qual será passado o caminho da requisição.
	 * 
	 * Nos resta verificar o resultado da mesma por meio do método andExpect do
	 * objeto mockMvc que receberá o resultado do método forwardedUrl da classe
	 * MockMvcResultMatchers no qual verificará se foi feito um redirecionamento no
	 * servidor para a view localizada em WEB-INF/views/home.jsp.
	 * 
	 * E para verificarmos a presença dos produtos na resposta da requisição?
	 * Faremos praticamente a mesma coisa que já fizemos: usaremos o andExpect
	 * novamente e também a classe MockMvcResultMatchers, mas com a pequena
	 * diferença que, desta vez, usaremos o método model para que consigamos obter
	 * informações sobre o objeto retornado pela requisição e neste objeto,
	 * verificaremos a existencia do atributo produtos utilizando o método
	 * attributeExists.
	 */
	@Test
	public void deveRetornarParaHomeComOsLivros() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("produtos"))
				.andExpect(MockMvcResultMatchers.forwardedUrl("/WEB-INF/views/home.jsp"));
	}

	/*
	 * Outro teste interessante de ser feito é verificar se a página de cadastro de
	 * produto em /produtos/form é realmente acessada apenas pelos administradores.
	 * Para tal tipo, é necessário configurar uma nova dependência no arquivo
	 * pom.xml. Esta dependência torna possível realizar testes no contexto do
	 * Spring Security.
	 * 
	 * Para testar uma tentativa de autenticação, precisamos que o Mock MVC faça a
	 * requisição um Post Processor. Ou seja, um processo de POST antes de executar
	 * o GET da página, passando neste Post Processor os dados de autenticação do
	 * usuário.
	 * 
	 * Note que para podermos usar o Post Processor, precisaremos fazer uso do
	 * método with que adiciona dados adicionais à requisição. A classe
	 * SecurityMockMvcRequestPostProcessors do Spring Security Test nos permite
	 * simular os dados de um usuário, com senha e Role configurados para a
	 * requisição. Por último, verificamos na resposta da requisição se o status da
	 * mesma foi um código 403 que significa um redirecionamento.
	 * 
	 * Por fim, para funcionar, precisamos inserir a classe SecurityConfiguration no
	 * @ContextConfiguration e criar um atributo Filter que será passado no setup()
	 * com o método springSecurityFilterChain () do mockMvc.
	 */
	@Test
	public void somenteAdminDeveAcessarProdutosForm() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/produtos/form").with(SecurityMockMvcRequestPostProcessors
				.user("user@casadocodigo.com.br").password("123456").roles("USUARIO")))
				.andExpect(MockMvcResultMatchers.status().is(403));
	}

}
