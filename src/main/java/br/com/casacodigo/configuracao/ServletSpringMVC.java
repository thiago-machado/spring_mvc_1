package br.com.casacodigo.configuracao;

import javax.servlet.Filter;
import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration.Dynamic;

import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

/**
 * Essa classe serve para fazermos as configurações básicas.
 * 
 * Essa é a Servlet do Spring MVC para que ela atenda as requisições recebidas
 * pelo servidor
 * 
 * @author thiag
 *
 */
public class ServletSpringMVC extends AbstractAnnotationConfigDispatcherServletInitializer {

	@Override
	protected Class<?>[] getRootConfigClasses() {
		return new Class[] { SecurityConfiguration.class, AppWebConfiguration.class, JPAConfiguracao.class };
	}

	/*
	 * Configuração para que o Spring conheça nosso Controller. Estamos difinindo
	 * que a classe AppWebConfiguration será usada como classe de configuração do
	 * servlet do SpringMVC.
	 * 
	 * Estamos também dizendo ao Spring que a classe JPAConfiguracao deve ser
	 * reconhecida.
	 * 
	 */
	@Override
	protected Class<?>[] getServletConfigClasses() {
		return new Class[] {};
	}

	/*
	 * Realiza o mapeamento dos Servlets do Spring
	 * 
	 */
	@Override
	protected String[] getServletMappings() {
		/*
		 * Estamos definindo que queremos mapear do "/" em diante
		 */
		return new String[] { "/" };
	}

	/*
	 * Definindo o UFT-8 para toda a aplicação
	 */
	@Override
	protected Filter[] getServletFilters() {
		CharacterEncodingFilter encoding = new CharacterEncodingFilter();
		encoding.setEncoding("UTF-8");
		return new Filter[] { encoding };
	}

	/*
	 * Iremos sobrescrever um método chamado customizeRegistration que recebe um
	 * objeto do tipo Dynamic que chamaremos de registration. Neste objeto, usaremos
	 * o método setMultipartConfig que requer um objeto do tipo
	 * MultipartConfigElement. O MultipartConfigElement espera receber uma String
	 * que configure o arquivo. Não usaremos nenhuma configuração para o arquivo,
	 * queremos receber este do jeito que vier. Passamos então uma String vazia.
	 */
	@Override
	protected void customizeRegistration(Dynamic registration) {
		registration.setMultipartConfig(new MultipartConfigElement(""));
	}

	/*
	 * Como separamos as configurações de DataSource da aplicação com Profiles, o
	 * Spring não consegue saber qual configuração usar e assim não usa nenhuma,
	 * causando o erro.
	 * 
	 * Para que possamos definir qual configuração de DataSource o Spring deve usar
	 * ao inicializar a aplicação, precisaremos de um ouvinte de contexto, que ao
	 * perceber a inicialização da aplicação, defina que o profile a ser utilizado
	 * será o de "dev".
	 * 
	 * Para isso usaremos um novo método na classe ServletSpringMVC que faz a
	 * inicilização do Spring. Este método se chama onStartup e recebe um objeto do
	 * tipo ServletContext com o qual, através do método addListeners adicionaremos
	 * um ouvinte de contexto de requisição, objeto da classe RequestContextListener
	 * e por meio do método setInitParameter definiremos o parametro que define o
	 * Profile da aplicação com o valor "dev".
	 */
	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		super.onStartup(servletContext);
		servletContext.addListener(new RequestContextListener());
		servletContext.setInitParameter("spring.profiles.active", "dev");
	}

}
