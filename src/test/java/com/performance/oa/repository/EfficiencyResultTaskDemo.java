package com.performance.oa.repository;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.performance.oa.service.efficiency.EfficiencyService;
import com.performance.oa.service.efficiencyResult.EfficiencyResultService;
import com.performance.oa.service.employe.EmployeService;

public class EfficiencyResultTaskDemo extends BaseTest{
	
	@Autowired
	private EfficiencyService efficiencyService;
	
	@Autowired
	private EmployeService employeService;
	
	@Autowired
	private EfficiencyResultService  efficiencyResultService; 
	
	/*@Test
	public void test(){
		Long employeId=2l;
		Date data=new Date();
		 
		EfficiencyResult er=efficiencyResultService.findEfficiencyResult(employeId, data);
		
		if(er!=null){
			System.out.println("+++++++++++++++++++++");
			System.out.println("id>>>>>>>"+er.getId());
			System.out.println("负荷率>>>>"+er.getLoadRate());
			System.out.println("效率>>>>>>"+er.getEfficiencyPercentage());
			System.out.println("总分>>>>>>"+er.getEfficiencyTotalScore());
		}
	}*/
	
	@Test
	public void test(){
		
		/*Calendar cal = Calendar.getInstance();
		final Date calDate = cal.getTime();
		final String code="121";
		List<Efficiency> es=efficiencyService.findEfficiencyByEmployeCodeAndTime(code, calDate);
		System.out.println("121员工的效能列表"+es.size());*/
		
		efficiencyResultService.calcEfficiencyResult();
		/*EmployeQuery employeQuery =new EmployeQuery();
		List<Employe> es=employeService.find(employeQuery).getContent();
		System.out.println("SIZE"+es.size());*/
		/*Calendar cal=Calendar.getInstance();
		cal.add(Calendar.MONTH, -1);
		Date calDate =cal.getTime();
		
		List<Efficiency> efs=efficiencyService.findEfficiencyByEmployeCodeAndTime(79l, calDate);
		System.out.println("size>>>>"+efs.size());*/
	}
	
	
	
}
