<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>部门列表</title>
</head>
<body>
	<form action="list" method="post" class="form-inline">
		<table>
			<tr>
				<td><label class="control-label">部门名称</label></td>
				<td><input type="text" name="departmentName"
					value="${searchParames.departmentName }"></input></td>
				<td><label class="control-label">MT负责人</label></td>
				<td><input type="text" name="mt" value="${searchParames.mt }" /></td>
			</tr>
			<tr>
				<td><label class="control-label">部门人员</label></td>
				<td><input type="text" name="eployeName"
					value="${searchParames.eployeName }" /></td>
			</tr>
		</table>
		<button class="btn" type="submit">搜索</button>
		<button class="btn" type="button" onclick="resetSearch();" /> 清空 </button>
		<button class="btn" type="button" onclick="location.href='addPage';">添加部门</button>
	</form>
	<table class="table table-striped table-bordered table-condensed">
		<tr>
			<td>序号</td>
			<td>部门编码</td>
			<td>部门名称</td>
			<td>部门人员名单</td>
			<td>部门负责人</td>
			<td>操作</td>
		</tr>
		<c:forEach var="list" items="${departmentList }" varStatus="status">
			<tr>
				<td>${status.index+1 }</td>
				<td>${list.code }</td>
				<td>${list.name }</td>
				<td><c:forEach var="employees" items="${list.es }"
						varStatus="status">
						
						<c:if test="${employees.isDel == 0 && !employees.isMt}">
								${employees.name }
						</c:if>
					</c:forEach></td>
				<td>${list.mt }</td>
				<td>
					<a href="viewPage/${list.id }">查看</a>
					<a href="editPage/${list.id }">编辑</a>
					<a href="delete/${list.id }">删除</a>
				</td>
			</tr>
		</c:forEach>
	</table>
	<script>
		function resetSearch() {
			$("input").val('');
		}
	</script>
</body>
</html>