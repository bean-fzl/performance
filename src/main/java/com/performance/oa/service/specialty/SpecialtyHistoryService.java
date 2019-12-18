package com.ync365.oa.service.specialty;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.ync365.oa.entity.SpecialtyHistory;
import com.ync365.oa.repository.SpecialtyHistoryDao;

/**
 * 专业性历史的业务逻辑类
 * @author lyh
 *
 */
@Component
@Transactional
public class SpecialtyHistoryService {
	@Autowired
	private SpecialtyHistoryDao specialtyHistoryDao;
	
	/**
	 * 按月份获取某个部门的专业性
	 * 
	 * @param departmentId
	 * @param createTimeAdd
	 * @return
	 */
	public List<SpecialtyHistory> getSpecialtyListByDepartmentId(final Long departmentId,final Date createTime) {
		// 查询该部门所有的专业性
		Specification<SpecialtyHistory> spec1 = new Specification<SpecialtyHistory>() {
			@Override
			public Predicate toPredicate(Root<SpecialtyHistory> root,
					CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predList = new ArrayList<Predicate>();
				if(departmentId!=null){
					predList.add(cb.and(cb.equal(root.get("departmentId"),
							departmentId)));					}
				
				if (createTime != null) {
					SimpleDateFormat f = new SimpleDateFormat("yyyy-MM");
					predList.add(cb.equal(cb.substring(root.get("createTime")
							.as(String.class), 1, 7), f.format(createTime)));
				}
				Predicate[] ps = new Predicate[predList.size()];
				query.where(cb.and(predList.toArray(ps)));
				return query.getGroupRestriction();
			}
		};		
		return specialtyHistoryDao.findAll(spec1);
	}
	
}
