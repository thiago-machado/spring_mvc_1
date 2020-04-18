package br.com.casacodigo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.ModelAndView;

import br.com.casacodigo.model.CarrinhoCompras;
import br.com.casacodigo.model.CarrinhoItem;
import br.com.casacodigo.model.Produto;
import br.com.casacodigo.model.TipoPreco;
import br.com.casacodigo.model.dao.ProdutoDAO;

/**
 * Controller no geral tem um papel bem definido, ele simplesmente trata a
 * requisição. Ele recebe os dados, repassa para um outro objeto e devolve uma
 * resposta. Pensando assim, podemos concluir que após a requisição ser
 * atendida, não faz sentido que o controller ainda esteja "vivo". Geralmente, o
 * escopo dos controllers é o de request. Isto significa que quando há uma
 * requisição, o controller é criado e depois, ela deixa de existir. Podemos
 * configurar isso através da anotação @Scope com o valor da constante
 * SCOPE_REQUEST da interface WebApplicationContext.
 */
@Controller
@RequestMapping("/carrinho")
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class CarrinhoComprasController {

	@Autowired
	private ProdutoDAO produtoDAO;

	@Autowired
	private CarrinhoCompras carrinhoCompras;

	@RequestMapping("/add")
	public ModelAndView add(Integer produtoId, TipoPreco tipo) {
		ModelAndView modelAndView = new ModelAndView("redirect:/carrinho");
		CarrinhoItem carrinhoItem = criaItem(produtoId, tipo);
		carrinhoCompras.add(carrinhoItem);
		return modelAndView;
	}

	private CarrinhoItem criaItem(Integer produtoId, TipoPreco tipo) {
		Produto produto = produtoDAO.find(produtoId);
		CarrinhoItem carrinhoItem = new CarrinhoItem(produto, tipo);
		return carrinhoItem;
	}
	
	/*
	 * Quando acessar vie GET, cairemos aqui
	 */
	@RequestMapping(method=RequestMethod.GET)
	public ModelAndView itens(){
	    return new ModelAndView("carrinho/itens");
	}
	
	@RequestMapping("/remover")
	public ModelAndView remover(Long produtoId, TipoPreco tipoPreco){
		System.out.println("entrou em remover");
		carrinhoCompras.remover(produtoId, tipoPreco);
	    return new ModelAndView("redirect:/carrinho");
	}

}
