<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="org.apache.shiro.web.filter.authc.FormAuthenticationFilter"%>
<%@ page import="org.apache.shiro.authc.ExcessiveAttemptsException"%>
<%@ page import="org.apache.shiro.authc.IncorrectCredentialsException"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
	<title>效能-项目页面</title>
</head>

<body>
	
	<form id="queryForm"   class="form-inline">
			<table >
				<tr>
					<td><label class="control-label">项目名称</label></td>
					<td><input type="text" name="name"></td>
					<td><label class="control-label">项目经理</label></td>
					<td><input type="text" name="pm"></td>
					<td><label class="control-label">项目ID</td>
					<td><input type="text" id =""name="id"></td>
					<td><label class="control-label">项目人员</label></td>
					<td><input type="text" name="projectPersonnel"></td> 
					
					<td><label class="control-label">项目状态</label></td>
					<td><select name="state">
							<option value="">全部</option>
							<option value="0">进行中</option>
							<option value="1">已完成</option>
						</select>
					</td>
					<td><label class="control-label">计划工时</label></td>
					<td><input type="text" id= "planHoursSearch" name="planHoursSearch"></td>
				</tr>
				<tr>
					<td><label class="control-label">实际工时</label></td>
					<td><input type="text" id= "actualHoursSearch" name="actualHoursSearch"></td>
					<td><label class="control-label">产出工时</label></td>
					<td><input type="text" id="outputHoursSearch" name="outputHoursSearch"></td>
					<td>
						<button class="btn" id="btnSearch" type="submit">搜索</button>
						<button class="btn" id="btnSearch" type="reset">清空</button>
					</td>
				</tr>
			</table> 
	</form>
	<div id="pager"></div>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
		   <tr>
		   		<td>序号</td>
		   		<td>项目ID</td>
		   		<td>项目名称</td>
		   		<td>项目人员</td>
		   		<td>计划工时</td>
		   		<td>实际工时</td>
		   		<td>产出工时</td>
		   		
		   		<td>项目经理</td>
		   		<td>项目状态</td>
		   		<td>操作</td>
		   </tr>
		 </thead>
		<tbody id="tbody">
		</tbody>
		 <%--  <c:forEach items="${list}" var="listTemp" varStatus="s" >
				<tr>
			   		<td>${listTemp.proId}</td>
			   		<td>${listTemp.name}</td>
			   		<td>${listTemp.projectPersonnel}</td>
			   		<td>${listTemp.planHoursAll}</td>
			   		
			   		<td>${listTemp.actualHoursAll}</td>
			   		<td>${listTemp.outputHoursAll}</td>
			   		<td>${listTemp.pm}</td>
			   		
			   		<td>${listTemp.state}</td>
			   		<td><a href="cheack?proId=${listTemp.proId}">查看</a></td>
		   		</tr>
		   </c:forEach>--%>
	
	</table>
	
	<script type="text/x-jquery-tmpl" id="tmplList">
		{{each(i,p) data.content}}
				<tr>
					<td>@{i+1}</td>
					<td>@{p.proId}</td>
					<td>@{p.name}</td>
					<td>@{p.projectPersonnel}</td>

					<td>@{p.planHoursAll}</td>
					<td>@{p.actualHoursAll}</td>
					<td>@{p.outputHoursAll}</td>

					<td>@{p.pm}</td>
					<td>@{formatState(p.state)}</td>
					<td><a href="${ctx}/admin/project/cheack?proId=@{p.proId}">查看</a></td>
					
				</tr>
		{{/each}}
	</script>
	<script type="text/javascript">
		var pageSize = 10;
		$(function() {
			validate();
			$("#queryForm").submit();
		})
		function formatDate(times) {
			var date = new Date(times);
			var fm = date.format('yyyy-MM-dd');
			return fm;
		}
		function formatState(state){
			if( null != state){
				if(0 == state){
					return "进行中";
				}else if(1 == state){
					return "已完成";
				}
			}else{
				return "-";
			}
		}
		function getData(pageIndex) {
			var queryData = $("#queryForm").serialize();
			pageIndex = pageIndex - 1;
			queryData = queryData + "&pageIndex=" + pageIndex + "&pageSize="
					+ pageSize;
			$.ajax({
				type : "POST",
				url : "${ctx}/admin/project/listAjax",
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
	function validate() {
		$("#queryForm").validate({
			rules : {
				id : {
					digits : true
				},
				planHoursSearch : {
					digits : true
				},
				actualHoursSearch : {
					digits : true
				},
				outputHoursSearch : {
					digits : true
				},
			//createTime:{date:true}
			},
			messages : {
				
			},
			submitHandler : function(form) {
				//alert("submitHandler:function");
				getData(1);
			}
		});
	}
	</script>
</body>
</html>
