<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<html>
<head>
<script src="${ctx }/static/My97/WdatePicker.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>员工管理</title>
<style type="text/css">
#page {
	margin-left: 950px;
}
</style>
<script type="text/javascript">
	function f(){
		var  myselect=document.getElementById("selectDept");
		var index=myselect.selectedIndex ; 
		var text=myselect.options[index].text;
		document.getElementById("departmentName").value=text;
	}
	
	function selectIsMt(){
		var isMt1=$("input[id='isMt1']:checked").val();
		var isMt2=$("input[id='isMt2']:checked").val();
	
		if(isMt1 == "on" ){
			$("#isMt1").val(1);
		}
		
		if(isMt2 == "on" ){
			$("#isMt2").val(0);
		}
	}

	function selectIsPm() {
		var isPm1=$("input[id='isPm1']:checked").val();
		var isPm2=$("input[id='isPm2']:checked").val();
	
		if(isPm1 == "on" ){
			$("#isPm1").val(1);
		}
		
		if(isPm2 == "on" ){
			$("#isPm2").val(0);
		}
	}
	
	function delEmploye(id){
		if (confirm("确认要删除本员工")) {
			location.href = '${ctx}/admin/employe/delete/'+id;	
        } 
	}
</script>
<script type="text/javascript">
var date = new Date();
var  dd= date.getDate();
var  year= date.getYear();
if(dd >=1 && dd<=5){
	$("#addBtn").remove();
}
</script>

</head>
<body>
	<div id="search">
		<form id="form1" class="form-inline">
			<table id="t1">
				<tr>
					<td><label class="control-label">姓名</label><input type="text"
						name="name" value="${searchParames.name}"></td>
					<td><label class="control-label">是否为项目经理</label> <input
						type="radio"  id="isPm1" name="isPm" onclick="selectIsPm();">是 <input
						type="radio"  id="isPm2" name="isPm" onclick="selectIsPm();">否</td>
					<td><label class="control-label">工号</label><input type="text"
						name="code" value="${searchParames.code }"></td>
					<td><label class="control-label">是否为MT负责人</label> <input
						type="radio" id="isMt1" name="isMt" onclick="selectIsMt();" >是 <input
						type="radio" id="isMt2" name="isMt" onclick="selectIsMt();" >否</td>
					<input type="hidden" id="departmentName" name="departmentName"/>
					<td><label class="control-label">部门</label>
					<select id="selectDept" onclick="f()">
								<option>----请选择-----</option>
								<c:forEach items="${dept }" var="dept">
									<option value="${dept.id}" >${dept.name}</option>
								</c:forEach>
							</select>
					</td>
					 <td><label class="control-label">入职时间</label> <input
						class="Wdate" id="first" type="text"
						onfocus="new WdatePicker({lang:'zh-cn',dateFmt:'yyyy-MM-dd'})"
						name="createStartTime" />~ <input class="Wdate" id="last"
						type="text"
						onfocus="new WdatePicker({lang:'zh-cn',dateFmt:'yyyy-MM-dd'})"
						name="createEndTime" /></td>
				</tr> 
			</table>
			<button id="submit_btn" class="btn" type="submit">搜索</button>
			<button id="clear_btn" class="btn" onclick="location.href='list';">清空</button>
			<button type="button" class="btn" onclick="location.href='addPage';" id="addBtn">新增员工</button>
			</br>
		</form>
	</div>

	<div id=data>
		<table class="table table-striped table-bordered table-condensed">
			<thead>
				<tr>
					<td>序号</td>
					<td>工号</td>
					<td>姓名</td>
					<td>部门</td>
					<%--<td>职位</td>--%>
					<td>是否为MT负责人</td>
					<td>是否为项目经理</td>
					<td>入职时间</td>
					<td>操作</td>
				</tr>
			</thead>
			<thead>
			<tbody id="tbody"></tbody>
			</thead>
			<div id="pager"></div>
			<script type="text/x-jquery-tmpl" id="tmplList">
				{{each(i,p) data.content}}
				<tr>
					<td>@{i+1}</td>
					<td>@{p.code}</td>
					<td>@{p.name}</td>
					<td>@{p.departmentName}</td>
					<td>@{formatIsMt(p.isMt)}</td>
					<td>@{formatIsPm(p.isPm)}</td>
					<td>@{formatDate(p.employmentDate)}</td>
					<td>
						<a href="view/@{p.id}">查看</a>
						{{if p.departmentName !=0 }}
							<a href="editPage/@{p.id}">编辑</a>
							<a href="#" onclick="delEmploye(@{p.id});">删除</a>
						{{/if}}
					</td>
				</tr>
				{{/each}}
		</script>
		</table>
		<script>
			var pageSize = 10;
			$(function() {
				validate();
				$("#form1").submit();
			});
			//格式化时间
			function formatDate(times) {
				var fm = "";
				if(times){
					var date = new Date(times);
					fm = date.format('yyyy-MM-dd');
				}
				return fm;
			}
			//格式话是否MT
			function formatIsMt(state) {
				var state;
				if (true == state) {
					return state = '是';
				} else {
					return state = '否';
				}
			}
			//格式话是否MT
			function formatIsPm(state) {
				var state;
				if (true == state) {
					return state = '是';
				} else {
					return state = '否';
				}
			}
			/*清空*/
			function resetSearch() {
				$("input").val('');
				$("input[name='isMt']").attr("checked",false);
				$("input[name='isPm']").attr("checked",false);
				document.getElementById('selectDept').options[0].selected=true;
			}
			
			function getData(pageIndex) {
				var queryData = $("#form1").serialize();
				pageIndex = pageIndex - 1;
				queryData = queryData + "&pageIndex=" + pageIndex
						+ "&pageSize=" + pageSize;
				var sort = "totalScore";
				$.ajax({
					type : "POST",
					url : "${ctx}/admin/employe/employeQuery",
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
				$("#form1").validate({
					rules : {
						id : {
							digits : true
						},
					},
					messages : {},
					submitHandler : function(form) {
						getData(1);
					}
				});
			}
		</script>
	</div>
</body>
</html>