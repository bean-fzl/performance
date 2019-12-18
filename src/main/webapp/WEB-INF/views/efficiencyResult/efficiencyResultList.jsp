<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<html>
<head>
<!-- 时间控件 css  -->
<link rel="stylesheet" type="text/css" href="${ctx}/static/My97/skin/WdatePicker.css" />
<!-- 时间控件js  -->
<script src="${ctx }/static/My97/WdatePicker.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>查看员工月度效能</title>
<script type="text/javascript">
	function f(){
		var  myselect=document.getElementById("selectDept");
		var index=myselect.selectedIndex ; 
		var  id=myselect.options[index].value;
		var text=myselect.options[index].text;
		 
		document.getElementById("departmentName").value=text;
		
	}
</script>
</head>
<body>
	<div id="search">
	<form id="form1" class="form-inline">
		<button id="btnExport" type="button" class="btn">导出</button>
		</br>
		</form>
	</div>
	<div id=data>
	<table class="table table-striped table-bordered table-condensed">
		<thead>
		<tr>
			<!-- <td>工号</td> -->
			<td>月份</td>
			<td>项目数</td>
			<td>参与员工数</td>
			<td>参与部门数</td>
			<td>计划总工时</td>
			<td>实际总工时</td>
			<td>产出总工时</td>
		</tr>
		</thead>
		<thead>
			<tbody id="tbody"></tbody>
		</thead>
		<div id="pager"></div>
		<script type="text/x-jquery-tmpl" id="tmplList">
			{{each(i,p) data}}
			<tr>
				<td>@{p.month}</td>
				<td>@{p.projectCount}</td>
				<td>@{p.employeCount}</td>
				<td>@{p.departmentCount}</td>
				<td>@{p.planHourCount}</td>
				<td>@{p.actualHourCount}</td>
				<td>@{p.outputHourCount}</td>
			<tr>
			{{/each}}
		</script>
	</table>
	<script>
	var pageSize= 10;
	$(function(){
		validate();
		$("#form1").submit();
		$("#btnExport").click(function() {
			$("#bar").css("display", "block");
			$.ajax({
				type : "POST",
				url : "${ctx}/admin/performanceEvaluation/exportEfficiencyMonth",
				success : function(data) {
					$("#bar").css("display", "none");
					var url = "${ctx}/admin/performanceEvaluation/download?fileName="+ data;
					window.open(url);
				}
			});
		})
	});
	function resetSearch() {
		$("input").val('');
		var a = document.getElementById("selectDept");
		a.options[0].selected = true;
	}
	//格式化时间
	function formatDate(times) {
		var date = new Date(times);
		var fm = date.format('yyyy-MM');
		return fm;
	}
	 
	function getData(pageIndex){
		var queryData=$("#form1").serialize();
		pageIndex = pageIndex-1;
		queryData = queryData +"&pageIndex=" +pageIndex +"&pageSize=" +pageSize;
		var sort="totalScore";
		$.ajax({
			type : "POST",
			url : "${ctx}/admin/efficiencyResult/efficiencyResultQuery",
			data : queryData,
			success : function(data) {
				if (data == null || data.length == 0) {
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
		$("#form1").validate({
			rules:{
				id:{digits:true},
			},
			messages:{
			},
			submitHandler:function(form){
				getData(1);
			}
		});
	}
	</script>
	</div>
</body>
</html>