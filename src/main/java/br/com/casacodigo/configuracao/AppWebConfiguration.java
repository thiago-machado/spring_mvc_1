package br.com.casacodigo.configuracao;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.format.datetime.DateFormatterRegistrar;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import br.com.casacodigo.controller.HomeController;
import br.com.casacodigo.infra.FileSaver;
import br.com.casacodigo.model.CarrinhoCompras;
import br.com.casacodigo.model.dao.ProdutoDAO;

/**
 * @EnableWebMvc = estamos utilizando a parte WEB MVC do Spring
 * 
 * @ComponentScan = definindo quais são os Controllers. Para definirmos os
 *                Controllers, podemos fazer de uma das duas formas: 1)
 *                (basePackages = "pacote") 2) (basePackageClasses =
 *                MinhaClasseController.class)
 * 
 *                ProdutoDAO foi inserido para que o Spring reconheça as
 *                injeções do pacote dao
 * 
 * 
 * 
 *                Por padrão, o Spring MVC nega o acesso à pasta resources.
 *                Consequentemente, o Tomcat não pode carregar os arquivos CSS.
 *                Para liberar o acesso, é preciso fazer duas alterações na
 *                classe AppWebConfiguration:
 * 
 *                1) a classe deve estender de WebMvcConfigurerAdapter; 2) deve
 *                implementar o método configureDefaultServletHandling para
 *                liberar o acesso;
 */

//O Spring vai pegar o pacote HomeController para fazer o mapeamento do Controller
@EnableWebMvc
@ComponentScan(basePackageClasses = { HomeController.class, ProdutoDAO.class, FileSaver.class, CarrinhoCompras.class })
public class AppWebConfiguration extends WebMvcConfigurerAdapter {

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

		/*
		 * Essa anotação indica que a classe será tratada como um Bean do Spring. Para
		 * que possamos acessar esse Bean em nossas view, precisaremos adicionar uma
		 * configuração na classe WebAppConfiguration. No método
		 * InternalResourceViewResolver poderiamos usar o método
		 * setExposeContextBeansAsAttributes do objeto resolver com o valor true, mas
		 * esta configuração tornará todos os Beans da aplicação disponíveis, o que
		 * parece não ser uma boa ideia.
		 * 
		 * Ao invés disso, usaremos o método setExposedContextBeanNames deste mesmo
		 * objeto. Este método nos permite dizer qual *Bean estará disponível para a
		 * view. Os nomes dos Beans seguem um padrão bem simples. O padrão é o nome da
		 * classe com sua primeira em minúsculo, ou seja, a classe CarrinhoCompras fica
		 * carrinhoCompras.
		 */
		resolver.setExposedContextBeanNames("carrinhoCompras");
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

	/*
	 * Libera o acesso aos arquivos CSS, JS e etc
	 */
	@Override
	public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
		configurer.enable();
	}

	/*
	 * Configuração básica para que o Spring consiga criar o objeto RestTemplate
	 * corretamente. Para isso criaremos um novo método na classe
	 * WebAppConfiguration anotado com @Bean e que apenas retorna um objeto do tipo
	 * RestTamplate.
	 */
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

}
