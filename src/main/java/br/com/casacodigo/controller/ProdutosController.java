package br.com.casacodigo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.casacodigo.model.Produto;
import br.com.casacodigo.model.TipoPreco;
import br.com.casacodigo.model.dao.ProdutoDAO;

/*
 * Definindo URL padrão inicial para todos os mapeamentos feitos na classe.
 * Ou seja, ao tentar abrir o formulário, temos apenas o requestmapping("form"),
 * mas no final, será "produtos/form".
 * Os métodos que não possuem requestmapping utilizará a URL padrão.
 */
@Controller
@RequestMapping("produtos")
public class ProdutosController {

	/*
	 * A anotação @Autowired serve para que nós não nos preocupemos em criar
	 * manualmente o ProdutoDAO no Controller.
	 * 
	 * Quando utilizamos o @AutoWired, pedimos para o Spring uma instância do objeto
	 * que foi anotado. Esse recurso é chamado de injeção de dependência e está
	 * disponível para qualquer Bean do Spring. É preferível esse tipo de abordagem,
	 * justamente para manter o conceito da inversão de controle que basicamente
	 * joga toda a responsabilidade de instanciar ou inicializar qualquer tipo de
	 * configuração necessária de um objeto para o servidor, nesse caso, o Spring.
	 * 
	 * Dessa forma, diminuímos tanto o trabalho na utilização desses objetos, como
	 * também, removemos o risco de esquecermos algum passo ou configuração durante
	 * a utilização.
	 */
	@Autowired
	private ProdutoDAO produtoDAO;

	/*
	 * Quando acessar casadocodigo/produtos/form, acessa o formulário de cadastro
	 */

	/*
	 * @RequestMapping("produtos/form") public String form() { return
	 * "produtos/form"; }
	 */
	@RequestMapping("/form")
	public ModelAndView form() {

		/*
		 * Quando utilizamos o ModelAndView, além retornar uma página, temos a
		 * possibilidade de enviar objetos de qualquer classe para essas páginas. Em
		 * outras palavras, dessa forma somos capazes de exibir, por exemplo, as
		 * informações das nossas classes modelos.
		 */
		ModelAndView modelAndView = new ModelAndView("produtos/form"); // define a página que vamos acessar
		modelAndView.addObject("tipos", TipoPreco.values()); // define o conteúdo a ser inserido na página

		return modelAndView;
	}

	/*
	 * Quando apertar o botão para cadastrar no formulário de cadastro, acessa esse
	 * método.
	 * 
	 * O Spring ao ler nossa classe Produto, irá verificar os campos submetidos com
	 * os atributos existentes na classe e fará o BIND. Ou seja, se no formulário
	 * existe um campo com o nome "titulo" e nosso Produto possui um atributo
	 * "titulo", o BIND será feito no nosso atributo pelo Spring
	 * 
	 * 
	 * _____________________________________________
	 * 
	 * RedirectAttributes é recurso do Spring que nos permite enviar informações
	 * entre requisições.
	 * 
	 * Entre nosso POST e o "redirect:", temos duas requisições diferentes. Para
	 * passar parâmetro via ModelAndView, nesse caso, não é possível por se tratar
	 * de duas requisções disttintas.
	 * 
	 * RedirectAttributes vem para resolver essa situação através do método
	 * addFlashAttribute. Esse método permite que transportar variáveis entre
	 * requisições distintas.
	 * 
	 * O método gravar agora deve receber um objeto do tipo RedirectAttributes
	 * fornecido pelo Spring. Usaremos então esse objeto para adicionar um atributo
	 * do tipo Flash (usando o método addFlashAttribute deste objeto), passando
	 * assim a uma chave sucesso e o valor dessa chave que é Produto cadastrado com
	 * sucesso!
	 * 
	 * Observação: Atributos do tipo Flash têm uma particularidade que é
	 * interessante observar. Eles só duram até a próxima requisição, ou seja,
	 * transportam informações de uma requisição para a outra e, então, deixam de
	 * existir.
	 */
	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView gravar(Produto produto, RedirectAttributes redirectAttributes) {

		System.out.println(produto);
		produtoDAO.gravar(produto);

		redirectAttributes.addFlashAttribute("sucesso", "Produto cadastrado!");
		/*
		 * Redirecionando o usuário para a listagem de produtos.
		 * 
		 * Isso é feito pois acontece que o navegador ainda está guardando os dados do
		 * POST do formulário. Apesar de ser um problema real, não podemos culpar o
		 * navegador, pois este é o funcionamento normal no caso de POSTS de formulário.
		 * 
		 * Usaremos um recurso chamado de redirect, que passa um status para o navegador
		 * carregar uma outra página e esquecer dos dados da requisição anterior. O
		 * status que o navegador recebe é um 302.
		 */
		return new ModelAndView("redirect:produtos");
	}

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView listar() {
		List<Produto> produtos = produtoDAO.listar();
		ModelAndView modelAndView = new ModelAndView("/produtos/lista");
		modelAndView.addObject("produtos", produtos);
		return modelAndView;
	}
}
