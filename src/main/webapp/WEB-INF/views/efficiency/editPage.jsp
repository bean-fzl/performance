<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
	<title>效能-项目页面</title>
	<!-- 时间控件 css  -->
	<link rel="stylesheet" type="text/css" href="${ctx}/static/My97/skin/WdatePicker.css" />
	<!-- 时间控件js  -->
	<script type="text/javascript" src="${ctx}/static/My97/WdatePicker.js"></script>
	
	<script type="text/javascript" src="${ctx}/static/custom/depar.js"></script>
	
	<!-- 工时校验-->
	<script type="text/javascript" src="${ctx}/static/custom/validateT.js"></script>
	
	<script type="text/javascript">
		$(function () { 
			startPlugin.init("${ctx}");
			startPlugin.startInit();
			 vidateTimes();
		});
		
		
		
		//alert(i);
		//var i = $("#tags").find("tr").length+1;
		var i =${list.size()};
		function addValueFun(){  
			var h=$("td.project_table_no_cls").size()+1;
			
			var str = "<tr>"
							+"<input type =\"hidden\" id='' name='efficiencyRecordBo["+i+"].efficiencyIds' />"
							+"<td class='project_table_no_cls'>"+h+"</td>"
							+"<td>\n" +
							"<input type=\"text\" id='efficiencyRecordBo["+i+"].name' name='efficiencyRecordBo["+i+"].name' class=\"input-small required\"  minlength=\"1\"/>\n" +
							"</td>"
							+"<td><select id='department_id_"+i+"' onchange='empFun(this.id)' name='efficiencyRecordBo["+i+"].departmentId' class='span2 required'><option>--请选择--</option></select></td>"
							+"<td><select id='employe_id_"+i+"' name='efficiencyRecordBo["+i+"].employeId' class='span2 required'><option>--请选择--</option></select></td>"
							+"<td>"
								+"周期:"
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
								+"工时：<input type='text' id='efficiencyRecordBo["+i+"].planHours' name='efficiencyRecordBo["+i+"].planHours' class='input-small required digits' minlength='1'/>"
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
			i=i+1;
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
		
		
		function formatDate(times) {
			var date = new Date(times);
			var fm = date.format('yyyy-MM-dd');
			return fm;
		}
		
		function vidateTimes(){
			var newDate = "${nDate}";
			var newDateStr = formatDate(newDate);
		 	var newDate = planDate_s(newDateStr);
			$("td.td_table_no_cls").each(function(index,element){
				var planDate = $(element).find("input[name ='begin_tiem']")[0].value;
				var datePlanDate = planDate_s(planDate);
				if(datePlanDate < newDate){
					var htm_em = "-";
					$(element).html(htm_em);
				} 
			}); 
		}
		
		function planDate_s(t){
			var dt;
			var type1 = typeof t;
            if (type1 == 'string')
            	dt = stringToTime(t);
            else if (t.getTime)
            	dt = t.getTime();
			return dt;
		}
	</script>
</head>

<body >
	<form id="formId" action="${ctx}/efficiency/edit" method="post" >
		<div class="control-group">
			<div class="controls">
				<label for="" class="control-label">项目名称:</label>
				<input type="text" id="projectNameBo" name="projectNameBo" value="${project.name}"  class="input-large" />
				<input type ="hidden" id="" name="proId" value="${project.id }" />
				<label for="taskType" class="control-label">任务属性:</label>
				<select id ="taskType" name="taskType">
					<option value="-1">—请选择—</option>
					<option value="1" <c:if test="${project.taskType == '1'}">selected</c:if>>行政管理工作任务</option>
					<option value="2" <c:if test="${project.taskType == '2'}">selected</c:if>>党群管理工作任务</option>
					<option value="3" <c:if test="${project.taskType == '3'}">selected</c:if>>财务管理工作任务</option>
					<option value="4" <c:if test="${project.taskType == '4'}">selected</c:if>>人事管理工作任务</option>
					<option value="5" <c:if test="${project.taskType == '5'}">selected</c:if>>安全管理工作任务</option>
					<option value="6" <c:if test="${project.taskType == '6'}">selected</c:if>>通信业务管理工作任务</option>
					<option value="7" <c:if test="${project.taskType == '7'}">selected</c:if>>信息化业务管理工作任务</option>
				</select>
				<label for="workFeeling" class="control-label">工作感想:</label>
				<textarea id="workFeeling" name="workFeeling" class="input-large" >${project.workFeeling}</textarea>
			</div>
		</div>	
		
		<br/><input type='button' value='添加相关人员' onclick='addValueFun()' class="btn"/>
		<table class="table table-striped table-bordered table-condensed">
			<thead>
			   <tr>
			   		<td>序号</td>
				    <td>任务名</td>
				    <td>部门</td>
			   		<td>计划时间</td>
				    <td>分值</td>
				    <td>权重</td>
			   		<td>操作</td>
			   </tr>
		   	</thead>
		   	<tbody id="tags"> 
		   		<c:forEach items="${list}" var="listTemp" varStatus="s" >
				   	 <tr>
				   	 	<input type ="hidden" id="" name="efficiencyRecordBo[${s.index}].efficiencyIds" value="${listTemp.id }" />
				   	   	<td class="project_table_no_cls">
				   	   		${s.index+1}
				   	   	</td>
						 <td>
							 <input type="text" id="efficiencyRecordBo[0].name" name="efficiencyRecordBo[0].name" value = "${listTemp.name}" class="input-small required"  minlength="1"/>
						 </td>
				   	   	<td>
				   	   		<select id="department_id_${s.index}"  onchange="empFun(this.id)"  name="efficiencyRecordBo[${s.index}].departmentId" class="span2 required">
								<!-- <option value = "">--请选择--</option> -->
								<c:forEach items="${departmentList}" var="temp" varStatus="x" >
										<c:choose>
										 <c:when test="${listTemp.departmentId == temp.id}">
											<option value="${temp.id}" selected="selected">${temp.name}</option>
										</c:when> 
										<%-- <c:otherwise> 
											<option value="${temp.id}" >${temp.name}</option>
										</c:otherwise>  --%>
									</c:choose>
								</c:forEach>
							</select>
						</td>
				   	   	<td>
							<select id="employe_id_${s.index}"  name="efficiencyRecordBo[${s.index}].employeId"  class="span2 required">
								<!-- <option value = "">--请选择--</option> -->
								<c:forEach items="${listTemp.employeList}" var="temp_em" varStatus="x" >
										<c:choose>
										 <c:when test="${listTemp.employeId == temp_em.id}">
											<option value="${temp_em.id}" selected="selected">${temp_em.name}</option>
										</c:when> 
										<%-- <c:otherwise> 
											<option value="${temp_em.id}" >${temp_em.name}</option>
										</c:otherwise>  --%>
									</c:choose>
								</c:forEach>
							</select>
						</td>
				   	   	<td>
				   	   		周期:
							<select id="${s.index}_month" name="efficiencyRecordBo[${s.index}].month" onchange="setPlanTime(${s.index})">
								<option value="-1">—请选择—</option>
								<option value="1" <c:if test="${listTemp.month == '1'}">selected</c:if>>一月份</option>
								<option value="2" <c:if test="${listTemp.month == '2'}">selected</c:if>>二月份</option>
								<option value="3" <c:if test="${listTemp.month == '3'}">selected</c:if>>三月份</option>
								<option value="4" <c:if test="${listTemp.month == '4'}">selected</c:if>>四月份</option>
								<option value="5" <c:if test="${listTemp.month == '5'}">selected</c:if>>五月份</option>
								<option value="6" <c:if test="${listTemp.month == '6'}">selected</c:if>>六月份</option>
								<option value="7" <c:if test="${listTemp.month == '7'}">selected</c:if>>七月份</option>
								<option value="8" <c:if test="${listTemp.month == '8'}">selected</c:if>>八月份</option>
								<option value="9" <c:if test="${listTemp.month == '9'}">selected</c:if>>九月份</option>
								<option value="10" <c:if test="${listTemp.month == '10'}">selected</c:if>>十月份</option>
								<option value="11" <c:if test="${listTemp.month == '11'}">selected</c:if>>十一月份</option>
								<option value="12" <c:if test="${listTemp.month == '12'}">selected</c:if>>十二月份</option>
							</select>
							工时:<input type="text" id="efficiencyRecordBo[${s.index}].planHours" name="efficiencyRecordBo[${s.index}].planHours" value = "${listTemp.planHours}" class="input-small required digits"  minlength="1"/>
						</td>
						 <td>
							 <input type="text" id="efficiencyRecordBo[0].score" name="efficiencyRecordBo[0].score" value = "${listTemp.score}" class="input-small required digits"  minlength="1"/>
						 </td>
						 <td>
							 <input type="text" id="efficiencyRecordBo[0].weight" name="efficiencyRecordBo[0].weight" value = "${listTemp.weight}" class="input-small required digits"  minlength="1"/>%
						 </td>
				   	   	<td class="td_table_no_cls">
				   	   		<input type="button" value="清空" onclick="delValueFun(this)"/>
				   	   		<input type="hidden" name = "begin_tiem" value="<fmt:formatDate value= '${listTemp.planBeginTime}' pattern='yyyy-MM-dd'/>" />
				   	   	</td>
				   	  </tr> 
			   	 </c:forEach>	
		   	</tbody>
			
		</table>
		<div class="form-actions">
			<input id="submit_btn" class="btn btn-primary" type="submit" value="提交"/>&nbsp;	
			<!-- <input id="cancel_btn" class="btn" type="button" value="返回" onclick="history.back()"/> -->
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
					 form.submit();
			    }  
			});
		}
	</script>
</body>
</html>
