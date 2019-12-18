package com.ync365.oa.service.satisfaction;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import com.ync365.commons.utils.StringUtils;
import com.ync365.oa.bo.SatisfactionDetailPage;
import com.ync365.oa.bo.SatisfactionGroupByProject;
import com.ync365.oa.entity.Department;
import com.ync365.oa.entity.Efficiency;
import com.ync365.oa.entity.Employe;
import com.ync365.oa.entity.Project;
import com.ync365.oa.entity.Satisfaction;
import com.ync365.oa.entity.SatisfactionResult;
import com.ync365.oa.query.SatisfactionQuery;
import com.ync365.oa.repository.EmployeDao;
import com.ync365.oa.repository.ProjectDao;
import com.ync365.oa.repository.SatisfactionDao;
import com.ync365.oa.repository.SatisfactionResultDao;
import com.ync365.oa.service.department.DepartmentService;

@Component
@Transactional
public class SatisfactionService {

	private Logger log = LoggerFactory.getLogger(SatisfactionService.class);

	@Autowired
	private SatisfactionDao satisfactionDao;

	@Autowired
	private SatisfactionResultDao satisfactionResultDao;

	@Autowired
	private ProjectDao projectDao;

	@Autowired
	private EmployeDao employeDao;

	@Autowired
	private DepartmentService departmentService;

	/**
	 * 添加插入客户满意度调查
	 * 
	 * @param satisfaction
	 */
	public Satisfaction add(Satisfaction satisfaction) {
		return satisfactionDao.save(satisfaction);
	}

	/**
	 * 根据id查询客户满意度
	 * 
	 * @param id
	 * @return
	 */
	public Satisfaction findOne(Long id) {
		return satisfactionDao.findOne(id);
	}

	public Satisfaction findByIdEvaluatedId(Long id, Long evaluatedId) {
		return satisfactionDao.findByIdAndEvaluatedId(id, evaluatedId);
	}

	/**
	 * 更新客户满意度调查
	 * 
	 * @param satisfaction
	 * @return
	 */
	public Satisfaction update(Satisfaction satisfaction) {
		return satisfactionDao.save(satisfaction);
	}

	/**
	 * 根据被评人ID、项目Id查询条件查询列表
	 * 
	 * @param q
	 * @return
	 */
	public List<Satisfaction> searchByBeEvaIdProId(final Long beEvaluatedId, final Long projectId) {
		SatisfactionQuery q = new SatisfactionQuery();
		q.setBeEvaluatedId(beEvaluatedId);
		q.setProjectId(projectId);
		Specification<Satisfaction> sp = buildSpecification(q);
		List<Satisfaction> list = null;
		Sort sort = new Sort(Direction.DESC, "evaluatedName");
		list = satisfactionDao.findAll(sp, sort);
		return list;
	}

	/**
	 * 根据评分人、项目查询条件查询列表
	 * 
	 * @param q
	 * @return
	 */
	public List<Satisfaction> searchByEvaIdProId(final Long evaluatedId, final Long projectId) {
		SatisfactionQuery q = new SatisfactionQuery();
		q.setEvaluatedId(evaluatedId);
		q.setProjectId(projectId);
		Specification<Satisfaction> sp = buildSpecification(q);
		List<Satisfaction> list = null;
		Sort sort = new Sort(Direction.DESC, "beEvaluatedName");
		list = satisfactionDao.findAll(sp, sort);
		return list;
	}

	/**
	 * 查询员工所在项目信息
	 * 
	 * @param id
	 * @return
	 */
	public List<Satisfaction> searchProjectListByUserId(final Long id) {
		Specification<Satisfaction> sp = new Specification<Satisfaction>() {

			@Override
			public Predicate toPredicate(Root<Satisfaction> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> list = new ArrayList<>();
				if (id != null) {
					list.add(cb.equal(root.get("evaluatedId").as(Long.class), id));
				}
				Predicate[] ps = new Predicate[list.size()];
				query.where(cb.and(list.toArray(ps)));
				query.groupBy(root.get("projectId"));
				return query.getGroupRestriction();
			}
		};
		Sort sort = new Sort(Direction.DESC, "createTime");
		List<Satisfaction> list = null;
		list = satisfactionDao.findAll(sp, sort);
		return list;
	}

