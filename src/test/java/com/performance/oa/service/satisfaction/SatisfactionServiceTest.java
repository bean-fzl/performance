package com.performance.oa.service.satisfaction;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.util.Assert;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.performance.oa.entity.Efficiency;
import com.performance.oa.entity.Satisfaction;
import com.performance.oa.query.SatisfactionQuery;
import com.performance.oa.repository.BaseTest;
import com.performance.oa.service.efficiencyResult.EfficiencyResultService;
import com.performance.oa.service.leaderAssessment.LeaderAssessmentService;
import com.performance.oa.service.satisfactionresult.SatisfactionResultService;
import com.performance.oa.service.specialty.SpecialtyJXService;
import com.performance.oa.service.task.TaskService;

public class SatisfactionServiceTest extends BaseTest {

	@Autowired
	SatisfactionService satisfactionService;
	@Autowired
	SatisfactionResultService satisfactionResultService;
    @Autowired
    private EfficiencyResultService efficiencyResultService;
    @Autowired
    private LeaderAssessmentService assessmentService;
    @Autowired
    private SpecialtyJXService specialtyJXService;
    @Autowired
    private TaskService taskService;
	@Test
	public void addTest() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, -1);
		Date createTime = cal.getTime();
		Satisfaction satisfaction = new Satisfaction();
		satisfaction.setBeEvaluatedDepartmentId(123L);
		satisfaction.setBeEvaluatedDepartmentName("测试");
		satisfaction.setBeEvaluatedId(321L);
		satisfaction.setBeEvaluatedName("孙轶凡");
		satisfaction.setComment("不错不错");
		satisfaction.setCreateTime(createTime);
		satisfaction.setEvaluatedDepartmentId(111L);
		satisfaction.setEvaluatedDepartmentName("java");
		satisfaction.setEvaluatedId(333L);
		satisfaction.setEvaluatedName("张栋");
		satisfaction.setEvaluatedTime(new Date());
		satisfaction.setProjectId(222L);
		satisfaction.setProjectName("测试项目");
		Satisfaction sat = satisfactionService.add(satisfaction);
		Assert.isTrue(sat.getId()!=null);
	}
	@Test
	public void DepartAppendEvaNameTest() throws JsonProcessingException {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, -3);
		Date beginTime = cal.getTime();
		cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, 3);
		Date endTime = cal.getTime();
		SatisfactionQuery q = new SatisfactionQuery();
		q.setId(2l);
		q.setPageSize(15);
		q.setPageIndex(0);
		Page<Satisfaction> pages = null;
		System.out.println("********************************\n");
		for(Satisfaction sa : pages.getContent()) {
			ObjectMapper mapper = new ObjectMapper();  
			System.out.println(mapper.writeValueAsString(sa));  
		}
	}
	@Test
	public void satisfactionAddTest() {
		List<Efficiency> efficiencyList = new ArrayList<Efficiency> ();
		Efficiency efficiency1 = new Efficiency();
		Efficiency efficiency2 = new Efficiency();
		Efficiency efficiency3 = new Efficiency();
		Efficiency efficiency4 = new Efficiency();
		
		efficiency1.setDepartmentId(1);
		efficiency2.setDepartmentId(2);
		efficiency3.setDepartmentId(3);
		efficiency4.setDepartmentId(4);
		
		efficiency1.setDepartmentName("产品");
		efficiency2.setDepartmentName("开发");
		efficiency3.setDepartmentName("UED");
		efficiency4.setDepartmentName("测试");
		
		efficiency1.setEmployeId(1);
		efficiency2.setEmployeId(2);
		efficiency3.setEmployeId(3);
		efficiency4.setEmployeId(4);
		
		efficiency1.setEmployeName("liugd");
		efficiency2.setEmployeName("sunyf");
		efficiency3.setEmployeName("buzd");
		efficiency4.setEmployeName("wanglix");
		
		efficiency1.setProjectId(111);
		efficiency2.setProjectId(111);
		efficiency3.setProjectId(111);
		efficiency4.setProjectId(111);
		
		efficiency1.setProjectName("测试项目asdf");
		efficiency2.setProjectName("测试项目asdf");
		efficiency3.setProjectName("测试项目asdf");
		efficiency4.setProjectName("测试项目asdf");
		
		
		efficiencyList.add(efficiency1);
		efficiencyList.add(efficiency2);
		efficiencyList.add(efficiency3);
		efficiencyList.add(efficiency4);
		
		satisfactionService.insertSatisfactionByEfficiency(efficiencyList);
	}
	@Test
	public void satisfactionCalTest() {
		//生成月度员工效能
        satisfactionResultService.calculatorSatisfactionResult();
	}
}
