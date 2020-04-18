<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!-- Import da taglib -->
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="security" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Livros de Java, Android, iPhone, Ruby, PHP e muito mais -
	Casa do Código</title>
	
<c:url value="/" var="contextPath" />
<link rel="stylesheet"
	href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css"
	integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh"
	crossorigin="anonymous">
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
		<h1>Lista de Produtos</h1>
		<h3>${sucesso}</h3>
		<h3>${falha}</h3>
		<table class="table table-bordered table-striped table-hover">
			<tr>
				<td>Título</td>
				<td>Descrição</td>
				<td>Páginas</td>
			</tr>

			<c:forEach items="${produtos}" var="produto">
				<tr>
					<!-- Passaremos o id para o mvcUrl através do método arg que recebe dois parâmetros: 
				1) o primeiro será a posição do parametro, que terá valor 0;
				2) o segundo será o valor do parâmetro. -->
					<td><a
						href="${contextPath}produtos/detalhe/${produto.id}">${produto.titulo}</a></td>
					<td>${produto.descricao}</td>
					<td>${produto.paginas}</td>
				</tr>
			</c:forEach>
		</table>
	</div>

</body>
</html>