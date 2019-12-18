package com.ync365.oa.service.efficiencyResult;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import com.ync365.commons.utils.WorkTimeCalUtils;
import com.ync365.oa.entity.Department;
import com.ync365.oa.entity.Efficiency;
import com.ync365.oa.entity.EfficiencyResult;
import com.ync365.oa.entity.Employe;
import com.ync365.oa.query.DepartmentQuery;
import com.ync365.oa.query.EfficiencyQuery;
import com.ync365.oa.query.EfficiencyResultQuery;
import com.ync365.oa.query.EmployeQuery;
import com.ync365.oa.repository.EfficiencyDao;
import com.ync365.oa.repository.EfficiencyResultDao;
import com.ync365.oa.repository.EmployeDao;
import com.ync365.oa.service.department.DepartmentService;
import com.ync365.oa.service.efficiency.EfficiencyService;
import com.ync365.oa.service.employe.EmployeService;
import com.ync365.oa.service.properties.PropertiesService;

@Component
@Transactional
public class EfficiencyResultService {
	
	private Logger log= LoggerFactory.getLogger(EfficiencyResult.class);
	
	private static final int h=8;//每天标准出勤工时

	@Autowired
	private EfficiencyResultDao  efficiencyResultDao;
	
	@Autowired
	private EfficiencyDao efficiencyDao;
	
	@Autowired
	private EmployeService employeService;
	
	@Autowired
	private EfficiencyService efficiencyService; 
	
	@Autowired
	private EmployeDao employeDao; 
	
	@Autowired
	private DepartmentService departmentservice; 
	/***
	 * 插入月度绩效
	 */
	public void add(EfficiencyResult efficiencyResult){
		 efficiencyResultDao.save(efficiencyResult);
	}
	/**
	 * 查询单个
	 * */
	public EfficiencyResult findOne(Long id){
		return efficiencyResultDao.findOne(id);
	}
	
	/***
	 *  查询月度绩效
	 * @return
	 */
	public Page<EfficiencyResult> find(final EfficiencyResultQuery erq){
		Specification<EfficiencyResult> sp=new Specification<EfficiencyResult>() {

			@Override
			public Predicate toPredicate(Root<EfficiencyResult> root,
					CriteriaQuery<?> query, CriteriaBuilder cb) {
				// TODO Auto-generated method stub
				List<Predicate> list=new ArrayList<>();
				
				if(StringUtils.isNotEmpty(erq.getEmployeName())){
					list.add(cb.like(root.get("employeName").as(String.class),"%"+erq.getEmployeName()+"%"));
				}
				if(StringUtils.isNotEmpty(erq.getEmployeCode())){
					list.add(cb.like(root.get("employeCode").as(String.class),"%"+erq.getEmployeCode()+"%"));
				}
				if(erq.getProjectCount()!=null){
					list.add(cb.equal(root.get("projectCount").as(Integer.class),erq.getProjectCount()));
				}
				if(StringUtils.isNotEmpty(erq.getDepartmentName())){
					list.add(cb.equal(root.get("departmentName").as(String.class), erq.getDepartmentName()));
				}
				if(erq.getActualHours()!=null){
					list.add(cb.equal(root.get("actualHours").as(Integer.class), erq.getActualHours()));
				}
			  if (erq.getCreateTime() != null) {
                    SimpleDateFormat f = new SimpleDateFormat("yyyy-MM");
                    list.add(cb.equal(cb.substring(root.get("createTime").as(String.class), 1, 7),
                            f.format(erq.getCreateTime())));
                }
				Predicate[] ps=new Predicate[list.size()];
				query.where(cb.and(list.toArray(ps)));
				if (StringUtils.isNotEmpty(erq.getSort())) {
                    query.orderBy(cb.desc(root.get(erq.getSort())));
                }
				return query.getGroupRestriction();
			}
		};
		PageRequest pageRequest =null;
		if (erq.getPageIndex() != null && erq.getPageSize() != null) {
            pageRequest = new PageRequest(erq.getPageIndex(),erq.getPageSize());
        }
		Page<EfficiencyResult> pages=null;
		pages=efficiencyResultDao.findAll(sp,pageRequest);
		return pages;
	}
	
	/***
	 * 查询某个月该名员工的效能
	 */
	
