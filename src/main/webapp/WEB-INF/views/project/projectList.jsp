<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
<script src="${ctx }/static/My97/WdatePicker.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>项目列表</title>
</head>
<body>
	<form id="queryForm" class="form-inline" style="margin-bottom: 0px;">
		<table >
			<tr>
				<td><label class="control-label">项目名称</label></td>
				<td><input type="text" name="name" value="${searchParames.name }"/></td>
				<td><label class="control-label">项目经理</label></td>
				<td><input type="text" name="pm" value="${searchParames.pm }"/></td>
				<td><label class="control-label">项目id</label></td>
				<td><input type="text" name="id" value="${searchParames.id }" /></td>
				<td><label class="control-label">项目人员</label></td>
				<td><input type="text" name="projectPersonnel" value="${searchParames.projectPersonnel }" /></td>
				<td><label class="control-label">状态</label></td>
				<td>
					<select name="state" id="searchSelector">
						<option value="">选择..</option>
						<option value="0">进行中</option>
						<option value="1">已完成</option>
					</select>
				</td>
				<%--<td><label class="control-label">项目时间</label></td>
				<td>
					<input type="text" name="projectBeginTime"  type="text" onfocus="new WdatePicker({lang:'zh-cn',dateFmt:'yyyy-MM-dd'})" value="${projectBeginTime }"/> ~ 
					<input type="text" name="projectEndTime"  type="text" onfocus="new WdatePicker({lang:'zh-cn',dateFmt:'yyyy-MM-dd'})" value="${projectEndTime }"/>
				</td>--%>
				<td>
					<button class="btn" type="submit" >搜索</button>
					<button class="btn" type="button" onclick="resetSearch();">清空</button>
				</td>
			</tr>
		</table>
	</form>
	</br>
	<table class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<td>序号</td>
				<td>项目ID</td>
				<td>项目名称</td>
				<td>项目人员名单</td>
				<td>项目经理</td>
				<td>时间</td>
				<td>状态</td>
				<td>操作</td>
			</tr>
		</thead>
		<thead>
			<tbody id="tbody">
			</tbody>
		</thead>
		<div id="pager"></div>
		<script type="text/x-jquery-tmpl" id="tmplList">
			{{each(i,p) data.content}}
			<tr>
				<td>@{i+1}</td>
				<td>@{p.id}</td>
				<td>@{p.name}</td>
				<td>@{p.projectPersonnel}</td>
				<td>@{p.pm}</td>
				<td>@{formatDate(p.projectBeginTime)} ~ @{formatDate(p.projectEndTime)}</td>
				<td>@{formatState(p.state)}</td>
				<td>
					<a href="viewPage/@{p.id}">查看</a>
				</td>
			</tr>
			{{/each}}
		</script>
	</table>
	<script>
		var pageSize = 10;
		$(function(){
			validate();
			$("#queryForm").submit();
			selector();
			
		});
		
		//格式化时间
		function formatDate(times) {
			var date = new Date(times);
			var fm = date.format('yyyy-MM-dd');
			return fm;
		}
		
		//格式化装态
		function formatState(state) {
			var state;
			if (1 == state) {
				return state = '已完成';
			}
			if (0 == state) {
				return state = '进行中';
			}
		}
		function resetSearch() {
			$("input").val('');
			$("#searchSelector").val("");
		}
		
		//选中
		function selector() {
			var state = '${state}';
			$("#searchSelector").find("option[text='+state+']").attr("selected",true);
			$("#searchSelector").val(state);
		}
		
		function getData(pageIndex) {
			var queryData = $("#queryForm").serialize();
			pageIndex = pageIndex - 1;
			queryData = queryData + "&pageIndex=" + pageIndex + "&pageSize="
					+ pageSize;
			var sort = "totalScore";
			$.ajax({
				type : "POST",
				url : "${ctx}/admin/project/projectListQuery",
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
</body>
</html>