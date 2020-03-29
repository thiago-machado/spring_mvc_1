package br.com.casacodigo.controller;

import java.util.concurrent.Callable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.casacodigo.model.CarrinhoCompras;
import br.com.casacodigo.model.DadosPagamento;

@RequestMapping("/pagamento")
@Controller
public class PagamentoController {

	@Autowired
	CarrinhoCompras carrinho;

	@Autowired
	RestTemplate restTemplate;

	/*
	 * O Spring nos disponibiliza uma classe com a qual podemos fazer as requisições
	 * que precisamos. Esta classe é a RestTemplate. Ela tem um método chamado
	 * postForObject que requer três parâmetros. O primeiro deles é a url na qual
	 * queremos enviar a requisição, o segundo é o objeto que representa a mensagem
	 * (o corpo) da requisição e por último uma classe na qual esperamos receber uma
	 * resposta do tipo.
	 * 
	 * 
	 * O comportamento padrão da nossa aplicação atualmente tem um problema bem
	 * comum em diversas aplicações do gênero. Perceba que ao finalizar uma compra a
	 * aplicação envia os dados de pagamento para um outro sistema e fica aguardando
	 * uma resposta. Enquanto aguarda uma reposta a aplicação simplesmente para.
	 * Isto porque ela executa em uma única thread.
	 * 
	 * Faremos com que ao o usuário finalizar uma compra a requisição seja feita de
	 * forma assíncrona e que somente este usuário aguarde a resposta do
	 * processamento. Desta forma os demais usuários continuam usando a aplicação
	 * sem nenhum problema.
	 * 
	 * Para implementarmos essa modificação, precisamos apenas modificar a
	 * assinatura do método finalizar na classe PagamentoController para que retorne
	 * um objeto Callable do tipo ModelAndView.
	 * 
	 * Observação: Observe que estamos usando novamente recursos do Java 8. Esta
	 * forma de usar lambda nos permite criar um objeto do mesmo tipo esperado pelo
	 * retorno do método, evitando que criemos uma classe anônima. Neste caso é
	 * perfeitamente aplicável o recurso, por que na interface Callable só há um
	 * método, de nome call.
	 */
	@RequestMapping(value = "/finalizar", method = RequestMethod.POST)
	public Callable<ModelAndView> finalizar(RedirectAttributes model) {

		return () -> {
			try {
				String uri = "http://book-payment.herokuapp.com/payment"; // URI de teste
				String response = restTemplate.postForObject(uri, new DadosPagamento(carrinho.getTotal()),
						String.class);

				System.out.println(response);
				model.addFlashAttribute("sucesso", response);
			} catch (HttpClientErrorException ex) {
				model.addFlashAttribute("falha", "Valor maior que o permitido!");
				ex.printStackTrace();
			}

			return new ModelAndView("redirect:/produtos");
		};
	}
}
