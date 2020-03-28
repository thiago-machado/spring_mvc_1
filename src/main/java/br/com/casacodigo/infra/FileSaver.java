package br.com.casacodigo.infra;

import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

/**
 * Nós precisamos que essa classe seja reconhecida pelo Spring para que ele
 * consiga fazer os injects corretamente. Está classe é importante e ela
 * representa um componente em nosso sistema. Teremos então que usar a
 * anotação @Component.
 * 
 * Nesta classe criaremos um método chamado write que fará a transferência do
 * arquivo e retornará o caminho onde o arquivo foi salvo. Este método então
 * precisara de duas informações, o local onde o arquivo será salvo e o arquivo
 * em si. O local será recebido como String e o arquivo como um objeto
 * MultipartFile. Os quais chamaremos de baseFolder e file respectivamente.
 * 
 * Com o baseFolder e o file em mãos, conseguiremos facilmente montar uma String
 * que indique o caminho do arquivo a ser salvo. Com esta String construída,
 * criaremos um novo objeto do tipo File que irá representar o arquivo a ser
 * gravado no servidor. Este último objeto será passado para o método transferTo
 * que será o método responsável por transferir o arquivo para o servidor.
 * 
 * Note que a String path monta o caminho do arquivo. O file.transferTO() faz a
 * transferência do arquivo e o objeto File representa um o arquivo no servidor.
 * O bloco try/catch foi adicionado por causa que operações I/O, ou seja, de
 * entrada e saída, que podem gerar erros. Perceba também que estamos retornando
 * a String path dentro do bloco try.
 * 
 * Apesar deste código parecer claro, não podemos definir com certeza o caminho
 * final do arquivo, o caminho absoluto que ele vai ter ao ser enviado. Podemos
 * mudar isto detectando o caminho atual que o usuário está em nosso sistema e
 * fazer o upload do arquivo baseado neste caminho. Para isso precisamos dos
 * dados da requisição, pois com ela sabemos onde o usuário está em nosso
 * sistema.
 * 
 * Pensando nisso, criaremos um atributo do tipo HttpServletRequest na classe
 * FileSaver, chamaremos este de request e o marcaremos com a
 * anotação @Autowired para que o Spring faça o inject desse atributo. A partir
 * deste objeto, conseguimos extrair o contexto atual em que o usuário se
 * encontra e então conseguir o caminho absoluto desse diretório em nosso
 * servidor.
 * 
 * O caminho do arquivo agora é diferente do que fizemos antes, ele não é mais
 * uma simples junção do baseFolder com o nome do arquivo. Este caminho agora
 * precisa ser concatenado com o caminho absoluto que acabamos de implementar
 * através do request. Sendo assim, guardaremos o retorno do
 * request.getServletContext().getRealPath("/"+baseFolder); em uma nova String
 * que chamaremos de realPath e usaremos esta String para concatenar ao path do
 * arquivo que geramos anteriormente.
 * 
 * A classe FileSaver está pronta. Ela recebe um arquivo e o nome de uma pasta,
 * transfere o arquivo enviado pelo formulário para a pasta e retorna o caminho
 * onde o arquivo foi salvo.
 */
@Component
public class FileSaver {

	@Autowired
	private HttpServletRequest request;

	/*
	 * 
	 */
	public String write(String baseFolder, MultipartFile file) {
		try {
			
			String realPath = request.getServletContext().getRealPath("/" + baseFolder);
			String path = realPath + "/" + file.getOriginalFilename();
			file.transferTo(new File(path));
			
			return baseFolder + "/" + file.getOriginalFilename();

		} catch (IllegalStateException | IOException e) {
			throw new RuntimeException(e);
		}
	}
}
