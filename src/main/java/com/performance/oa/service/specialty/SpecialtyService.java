package com.ync365.oa.service.specialty;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.ync365.oa.bo.SpecialtyVo;
import com.ync365.oa.entity.Specialty;
import com.ync365.oa.query.SpecialtyQuery;
import com.ync365.oa.repository.DepartmentDao;
import com.ync365.oa.repository.EmployeDao;
import com.ync365.oa.repository.SpecialtyDao;
import com.ync365.oa.repository.SpecialtyJXDao;


/**
 * 专业性管理的业务逻辑类
 * @author lyh
 *
 */
@Component
@Transactional
public class SpecialtyService {	
	@Autowired
	private SpecialtyDao specialtyDao;		
	@Autowired
	private SpecialtyJXDao specialtyJXDao;	
	@Autowired
	private EmployeDao employeDao;	
	@Autowired
	private DepartmentDao departmentDao;	
	
	/**
	 * 添加新的专业性
	 */
	public void addSpecialty(SpecialtyVo specialtyVo){
		if(specialtyVo.getSpecialtyList().size()>0){
			for (Specialty specialty : specialtyVo.getSpecialtyList()) {
				specialty.setDepartmentId(specialtyVo.getDepartmentId());
				specialty.setDepartmentName(specialtyVo.getDepartmentName());
				specialty.setMt(specialtyVo.getMt());
				specialty.setCreateTime(new Date());
				specialtyDao.save(specialty);
			}			
		}		
	}
	
	/**
	 * 删除专业性
	 */
	public void deleteSpecialty(Long id){		
		specialtyDao.delete(id);			
	}
	
	/**
	 * 多条件组合查询某个部门的专业性
	 * @return List<Specialty>
	 */
	public List<Specialty> getSpecialtyByMultiple(final SpecialtyQuery specialty){		
		Specification<Specialty> spec=new Specification<Specialty>() {			
			@Override
			public Predicate toPredicate(Root<Specialty> root, CriteriaQuery<?> query,
					CriteriaBuilder cb) {
				List<Predicate> predList = new ArrayList<Predicate>();
			
				if(StringUtils.isNotBlank(specialty.getName())){
					predList.add(cb.equal(root.get("name").as(String.class), specialty.getName()));
				}
				if(StringUtils.isNotBlank(specialty.getMt())){
					predList.add(cb.equal(root.get("mt").as(String.class), specialty.getMt()));
				}				  
				if(specialty.getDepartmentId()!=null){					
					predList.add(cb.equal(root.get("departmentId").as(Long.class), specialty.getDepartmentId()));
				}					
				Predicate[] ps = new Predicate[predList.size()];
                query.where(cb.and(predList.toArray(ps)));                
                return query.getGroupRestriction();
			}
		};	
		return specialtyDao.findAll(spec);
	}
	
	/**
	 * 获取所有的专业性
	 * @return List<Specialty>
	 */
	public List<Specialty> getAllSpecialty(){
		Specification<Specialty> spec=new Specification<Specialty>() {
			@Override
			public Predicate toPredicate(Root<Specialty> root,
					CriteriaQuery<?> query, CriteriaBuilder cb) {
				//按照部门排序
				query.orderBy(cb.asc(root.get("departmentId")));				
				return query.getGroupRestriction();
			}
		};		
		return specialtyDao.findAll(spec);
	}
	/**
	 * 根据专业性id查询某个专业性
	 * @return Specialty
	 */
	public Specialty getSpecialtyById(Long specialtyId) {		
		return specialtyDao.findOne(specialtyId);
	}
	/**
	 * 检测某个部门的专业性个数
	 * @param departmentId
	 * @return
	 */
	public int checkSpecialtyNumByDepartmentId(final Long departmentId) {
		Specification<Specialty> spec=new Specification<Specialty>() {
			@Override
			public Predicate toPredicate(Root<Specialty> root,
					CriteriaQuery<?> query, CriteriaBuilder cb) {				
				if(departmentId!=null){
					query.where(cb.equal(root.get("departmentId"),departmentId));	
				}					
				return query.getGroupRestriction();
			}
		};	
		List<Specialty> list = specialtyDao.findAll(spec);
		if(list!=null){
			return list.size();
		}else{
			return 0;
		}		
	}	
}