	/**
	 * 分页查询，返回结果将所在部门和被测评人名称拼接
	 * 
	 * @param q
	 * @return
	 */
	public Page<Satisfaction> findAllByPage(final SatisfactionQuery q) {
		Specification<Satisfaction> sp = buildSpecification(q);
		PageRequest pageRequest = null;
		if (q.getPageIndex() != null && q.getPageSize() != null) {
			pageRequest = new PageRequest(q.getPageIndex(), q.getPageSize());
		}
		Page<Satisfaction> pages = null;
		pages = satisfactionDao.findAll(sp, pageRequest);

		return pages;
	}

	/**
	 * 查询，返回结果将所在部门和被测评人名称拼接
	 * 
	 * @param q
	 * @return
	 */
	public List<Satisfaction> findAll(final SatisfactionQuery q) {
		Specification<Satisfaction> sp = buildSpecification(q);
		List<Satisfaction> list = new ArrayList<Satisfaction>();
		list = satisfactionDao.findAll(sp);
		return list;
	}

	/**
	 * 客户满意度详情页面
	 * 
	 * @param q
	 * @return
	 */
	public SatisfactionDetailPage searchGroupByProject(final Long id) {
		SatisfactionResult satisfactionResult = satisfactionResultDao.findOne(id);
		SatisfactionQuery q = new SatisfactionQuery();
		q.setBeEvaluatedId(satisfactionResult.getBeEvaluatedId());
		q.setEvaluatedTime(satisfactionResult.getEvaluatedTime());
		SatisfactionGroupByProject satisfactionGroupByProject = new SatisfactionGroupByProject();
		SatisfactionDetailPage satisfactionDetailPage = new SatisfactionDetailPage();
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM");
		SimpleDateFormat f1 = new SimpleDateFormat("yyyy-MM");
		Employe employe = employeDao.findOne(q.getBeEvaluatedId());
		satisfactionDetailPage.setProject(searchListGroupByProject(q));
		satisfactionDetailPage.setDetail(satisfactionGroupByProject);
		satisfactionDetailPage.setBeEvaluatedName(employe.getName());
		satisfactionDetailPage.setBeEvaluatedDepartmentName(employe.getDepartmentName());
		satisfactionDetailPage.setEvaluatedTime(f.format(q.getEvaluatedTime()));
		satisfactionDetailPage.setProjectNum(String.valueOf(getProjectNum(q)));
		satisfactionDetailPage.setTotalScore(satisfactionResult.getScore());
		return satisfactionDetailPage;
	}

	/**
	 * 按项目获取客户满意度列表
	 * 
	 * @param q
	 * @return
	 */
	private List<SatisfactionGroupByProject> searchListGroupByProject(final SatisfactionQuery q) {
		List<Satisfaction> temp = buildSatisfactionList(q);
		List<SatisfactionGroupByProject> list = new ArrayList<SatisfactionGroupByProject>();
		for (Satisfaction satisfaction : temp) {
			boolean isNewProject = true;
			for (SatisfactionGroupByProject satisfactionGroupByProject : list) {
				if (satisfactionGroupByProject.getProjectId().equals(satisfaction.getProjectId())) {
					satisfactionGroupByProject.getList().add(satisfaction);
					isNewProject = false;
				}
			}
			if (isNewProject) {
				Project project = projectDao.findOne(satisfaction.getProjectId());
				SatisfactionGroupByProject tempSGBP = new SatisfactionGroupByProject();
				tempSGBP.setProjectId(project.getId());
				tempSGBP.setProjectName(project.getName());
				List<Satisfaction> tempSatisfactionList = new ArrayList<Satisfaction>();
				tempSatisfactionList.add(satisfaction);
				tempSGBP.setList(tempSatisfactionList);
				list.add(tempSGBP);
			}
		}
		return list;
	}

	/**
	 * 获取项目数
	 * 
	 * @param q
	 * @return
	 */
	public Long getProjectNum(final SatisfactionQuery q) {
		Specification<Satisfaction> sp = buildSpecification(q);
		List<Satisfaction> list = satisfactionDao.findAll(sp);
		List<Satisfaction> temp =new ArrayList<Satisfaction>();
		for (Satisfaction satisfaction : list) {
			boolean flag = false;
			for (Satisfaction haveSatisfaction : temp) {
				if(satisfaction.getProjectId().equals(haveSatisfaction.getProjectId())) {
					flag = true;
				}
			}
			if(!flag) {
				temp.add(satisfaction);
			}
		}
		return Long.valueOf(String.valueOf(temp.size()));
	}

