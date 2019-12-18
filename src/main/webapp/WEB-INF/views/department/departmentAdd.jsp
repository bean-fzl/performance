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
<script type="text/javascript">
	function selectDeptType(){
		var sel =document.getElementById("seleteDeptType");
		var sel_value=sel.options[sel.selectedIndex].value;
		document.getElementById("type").value=sel_value;
	}
</script>
</head>
<body>
	<form action="add" method="post">
		<table class="table table-striped table-bordered table-condensed">
			<tr>
				<td>部门编号：</td>
				<td><input type="text" name="code" /></td>
			</tr>
			<tr>
				<td>部门名称：</td>
				<td><input type="text" name="name" /></td>
			</tr>
			<!-- <tr>
				<td>部门负责人：</td>
				<td><input type="text" name="mt" /></td>
			</tr> -->
			<tr>
				<td>部门类型：</td>
				<td><input type="hidden" name="type" id="type" value="1">
					<select id="seleteDeptType" onclick="selectDeptType()">
						<option value="1" selected="selected">行政部</option>
						<option value="2" >政治部</option>
						<option value="3" >财务部</option>
						<option value="4" >人事部</option>
						<option value="5" >安全部</option>
						<option value="6" >技术部</option>
					</select>
				</td>
			</tr>
		</table>
		<div style="padding-left: 600px;">
			<br>
			<button class="btn">提交</button>
			<button class="btn" type="button"  onclick="history.back()">返回</button>
		</div>
	</form>
</body>
</html>