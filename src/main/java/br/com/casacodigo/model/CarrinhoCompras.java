package br.com.casacodigo.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

/**
 * Escopo padrão: SCOPE_APPLICATION
 * 
 * Quando se faz necessário que um recurso seja individual, ou seja, único para
 * cada usuário, definimos os recursos com o escopo de sessão. Este é criado a
 * partir do momento em que o usuário entra em uma determinada aplicação e segue
 * até o encerramento da mesma - ou ao fechar do navegador em alguns casos.
 */
@Component
@Scope(value = WebApplicationContext.SCOPE_SESSION)
public class CarrinhoCompras implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Map<CarrinhoItem, Integer> itens = new LinkedHashMap<CarrinhoItem, Integer>();

	public Collection<CarrinhoItem> getItens() {
	    return itens.keySet();
	}
	
	public void add(CarrinhoItem item) {
		itens.put(item, getQuantidade(item) + 1);
	}

	public int getQuantidade(CarrinhoItem item) {
		if (!itens.containsKey(item)) {
			itens.put(item, 0);
		}
		return itens.get(item);
	}

	/*
	 * Este código percorre toda a lista de itens e soma o valor de cada item a um
	 * aculumador.
	 */
	public int getQuantidade() {
		return itens.values().stream().reduce(0, (proximo, acumulador) -> (proximo + acumulador));
	}
	
	/*
	 * Total por item
	 */
	public BigDecimal getTotal(CarrinhoItem item){
	    return item.getTotal(getQuantidade(item));
	}
	
	/*
	 * Total geral
	 */
	public BigDecimal getTotal(){
	    BigDecimal total = BigDecimal.ZERO;
	    for (CarrinhoItem item : itens.keySet()) {
	        total = total.add(getTotal(item));
	    }
	    return total;
	}
}