	/**
	 * 根据被测评人id查询
	 * 
	 * @param q
	 * @return
	 */
	private List<Satisfaction> buildSatisfactionList(final SatisfactionQuery q) {
		Specification<Satisfaction> sp = buildSpecification(q);
		List<Satisfaction> pages = null;
		pages = satisfactionDao.findAll(sp);
		return pages;
	}

	/**
	 * 查询条件生成通用方法
	 * 
	 * @param q
	 * @return
	 */
	private Specification<Satisfaction> buildSpecification(final SatisfactionQuery q) {
		Specification<Satisfaction> sp = new Specification<Satisfaction>() {

			@Override
			public Predicate toPredicate(Root<Satisfaction> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> list = new ArrayList<>();
				if (q.getId() != null) {
					list.add(cb.equal(root.get("id").as(Long.class), q.getId()));
				}
				if (!StringUtils.isBlank(q.getProjectName())) {
					list.add(cb.equal(root.get("projectName").as(String.class), q.getProjectName()));
				}
				if (q.getProjectId() != null) {
					list.add(cb.equal(root.get("projectId").as(Long.class), q.getProjectId()));
				}
				if (q.getEvaluatedTime() != null) {
					SimpleDateFormat f = new SimpleDateFormat("yyyy-MM");
					list.add(cb.equal(cb.substring(root.get("evaluatedTime").as(String.class), 1, 7),
							f.format(q.getEvaluatedTime())));
				}
				if (!StringUtils.isBlank(q.getBeEvaluatedDepartmentName())) {
					list.add(cb.equal(root.get("beEvaluatedDepartmentName").as(String.class),
							q.getBeEvaluatedDepartmentName()));
				}
				if (!StringUtils.isBlank(q.getProjectName())) {
					list.add(cb.equal(root.get("projectName").as(String.class), q.getProjectName()));
				}
				if (!StringUtils.isBlank(q.getBeEvaluatedName())) {
					list.add(cb.equal(root.get("beEvaluatedName"), q.getBeEvaluatedName()));
				}
				if (!StringUtils.isBlank(q.getEvaluatedName())) {
					list.add(cb.equal(root.get("evaluatedName"), q.getEvaluatedName()));
				}
				if (q.getScore() != null) {
					list.add(cb.equal(root.get("score"), q.getScore()));
				}
				if (q.getBeEvaluatedDepartmentId() != null) {
					list.add(cb.equal(root.get("beEvaluatedDepartmentId").as(Long.class),
							q.getBeEvaluatedDepartmentId()));
				}
				if (q.getBeEvaluatedId() != null) {
					list.add(cb.equal(root.get("beEvaluatedId").as(Long.class), q.getBeEvaluatedId()));
				}
				if (q.getEvaluatedId() != null) {
					list.add(cb.equal(root.get("evaluatedId").as(Long.class), q.getEvaluatedId()));
				}

				Predicate[] ps = new Predicate[list.size()];
				query.where(cb.and(list.toArray(ps)));
				return query.getGroupRestriction();
			}
		};
		return sp;
	}

