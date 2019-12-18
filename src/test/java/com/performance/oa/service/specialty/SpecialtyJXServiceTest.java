package com.performance.oa.service.specialty;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;

import com.performance.oa.entity.Department;
import com.performance.oa.entity.Employe;
import com.performance.oa.entity.SpecialtyJX;
import com.performance.oa.repository.BaseTest;
import com.performance.oa.repository.DepartmentDao;
import com.performance.oa.repository.EmployeDao;
import com.performance.oa.repository.SpecialtyDao;
import com.performance.oa.repository.SpecialtyJXDao;
import com.performance.oa.service.department.DepartmentService;
import com.performance.oa.service.efficiencyResult.EfficiencyResultService;
import com.performance.oa.service.employe.EmployeService;
import com.performance.oa.service.leaderAssessment.LeaderAssessmentService;
import com.performance.oa.service.pecontroller.PeControllerService;
import com.performance.oa.service.performanceevaluation.PerformanceEvaluationService;
import com.performance.oa.service.properties.PropertiesService;
import com.performance.oa.service.satisfactionresult.SatisfactionResultService;
import com.performance.oa.service.task.TaskService;

public class SpecialtyJXServiceTest extends BaseTest{
	@Autowired
	private SpecialtyJXDao specialtyJXDao;
	@Autowired
	private SpecialtyDao specialtyDao;
	@Autowired
	private EmployeDao employeDao;
	@Autowired
	private DepartmentDao departMentDao;
	
	private Logger log = LoggerFactory.getLogger(TaskService.class);
    @Autowired
    private PropertiesService s;
    @Autowired
    private PeControllerService peControllerService;
    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private SatisfactionResultService satisfactionResultService;
    SimpleDateFormat f = new SimpleDateFormat("yyyy-MM");
    @Autowired
    private EmployeService employeService;
    @Autowired
    private PerformanceEvaluationService performanceEvaluationService;
    @Autowired
    private EfficiencyResultService efficiencyResultService;
    @Autowired
    private LeaderAssessmentService assessmentService;
    @Autowired
    private SpecialtyJXService specialtyJXService;
	
	@Test
	public void testGetSpecialtyJX(){
		//List<SpecialtyJX> list = specialtyJXDao.findAll(spec);
		List<SpecialtyJX> list = new ArrayList<SpecialtyJX>();
		SpecialtyJX jx1 = new SpecialtyJX();		
		SpecialtyJX jx2 = new SpecialtyJX();
		SpecialtyJX jx3 = new SpecialtyJX();
		SpecialtyJX jx4 = new SpecialtyJX();
		jx1.setScore(4d);
		jx2.setScore(5d);
		jx3.setScore(7d);
		jx4.setScore(8d);
		list.add(jx1);
		list.add(jx2);
		list.add(jx3);
		list.add(jx4);
		if(list!=null&&list.size()>0){
			Double totalScore = 0d;
			for (SpecialtyJX specialtyJX : list) {
				//计算某个被评价人的专业性总分
				totalScore += specialtyJX.getScore();				
			}
			SpecialtyJX specJX = list.get(2);
			specJX.setTotalScore(totalScore);
			System.out.println("小张的专业评价总分为：=============="+specJX.getTotalScore());
		}
		
	}
	@Test
	public void addSpecialtyJXList(){
			
		
	}
	@Test
	public void getEmployesByDepartmentId(){
		Specification<Employe> spec = new Specification<Employe>() {
			@Override
			public Predicate toPredicate(Root<Employe> root,
					CriteriaQuery<?> query, CriteriaBuilder cb) {				
				return cb.and(cb.equal(root.get("departmentId"),2));
			}			
		};		
		List<Employe> empList = employeDao.findAll(spec);
		for (Employe employe : empList) {
			System.out.println(employe.getName()+"==========的部门是："+employe.getDepartmentName());
		}		
	}
	@Test
	public void testInit(){
		
	}
	@Test
	//测试定时任务生成列表数据
	public void testCreateData(){
		log.info("doNotifyMT >> 每月1号0点执行");
        List<Department> list = departmentService.getAll();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -1);
        for (Department department : list) {
            peControllerService.checkIsProcessed(department.getId(), department.getName(),
                    f.format(calendar.getTime()));
        }
        //生成月度客户评价
        //satisfactionResultService.calculatorSatisfactionResult();
        //生成月度上级评价
        //assessmentService.createLeaderAssessment();
        //生成月度员工效能
        //efficiencyResultService.calcEfficiencyResult();
        //生成专业评价
        specialtyJXService.initSpecialtyJXList();
	}
}
