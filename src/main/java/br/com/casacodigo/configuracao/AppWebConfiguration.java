package br.com.casacodigo.configuracao;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import br.com.casacodigo.controller.HomeController;

/*
 * @EnableWebMvc = estamos utilizando a parte WEB MVC do Spring
 * 
 * @ComponentScan = definindo quais são os Controllers. Para definirmos os 
 * Controllers, podemos fazer de uma das duas formas:
 * 1) (basePackages = "pacote")
 * 2) (basePackageClasses = MinhaClasseController.class)
 */
@EnableWebMvc
@ComponentScan(basePackageClasses = { HomeController.class }) // O Spring vai pegar o pacote HomeController para fazer o
																// mapeamento do Controller
public class AppWebConfiguration {

	/*
	 * Sobre a pasta WEB-INF:
	 * 
	 * A pasta WEB-INF é uma pasta protegida pelo servidor. Este que não permite que
	 * os arquivos dentro dela sejam acessados diretamente. É uma boa prática que
	 * deixemos nossas views dentro desta pasta para que o usuário não consiga
	 * acessar as páginas de forma direta, sem que ele passe pelo controller, pois
	 * se a view for acessada sem passar pelo controller, podemos ter quebra de
	 * regras negócio para aquela view.
	 * 
	 * 
	 * Nosso próximo passo é configurar o projeto para que o SpringMVC consiga
	 * encontrar as views. Essa configuração é feita na classe de configuração
	 * AppWebConfiguration. Nesta criaremos um novo método que ajudará o SpringMVC a
	 * encontrar nossas views.
	 * 
	 * A anotação @Bean é para que o retorno da chamada deste metódo possa ser
	 * gerenciado pelo SpringMVC, sem ela nossa configuração não funciona.
	 * Toda classe gerenciada pelo Spring é um Bean.
	 */
	@Bean
	public InternalResourceViewResolver internalResourceViewResolver() {
		InternalResourceViewResolver resolver = new InternalResourceViewResolver();
		resolver.setPrefix("/WEB-INF/views/"); // definindo onde as páginas se encontram
		resolver.setSuffix(".jsp"); // definindo a extensão dos arquivos
		return resolver;
	}

}
