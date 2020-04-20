package br.com.casacodigo.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

/**
 * Controller que monitora todos os outros controllers da aplicação. Neste caso,
 * a anotação muda de @Controller para @ControllerAdvice.
 */
@ControllerAdvice
public class ExcecaoController {

	/*
	 * Este método precisa ser anotado com @ExceptionHandler para que ao lançamento
	 * da excessão, o Spring repasse a mesma para este método. Esta anotação precisa
	 * do identificador da exceção a ser capturada, sendo este o nome da classe da
	 * qual é a excessão.
	 * 
	 * 
	 */
	@ExceptionHandler(Exception.class)
	public ModelAndView tratarExcecao(Exception exception) {
		exception.printStackTrace();

		ModelAndView modelAndView = new ModelAndView("erro"); // retornando para página erro.jsp
		modelAndView.addObject("exception", exception); // passando a exception como parâmetro para a página

		return modelAndView;
	}

}
