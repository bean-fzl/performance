<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
	<title>效能-项目页面</title>
	<!-- 时间控件 css  -->
	<link rel="stylesheet" type="text/css" href="${ctx}/static/My97/skin/WdatePicker.css" />
	<!-- 时间控件js  -->
	<script type="text/javascript" src="${ctx}/static/My97/WdatePicker.js"></script>
	
	<!--  部门查询-->
	<script type="text/javascript" src="${ctx}/static/custom/depar.js"></script>
	
	<!-- 工时校验-->
	<script type="text/javascript" src="${ctx}/static/custom/validateT.js"></script>
	
	<!-- 弹框 js -->
	<script type="text/javascript" src="${ctx}/static/artdialog/artDialog.js?skin=brief"></script>
	<script type="text/javascript" src="${ctx}/static/artdialog/plugins/iframeTools.js"></script>
	
	<script type="text/javascript">
		$(function () { 
			startPlugin.init("${ctx}");
			startPlugin.startInit();
			startPlugin.startInitParam({
				departmentId:"department_id"
			});
		});
	
		var i =0;
		function addValueFun(){
			var h=$("td.project_table_no_cls").size()+1;
			i = i+1;
			/* h = $("#tags").find("tr").length+1; */
			var str = "<tr>"
							+"<td  class='project_table_no_cls'>"+h+"</td>"
							+"<td>\n" +
							"<input type=\"text\" id='efficiencyRecordBo["+i+"].name' name='efficiencyRecordBo["+i+"].name' class=\"input-small required\"  minlength=\"1\"/>\n" +
							"</td>"
							+"<td><select id='department_id_"+i+"' onchange='empFun(this.id)' name='efficiencyRecordBo["+i+"].departmentId' class='span2 required'><option>--请选择--</option></select></td>"
							+"<td><select id='employe_id_"+i+"' name='efficiencyRecordBo["+i+"].employeId' class='span2 required'><option>--请选择--</option></select></td>"
							+"<td>"
								+"周期："
								+"<input type=\"hidden\" id="+i+"_planBeginTime\" name=\"efficiencyRecordBo["+i+"].planBeginTime\" />\n" +
								"<input type=\"hidden\" id="+i+"_planEndTime\" name=\"efficiencyRecordBo["+i+"].planEndTime\"  />\n" +
								"<select id="+i+"_month\" name=\"efficiencyRecordBo["+i+"].month\" onchange=\"setPlanTime("+i+")\">\n" +
								"\t\t\t\t\t\t\t<option value=\"-1\">—请选择—</option>\n" +
								"\t\t\t\t\t\t\t<option value=\"1\">一月份</option>\n" +
								"\t\t\t\t\t\t\t<option value=\"2\">二月份</option>\n" +
								"\t\t\t\t\t\t\t<option value=\"3\">三月份</option>\n" +
								"\t\t\t\t\t\t\t<option value=\"4\">四月份</option>\n" +
								"\t\t\t\t\t\t\t<option value=\"5\">五月份</option>\n" +
								"\t\t\t\t\t\t\t<option value=\"6\">六月份</option>\n" +
								"\t\t\t\t\t\t\t<option value=\"7\">七月份</option>\n" +
								"\t\t\t\t\t\t\t<option value=\"8\">八月份</option>\n" +
								"\t\t\t\t\t\t\t<option value=\"9\">九月份</option>\n" +
								"\t\t\t\t\t\t\t<option value=\"10\">十月份</option>\n" +
								"\t\t\t\t\t\t\t<option value=\"11\">十一月份</option>\n" +
								"\t\t\t\t\t\t\t<option value=\"12\">十二月份</option>\n" +
								"</select>"
								+"工时：<input type='text' id='efficiencyRecordBo["+i+"].planHours' name='efficiencyRecordBo["+i+"].planHours' class='input-small digits'  minlength='1'/>"
							+"</td>"
							+"<td>\n" +
							"<input type='text' id='efficiencyRecordBo["+i+"].score' name='efficiencyRecordBo["+i+"].score' class='input-small required digits' minlength='1'/>"+
							"</td>\n" +
							"<td>\n" +
							"<input type='text' id='efficiencyRecordBo["+i+"].weight' name='efficiencyRecordBo["+i+"].weight' class=\"input-small required digits\"  minlength=\"1\"/>%\n" +
							"</td>"
							+"<td><input type='button' value='清空' onclick='delValueFun(this)'/></td>"
						+"</tr>";
						startPlugin.startInitParam({
							departmentId:"department_id_"+i
						});
			$("#tags").append(str);
		   
		} 
		
		function delValueFun(obj){  
			$(obj).parent().parent().remove();
			genProjectTableNo();
		}
		
		function genProjectTableNo(){
			$("td.project_table_no_cls").each(function(index,element){
				$(element).html(index+1);
				
			});
			
		}
		function empFun(dep_id){
			var t = dep_id;
			var h = $("#"+t).find("option:selected").val();
		
			var first=$("#"+t );
			var second=first.parent().next().find('select')[0].id;
			
			if("" != t  ){
				$.ajax({ 
			        type: "GET", 
			       	url: "${ctx}/admin/employe/listAjax?departmentId="+h, 
			        dataType:"json", 
			        success: function(data){ 
			        		$("#"+second).html("");
			        	 	var tml = "<option  value = ''>--请选择--</option>"
				             $.each(data, function(){
				                tml += "<option value= "+this.id+">"+this.name+"</option>" ;
				             });
			        	 	$("#"+second).html(tml);
			        } ,
				});
			}
		}
		function bodyOnload() {
			/* location.reload(true); */
	
			 window.parent.location.reload();
			
		}
		
	</script>
</head>

