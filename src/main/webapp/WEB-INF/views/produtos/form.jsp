<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!-- Import da taglib -->
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="security" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Livros de Java, Android, iPhone, Ruby, PHP e muito mais -
	Casa do Código</title>
	
	<c:url value="/" var="contextPath" />
	<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css" integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh" crossorigin="anonymous">
	
</head>
<body>

	<nav class="navbar navbar-expand-lg navbar-light bg-light">
		<a class="navbar-brand" href="${contextPath}">Casa do Código</a>
		<div class="collapse navbar-collapse" id="navbarSupportedContent">
			<ul class="navbar-nav mr-auto">
				<li><a href="${contextPath}produtos" rel="nofollow">Listagem de Produtos</a></li>
				<li>&nbsp;|&nbsp;</li>
				<li><a href="${contextPath}produtos/form" rel="nofollow">Cadastro de Produtos</a></li>
				<li>&nbsp;|&nbsp;</li>
				<li class="nav-item"><a href="<c:url value="/logout" />">Sair</a></li>
			</ul>
			<!-- usando a taglib de seguranca para exibir o usuario logado -->
			<!-- "principal" é o nome padrão para definir um usuário logado -->
			<!-- "usuario" é o nome da variável -->
			<ul class="nav navbar-nav navbar-right">
  				<li><security:authentication property="principal" var="usuario"/>Usuário: <b>${usuario.username}</b></li>
			</ul>
		</div>
	</nav>

	<div class="container">
		<!-- PC = ProdutoControler, são as inciiais do nome da classe 
		PC#gravar = ProdutoControler.gravar();
		
		Para evitar de ficar mexendo na action do formulário, peça para o Spring preencher 
		a URL para você. Para isso, adicionar a lib ".../tags".
		-->
		<form:form servletRelativeAction="/produtos" method="post" commandName="produto" 
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