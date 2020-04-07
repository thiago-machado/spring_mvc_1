package br.com.casacodigo.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.casacodigo.infra.FileSaver;
import br.com.casacodigo.model.Produto;
import br.com.casacodigo.model.TipoPreco;
import br.com.casacodigo.model.dao.ProdutoDAO;
import br.com.casacodigo.validacao.ProdutoValidacao;

/*
 * Definindo URL padrão inicial para todos os mapeamentos feitos na classe.
 * Ou seja, ao tentar abrir o formulário, temos apenas o requestmapping("form"),
 * mas no final, será "produtos/form".
 * Os métodos que não possuem requestmapping utilizará a URL padrão.
 */
@Controller
@RequestMapping("/produtos")
public class ProdutosController {

	/*
	 * Agora precisamos criar um método em nosso controller chamado InitBinder que
	 * terá uma anotação com o mesmo nome do método @InitBinder. Este método
	 * recebera um objeto do tipo WebDataBinder que chamaremos apenas de binder. O
	 * objeto binder tem um médoto chamado addValidators que recebe uma instância de
	 * uma classe que implemente a interface Validator do pacote
	 * org.springframwork.validation.
	 * 
	 * Observação: O Binder, por assim dizer, é o responsável por conectar duas
	 * coisas. Por exemplo, conectar nosso "@Valid Produto" com nossa validação em
	 * ProdutoValidacao.
	 */
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.addValidators(new ProdutoValidacao());
	}

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

	@Autowired
	private FileSaver fileSaver;

	/*
	 * Quando acessar casadocodigo/produtos/form, acessa o formulário de cadastro
	 */

	/*
	 * @RequestMapping("produtos/form") public String form() { return
	 * "produtos/form"; }
	 */

	/*
	 * O Spring tenta usar um objeto da classe Produto para poder exibir no
	 * formulário. Isso acontece porque já que configuramos o formulário para
	 * guardar os dados mesmo quando acontecer erros de validação, dessa forma, ele
	 * precisa de um objeto para poder armazenar esses dados e para poder exibir o
	 * formulário, mesmo que vazio.
	 * 
	 * Para que o objeto do tipo Produto fique disponível em nosso formulário, só
	 * precisamos fazer uma pequena alteração em nosso ProdutosController. Em nosso
	 * método form() só precisamos colocar que queremos receber como parametro um
	 * objeto do tipo Produto. Dessa forma o Spring já deixará este objeto
	 * disponível na requisição.
	 * 
	 * NOTA: curioso que precisamos somente declarar o objeto e não precisa fazer
	 * mais nada.
	 */
	@RequestMapping("/form")
	public ModelAndView form(Produto produto) {

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
	 * 
	 * 
	 * @Valid = O Spring realizará a validação e informará o usuário na página.
	 * 
	 * 
	 * BindingResult = Falta recebermos o resultado da verificação, da validação em
	 * sí em nosso controller e verificar se houve algum erro. Faremos isto
	 * recebendo na assinatura do nosso método gravar um objeto do tipo
	 * BindingResult que tem um método chamado hasErrors, que informa se houve erros
	 * de validação ou não.
	 * 
	 * Note que o BindingResult vem logo após o atributo que está assinado com a
	 * anotação @Valid, essa ordem não é por acaso, precisa ser desta forma para que
	 * o Spring consiga fazer as validações da forma correta.
	 * 
	 * 
	 * O Spring enviará nosso arquivo para o ProdutosController como um objeto do
	 * tipo MultipartFile, que chamaremos de sumario.
	 */
	@RequestMapping(method = RequestMethod.POST)
	@CacheEvict(value="produtoHome", allEntries=true) // limpando o cache "produtoHome" por inteiro (allEntries)
	public ModelAndView gravar(MultipartFile sumario, @Valid Produto produto, BindingResult result,
			RedirectAttributes redirectAttributes) {

		if (result.hasErrors()) {
			return form(produto); // enviando o produto para preenchimento no formulário
		}

		String path = fileSaver.write("arquivos-sumario", sumario);
		produto.setSumarioPath(path);
		System.out.println(path);

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

	/*
	 * Antes, nossa URL ficava assim:
	 * http://localhost:8080/casadocodigo/produtos/detalhe?id=2
	 * 
	 * Vamos deixá-la assim: http://localhost:8080/casadocodigo/produtos/detalhe/2
	 * 
	 * ******************************************************************************
	 * 
	 * Se chamarmos: http://localhost:8080/casadocodigo/produtos/detalhe/2
	 * Abrirá a página HTML
	 * 
	 * Se chamarmos: http://localhost:8080/casadocodigo/produtos/detalhe/2.json
	 * Retornará o JSON
	 * Isso é possível através do método que criamos em AppWebConfiguration chamado 
	 * contentNegotiationViewResolver(...)
	 * 
	 * ******************************************************************************
	 * 
	 * Para isso, precisamos mudar a assinatura do método detalhe em nosso
	 * ProdutosController. Ela precisa receber o parâmetro separado pela barra (/).
	 * A anotação @RequestMapping permite que façamos isso.
	 * 
	 * Apenas isto não é o suficient! Precisaremos indicar para o método
	 * detalhe que o parâmetro id será recuperado do caminho da url. Então, usaremos
	 * uma nova anotação: @PathVariable passando o id desta
	 * forma: @PathVariable("id").
	 * 
	 * Obs.: o tipo enviado pela página deve ser o mesmo dentro do método que o está recebendo.
	 * Ou seja, se estou enviando um Long pela página JSP, no método o tipo também 
	 * deve ser um Long.
	 */
	@RequestMapping("/detalhe/{id}")
	public ModelAndView detalhe(@PathVariable("id") Long id) {
		ModelAndView modelAndView = new ModelAndView("/produtos/detalhe");
		Produto produto = produtoDAO.find(id);
		modelAndView.addObject("produto", produto);
		return modelAndView;
	}
	
	/*
	 * Essa chamada retornará um JSON de um produto
	 * @ResponseBody = para que o Spring consiga responder a requisição da forma que queremos neste caso, 
	 * usaremos a anotação ResponseBody.
	 * 
	 * É importante notar que só funcionou, porque o Jackson está configurado no projeto. 
	 * Se não tivéssemos configurado a biblioteca anteriormente no primeiro módulo do curso, o 
	 * recurso não teria funcionado. Teríamos problema também, caso mais de uma biblioteca estivesse 
	 * configurada para transformar objetos em JSON, no projeto. Haveria conflito e teríamos que realizar 
	 * novas configurações para obter o mesmo resultado.
	 */
	@RequestMapping("/{id}")
	@ResponseBody
	public Produto detalheJSON(@PathVariable("id") Integer id){
	    return produtoDAO.find(id);
	}
}
