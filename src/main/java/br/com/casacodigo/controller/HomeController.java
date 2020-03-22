package br.com.casacodigo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/*
 * Para baixar o Forge:
 * https://forge.jboss.org/download
 * 
 * Para criar um projeto pelo forge:
 * 1) Acessar pasta /bin
 * 2) executar comando: forge
 * 3) Executar comando: project-new --named meuprojeto
 * 
 * 
 * @Controller = Definindo que essa classe é um Controller
 * Para que esse Controller funcione, é necessário dizer ao servidor
 * que o Spring tomará conta das requisições depois de "/".
 * Para isso, vamos criar uma classe chamada ServletSpringMVC.
 */
@Controller
public class HomeController {

	/*
	 * Mapeando as requisições em "/"
	 */
	@RequestMapping("/")
	public String index() {
		System.out.println("entrando na home");
		return "home";
	}
}
