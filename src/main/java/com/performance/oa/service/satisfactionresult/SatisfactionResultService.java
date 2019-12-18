package com.ync365.oa.service.satisfactionresult;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import com.ync365.commons.utils.StringUtils;
import com.ync365.oa.entity.Satisfaction;
import com.ync365.oa.entity.SatisfactionResult;
import com.ync365.oa.query.SatisfactionQuery;
import com.ync365.oa.query.SatisfactionResultQuery;
import com.ync365.oa.repository.SatisfactionDao;
import com.ync365.oa.repository.SatisfactionResultDao;
import com.ync365.oa.service.satisfaction.SatisfactionService;

@Component
@Transactional
public class SatisfactionResultService {

	private Logger log = LoggerFactory.getLogger(SatisfactionResultService.class);

	@Autowired
	private SatisfactionResultDao satisfactionResultDao;

	@Autowired
	private SatisfactionDao satisfactionDao;

	@Autowired
	private SatisfactionService satisfactionService;

	/**
	 * 添加客户满意度结果记录
	 * 
	 * @param satisfactionResult
	 * @return
	 */
	public SatisfactionResult add(SatisfactionResult satisfactionResult) {
		return satisfactionResultDao.save(satisfactionResult);
	}

	/**
	 * 根据id查询客户满度结果记录
	 * 
	 * @param id
	 * @return
	 */
	public SatisfactionResult findOne(Long id) {
		return satisfactionResultDao.findOne(id);
	}

	/**
	 * 更新记录
	 * 
	 * @param satisfactionResult
	 * @return
	 */
	public SatisfactionResult update(SatisfactionResult satisfactionResult) {
		return satisfactionResultDao.save(satisfactionResult);
	}

	/**
	 * 客户满意度结果分页查询
	 * 
	 * @param q
	 * @return
	 */
	public Page<SatisfactionResult> buildPageRequest(final SatisfactionResultQuery q) {
		Specification<SatisfactionResult> sp = buildSpecification(q);
		PageRequest pageRequest = null;
		if (q.getPageIndex() != null && q.getPageSize() != null) {
			pageRequest = new PageRequest(q.getPageIndex(), q.getPageSize());
		}
		Page<SatisfactionResult> pages = null;
		pages = satisfactionResultDao.findAll(sp, pageRequest);
		return pages;
	}

