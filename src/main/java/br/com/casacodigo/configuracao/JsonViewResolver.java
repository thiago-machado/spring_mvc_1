package br.com.casacodigo.configuracao;

import java.util.Locale;

import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

public class JsonViewResolver implements ViewResolver {

	/*
	 * Precisamos fazer com que este método consiga retornar como view o JSON que
	 * representa nossos produtos. O próprio Spring tem uma classe que é proveniente
	 * da integração com a biblioteca Jackson e se chama MappingJackson2JsonView e
	 * tudo que precisamos fazer é retornar um objeto desta classe
	 */
	@Override
	public View resolveViewName(String viewName, Locale locale) throws Exception {
		MappingJackson2JsonView jsonView = new MappingJackson2JsonView();

		/*
		 * O trecho jsonView.setPrettyPrint(true) foi adicionado para que o Jackson
		 * mantenha uma formatação amigável ao retornar o JSON dos nossos produtos.
		 * Pronto!
		 */
		jsonView.setPrettyPrint(true);
		return jsonView;
	}

}
