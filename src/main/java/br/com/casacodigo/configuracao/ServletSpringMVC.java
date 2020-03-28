package br.com.casacodigo.configuracao;

import javax.servlet.Filter;
import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletRegistration.Dynamic;

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
		// TODO Auto-generated method stub
		return null;
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
		return new Class[] { AppWebConfiguration.class, JPAConfiguracao.class };
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

}
