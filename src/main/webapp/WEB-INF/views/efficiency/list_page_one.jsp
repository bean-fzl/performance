<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="org.apache.shiro.web.filter.authc.FormAuthenticationFilter"%>
<%@ page import="org.apache.shiro.authc.ExcessiveAttemptsException"%>
<%@ page import="org.apache.shiro.authc.IncorrectCredentialsException"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

	
	计划时间：<br/>
	<form id="queryForm" >
	<input type = "hidden" name= "projectId" value="${project.id}"/>
	
	</form>
	
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
		   <tr>
		   		<td>序号</td>
			    <td>任务名</td>
		   		<td>姓名</td>
		   		<td>部门</td>
			    <td>分值</td>
			    <td>权重(%)</td>
		   		<td>计划时长</td>
		   </tr>
		 </thead>
		<tbody id="tbody">
		</tbody>
		 <%--   <c:forEach items="${list}" var="listTemp" varStatus="s" >
				<tr>
			   		<td>${listTemp.employeName}</td>
			   		<td>${listTemp.departmentName}</td>
			   		<td>周期：${listTemp.planBeginTime}至${listTemp.planEndTime}；工时：${listTemp.planHours}小时</td>
		   		</tr>
		   </c:forEach> --%>
	
	</table>
	<div id="pager"></div>
	<script type="text/x-jquery-tmpl" id="tmplList">
		{{each(i,p) data.content}}
				<tr>
					<td>@{i+1}</td>
					<td>@{p.name}</td>
					<td>@{p.employeName}</td>
					<td>@{p.departmentName}</td>
					<td>@{p.score}</td>
					<td>@{p.weight}</td>
					<td>@{"周期："+formatDate(p.planBeginTime)+"至"+formatDate(p.planEndTime)+"；工时："+p.planHours}</td>
					
				</tr>
		{{/each}}
	</script>
	<script type="text/javascript">
		var pageSize = 10;
		$(function() {
			getData(1);
			//$("#queryForm").submit();
		})
		function formatDate(times) {
			var date = new Date(times);
			var fm = date.format('yyyy-MM-dd');
			return fm;
		}
		function getData(pageIndex) {
			var queryData = $("#queryForm").serialize();
			pageIndex = pageIndex - 1;
			queryData = queryData + "&pageIndex=" + pageIndex + "&pageSize="
					+ pageSize;
			$.ajax({
				type : "POST",
				url : "${ctx}/efficiency/efficiencyListAjax",
				data : queryData,
				success : function(data) {
					if (data == null || data.records == 0) {
						$("#tbody").html("暂无记录");
					} else {
						$("#tbody").html($("#tmplList").tpl({
							data : data
						}));
					}
					//分页控件
					var pager = new pagination(function() {
						getData($(this).attr("data-index"));
					});
					pager.pageIndex = data.number + 1;//当前页码
					pager.pageSize = data.size;//页大小
					pager.totalCount = data.totalElements;//总条数
					pager.totalPage = data.totalPages;//总页数
					$("#pager").html(pager.creat());
				}
			});
		};
	</script>
	
	<script type="text/javascript">
		
	</script>
