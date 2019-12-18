<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="org.apache.shiro.web.filter.authc.FormAuthenticationFilter"%>
<%@ page import="org.apache.shiro.authc.ExcessiveAttemptsException"%>
<%@ page import="org.apache.shiro.authc.IncorrectCredentialsException"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
	<title>效能-项目页面</title>
	<!-- 时间控件 css  -->
	<link rel="stylesheet" type="text/css" href="${ctx}/static/My97/skin/WdatePicker.css" />
	<!-- 时间控件js  -->
	<script type="text/javascript" src="${ctx}/static/My97/WdatePicker.js"></script>
</head>

<body>
	<form id="queryForm"  class="form-inline">
			<table id="t1">
				<tr>
					<td><label class="control-label">姓名</label></td>
					<td><input type="text" id = "employeName" name="employeName"></td>
					<td><label class="control-label">项目经理</label></td>
					<td><input type="text" id ="pm" name="pm"></td>
					<!-- <td><label class="control-label">工号</label></td>
					<td><input type="text" id="employeCode" name="employeCode"></td> -->
					<td><label class="control-label">项目</label></td>
					<td><input type="text" id = "projectName" name="projectName"></td>
				</tr>
				<tr>
					<td><label class="control-label">计划工时</label></td>
					<td><input type="text" id = "planHours" name="planHours"></td>
					<td><label class="control-label">项目状态</label>
					<td><select name="projectState">
							<option value="">全部</option>
							<option value="0">进行中</option>
							<option value="1">已完成</option>
						</select>
					</td>
					<td><label class="control-label">周期</label></td>
					<td><input class="Wdate" id="first" type="text" onfocus="new WdatePicker({lang:'zh-cn',dateFmt:'yyyy-MM-dd'})" id="planBeginTime" name="planBeginTime"/>
								~
						<input class="Wdate" id="last" type="text" onfocus="new WdatePicker({lang:'zh-cn',dateFmt:'yyyy-MM-dd'})" id = "planEndTime"name="planEndTime"/>
					</td>
					
				</tr>
				<tr>
					<td><label class="control-label">实际工时</label></td>
					<td><input type="text" id = "actualHours" name="actualHours"></td>
					<td><label class="control-label">产出工时</label></td>
					<td><input type="text" id = "outputHours" name="outputHours"></td>
				</tr>
			</table>
			<button class="btn" id="btnSearch" type="submit">搜索</button>
			<button class="btn" id="clear" type="reset">清空</button>
			<button id="btnExport" type="button" class="btn">导出</button>
	</form>
	<div id="pager"></div>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
		   <tr>
		   		<td>序号</td>
		   		<td>姓名</td>
		   		<!-- <td>工号</td> -->
		   		<td>项目</td>
		   		<td>周期</td>
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
		 <%--   <c:forEach items="${list.content}" var="listTemp" varStatus="s" >
				<tr>
			   		<td>${listTemp.employeName}</td>
			   		<td>${listTemp.employeCode}</td>
			   		
			   		<td>${listTemp.projectName}</td>
			   		<td>${listTemp.planBeginTime}—${listTemp.planEndTime}</td>
			   		
			   		<td>${listTemp.planHours}</td>
			   		<td>${listTemp.actualHours}</td>
			   		<td>${listTemp.outputHours}</td>
			   		
			   		<td>${listTemp.pm}</td>
			   		<td></td>
			   		
			   		<td><a href="${ctx}/admin/efficiency/cheack?id=${listTemp.id}">查看</a></td>
		   		</tr>
		   </c:forEach> --%>
	
	</table>
	
	<script type="text/x-jquery-tmpl" id="tmplList">
		{{each(i,p) data.content}}
				<tr>
					<td>@{i+1}</td>
					<td>@{p.employeName}</td>
					
					<td>@{p.projectName}</td>

					<td>@{formatDate(p.planBeginTime)+"--"+formatDate(p.planEndTime)}</td>

					<td>@{p.planHours}</td>
					<td>@{p.actualHours}</td>
					<td>@{p.outputHours}</td>

					<td>@{p.pm}</td>
					<td>@{formatState(p.projectState)}</td>
					<td><a href="${ctx}/admin/efficiency/cheack?id=@{p.id}">查看</a></td>
					
				</tr>
		{{/each}}
	</script>
	<script type="text/javascript">
		var pageSize = 10;
		$(function() {
			validate();
			$("#queryForm").submit();
			$("#btnExport").click(function() {
				$("#bar").css("display", "block");
				var queryData = $("#queryForm").serialize();
				$.ajax({
					type : "POST",
					url : "${ctx}/admin/performanceEvaluation/export",
					data : queryData,
					success : function(data) {
						$("#bar").css("display", "none");
						var url = "${ctx}/admin/performanceEvaluation/download?fileName="+ data;
						window.open(url);
					}
				});
			})
		});




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
				url : "${ctx}/admin/efficiency/listAjax",
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
				planHours : {
					digits : true
				},
				actualHours : {
					digits : true
				},
				outputHours : {
					digits : true
				},
				planEndTime : {
					date : true
				},
				planBeginTime : {
					date : true
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
