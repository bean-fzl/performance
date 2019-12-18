package com.ync365.oa.service.department;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import com.ync365.oa.entity.Department;
import com.ync365.oa.entity.Employe;
import com.ync365.oa.query.DepartmentQuery;
import com.ync365.oa.repository.DepartmentDao;
import com.ync365.oa.repository.EmployeDao;

@Component
@Transactional
public class DepartmentService {
	private static Logger logger=LoggerFactory.getLogger(DepartmentService.class);
	
	@Autowired
	private DepartmentDao departmentDao;
	
	@Autowired
	private EmployeDao employeDao;
	
	//添加部门
	public void add(Department d){
		d.setCreateTime(Calendar.getInstance().getTime());
		departmentDao.save(d);
	}
	
	//查看单个部门
	public Department findOne(Long id){
		return departmentDao.findOne(id);
	}
	
	//编辑员工
	public void update(Department d){
		//d.setCreateTime(Calendar.getInstance().getTime());
		//departmentDao.save(d);
		
		Department department = findOne(d.getId());
		BeanUtils.copyProperties(d, department);
		departmentDao.save(department);
	}
	
	//删除部门
	public void delete(Long id){
		departmentDao.delete(id);
	}
	//查询全部
	public List<Department> getAll(){
		return (List<Department>) departmentDao.findAll();
	}
	//查询员工类表
	public List<Department> find(final DepartmentQuery d){
		
		Specification<Department> sp=new Specification<Department>() {

			@Override
			public Predicate toPredicate(Root<Department> root,
					CriteriaQuery<?> query, CriteriaBuilder cb) {
				// TODO Auto-generated method stub
				List<Predicate> list=new ArrayList<>();
				if(d.getId()!=null){
					list.add(cb.equal(root.get("id").as(Long.class),d.getId()));
				}
				if(StringUtils.isNotEmpty(d.getMt())){
					list.add(cb.like(root.get("mt").as(String.class), "%"+d.getMt()+"%"));
				}
				if(StringUtils.isNotEmpty(d.getDepartmentName())){
					list.add(cb.like(root.get("name").as(String.class),"%"+d.getDepartmentName()+"%"));
				}
				
				
				Predicate[] ps = new Predicate[list.size()];
                query.where(cb.and(list.toArray(ps)));
                if (StringUtils.isNotEmpty(d.getSort())) {
                    query.orderBy(cb.desc(root.get(d.getSort())));
                }
				return query.getGroupRestriction();
			}
		};
		PageRequest pageRequest =null;
		if (d.getPageIndex() != null && d.getPageSize() != null) {
            pageRequest = new PageRequest(d.getPageIndex(), d.getPageSize());
        }
		
		List<Department> dlist=departmentDao.findAll(sp);

		Page<Department> pages=departmentDao.findAll(sp,pageRequest);
		if(StringUtils.isNotBlank(d.getEployeName())){
			List<Department> list=new ArrayList<Department>();
			for(Department dept:dlist){
				for(Employe e:dept.getEs()){
					if(e.getName().equals(d.getEployeName())){
						list.add(dept);
						break;
					}
				}
			}
			return list;
		}else{
			return dlist;
		}
	}
}
