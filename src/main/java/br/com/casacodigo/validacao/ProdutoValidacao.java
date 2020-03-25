package br.com.casacodigo.validacao;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import br.com.casacodigo.model.Produto;

public class ProdutoValidacao implements Validator {

	/*
	 * O método supports também precisa ser implementado. A implementação desse
	 * método indica a qual classe a validação dará suporte. Sabemos que será a
	 * classe Produto.
	 * 
	 * O que esse código faz é verificar se o objeto recebido para a validação tem
	 * uma assinatura da classe Produto. Com isso o Spring pode verificar se o
	 * objeto é uma instância daquela classe ou não.
	 */
	@Override
	public boolean supports(Class<?> clazz) {
		return Produto.class.isAssignableFrom(clazz);
	}

	/*
	 * O Spring tem uma classe chamada ValidationUtils, com alguns métodos que
	 * validam dados.
	 * 
	 * 
	 * Dentre os métodos disponíveis em ValidationUtils, teremos um que se encaixa
	 * exatamente com nosso caso, que é o rejectIfEmpty, que traduzido para
	 * português significa "rejeite se for vazio". É exatamente o que queremos. Este
	 * método recebe três parâmetros: Um objeto errors que contém os erros da
	 * validação; O nome do campo que iremos validar passado como String; e um
	 * errorCode que também é passado como String, mas que é reconhecido pelo
	 * Spring. Neste último parâmetro, usaremos o errorCode com o valor
	 * "field.required" para informar ao Spring que aquele campo é obrigatório.
	 * 
	 * 
	 * Note que o objeto errors não é gerenciado por nós, mas sim pelo Spring. O
	 * objeto errors terá as informações se a validação falhou ou não.
	 */
	@Override
	public void validate(Object target, Errors errors) {
		
		ValidationUtils.rejectIfEmpty(errors, "titulo", "field.required");
		ValidationUtils.rejectIfEmpty(errors, "descricao", "field.required");

		Produto produto = (Produto) target;
		if (produto.getPaginas() <= 0) {
			ValidationUtils.rejectIfEmpty(errors, "paginas", "field.required");
		}

	}
}
