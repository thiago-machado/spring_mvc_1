package br.com.casacodigo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import br.com.casacodigo.model.Produto;
import br.com.casacodigo.model.dao.ProdutoDAO;

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

	@Autowired
    ProdutoDAO produtoDAO;

	/*
	 * Algo comum em aplicações deste tipo é o uso de Cache - um recurso que permite que guardemos 
	 * informações no contexto da aplicação. Geralmente, os dados ficam armazenados no 
	 * banco de dados - com acesso ao disco rígido. Com o uso de cache, quando as informações forem 
	 * requisitadas, a aplicação já as terá e não precisará carregá-las novamente.
	 * 
	 * A subtituição por um acesso em memória, onde o cache é guardado, agiliza o processo. 
	 * E a listagem dos livros parece ser um bom lugar para o uso de cache. Como a lista não muda 
	 * com muita frequência, iremos usar este recurso, então. Faremos isto facilmente no Spring. 
	 * Precisamos apenas marcar o método que tem os resultados a serem "cacheados" com a 
	 * anotação @Cacheable. 
	 * 
	 * Simplesmente usar a anotação não resolverá o problema, precisaremos habilitar o cache do Spring.
	 * 
	 * Esta configuração é feita na classe AppWebConfiguration e terá dois passos. 
	 * O primeiro deles é marcar a classe com a anotação @EnableCaching. 
	 * O segundo é fornecer um gerenciador de cache para que o Spring o use. 
	 * Criaremos um novo método nesta mesma classe chamado cacheManager, que retornará um objeto do 
	 * tipo CacheManager. Usaremos um gerenciador de cache que o Spring já fornece chamado 
	 * ConcurrentMapCacheManager. Lembre-se de marcar este método com a anotação @Bean. 
	 */
    @RequestMapping("/")
    @Cacheable(value = "produtoHome")
    public ModelAndView index(){
        List<Produto> produtos = produtoDAO.listar();
        ModelAndView modelAndView = new ModelAndView("home");
        modelAndView.addObject("produtos", produtos);
        return modelAndView;
    }
}
