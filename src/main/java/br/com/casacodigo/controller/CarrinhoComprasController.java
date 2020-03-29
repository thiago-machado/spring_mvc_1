package br.com.casacodigo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import br.com.casacodigo.model.CarrinhoCompras;
import br.com.casacodigo.model.CarrinhoItem;
import br.com.casacodigo.model.Produto;
import br.com.casacodigo.model.TipoPreco;
import br.com.casacodigo.model.dao.ProdutoDAO;

@Controller
@RequestMapping("/carrinho")
public class CarrinhoComprasController {

	@Autowired
	private ProdutoDAO produtoDAO;
	
	@Autowired
	private CarrinhoCompras carrinhoCompras;
	
	@RequestMapping("/add")
	public ModelAndView add(Integer produtoId, TipoPreco tipo) {
		ModelAndView modelAndView = new ModelAndView("redirect:/produtos");
		CarrinhoItem carrinhoItem = criaItem(produtoId, tipo);
		carrinhoCompras.add(carrinhoItem);
		return modelAndView;
	}
	
	private CarrinhoItem criaItem(Integer produtoId, TipoPreco tipo){
	    Produto produto = produtoDAO.find(produtoId);
	    CarrinhoItem carrinhoItem = new CarrinhoItem(produto, tipo);
	    return carrinhoItem;
	}

}
