package br.com.casacodigo.configuracao;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

/**
 * Essa classe serve para fazermos as configurações básicas.
 * 
 * Essa é a Servlet do Spring MVC para que ela atenda as requisições recebidas pelo servidor
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
	 * Configuração para que o Spring conheça nosso Controller.
	 * Estamos difinindo que a classe AppWebConfiguration será usada 
	 * como classe de configuração do servlet do SpringMVC
	 * 
	 */
	@Override
	protected Class<?>[] getServletConfigClasses() {
		return new Class[] { AppWebConfiguration.class };
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
		return new String[] {"/"};
	}

}
