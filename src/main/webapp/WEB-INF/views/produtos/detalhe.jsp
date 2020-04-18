<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="security"%><!-- taglib para trabalhar com segurança -->
<%@ taglib tagdir="/WEB-INF/tags" prefix="tags"%><!-- nosso arquivo de template -->

<tags:template titulo="${ produto.titulo }">



	<article id="${ produto.id }">
		<header id="product-highlight" class="clearfix">
			<div id="product-overview" class="container">
				<img width="280px" height="395px"
					src="http://cdn.shopify.com/s/files/1/0155/7645/products/css-eficiente-featured_large.png?v=1435245145"
					class="product-featured-image" />
				<h1 class="product-title">${ produto.titulo }</h1>
				<p class="product-author">
					<span class="product-author-link"> </span>
				</p>

				<p class="book-description">${ produto.descricao }</p>
			</div>
		</header>


		<section class="buy-options clearfix">
			<form action='<c:url value="/carrinho/add" />' method="post"
				class="container">
				<input type="hidden" name="produtoId" value="${ produto.id }" />
				<ul id="variants" class="clearfix">
					<c:forEach items="${ produto.precos }" var="preco">
						<li class="buy-option"><input type="radio" name="tipo"
							class="variant-radio" id="tipo" value="${ preco.tipo }"
							checked="checked" /> <label class="variant-label">${ preco.tipo }</label>
							<small class="compare-at-price">R$ 39,90</small>
							<p class="variant-price">${ preco.valor }</p></li>
					</c:forEach>
				</ul>
				<button type="submit" class="submit-image icon-basket-alt"
					alt="Compre Agora" title="Compre Agora"></button>

				<!-- CSRF é uma sigla que representa a frase Cross Site Request Forgery, uma técnica de ataque 
				a sites muito comum na web. A técnica representa o cenário de que um outro site está enviando 
				dados para nossa aplicação, em vez do usuário diretamente. Geralmente, acontece com páginas 
				clonadas, uma página falsa é apresentada ao usuário e este sem saber submete seus dados que 
				por sua vez podem ser enviados ao servidor original ou podem ser feitas cópias em um servidor 
				falso para posteriormente ter o uso indevido. -->

				<!-- A solução mais simples para esse caso é criar o input hidden abaixo. -->

				<!-- Outra forma é criar a tag form:form, como na página de cadastro de produtos.
				Criar essa tag não necessitará criar o input hiden abaixo, pois o Spring criará o 
				input de forma automática. -->
				<input type="hidden" name="${_csrf.parameterName }"
					value="${_csrf.token }" />
			</form>

		</section>

		<div class="container">
			<section class="summary">
				<ul>
					<li><h3>
							E muito mais... <a href='/pages/sumario-java8'>veja o sumário</a>.
						</h3></li>
				</ul>
			</section>

			<section class="data product-detail">
				<h2 class="section-title">Dados do livro:</h2>
				<p>
					Número de páginas: <span>${ produto.paginas }</span>
				</p>
				<p>
					Data de publicação:
					<fmt:formatDate pattern="dd/MM/yyyy"
						value="${ produto.dataLancamento.time }" />
				</p>
				<p>
					Encontrou um erro? <a href='/submissao-errata' target='_blank'>Submeta
						uma errata</a>
				</p>
			</section>
		</div>

	</article>
</tags:template>