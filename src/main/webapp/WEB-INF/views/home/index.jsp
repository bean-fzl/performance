<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>主页</title>
</head>
<body>
	<script type="text/javascript">
		$(document).ready(function(){
			<shiro:hasAnyRoles name="employe,pm">
			//window.location.href="${ctx }/home/myhome";
			window.location.href="${ctx }/project/findAll";
			</shiro:hasAnyRoles>
		});
	</script>
</body>
</html>