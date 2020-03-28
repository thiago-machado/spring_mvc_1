package br.com.casacodigo.configuracao;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.format.datetime.DateFormatterRegistrar;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import br.com.casacodigo.controller.HomeController;
import br.com.casacodigo.infra.FileSaver;
import br.com.casacodigo.model.dao.ProdutoDAO;

/*
 * @EnableWebMvc = estamos utilizando a parte WEB MVC do Spring
 * 
 * @ComponentScan = definindo quais são os Controllers. Para definirmos os 
 * Controllers, podemos fazer de uma das duas formas:
 * 1) (basePackages = "pacote")
 * 2) (basePackageClasses = MinhaClasseController.class)
 * 
 * ProdutoDAO foi inserido para que o Spring reconheça as injeções do pacote dao
 */

//O Spring vai pegar o pacote HomeController para fazer o mapeamento do Controller
@EnableWebMvc
@ComponentScan(basePackageClasses = { HomeController.class, ProdutoDAO.class, FileSaver.class })
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
	 * gerenciado pelo SpringMVC, sem ela nossa configuração não funciona. Toda
	 * classe gerenciada pelo Spring é um Bean.
	 */
	@Bean
	public InternalResourceViewResolver internalResourceViewResolver() {
		InternalResourceViewResolver resolver = new InternalResourceViewResolver();
		resolver.setPrefix("/WEB-INF/views/"); // definindo onde as páginas se encontram
		resolver.setSuffix(".jsp"); // definindo a extensão dos arquivos
		return resolver;
	}

	/*
	 * Método que carregará nossos arquivos de mensagens.
	 */
	@Bean
	public MessageSource messageSource() {
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.setBasename("/WEB-INF/messages"); // onde fica nosso arquivo de configuração de mensagens do
														// sistema
		messageSource.setDefaultEncoding("UTF-8"); // encoding
		messageSource.setCacheSeconds(1); // quantos segundos vai levar para recarregar
		return messageSource;
	}

	/*
	 * Criando um conversor universal para todas as datas do sistema. Ou seja,
	 * quando realizar o POST de uma data no formato dd/MM/yyyy, esse método
	 * conseguirá fazer a conversão de um tipo String para um objeto de data.
	 */
	@Bean
	public FormattingConversionService mvcConversionService() {
		DefaultFormattingConversionService conversionService = new DefaultFormattingConversionService();
		DateFormatterRegistrar formatterRegistrar = new DateFormatterRegistrar();
		formatterRegistrar.setFormatter(new DateFormatter("dd/MM/yyyy"));
		formatterRegistrar.registerFormatters(conversionService);

		return conversionService;
	}

	/*
	 * Vamos criar um método chamado multipartResolver que retorna um objeto do tipo
	 * MultipartResolver. Este objeto será instanciado da classe
	 * StandardServletMultipartResolver e retornado.
	 * 
	 * Observação: MultipartResolver se refere a um resolvedor de dados multimídia.
	 * Quando temos texto e arquivos por exemplo. Os arquivos podem ser: imagem, PDF
	 * e outros. Este objeto é que identifica cada um dos recursos enviados e nos
	 * fornece uma forma mais simples de manipulá-los.
	 */
	@Bean
	public MultipartResolver multipartResolver() {
		return new StandardServletMultipartResolver();
	}

}
