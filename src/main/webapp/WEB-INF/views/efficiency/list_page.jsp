<%@ page contentType="text/html;charset=UTF-8"%>
<%@ page
	import="org.apache.shiro.web.filter.authc.FormAuthenticationFilter"%>
<%@ page import="org.apache.shiro.authc.ExcessiveAttemptsException"%>
<%@ page import="org.apache.shiro.authc.IncorrectCredentialsException"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<html>
<head>
<title>效能-项目页面</title>
<style type="text/css">
    html{overflow:hidden;}//隐藏整个页面的滚动条；
</style>
<script type="text/javascript">
		function js_method_fun(eff_id,t_id){
			var effId = eff_id;
			var tId = t_id;
			var a = "${ctx}/efficiency/list?proId="+effId;
			 $("#iframe_id").attr("src",a);
		}
		function js_add_fun(){
			var a = "${ctx}/efficiency/addPage";
			 $("#iframe_id").attr("src",a);
		}
		$(function(){
			windowHeig($("#content"));
		})
		function windowHeig(elem1){
			var h = elem1.height();
			if(h<500){
				$("#iframe_id",window.parent.document).css("height",Number(600)+"px");
			}else{
				$("#iframe_id",window.parent.document).css("height",Number(elem1.height()+100)+"px");
			}
			/* $("#iframe_id",window.parent.document).css("width",Number(elem1.height())+"px"); */
		};
	</script>
</head>

<body>
	<%-- <c:forEach items="${projectList}" var="listTemp" varStatus="s" >
			<a href="${ctx}/efficiency/list?proId=${listTemp.id}">${listTemp.name}</a>
			<br/>
		</c:forEach> --%>
	<div class="container-fluid ">
		<div class="row-fluid">
			<div class="col-lg-2 col-md-4 col-sm-4">
				<ul class="nav nav-tabs nav-stacked">
					<li class="nav-header"><h4>项目列表</h4></li>
					<c:forEach items="${projectList}" var="listTemp" varStatus="s">
						<%-- <a href="${ctx}/efficiency/list?proId=${listTemp.id}"></a> --%>
						<li><a href="javascript:void(0);" id="t_${s.index}" onclick="js_method_fun(${listTemp.id},this.id)">${listTemp.name}<span style="float: right;">${listTemp.taskOver}/${listTemp.taskAll}</span></a>
						</li>
					</c:forEach>
				</ul>
			</div>
			<div class="col-lg-10  col-md-8 col-sm-8">
				<div class="container-fluid ">
					<div class="row-fluid">
						<shiro:hasAnyRoles name="pm">
							<%-- <a href="${ctx}/efficiency/addPage"></a>	 --%>
							<a href="javascript:void(0);"  onclick="js_add_fun()">新建项目</a>
							<br/>
						</shiro:hasAnyRoles>
						<iframe id="iframe_id" src="" style="border: 0px;width: 100%;"> </iframe>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>

</html>
