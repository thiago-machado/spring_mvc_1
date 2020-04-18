package br.com.casacodigo.configuracao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import br.com.casacodigo.model.dao.UsuarioDAO;

/**
 * Desta forma, o Spring através da classe com esta anotação já configura alguns
 * detalhes de segurança de forma automática. Mas para que isso funcione, o
 * Spring precisa saber que a classe existe. Lembra qual classe que carrega
 * todas as configurações de nossa aplicação? É a ServletSpringMVC. Nesta usamos
 * o método getServletConfigClasses para carregar as configurações da aplicação
 * e do JPA no primeiro módulo deste curso.
 * 
 * Mas agora, o método que usaremos é o getRootConfigClasses que carrega
 * configurações logo ao iniciar a aplicação. Este método simplesmente retorna
 * um Array de classes do mesmo jeito que o método getServletConfigClasses faz.
 * Assim faremos a classe SecurityConfiguration ser reconhecida pelo Spring.
 */
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	private UsuarioDAO usuarioDAO;

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		/*
		 * Na base de dados cadastramos ROLE_ALGUMA_COISA, mas aqui no Java 
		 * consta somente ALGUMA_COISA, ocultando o prefixo ROLE_.
		 * O Spring já entende que qualquer role que for inserida em hasRole 
		 * tem o prefixo ROLE_ na base de dados, e por isso não é 
		 * necessário informarmos.
		 */
		http.authorizeRequests()
			.antMatchers("/resources/**").permitAll()
			.antMatchers("/carrinho/**").permitAll()
			.antMatchers("/pagamento/**").permitAll()
			.antMatchers("/produtos/form").hasRole("ADMIN")
			.antMatchers(HttpMethod.POST, "/produtos").hasRole("ADMIN")
			.antMatchers(HttpMethod.GET, "/produtos").hasRole("ADMIN")
			.antMatchers("/produtos/**").permitAll()
			.antMatchers("/").permitAll()
			.anyRequest()
			.authenticated()
			.and().formLogin().loginPage("/login").permitAll() // informando o Spring que a página de login será a nossa
			.and().logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout")); // informando o Spring que podemos fazer logout pela URL inserindo /logout

	}

	/*
	 * O primeiro passo é sobrescrever o método configure na classe
	 * SecurityConfiguration, que recebe um objeto do tipo
	 * AuthenticationManagerBuilder chamado de auth e usar o método
	 * userDetailsService passando para este método um objeto do tipo UsuarioDAO.
	 * Criaremos o objeto como um atributo da classe SecurityConfiguration e o
	 * anotaremos com @Autowired.
	 * 
	 * Isso apenas não é suficiente para configurar a autenticação dos usuários.
	 * Aconteque que o método userDetailsService espera receber um objeto que
	 * implemente uma interface com este mesmo nome. Faremos então classe UsuarioDAO
	 * implementar a interface e adicionar à classe os método que precisam ser
	 * implementados.
	 * 
	 * Usaremos uma criptografia chamada BCrypt e faremos isto utilizando a classe
	 * BCryptPasswordEncoder. Em seguida, vamos usar o método passwordEncoder do
	 * objeto auth, no método configure da classe SecurityConfiguration.
	 */
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(usuarioDAO).passwordEncoder(new BCryptPasswordEncoder());
	}
}
