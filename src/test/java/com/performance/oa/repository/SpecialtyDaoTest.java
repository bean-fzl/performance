package com.performance.oa.repository;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;

import com.performance.oa.entity.Specialty;

public class SpecialtyDaoTest extends BaseTest{
	@Autowired
	private SpecialtyDao specialtyDao;	
	@Test
	public void testAdd(){
		Specialty specialty = new Specialty();
		specialty.setName("诚实、任劳任怨");
		specialty.setDepartmentId(56l);
		specialty.setDepartmentName("平台研发MT");
		specialty.setMt("张三");
		specialty.setCreateTime(new Date());
		//System.out.println("保存前==================："+specialty.getId());
		specialtyDao.save(specialty);
		System.out.println("保存后==================："+specialty.getId());
	}
	@Test
	public void testQuery(){		
		final Specialty specialty = new Specialty();
		//specialty.setName("积极，乐观向上");
		specialty.setDepartmentId(56l);
		specialty.setDepartmentName("平台研发MT");
		specialty.setMt("张三");
		Specification<Specialty> spec=new Specification<Specialty>() {			
			@Override
			public Predicate toPredicate(Root<Specialty> root, CriteriaQuery<?> query,CriteriaBuilder cb) {
				List<Predicate> predList = new ArrayList<Predicate>();
				
				if(StringUtils.isNotBlank(specialty.getName())){
					predList.add(cb.equal(root.get("name").as(String.class), specialty.getName()));
				}
				if(StringUtils.isNotBlank(specialty.getMt())){
					predList.add(cb.equal(root.get("mt").as(String.class), specialty.getMt()));
				}
				if(StringUtils.isNotBlank(specialty.getDepartmentName())){					
					predList.add(cb.equal(root.get("departmentName").as(String.class), specialty.getDepartmentName()));
				}
				 SimpleDateFormat f = new SimpleDateFormat("yyyy-MM");
                 predList.add(cb.equal(cb.substring(root.get("createTime").as(String.class), 1, 7),
                         f.format(new Date("2015/12/01"))));               
				Predicate[] ps = new Predicate[predList.size()];
				query.where(cb.and(predList.toArray(ps)));                  
                return query.getGroupRestriction();				
			}
		};	
		List<Specialty> list = specialtyDao.findAll(spec);
		for (Specialty spe : list) {
			System.out.println("查询结果：$$$$$$$$$$$$$$$$"+spe.getName());
		}
	}
}
