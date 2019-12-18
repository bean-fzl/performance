<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="org.apache.shiro.web.filter.authc.FormAuthenticationFilter"%>
<%@ page import="org.apache.shiro.authc.ExcessiveAttemptsException"%>
<%@ page import="org.apache.shiro.authc.IncorrectCredentialsException"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
	<!-- 工时校验-->
	<script type="text/javascript" src="${ctx}/static/custom/validateT.js"></script>

	
	<script type="text/javascript">
		function js_method(eff_id,t_id,s){
			var effId = eff_id;//主键id
			var tId = t_id;//当前a标签id
			var ss =s;
			
			var first=$("#"+tId);//获取当前id的对象
			var second=first.parent().parent().prev().prev()[0].id;//获取录入工时的id

			var input_id = first.parent().parent().find('input')[0].id;//获取录入工时input的id first.prev()[0].id

			var input_val = $("#"+input_id).val();//获取input输入的信息值
			if("" != input_val){
				
				 $.ajax({
						type : "GET",
						url : "${ctx}/efficiency/listAjax?hours="+input_val+"&id="+effId+"&i="+ss,
						dataType:"json",
						success : function(data) {
							
							$("#"+second).html("");
							var tml = "";
							var htm = "";
							if(ss ==1 ){
			        	 	 	tml =tml+ "周期："+formatDate(data.actualBeginTime)+"至"+formatDate(data.actualEndTime)+";工时："+data.actualHours;
			        	 	 
			        	 	 	$("#"+second).html(tml);
			        	 	 	document.location.reload();
							}else if(ss == 2){
								tml =tml+ "周期："+formatDate(data.outputBeginTime)+"至"+formatDate(data.outputEndTime)+";工时："+data.outputHours;
								$("#"+second).html(tml);
								document.location.reload();
							}else{
								
							}
						}
					});
			}else{
				alert("不能为空");
			}
			
		}
	</script>

	
	实际时间：<br/>
	<form id="queryForm_two" >
	<input type = "hidden" name= "projectId" value="${project.id}"/>
	
	
	
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
		  <tr>
		  	<td>序号</td>
			<td>任务名</td>
	   		<td>姓名</td>
	   		<td>部门</td>
			<td>分值</td>
			<td>权重(%)</td>
			<td>实际工时</td>
	   		<td>产出工时</td>
	   		<shiro:hasAnyRoles name="employe">
	   		<td>录入实际工时</td>
	   		</shiro:hasAnyRoles>
	   		<shiro:hasAnyRoles name="pm">
	   		<td>录入产出工时</td>
	   		</shiro:hasAnyRoles>
	   		</tr>
		 </thead>
		<tbody id="tbody_two">
		</tbody>
		 <%--  <c:forEach items="${list}" var="listTemp" varStatus="s" >
			<tr>
		   		<td>
		   			${listTemp.employeName}
		   			<input type ="text" id="" name="id" value="${listTemp.id }" />
		   		</td>
		   		<td>${listTemp.departmentName}</td>
		   		<td id= "td_t_${s.index}">周期：${listTemp.actualBeginTime}至${listTemp.actualEndTime}；工时：${listTemp.actualHours}</td>
		   		<td id = "td_td_t_${s.index}">周期：${listTemp.outputBeginTime}至${listTemp.outputEndTime}；工时：${listTemp.outputHours}</td>
		   		<td>
		   			<input type ="text" name="" id="id_i_${s.index}"  />
		   			<a  href="javascript:void(0);" id = "t_${s.index}" onclick="js_method(${listTemp.id },this.id,1)">录入</a>
		   		</td>
		   		<td>
			   		<input type ="text" id="input_id_${s.index}" name=""  />
			   		<a href="javascript:void(0);" id = "a_id_${s.index}" onclick="js_method(${listTemp.id },this.id,2)">录入</a>
		   		</td>
	   		</tr>
	   	</c:forEach> --%>
	</table>
	<div id="pager_two"></div>
	</form>
	<script type="text/x-jquery-tmpl" id="tmplList_two">
		{{each(i,p) data.content}}
				<tr>
					<td>@{i+1}
						<input type = "hidden" name ="he_i" value = "@{formatDate(p.actualBeginTime)}"/>
					</td>
					<td>@{p.name}</td>
					<td>@{p.employeName}</td>
					<td>@{p.departmentName}</td>
					<td>@{p.score}</td>
					<td>@{p.weight}</td>

					<td id= "td_t_@{i}">@{"周期："+formatDate(p.actualBeginTime)+"至"+formatDate(p.actualEndTime)+"；工时："}@{format_Int(p.actualHours)}</td>
					<td id= "td_td_t_@{i}">@{"周期："+formatDate(p.outputBeginTime)+"至"+formatDate(p.outputEndTime)+"；工时："}@{format_Int(p.outputHours)}</td>
					
					<shiro:hasAnyRoles name="employe">
					<td class="employe_c_e" id="td_em_@{i}">
						{{if p.outputHours == null &&　p.actualHours　== null}}
							<input type ="text" name="name" id="id_i_@{i}"  hhh="@{i}" class="input-small digits_one noact"/>
							 <span style="display: inline;" id = "span_one_@{i}">
			   				<a  href="javascript:void(0);" id = "t_@{i}" onclick="js_method(@{p.id},this.id,1)">录入</a>
							</span>
							<input type="hidden" name = "" value="@{format_Int(p.planHours)}"/>
						{{else p.actualHours != null  &&　p.outputHours　== null}}
							<input type ="text" name="name" id="id_i_@{i}" value="@{p.actualHours}" hhh="@{i}" class="input-small digits_one noact"/>
							<span style="display: inline;" id = "span_one_@{i}">
							<a  href="javascript:void(0);" id = "t_@{i}" onclick="js_method(@{p.id},this.id,1)">修改</a>
							</span>
							<input type="hidden" name = "" value="@{format_Int(p.planHours)}"/>
						{{else p.outputHours != null}}
							<input type ="text" name="name" id="id_i_@{i}" readonly="readonly" value="@{p.actualHours}" hhh="@{i}" class="input-small digits_one noact"/>
							<span style="display: inline;" id = "span_one_@{i}">
			   				已录入
							</span>
							<input type="hidden" name = "" value="@{format_Int(p.planHours)}"/>
						{{/if}}
					</td>
					</shiro:hasAnyRoles>
					<shiro:hasAnyRoles name="pm">
					<td class = "pm_td_re" id ="pm_td_id_@{i}">
						{{if p.actualHours != null &&　p.outputHours　== null}}
							<input type ="text" id="input_id_@{i}" name="inputName" ttt="@{i}" class="input-small digits_two noout vatim-two" />
							<span style="display: inline;" id = "span_two_@{i}" class="c">
			   				<a href="javascript:void(0);" id = "a_id_@{i}" onclick="js_method(@{p.id},this.id,2)">录入</a>
							</span>
							<input type="hidden" name = "" value="@{format_Int(p.actualHours)}"/>
						{{else p.actualHours != null  &&　p.outputHours　!= null}}
							<input type ="text" id="input_id_@{i}" name="inputName" value="@{p.outputHours}" ttt="@{i}" class="input-small digits_two noout vatim-two" />
							<span style="display: inline;" id = "span_two_@{i}" class="c">
							<a href="javascript:void(0);" id = "a_id_@{i}" onclick="js_method(@{p.id},this.id,2)">修改</a>
							</span>
							<input type="hidden" name = "" value="@{format_Int(p.actualHours)}"/>
						{{else p.actualHours == null}}
							<input type ="text" id="input_id_@{i}"  readonly="readonly" name="inputName" ttt="@{i}" class="input-small digits_two noout vatim-two" />
							<span style="display: inline;" id = "span_two_@{i}" class="c">
			   				待录入
							</span>
							<input type="hidden" name = "" value="@{format_Int(p.actualHours)}"/>
						{{/if}}
					</td>
					</shiro:hasAnyRoles>
					
				</tr>
		{{/each}}
	</script>
	<script type="text/javascript">
		var pageSize_two = 10;
		$(function() {
			getData_two(1);
			
			//$("#queryForm").submit();
		})
		function formatDate(times) {
			var date = new Date(times);
			var fm = date.format('yyyy-MM-dd');
			return fm;
		}
		function format_Int(coun){
			var count = coun;
			if(null != count){
				return count;
			}else{
				return 0;
			}
		}
		function getData_two(pageIndex_two) {
			var queryData_two = $("#queryForm_two").serialize();
			pageIndex_two = pageIndex_two - 1;
			queryData_two = queryData_two + "&pageIndex=" + pageIndex_two + "&pageSize="
					+ pageSize_two;
			$.ajax({
				type : "POST",
				url : "${ctx}/efficiency/efficiencyListAjax",
				data : queryData_two,
				success : function(data) {
					if (data == null || data.records == 0) {
						$("#tbody_two").html("暂无记录");
					} else {
						$("#tbody_two").html($("#tmplList_two").tpl({
							data : data
						}));
					}
					//分页控件
					var pager_two = new pagination(function() {
						getData_two($(this).attr("data-index"));
					});
					pager_two.pageIndex = data.number + 1;//当前页码
					pager_two.pageSize = data.size;//页大小
					pager_two.totalCount = data.totalElements;//总条数
					pager_two.totalPage = data.totalPages;//总页数
					$("#pager_two").html(pager_two.creat());
					validateTime();//调用时间限制
				}
			});
		};
	</script>
	
	<script type="text/javascript">
	$(function() {
		 validate();
		 
	})
	</script>
	<script type="text/javascript">
	function validate() {
		$("#queryForm_two").validate({
			rules : {
				
			},
			messages : {
				
			},
			submitHandler:function(form){
				js_method();
		    }
		});
	}
	//此方法为录入工时时，只能是当前时间月份的月份
	function validateTime(){
		var newDate = "${nDate}";
		var newDateStr = formatDate(newDate);
		var newDateStrYear = newDateStr.substring(0, 4);
		var newDateStrMonth = newDateStr.substring(5, 7);
		<shiro:hasAnyRoles name="employe">
		$("td.employe_c_e").each(function(index,element){
			var planDate = $(element).parent().find("input[name ='he_i']")[0].value;
			var planDateYear = planDate.substring(0, 4);
			var planDateMonth = planDate.substring(5, 7);
			var input_em = $(element).find("input[type='text']")[0].value;
			if(newDateStrYear != planDateYear && newDateStrMonth != planDateMonth){
				var htm_em = "<input type= 'text' readonly='readonly'class='input-small' value='"+input_em+"'/> 已不在录入时间之内"
				$("#td_em_"+index).html(htm_em);
			} 
		}); 
		</shiro:hasAnyRoles>
		<shiro:hasAnyRoles name="pm">
		$("td.pm_td_re").each(function(index,element){
			var planDate = $(element).parent().find("input[name ='he_i']")[0].value;
			var planDateYear = planDate.substring(0, 4);
			var planDateMonth = planDate.substring(5, 7);
			var input_em = $(element).find("input[type='text']")[0].value;
			if(newDateStrYear != planDateYear && newDateStrMonth != planDateMonth){
				var htm_em = "<input type= 'text' readonly='readonly'class='input-small' value='"+input_em+"'/> 已不在录入时间之内"
				$("#pm_td_id_"+index).html(htm_em);
			} 
		}); 
		</shiro:hasAnyRoles>
		
	} 
	
	</script>
	
