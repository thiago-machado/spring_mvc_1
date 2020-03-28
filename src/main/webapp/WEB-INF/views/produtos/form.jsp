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
</head>
<body>

	<!-- PC = ProdutoControler, são as inciiais do nome da classe 
	PC#gravar = ProdutoControler.gravar();
	
	Para evitar de ficar mexendo na action do formulário, peça para o Spring preencher 
	a URL para você. Para isso, adicionar a lib ".../tags".
	-->
	<form:form action="${s:mvcUrl('PC#gravar').build()}" method="post" commandName="produto" 
		enctype="multipart/form-data">
		<div>
			<label>Título</label> 
			<!--  form:errors path="produto.titulo" / -->
			<!-- Agora usados form:input para que o Spring possa saber que queremos preencher os inputs com valores caso quisermos.
			Isso é muito útil em caso de erro no POST. -->
			<form:errors path="titulo" />
			<form:input path="titulo" />
		</div>
		<div>
			<label>Descrição</label>
			<form:errors path="descricao" />
			<form:textarea rows="10" cols="20" path="descricao" />
		</div>
		<div>
			<label>Páginas</label> 
			<form:errors path="paginas" />
			<form:input path="paginas" />
		</div>

		<div>
		    <label>Data de Lançamento</label>
		    <form:errors path="dataLancamento" />
		    <form:input path="dataLancamento" />
		</div>
		
		<c:forEach items="${tipos}" var="tipoPreco" varStatus="status">
			<div>
				<label>${tipoPreco}</label> 
				<form:input path="precos[${status.index}].valor" /> 
            	<form:hidden path="precos[${status.index}].tipo" value="${tipoPreco}" />
			</div>
		</c:forEach>
		
		<div>
		    <label>Sumário</label>
		    <input name="sumario" type="file" />
		</div>

		<button type="submit">Cadastrar</button>
	</form:form>

</body>
</html>