<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>我的主页</title>
</head>
<body>
	<div class="container text-center">
		<div class="row-fluid">
			<div class="span12">
				<div class="row-fluid hide">
					<div class="span3">
						<h4>效能</h4>
					</div>
					<div class="span3">
						<h4>专业</h4>
					</div>
					<div class="span3">
						<h4>客户满意</h4>
					</div>
					<div class="span3">
						<h4>上级</h4>
					</div>
				</div>
				<br />
				<div class="row-fluid hide">
					<div class="span3">
						<p>绩效占比</p>
						<h4>30%</h4>
						<h6>以项目计划工时、标准工时、实际工时三个数据比衡量效率 绩效占比</h6>
					</div>
					<div class="span3">
						<p>绩效占比</p>
						<h4>30%</h4>
						<h6>各个岗位的专业维度考量， 如需求覆盖率，bug率，代码质量等</h6>
					</div>
					<div class="span3">
						<p>绩效占比</p>
						<h4>30%</h4>
						<h6>价值链客户评价上游， 如研发评价产品</h6>
					</div>
					<div class="span3">
						<p>绩效占比</p>
						<h4>10%</h4>
						<h6>行政管理评价，如敬业度，团队配合等</h6>
					</div>
				</div>
				<br />
				<div class="row-fluid">
					<div class="span3">
						<shiro:lacksRole name="employe">
							<shiro:lacksRole name="pm">
								<a href="#" class="btn btn-large btn-block disabled"
									type="button">进入</a>
							</shiro:lacksRole>
						</shiro:lacksRole>
						<shiro:hasAnyRoles name="employe,pm">
							<a href="${ctx }/project/findAll" class="btn btn-large btn-block"
								type="button">进入</a>
						</shiro:hasAnyRoles>
					</div>
					<div class="span3 hide">
						<shiro:lacksRole name="employe">
							<shiro:lacksRole name="mt">
								<a href="#" class="btn btn-large btn-block disabled"
									type="button">进入</a>
							</shiro:lacksRole>
						</shiro:lacksRole>
						<shiro:hasRole name="employe">
							<a href="${ctx }/specialtyJX/employeJX"
								class="btn btn-large btn-block" type="button">进入</a>
						</shiro:hasRole>
						<shiro:hasRole name="mt">
							<a href="${ctx }/specialtyJX/employeJXList"
								class="btn btn-large btn-block" type="button">进入</a>
						</shiro:hasRole>
					</div>
					<div class="span3 hide">
						<shiro:lacksRole name="employe">
							<a href="#" class="btn btn-large btn-block disabled"
								type="button">进入</a>
						</shiro:lacksRole>
						<shiro:hasRole name="employe">
							<a href="${ctx }/satisfaction/list"
								class="btn btn-large btn-block" type="button">进入</a>
						</shiro:hasRole>
					</div>
					<div class="span3 hide">
						<shiro:lacksRole name="employe">
							<shiro:lacksRole name="mt">
								<a href="#" class="btn btn-large btn-block disabled"
									type="button">进入</a>
							</shiro:lacksRole>
						</shiro:lacksRole>
						<shiro:hasRole name="employe">
							<a href="${ctx }/leaderAssessment/employeeViewPage"
								class="btn btn-large btn-block" type="button">进入</a>
						</shiro:hasRole>
						<shiro:hasRole name="mt">
							<a href="${ctx }/leaderAssessment/depList"
								class="btn btn-large btn-block" type="button">进入</a>
						</shiro:hasRole>
					</div>
				</div>
				<br /> <br />
				<div class="row-fluid">
					<div class="span12">
						<shiro:lacksRole name="employe">
							<a href="#" class="btn btn-large btn-block disabled"
								type="button">我的绩效总览</a>
						</shiro:lacksRole>
						<shiro:hasRole name="employe">
							<a href="${ctx }/performanceEvaluation/detail"
								class="btn btn-large btn-block" type="button">我的绩效总览</a>
						</shiro:hasRole>
					</div>
				</div>
				<br /> <br />
				<div class="row-fluid">
					<div class="span12">
						<shiro:lacksRole name="mt">
							<a href="#" class="btn btn-large btn-block disabled"
								type="button">部门绩效总览</a>
						</shiro:lacksRole>
						<shiro:hasRole name="mt">
							<a href="${ctx }/performanceEvaluation/list"
								class="btn btn-large btn-block" type="button">部门绩效总览</a>
						</shiro:hasRole>
					</div>
				</div>
			</div>

		</div>
	</div>
	<br />
	<br />
	<br />
	<br />
	</div>
</body>
</html>