<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/security/tags"
	prefix="security"%>
<!-- taglib para trabalhar com segurança -->
<%@ taglib tagdir="/WEB-INF/tags" prefix="tags"%>
<!-- nosso arquivo de template -->


<!-- tag body do nosso arquivo de template -->
<!-- jsp:attribute deve vir antes de jsp:body -->
<!-- comjsp:attribute, nem comentários podem ser inseridos entre a tag tags:template e jsp:attribute -->
<tags:template titulo="Livros de Java, Android, IOs, Mobile e muito mais...">

	<jsp:attribute name="extraScripts">
        <script type="text/javascript">
        	console.log("Teste de script para uma página específica. Notar que agora somos forçados a inserir a tag jsp:body dentro de tags:template");
        </script>
    </jsp:attribute>

	<jsp:body>
    
		<section id="index-section" class="container middle">
			<h1 class="cdc-call">Últimos dias com os preços promocionais.
				Aproveite!</h1>
	
			<ul class="clearfix book-collection">
	
				<c:forEach items="${produtos}" var="produto">
					<li><a href="${contextPath}produtos/detalhe/${produto.id}"
							class="block clearfix">
							<h2 class="product-title">${produto.titulo}</h2> <img width="143"
								height="202"
								src="https://cdn.shopify.com/s/files/1/0155/7645/products/java8-featured_large.png?v=1411490181"
								alt="Java 8 Prático" title="Java 8 Prático" /> <small
								class="buy-button">Compre</small>
					</a></li>
				</c:forEach>
	
			</ul>
	
			<h2 class="cdc-call">Diferenciais da Casa do Código</h2>
	
			<ul id="cdc-diferenciais" class="clearfix">
				<li class="col-left">
					<h3>E-books sem DRM. Leia onde quiser</h3>
					<p>
						<span class="sprite" id="sprite-drm"></span> Nossos e-books não
						possuem DRM, ou seja, você pode ler em qualquer computador, tablet
						e smartphone.
					</p>
				</li>
				<li class="col-right">
					<h3>Autores de renome na comunidade</h3>
					<p>
						<span class="sprite" id="sprite-renome"></span> Autores que
						participam ativamente na comunidade com Open Source, listas de
						discussão, grupos e mais.
					</p>
				</li>
				<li class="col-left">
					<h3>Receba atualizações dos e-books</h3>
					<p>
						<span class="sprite" id="sprite-atualizacoes"></span> Quando você
						compra um e-book, automaticamente tem direito às atualizações e
						correções dele.
					</p>
				</li>
				<li class="col-right">
					<h3>Livros com curadoria da Caelum</h3>
					<p>
						<span class="sprite" id="sprite-caelum"></span> Desenvolvedores
						experientes que avaliam e revisam os livros constantemente.
					</p>
				</li>
			</ul>
		</section>
	</jsp:body>
</tags:template>