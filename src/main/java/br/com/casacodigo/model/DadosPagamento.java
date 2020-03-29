package br.com.casacodigo.model;

import java.math.BigDecimal;

public class DadosPagamento {

	/*
	 * Observe que o nome do atributo é o mesmo que o sistema de pagamentos espera
	 * receber. Isto é necessário pois o Spring irá transformar o objeto desta
	 * classe em um objeto JSON. Por padrão ele irá criar a chave com o nome do
	 * atributo da classe e o valor será o mesmo do definido no atributo.
	 * 
	 * {"value" : 120}
	 */
	private BigDecimal value;

	public DadosPagamento(BigDecimal value) {
		this.value = value;
	}

	public DadosPagamento() {
	}

	public BigDecimal getValue() {
		return value;
	}
}
