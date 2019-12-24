<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<nav class="navbar navbar-default">
	<div id="header" class="container-fluid">
		<!-- Brand and toggle get grouped for better mobile display -->
		<div class="navbar-header">
			<button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1" aria-expanded="false">
				<span class="sr-only">Toggle navigation</span>
				<span class="icon-bar"></span>
				<span class="icon-bar"></span>
				<span class="icon-bar"></span>
			</button>
			<a href="${ctx}/login/success" class="navbar-brand" href="#">绩效管理系统</a>
		</div>
		<shiro:user>
		<!-- Collect the nav links, forms, and other content for toggling -->
		<div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
			<shiro:hasRole name="hr">
			<ul class="nav navbar-nav">
				<li class="dropdown">
					<a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">效能管理 <span class="caret"></span></a>
					<ul class="dropdown-menu">
						<li><a href="javascript:void(0);" onclick="js_method_fun('/admin/efficiencyResult/list',this)">月度效能</a></li>
						<li><a href="javascript:void(0);" onclick="js_method_fun('/admin/efficiency/list',this)">员工效能</a></li>
						<li><a href="javascript:void(0);" onclick="js_method_fun('/admin/project/list',this)">项目效能</a></li>
					</ul>
				</li>
				<li><a href="javascript:void(0);" onclick="js_method_fun('/admin/project/projectList',this)">项目管理</a></li>
				<li><a href="javascript:void(0);" onclick="js_method_fun('/admin/department/list',this)">部门管理</a></li>
				<li><a href="javascript:void(0);" onclick="js_method_fun('/admin/employe/list',this)">员工管理</a></li>
			</ul>
			</shiro:hasRole>
			<ul class="nav navbar-nav navbar-right">
				<li class="dropdown">
					<a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false"><shiro:principal property="name" /> <span class="caret"></span></a>
					<ul class="dropdown-menu">
						<li><a href="${ctx}/logout">退出</a></li>
						<li><a href="${ctx }/profile">修改密码</a></li>
					</ul>
				</li>
			</ul>
		</div><!-- /.navbar-collapse -->
		</shiro:user>
	</div><!-- /.container-fluid -->
</nav>