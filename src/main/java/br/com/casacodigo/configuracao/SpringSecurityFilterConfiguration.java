package br.com.casacodigo.configuracao;

import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

/**
 * Para começarmos a utilizar realmente os recursos de segurança do Spring é
 * criar a classe responsável por inicializar o filtro de segurança (essa
 * classe).
 * 
 * Apenas isto não será o suficiente. A classe da forma que está já funciona,
 * mas ela apenas inicializa o filtro de segurança do Spring. Onde estão
 * realmente as configurações de segurança? Em nenhum lugar! Para armazenar as
 * configurações de segurança, criaremos uma nova classe chamada
 * SecurityConfiguration no mesmo pacote, iremos anotá-la com EnableWebSecurity.
 */
public class SpringSecurityFilterConfiguration extends AbstractSecurityWebApplicationInitializer {

}
