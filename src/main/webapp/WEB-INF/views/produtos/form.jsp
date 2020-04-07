<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!-- Import da taglib -->
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Livros de Java, Android, iPhone, Ruby, PHP e muito mais -
	Casa do Código</title>
	
	<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css" integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh" crossorigin="anonymous">
	
</head>
<body>

	<nav class="navbar navbar-expand-lg navbar-light bg-light">
		<a class="navbar-brand" href="${s:mvcUrl('HC#index').build()}">Casa do Código</a>
		<button class="navbar-toggler" type="button" data-toggle="collapse"
			data-target="#navbarSupportedContent"
			aria-controls="navbarSupportedContent" aria-expanded="false"
			aria-label="Toggle navigation">
			<span class="navbar-toggler-icon"></span>
		</button>

		<div class="collapse navbar-collapse" id="navbarSupportedContent">
			<ul class="navbar-nav mr-auto">
				<li class="nav-item"><a class="nav-link" href="${s:mvcUrl('PC#listar').build()}">Lista de Produtos</a></li>
    			<li class="nav-item"><a class="nav-link" href="${s:mvcUrl('PC#form').build()}">Cadastro de Produtos</a></li>
			</ul>
		</div>
	</nav>

	<div class="container">
		<!-- PC = ProdutoControler, são as inciiais do nome da classe 
		PC#gravar = ProdutoControler.gravar();
		
		Para evitar de ficar mexendo na action do formulário, peça para o Spring preencher 
		a URL para você. Para isso, adicionar a lib ".../tags".
		-->
		<form:form action="${s:mvcUrl('PC#gravar').build()}" method="post" commandName="produto" 
			enctype="multipart/form-data">
			<div class="form-group">
				<label>Título</label> 
				<!--  form:errors path="produto.titulo" / -->
				<!-- Agora usados form:input para que o Spring possa saber que queremos preencher os inputs com valores caso quisermos.
				Isso é muito útil em caso de erro no POST. -->
				<form:errors path="titulo" />
				<form:input path="titulo" cssClass="form-control" />
			</div>
			<div class="form-group">
				<label>Descrição</label>
				<form:errors path="descricao" />
				<form:textarea rows="10" cols="20" path="descricao" cssClass="form-control" />
			</div>
			<div class="form-group">
				<label>Páginas</label> 
				<form:errors path="paginas" />
				<form:input path="paginas" cssClass="form-control" />
			</div>
	
			<div class="form-group">
			    <label>Data de Lançamento</label>
			    <form:errors path="dataLancamento" />
			    <form:input path="dataLancamento" cssClass="form-control" />
			</div>
			
			<c:forEach items="${tipos}" var="tipoPreco" varStatus="status">
				<div class="form-group">
					<label>${tipoPreco}</label> 
					<form:input path="precos[${status.index}].valor" cssClass="form-control" /> 
	            	<form:hidden path="precos[${status.index}].tipo" value="${tipoPreco}" />
				</div>
			</c:forEach>
			
			<div class="form-group">
			    <label>Sumário</label>
			    <input name="sumario" type="file" class="form-control" />
			</div>
	
			<button type="submit" class="btn btn-primary">Cadastrar</button>
		</form:form>
	</div>

</body>
</html>