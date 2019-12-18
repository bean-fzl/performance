<%@ page language="java" contentType="text/html; charset=utf-8"  pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>导入员工</title>
<link href="${ctx}/static/jquery-validation/1.11.1/validate.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="${ctx}/static/jquery/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="${ctx}/static/jquery-validation/1.11.1/jquery.validate.min.js"></script>
<script src="${ctx}/static/jquery-validation/1.11.1/messages_bs_zh.js" type="text/javascript"></script>
<script src="${ctx }/static/My97/WdatePicker.js"></script>
<script type="text/javascript">
function f(){
	var  myselect=document.getElementById("selectDept");
	var index=myselect.selectedIndex ; 
	var  id=myselect.options[index].value;
	var text=myselect.options[index].text;
	 
	document.getElementById("departmentId").value=id;
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
</script>
<script type="text/javascript">
 //邮箱验证
 jQuery.validator.addMethod("checkemail", function(value, element) {
	 var flag="";
	 $.ajaxSetup({
		   async: false
		   });
	 $.ajax({ 
	        type: "GET", 
	       	url: "${ctx}/admin/employe/checkLoginName/"+value, 
	        dataType:"text", 
	        success: function(data){ 
	        	 flag=data;
	        } ,
		});
	 return this.optional(element) || (flag == "YES");
 }, "邮箱已存在");
//手机号码验证
 jQuery.validator.addMethod("isMobile", function(value, element) {
     var length = value.length;
     var mobile = /^(13[0-9]{9})|(18[0-9]{9})|(14[0-9]{9})|(17[0-9]{9})|(15[0-9]{9})$/;
     return this.optional(element) || (length == 11 && mobile.test(value));
 }, "请正确填写您的手机号码");

</script>
<script type="text/javascript">
$(function(){
	document.getElementById('selectDept').options[0].selected=true;
	var id = document.getElementById('selectDept').options[0].value;
	var text=document.getElementById('selectDept').options[0].text;
	document.getElementById("departmentId").value=id;
	document.getElementById("departmentName").value=text;
});
$(function(){
	$("#inputForm").validate({
		rules:{
			name:{required:true},
			mobile:{isMobile:true},
			email:{required:true, email:true, checkemail:true}
		},
	});	
})
$(document).ready(function(){
	$("#selectDept")
});
</script>
</head>
<body>
	<form id="inputForm" action="${ctx}/admin/employe/add" method="post" class="form-horizontal">
		<fieldset>
			<legend><small>导入员工信息</small></legend>
			<div class="control-group">
				<label for="name" class="control-label">姓名:</label>
				<div class="controls">
					<input type="text" id="name" name="name"  />
				</div>
			</div>
			<div class="control-group">
				<label for="code" class="control-label">工号:</label>
				<div class="controls">
					<input type="text" id="code" name="code"  />
				</div>
			</div>
			<div class="control-group">
				<label for="name" class="control-label">邮箱:</label>
				<div class="controls">
					<input type="text" id="email" name="email"  />
				</div>
			</div>
			<div class="control-group">
				<label for="name" class="control-label">手机:</label>
				<div class="controls">
					<input type="text" id="mobile" name="mobile"  />
				</div>
			</div>
			<div class="control-group">
				<label for="departmentName" class="control-label">部门:</label>
				<div class="controls">
				<input type="hidden" id="departmentName" name="departmentName"/>
				<input type="hidden" id="departmentId" name="departmentId" >  
					<select id="selectDept" onclick="f()">
						<c:forEach items="${dept }" var="dept">
							<option value="${dept.id}" >${dept.name}</option>
						</c:forEach>
						<option value="-1">其它</option>
					</select>
				</div>
			</div>
			 <div class="control-group">
				<label for="createTime" class="control-label">入职时间:</label>
				<div class="controls">
					<input type="text" id="createTime" name="employmentDate" class="Wdate"  onfocus="new WdatePicker({lang:'zh-cn',dateFmt:'yyyy-MM-dd'})"/>
				</div>
			</div>  
			<!--<div class="control-group">
				<label for="departmentName" class="control-label">职位:</label>
				<div class="controls">
					<input type="text" id="position" name="position" />
				</div>
			</div>-->
			<div class="control-group">
				<label for="departmentName" class="control-label">是否为MT负责人:</label>
				<div class="controls">
					 <input type="radio"  id="isMt1" name="isMt" onclick="selectIsMt();">是&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					 <input type="radio"  id="isMt2" name="isMt" onclick="selectIsMt();"  checked="checked"  value="0">否 
				</div>
			</div>
					<div class="control-group">
				<label for="departmentName" class="control-label">是否为项目负责人:</label>
				<div class="controls">
					 <input type="radio"  id="isPm1" name="isPm" onclick="selectIsPm();">是&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					 <input type="radio"  id="isPm2" name="isPm" onclick="selectIsPm();" checked="checked" value="0">否 
				</div>
			</div> 	
			<div class="form-actions">
				<input id="submit_btn" class="btn" type="submit" value="提交" onclick="checkdept();"/>&nbsp;	
				<input id="cancel_btn" class="btn" type="button" value="返回" onclick="history.back()"/>
			</div>
		</fieldset>
	</form>
</body>
</html>