	public List<Efficiency> findEmployeEfficiency(final EfficiencyQuery eq){
		Specification<Efficiency> sp=new Specification<Efficiency>() {

			@Override
			public Predicate toPredicate(Root<Efficiency> root,
					CriteriaQuery<?> query, CriteriaBuilder cb) {
				// TODO Auto-generated method stub
				List<Predicate> list=new ArrayList<>();
				if(eq.getEmployeId()!=null){
					list.add(cb.equal(root.get("employeId").as(Integer.class), eq.getEmployeId()));
				}
				if(eq.getCreateTime()!=null){
					SimpleDateFormat f=new SimpleDateFormat("yyyy-MM");
					list.add(cb.equal(cb.substring(root.get("planBeginTime").as(String.class),1,7), f.format(eq.getCreateTime())));
				}
				Predicate[] ps=new Predicate[list.size()];
				query.where(cb.and(list.toArray(ps)));
				return query.getGroupRestriction();
			}
		};
		
		return efficiencyDao.findAll(sp);
		 
	}
	
	/***
	 * 查询项目下面的员工效能
	 * 
	 */
	public List<Efficiency> selectEfficiencyByProjectId(final Integer id){
		Specification<Efficiency> sp=new Specification<Efficiency>() {

			@Override
			public Predicate toPredicate(Root<Efficiency> root,
					CriteriaQuery<?> query, CriteriaBuilder cb) {
				// TODO Auto-generated method stub
				if(id!=null){
					Predicate p1=cb.equal(root.get("employeId").as(Integer.class), id);
					query.where(p1);
				}				
				return query.getRestriction();
			}
		};
		List<Efficiency> efficiencyList=efficiencyDao.findAll(sp);
		return efficiencyList;
	}
	
	/****
	 * 月度效能绩效定时任务调度方法
	 * @throws ParseException 
	 */
	public void calcEfficiencyResult(){
		/*当前时间*/
		Calendar cal = Calendar.getInstance();
		/*月初总计上一个月的绩效*/
		cal.add(Calendar.MONTH, -1);
		Date calDate = cal.getTime();

		/*查找所有的员工*/
		EmployeQuery employeQuery=new EmployeQuery();
		List<Employe> es=employeService.find(employeQuery).getContent();
		/*做定时把所有员工的月度效能查找出来并且插入到效能结果表中*/
		DepartmentQuery departQuery=new DepartmentQuery();
		List<Department> depts=departmentservice.find(departQuery);
		for(Department d:depts){
			for(Employe e:d.getEs()){
				List<Efficiency> efs=efficiencyService.findEfficiencyByEmployeCodeAndTime(e.getId(), calDate);
				if(efs.size()!=0){
					EfficiencyResult efficiencyResult= calcEfficiency(efs);
					add(efficiencyResult);
				}
			}
		}
	}
	/**
     * 算分方法
	 * @throws ParseException 
     * */
    private EfficiencyResult calcEfficiency(List<Efficiency> efs){
    	Integer planHoursTotal=0;//计划工时
    	Integer outputHoursTotal=0;//产出工时
    	Integer basicHoursTotal=0;//标准工时
    	Integer actualHoursTotal=0;//实际工时
    	
    	Double loadRate=0.0;//负荷率
    	Double efficiencyPercentage=0.0;//效率
    	Double efficiencyTotalScore=0.0;//总分
    	
    	//计算月份
    	Calendar cal=Calendar.getInstance();
    	cal.setTime(efs.get(0).getPlanBeginTime());
    	SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
    	String dateTime=sdf.format(cal.getTime());
    	/*
    	 * 计算标准工时
    	 * */
    	String strDateStart=WorkTimeCalUtils.getMinMonthDate(dateTime);
    	String strDateEnd=WorkTimeCalUtils.getMaxMonthDate(dateTime);
    	String day=WorkTimeCalUtils.WorkTimeCalUtils(strDateStart, strDateEnd);
    	//每月的工作日*9等於標準工時
    	basicHoursTotal=(Integer.parseInt(day))*h;
    	
    	/*计算计划工时总和，产出工时总和，实际工时综合*/
    	for(Efficiency e:efs){
    	    //判空  为null 则不加
    		if(null != e ){
    		    if(null != e.getPlanHours() ){
    		        planHoursTotal += e.getPlanHours();
    		    }
    		    if(null != e.getActualHours() ){
    		        actualHoursTotal +=e.getActualHours();
    		    }
    		    if(null != e.getOutputHours()){
    		        outputHoursTotal +=e.getOutputHours();
    		    }
    		}
    	}
    	/***
    	 * 计算负荷率 效率 总分
    	 */
    	BigDecimal o=new BigDecimal(outputHoursTotal.toString());
    	BigDecimal b=new BigDecimal(basicHoursTotal.toString());
    	BigDecimal p=new BigDecimal(planHoursTotal.toString());
    	//负荷率计算：产出工时/标准工时*100    效率计算：计划工时/产出工时  总分：
    	if(basicHoursTotal !=0 ){
    		loadRate =o.divide(b,3,BigDecimal.ROUND_HALF_UP).doubleValue();  //精确到小数点后三位
    	}
    	if(outputHoursTotal !=0){
    		efficiencyPercentage=p.divide(o,3,BigDecimal.ROUND_HALF_UP).doubleValue();
    	}
    	
    	BigDecimal le=new BigDecimal(Double.toString(loadRate+efficiencyPercentage));
    	BigDecimal s=new BigDecimal(2);
    	//求出平均数
    	Double d=le.divide(s,3,BigDecimal.ROUND_HALF_UP).doubleValue();//效率和负荷率的平均数
    	BigDecimal da=new BigDecimal(Double.toString(d));
    	BigDecimal es=null;
    	 
    	if(PropertiesService.EFFICIENCY_SCORE == null || PropertiesService.EFFICIENCY_SCORE == 0){
    		efficiencyTotalScore=d;
    	}else{
    		es=new BigDecimal((PropertiesService.EFFICIENCY_SCORE).toString()).multiply(new BigDecimal(d)).setScale(1, BigDecimal.ROUND_HALF_UP);
    		if(es.doubleValue()>=PropertiesService.EFFICIENCY_SCORE ){
    			efficiencyTotalScore=es.doubleValue();
    		}else{
    			efficiencyTotalScore=es.doubleValue();
    		}
    	}
    	
    	BigDecimal loadRateResult=new BigDecimal(loadRate);
    	BigDecimal efficiencyPercentageResult=new BigDecimal(efficiencyPercentage);
    	
    	EfficiencyResult efficiencyResult=new EfficiencyResult();
    	efficiencyResult.setEmployeId(efs.get(0).getEmployeId());
    	efficiencyResult.setEmployeCode(efs.get(0).getEmployeCode());
    	efficiencyResult.setEmployeName(efs.get(0).getEmployeName());
    	efficiencyResult.setDepartmentId(efs.get(0).getDepartmentId());
    	efficiencyResult.setDepartmentName(efs.get(0).getDepartmentName());
    	efficiencyResult.setProjectCount(efs.size());
    	efficiencyResult.setPlanHours(planHoursTotal);
    	efficiencyResult.setActualHours(actualHoursTotal);
    	efficiencyResult.setOutputHours(outputHoursTotal);
    	efficiencyResult.setBasicHours(basicHoursTotal);
    	efficiencyResult.setLoadRate(loadRateResult.multiply(new BigDecimal(100)).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue());
    	efficiencyResult.setEfficiencyPercentage(efficiencyPercentageResult.multiply(new BigDecimal(100)).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue());
    	efficiencyResult.setEfficiencyTotalScore(efficiencyTotalScore);
    	efficiencyResult.setCreateTime(new Date());
    	
    	return efficiencyResult;
    }
    
