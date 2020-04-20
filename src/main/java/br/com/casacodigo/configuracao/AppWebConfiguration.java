package br.com.casacodigo.configuracao;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.guava.GuavaCacheManager;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.format.datetime.DateFormatterRegistrar;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.view.ContentNegotiatingViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.google.common.cache.CacheBuilder;

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
@EnableCaching
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

	@Bean
	public CacheManager cacheManager() {

		/*
		 * A documentação recomenda o uso do Guava, um framework de cache fornecido pelo
		 * Google.
		 * 
		 * Criando um CacheBuilder que aceita até 100 elementos e onde seu tempo de vida
		 * é de 5 mintos
		 */
		CacheBuilder<Object, Object> builder = CacheBuilder.newBuilder().maximumSize(100).expireAfterAccess(5,
				TimeUnit.MINUTES);
		GuavaCacheManager manager = new GuavaCacheManager();
		manager.setCacheBuilder(builder);
		return manager;
		// return new ConcurrentMapCacheManager(); // deve ser usado somente para
		// desenvolvimento
	}

	/*
	 * Desenvolveu-se no mercado um padrão que provê uma negociação do conteúdo
	 * retornado pela aplicação. Através da técnica chamada de Content Negotiation é
	 * possível que uma mesma URL retorne as informações em formatos diferentes.
	 * Exemplo: acessar a URL localhost:8080/casadocodigo/produtos/5 traria como
	 * resposta o HTML da página de detalhes daquele produto, enquanto acessar
	 * localhost:8080/casadocodigo/produtos/5.json retornaria o JSON que representa
	 * aquele produto.
	 * 
	 * Perceba que a URL não muda, mas sua extensão, sim.
	 * 
	 * Com a configuração abaixo, apesar de não aparentar, nossa aplicação inteira
	 * já pode ser lida em dois formatos, JSON e HTML.
	 */
	@Bean
	public ViewResolver contentNegotiationViewResolver(ContentNegotiationManager manager) {

		List<ViewResolver> viewResolvers = new ArrayList<>();
		viewResolvers.add(internalResourceViewResolver()); // resolvedor de HTML
		viewResolvers.add(new JsonViewResolver()); // resolvedor de JSON

		ContentNegotiatingViewResolver resolver = new ContentNegotiatingViewResolver();
		resolver.setViewResolvers(viewResolvers);
		resolver.setContentNegotiationManager(manager);

		return resolver;
	}

	/*
	 * Definimos os links dos locales na página cabecalho.jsp, mas é necessário as
	 * configurações abaixo.
	 * 
	 * O que precisamos fazer é que alguém verifique na requisição a mudança do
	 * locale e também armazenar essa mudança em algum lugar, se não o usuário terá
	 * que mudar de idioma toda vez que mudar de página. O responsável por
	 * interpretar a mudança será um interceptor e armazenaremos o valor do locale
	 * do usuário nos cookies do navegador.
	 * 
	 * Na classe AppWebConfiguration, usaremos o método addInterceptors que recebe
	 * um objeto chamado registry do tipo InterceptorRegistry e através deste
	 * objeto, usaremos o método addInterceptor para adicionar um novo
	 * interceptador, este que verificará a mudança de locale do usuário, um objeto
	 * do tipo LocaleChangeInterceptor.
	 */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new LocaleChangeInterceptor());
	}

	/*
	 * O segundo passo é fornecer para o Spring um resolvedor de locale que, além de
	 * armazenar a configuração de locale do usuário, possa também carregar as
	 * páginas no idioma correto. Para isso, criaremos o método localeResolver que
	 * retorna um objeto do mesmo tipo e dentro deste método apenas retornaremos um
	 * objeto da classe CookieLocaleResolver.
	 */
	@Bean
	public LocaleResolver localeResolver() {
		return new CookieLocaleResolver();
	}

	/*
	 * A classe que implementa a interface MailSender é a JavaMailSenderImpl e será
	 * através do objeto desta classe que iremos configurar todo o acesso ao
	 * servidor de emails.
	 * 
	 * Por último, precisaremos adicionar como dependência do nosso projeto a
	 * biblioteca do Java Mail, pois sem esta, o envio de email simplesmente não irá
	 * funcionar.
	 * 
	 * O Spring utiliza o JavaMail internamente, pois ele próprio não possui a
	 * implementação de como enviar e-mail. Ele apenas criou uma forma simples para
	 * nós usarmos, e ele próprio realiza o envio através da biblioteca JavaMail.
	 */
	@Bean
	public MailSender mailSender() {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

		mailSender.setHost("smtp.gmail.com");
		mailSender.setUsername("machado.priest@gmail.com");
		mailSender.setPassword("killers81");
		mailSender.setPort(587);

		Properties mailProperties = new Properties();
		mailProperties.put("mail.smtp.auth", true);
		mailProperties.put("mail.smtp.starttls.enable", true);

		mailSender.setJavaMailProperties(mailProperties);
		return mailSender;
	}

}