	/**
	 * 分数计算
	 * 
	 * @return
	 */
	public Integer calculatorSatisfactionResult() {
		//若多次执行，则删除上次生成的数据。
		SatisfactionResultQuery srq = new SatisfactionResultQuery();
		srq.setCreateTime(new Date());
		Specification<SatisfactionResult>  sp = buildSpecification(srq);
		List <SatisfactionResult> listDel = satisfactionResultDao.findAll(sp);
		satisfactionResultDao.delete(listDel);
		//添加客户满意度互评结果信息
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, -1);
		Date calDate = cal.getTime();
		List<Satisfaction> list = new ArrayList<Satisfaction>();
		SatisfactionQuery satisfactionQuery = new SatisfactionQuery();
		satisfactionQuery.setEvaluatedTime(calDate);
		list = satisfactionService.findAll(satisfactionQuery);
		List<SatisfactionResult> listResult = new ArrayList<SatisfactionResult>();
		Map<Long, Integer> countMap = new HashMap<Long, Integer>();
		for (Satisfaction satisfaction : list) {
			boolean haveItem = false;
			for (SatisfactionResult satisfactionResult : listResult) {
				if (satisfactionResult.getBeEvaluatedId().equals(satisfaction.getBeEvaluatedId())
						&& satisfaction.getScore() != null) {// 分数不能为空
					satisfactionResult.setScore(new BigDecimal(satisfactionResult.getScore())
							.add(new BigDecimal(satisfaction.getScore())).doubleValue());// 计算总分
					haveItem = true;
				}
			}
			if (!haveItem && satisfaction.getScore() != null) {
				SatisfactionQuery q = new SatisfactionQuery();
				q.setBeEvaluatedId(satisfaction.getBeEvaluatedId());
				q.setEvaluatedTime(calDate);
				SatisfactionResult satisfactionResult = new SatisfactionResult();
				satisfactionResult.setBeEvaluatedDepartmentId(satisfaction.getBeEvaluatedDepartmentId());
				satisfactionResult.setBeEvaluatedDepartmentName(satisfaction.getBeEvaluatedDepartmentName());
				satisfactionResult.setBeEvaluatedId(satisfaction.getBeEvaluatedId());
				satisfactionResult.setBeEvaluatedName(satisfaction.getBeEvaluatedName());
				satisfactionResult.setCreateTime(new Date());
				satisfactionResult.setProjectNum(satisfactionService.getProjectNum(q));
				satisfactionResult.setScore(satisfaction.getScore());
				satisfactionResult.setEvaluatedTime(calDate);
				listResult.add(satisfactionResult);
			}
			if ((!countMap.containsKey(satisfaction.getBeEvaluatedId())) && satisfaction.getScore() != null) {// 计数
				countMap.put(satisfaction.getBeEvaluatedId(), 1);
			} else {
				countMap.put(satisfaction.getBeEvaluatedId(), countMap.get(satisfaction.getBeEvaluatedId())+1);
			}
		}
		for (SatisfactionResult satisfactionResult : listResult) {// 求平均
			Integer count = countMap.get(satisfactionResult.getBeEvaluatedId());
			satisfactionResult.setScore(
					new BigDecimal(satisfactionResult.getScore()).divide(new BigDecimal(count), 1,RoundingMode.HALF_UP).doubleValue());
			satisfactionResultDao.save(satisfactionResult);

		}
		return null;
	}

	/**
	 * 获取单个员工月度结果得分
	 * 
	 * @param id
	 * @param date
	 * @return
	 */
	public Double getScoreByDateEmployeId(Long id, Date date) {
		SatisfactionResultQuery q = new SatisfactionResultQuery();
		q.setCreateTime(date);
		q.setBeEvaluatedId(id);
		Specification<SatisfactionResult> sp = buildSpecification(q);
		SatisfactionResult satisfactionResult = satisfactionResultDao.findOne(sp);
		if(null == satisfactionResult) {
			return (double) 0l;
		}
		return satisfactionResult.getScore();

	}

	/**
	 * 查询条件处理方法
	 * 
	 * @param q
	 * @return
	 */
	private Specification<SatisfactionResult> buildSpecification(final SatisfactionResultQuery q) {
		Specification<SatisfactionResult> sp = new Specification<SatisfactionResult>() {

			@Override
			public Predicate toPredicate(Root<SatisfactionResult> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> list = new ArrayList<>();
				if (q.getId() != null) {
					list.add(cb.equal(root.get("id").as(Long.class), q.getId()));
				}
				if (q.getEvaluatedTime() != null) {
					SimpleDateFormat f = new SimpleDateFormat("yyyy-MM");
					list.add(cb.equal(cb.substring(root.get("evaluatedTime").as(String.class), 1, 7),
							f.format(q.getEvaluatedTime())));
				}
				if (q.getCreateTime() != null) {
					SimpleDateFormat f = new SimpleDateFormat("yyyy-MM");
					list.add(cb.equal(cb.substring(root.get("createTime").as(String.class), 1, 7),
							f.format(q.getCreateTime())));
				}
				if (StringUtils.isNotEmpty(q.getBeEvaluatedDepartmentName())) {
					list.add(cb.equal(root.get("beEvaluatedDepartmentName").as(String.class),
							q.getBeEvaluatedDepartmentName()));
				}
				if (StringUtils.isNotEmpty(q.getBeEvaluatedName())) {
					list.add(cb.equal(root.get("beEvaluatedName"), q.getBeEvaluatedName()));
				}
				if (q.getScore() != null) {
					list.add(cb.equal(root.get("score"), q.getScore()));
				}
				if (q.getProjectNum() != null) {
					list.add(cb.equal(root.get("projectNum"), q.getProjectNum()));
				}
				if (q.getBeEvaluatedDepartmentId() != null) {
					list.add(cb.equal(root.get("beEvaluatedDepartmentId"), q.getBeEvaluatedDepartmentId()));
				}
				if (q.getBeEvaluatedId() != null) {
					list.add(cb.equal(root.get("beEvaluatedId"), q.getBeEvaluatedId()));
				}
				Predicate[] ps = new Predicate[list.size()];
				query.where(cb.and(list.toArray(ps)));
				return query.getGroupRestriction();
			}
		};
		return sp;
	}
}
 