<body >
	<form id="formId" action="${ctx}/efficiency/add" method="post"  >
		<div class="control-group">
			<div class="controls">
				<label for="projectNameBo" class="control-label">项目名称:</label>
				<input type="text" id="projectNameBo" name="projectNameBo" class="input-large" />
				<label for="taskType" class="control-label">任务属性:</label>
				<select id ="taskType" name="taskType">
					<option value="-1">—请选择—</option>
					<option value="1">行政管理工作任务</option>
					<option value="2">党群管理工作任务</option>
					<option value="3">财务管理工作任务</option>
					<option value="4">人事管理工作任务</option>
					<option value="5">安全管理工作任务</option>
					<option value="6">通信业务管理工作任务</option>
					<option value="7">信息化业务管理工作任务</option>
				</select>
				<label for="workFeeling" class="control-label">工作感想:</label>
				<textarea id="workFeeling" name="workFeeling" class="input-large" ></textarea>
			</div>
		</div>
		
		<br/><input type='button' value='添加相关人员' onclick='addValueFun()' class="btn"/>
		<table class="table table-striped table-bordered table-condensed">
			<thead>
			   <tr>
			   		<td>序号</td>
				    <td>任务名</td>
			   		<td>部门</td>
			   		<td>姓名</td>
			   		<td>计划时间</td>
				    <td>分值</td>
				    <td>权重</td>
			   		<td>操作</td>
			   </tr>
		   	</thead>
		   	<tbody id="tags"> 
			   	 <tr>
			   	   	<td class="project_table_no_cls">1</td>
					 <td>
						 <input type="text" id="efficiencyRecordBo[0].name" name="efficiencyRecordBo[0].name" class="input-small required"  minlength="1"/>
					 </td>
			   	   	<td>
			   	   		<select id="department_id"  onchange="empFun(this.id)" name="efficiencyRecordBo[0].departmentId" class="span2 required">
							<option value=''>--请选择--</option>
						</select>
					</td>
			   	   	<td>
						<select id="employe_id" name="efficiencyRecordBo[0].employeId" class="span2 required">
							<option value = "">--请选择--</option>
						</select>
					</td>
			   	   	<td>
			   	   		周期：
						<input type="hidden" id="0_planBeginTime" name="efficiencyRecordBo[0].planBeginTime" />
						<input type="hidden" id="0_planEndTime" name="efficiencyRecordBo[0].planEndTime"  />
						<select id="0_month" name="efficiencyRecordBo[0].month" onchange="setPlanTime(0)">
							<option value="-1">—请选择—</option>
							<option value="1">一月份</option>
							<option value="2">二月份</option>
							<option value="3">三月份</option>
							<option value="4">四月份</option>
							<option value="5">五月份</option>
							<option value="6">六月份</option>
							<option value="7">七月份</option>
							<option value="8">八月份</option>
							<option value="9">九月份</option>
							<option value="10">十月份</option>
							<option value="11">十一月份</option>
							<option value="12">十二月份</option>
						</select>
						工时：<input type="text" id="efficiencyRecordBo[0].score" name="efficiencyRecordBo[0].score" class="input-small required digits"  minlength="1"/>
					</td>
					<td>
						<input type="text" id="efficiencyRecordBo[0].planHours" name="efficiencyRecordBo[0].planHours" class="input-small required digits"  minlength="1"/>
					</td>
					<td>
						<input type="text" id="efficiencyRecordBo[0].weight" name="efficiencyRecordBo[0].weight" class="input-small required digits"  minlength="1"/>%
					</td>
			   	   	<td><input type="button" value="清空" onclick="delValueFun(this)"/></td>
			   	  </tr> 	
		   	</tbody>
		</table>
		<div class="form-actions">
			<input id="submit_btn" class="btn btn-primary" type="submit" value="提交"/>
			<input id="cancel_btn" class="btn" type="button" value="返回" onclick="history.back()"/>
		</div>
	</form>
	
	
	<script type="text/javascript">
	$(function() {
		 validate();
	})
	</script>
	<script type="text/javascript">
	function validate() {
		$("#formId").validate({
			rules : {
				projectNameBo:{
					required:true,
					minlength: 2,
					maxlength:20,
				},
			},
			messages : {
				projectNameBo:{
				    required: "必填字段",
				    minlength: jQuery.format("请输入一个长度不能小于{0}"),
				    maxlength: jQuery.format("请输入一个 长度不能超过 {0}"),
				   },
			},
			submitHandler:function(form){
					dialogFun();
				 /* form.submit(); */
		    }  
		});
	}
	
	//弹框方法
	 function dialogFun(){
		  art.dialog({
			  width:400,
			  height:300,
			  padding:0,
			  lock:true,
			  content:$("#show_id").html(),
			  ok:function(){
				  var win = art.dialog.open.origin;  
				  win.document.getElementById("formId").submit();
			  },
			  cancel:function(){
				  var content = $(this.DOM.content[0]);
				  content.find("#form_two_id").submit();//提交
			  }
		  }).show();
	 }

	 function setPlanTime(i){
		var value = $("#"+i+"_month").val();
		if(value!="-1"){
			var date=new Date;
			var year=date.getFullYear();
			var begin = new Date(year,value-1);
			var end = new Date(year,value,0);
			var s = $("#"+i+"_planBeginTime").val();

			$("#"+i+"_planBeginTime").val(begin.format('yyyy-MM-dd'));
			$("#"+i+"_planEndTime").val(end.format('yyyy-MM-dd'));
		}
	 }
	</script>
	
	<div style="display:none;" id="show_id">
		<td>新建此项目</td>
		<form action="${ctx}/efficiency/isfalse" id = "form_two_id" method="post">		
		</form>
	</div>

</body>
</html>
