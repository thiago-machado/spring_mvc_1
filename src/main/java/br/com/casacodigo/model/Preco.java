package br.com.casacodigo.model;

import java.math.BigDecimal;

import javax.persistence.Embeddable;

/**
 * @Embeddable: permite Preco ser persistida, desde que ela seja um atributo de
 *              uma entidade. No caso ela é um atributo da classe Produto, que
 *              é uma entidade.
 */
@Embeddable
public class Preco {

	private BigDecimal valor;
	private TipoPreco tipo;

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public TipoPreco getTipo() {
		return tipo;
	}

	public void setTipo(TipoPreco tipo) {
		this.tipo = tipo;
	}

}
