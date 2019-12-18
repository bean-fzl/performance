<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>添加部门</title>
<style>
	td{
		text-align:center;
	}
</style>
</head>
<body>
	<table class="table table-striped table-bordered table-condensed">
			<tr>
				<td>部门编码：</td>
				<td>${department.code }</td>
			</tr>
			<tr>
				<td>部门名称：</td>
				<td>${department.name }</td>
			</tr>
			<tr>
				<td>部门id：</td>
				<td>${department.id }</td>
			</tr>
			<tr>
				<td>部门人员：</td>
				<td>
					<c:forEach var="list" items="${department.es }">
						<c:if test="${list.isDel == 0 && !list.isMt}">
								${list.name }
						</c:if>
					</c:forEach>
				</td>
			</tr>
			<tr>
				<td>MT负责人：</td>
				<td>${department.mt }</td>
			</tr>
		</table>
	</table>
</body>
</html>