    /***
     *查找单个员工的月度效能结果
     */
    public EfficiencyResult findEfficiencyResult(Long id , Date date){
    	Employe e=employeDao.findOne(id);
    	//配置查询条件员工编号和查询的时间
    	EfficiencyResultQuery q=new EfficiencyResultQuery();
    	q.setEmployeId(e.getId().intValue());
    	q.setCreateTime(date);
    	//查找单个员工的月度效能结果表
    	Specification<EfficiencyResult> sp=buildSpecification(q);
    	List<EfficiencyResult> efficiencyResult=efficiencyResultDao.findAll(sp);
    	return efficiencyResult!=null&&efficiencyResult.size()>0?efficiencyResult.get(0):null;
    }
    
    /***
     * 查询处理条件
     */
    private Specification<EfficiencyResult> buildSpecification(final EfficiencyResultQuery q) {
		Specification<EfficiencyResult> sp = new Specification<EfficiencyResult>() {

			@Override
			public Predicate toPredicate(Root<EfficiencyResult> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> list = new ArrayList<>();
				
				if (q.getCreateTime() != null) {
					SimpleDateFormat f = new SimpleDateFormat("yyyy-MM");
					list.add(cb.equal(cb.substring(root.get("createTime").as(String.class), 1, 7),
							f.format(q.getCreateTime())));
				}
				if (StringUtils.isNotEmpty(q.getEmployeCode())) {
					list.add(cb.equal(root.get("employeCode").as(String.class),
							q.getEmployeCode()));
				}
				if (null != q.getEmployeId()) {
					list.add(cb.equal(root.get("employeId").as(Long.class),
							q.getEmployeId()));
				}
				
				Predicate[] ps = new Predicate[list.size()];
				query.where(cb.and(list.toArray(ps)));
				return query.getGroupRestriction();
			}
		};
		return sp;
	}
}
