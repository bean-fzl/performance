<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>查看</title>
<style type="text/css">
.btn_passwdReset{
	margin-left: 800px;
}
</style>
<script type="text/javascript">
	function firm(id){
		 if(confirm("密码重置后初始密码为邮箱前缀+123")){
			location.href = '${ctx}/admin/employe/passwdReset/'+id;
		} 
	}
</script>
</head>
<body>
	<form id="inputForm" action="" method="post" class="form-horizontal">
		<input type="button" value="密码重置" class="btn_passwdReset" onclick="firm(${employe.id })" />
		<fieldset>
			<div class="control-group">
				<label class="control-label">姓名:</label>
				<div class="controls">
					 ${employe.name} 
				</div>
			</div>	
			<div class="control-group">
				<label class="control-label">工号:</label>
				<div class="controls">
					 ${employe.code}
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">部门:</label>
				<div class="controls">
					 ${employe.departmentName}
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">是否为MT负责人:</label>
				<div class="controls">
					<c:choose>
	                      <c:when test="${employe.isMt==true }">是</c:when>
	                      <c:otherwise>否</c:otherwise>
	            	</c:choose>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">是否为项目经理:</label>
				<div class="controls">
					<c:choose>
	                      <c:when test="${employe.isPm==true }">是</c:when>
	                      <c:otherwise>否</c:otherwise>
	            	</c:choose>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">入职时间:</label>
				<div class="controls">
					 <fmt:formatDate value="${employe.employmentDate }" pattern="yyyy-MM-dd"/>
				</div>
			</div>
			<div class="form-actions">
				<input id="cancel_btn" class="btn" type="button" value="返回" onclick="history.back()"/>
			</div>
		</fieldset>
	</form>
</body>
</body>
</html>