	/**
	 * 效能添加用户后创建用户互评
	 * 
	 * @param efficiencyList
	 * @return
	 */
	public Integer insertSatisfactionByEfficiency(List<Efficiency> efficiencyList) {
		for (Efficiency efficiency : efficiencyList) {
			Department department = departmentService.findOne(efficiency.getDepartmentId().longValue());
				switch (department.getType()) {
				case 1: {
					for (Efficiency efficiencyed : efficiencyList) {
						Department departmented = departmentService.findOne(efficiencyed.getDepartmentId().longValue());
						if (4 == departmented.getType()) {
							if (null == satisfactionDao.findByBeEvaluatedIdAndEvaluatedIdAndProjectId(
									efficiencyed.getEmployeId().longValue(),
									efficiency.getEmployeId().longValue(),
									efficiencyed.getProjectId().longValue())) {
								Satisfaction satisfaction = getSatisfactionByEfficiency(efficiency, efficiencyed);// 产品评价测试
								satisfactionDao.save(satisfaction);
							}
						}
					}
					break;
				}
				case 2: {
					for (Efficiency efficiencyed : efficiencyList) {
						Department departmented = departmentService.findOne(efficiencyed.getDepartmentId().longValue());
						if (3 == departmented.getType()) {
							if (null == satisfactionDao.findByBeEvaluatedIdAndEvaluatedIdAndProjectId(
									efficiencyed.getEmployeId().longValue(),
									efficiency.getEmployeId().longValue(),
									efficiencyed.getProjectId().longValue())) {
								Satisfaction satisfaction = getSatisfactionByEfficiency(efficiency, efficiencyed);// 开发评价视觉创意
								satisfactionDao.save(satisfaction);
							}
						}
						if (1 == departmented.getType()) {
							if (null == satisfactionDao.findByBeEvaluatedIdAndEvaluatedIdAndProjectId(
									efficiencyed.getEmployeId().longValue(),
									efficiency.getEmployeId().longValue(), efficiencyed.getProjectId().longValue()))

							{
								Satisfaction satisfaction = getSatisfactionByEfficiency(efficiency, efficiencyed);// 开发评价产品
								satisfactionDao.save(satisfaction);
							}
						}
					}
					break;
				}
				case 3: {
					for (Efficiency efficiencyed : efficiencyList) {
						Department departmented = departmentService.findOne(efficiencyed.getDepartmentId().longValue());
						if (1 == departmented.getType()) {
							if (null == satisfactionDao.findByBeEvaluatedIdAndEvaluatedIdAndProjectId(
									efficiencyed.getEmployeId().longValue(),
									efficiency.getEmployeId().longValue(),
									efficiencyed.getProjectId().longValue())) {
								Satisfaction satisfaction = getSatisfactionByEfficiency(efficiency, efficiencyed);// ued评价产品
								satisfactionDao.save(satisfaction);
							}
						}
					}
					break;
				}
				case 4: {
					for (Efficiency efficiencyed : efficiencyList) {
						Department departmented = departmentService.findOne(efficiencyed.getDepartmentId().longValue());
						if (2 == departmented.getType()) {
							if (null == satisfactionDao.findByBeEvaluatedIdAndEvaluatedIdAndProjectId(
									efficiencyed.getEmployeId().longValue(),
									efficiency.getEmployeId().longValue(),
									efficiencyed.getProjectId().longValue())) {
								Satisfaction satisfaction = getSatisfactionByEfficiency(efficiency, efficiencyed);// 测试评价开发
								satisfactionDao.save(satisfaction);
							}
						}
					}
				}
					break;
				}
			}
		return 0;
	}

	/**
	 * 效能删除
	 * 
	 * @param efficiencyList
	 * @return
	 */
	public Integer deleteSatisfactionByEffciency(List<Efficiency> efficiencyList) {
		for (Efficiency efficiency : efficiencyList) {
			satisfactionDao.deleteByProjectIdAndEvaluatedId(efficiency.getProjectId().longValue(),
					efficiency.getEmployeId().longValue());

		}
		for (Efficiency efficiency : efficiencyList) {
			satisfactionDao.deleteByProjectIdAndBeEvaluatedId(efficiency.getProjectId().longValue(),
					efficiency.getEmployeId().longValue());
			
		}
		return null;
	}

	private Satisfaction getSatisfactionByEfficiency(Efficiency efficiency, Efficiency efficiencyed) {
		Satisfaction satisfaction = new Satisfaction();
		Date curDate = new Date();
		satisfaction.setProjectId(efficiency.getProjectId().longValue());
		satisfaction.setProjectName(efficiency.getProjectName());
		satisfaction.setEvaluatedId(efficiency.getEmployeId().longValue());
		satisfaction.setEvaluatedName(efficiency.getEmployeName());
		satisfaction.setCreateTime(curDate);
		satisfaction.setEvaluatedDepartmentId(efficiency.getDepartmentId().longValue());
		satisfaction.setEvaluatedDepartmentName(efficiency.getDepartmentName());
		satisfaction.setBeEvaluatedId(efficiencyed.getEmployeId().longValue());
		satisfaction.setBeEvaluatedName(efficiencyed.getEmployeName());
		satisfaction.setBeEvaluatedDepartmentId(efficiencyed.getDepartmentId().longValue());
		satisfaction.setBeEvaluatedDepartmentName(efficiencyed.getDepartmentName());
		return satisfaction;
	}
}
