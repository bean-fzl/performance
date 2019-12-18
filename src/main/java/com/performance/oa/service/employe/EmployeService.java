package com.ync365.oa.service.employe;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ync365.oa.entity.Employe;
import com.ync365.oa.query.EmployeQuery;
import com.ync365.oa.repository.EmployeDao;

@Component
@Transactional
public class EmployeService {
	private static Logger logger=LoggerFactory.getLogger(EmployeService.class);
	
	@Autowired
	private EmployeDao employeDao;
	
	//添加员工
	public void add(Employe e){
		e.setCreateTime(Calendar.getInstance().getTime());
		employeDao.save(e);
	}
	//查看单个员工
	public Employe findOne(long id){
		return employeDao.findOne(id);
	}
	
	//编辑员工
	public void update(Employe e){
		e.setCreateTime(Calendar.getInstance().getTime());
		employeDao.save(e);
	}
	//删除员工
	public void delete(Long id){
		employeDao.delete(id);
	}
	
	//密码重置
	public void passwdReset(Long id,String password){
		Employe e=employeDao.findOne(id);
		e.setPassword(password);
		employeDao.save(e);
	}
	
	//查看员工列表
	public Page<Employe> find(final EmployeQuery e){
		Specification<Employe> sp=new Specification<Employe>() {
			@Override
			public Predicate toPredicate(Root<Employe> root,
					CriteriaQuery<?> query, CriteriaBuilder cb) {
				// TODO Auto-generated method stub
				List<Predicate> list=new ArrayList<>();
				list.add(cb.equal(root.get("isDel").as(Integer.class),EmployeQuery.AVAILABLE));
				if(StringUtils.isNotEmpty(e.getName())){
					list.add(cb.like(root.get("name").as(String.class), "%" +e.getName()+"%"));
				}
				if(StringUtils.isNotEmpty(e.getCode())){
					list.add(cb.like(root.get("code").as(String.class), "%" +e.getCode()+"%"));
				}
				if(StringUtils.isNotEmpty(e.getDepartmentName())){
					list.add(cb.like(root.get("departmentName").as(String.class), "%"+e.getDepartmentName()+"%"));
				}
				if(e.getIsMt()!=null){
					list.add(cb.equal(root.get("isMt").as(Boolean.class), e.getIsMt()));
				}
				if(e.getIsPm()!=null){
					list.add(cb.equal(root.get("isPm").as(Boolean.class), e.getIsPm()));
				}
				if(e.getCreateEndTime()!=null && e.getCreateEndTime()!=null){
					SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
					list.add(cb.and(cb.lessThanOrEqualTo(cb.substring(root.get("employmentDate").as(String.class),1,10), sdf.format(e.getCreateEndTime())),
					cb.greaterThanOrEqualTo(cb.substring(root.get("employmentDate").as(String.class),1,10), sdf.format(e.getCreateStartTime()))));
				}
				if(StringUtils.isNotEmpty(e.getPosition())){
					list.add(cb.like(root.get("position").as(String.class),"%"+e.getPosition()+"%"));
				}
				Predicate[] ps = new Predicate[list.size()];
                query.where(cb.and(list.toArray(ps)));
                if (StringUtils.isNotEmpty(e.getSort())) {
                    query.orderBy(cb.desc(root.get(e.getSort())));
                }
				return query.getGroupRestriction();
			}
		};
		PageRequest pageRequest =null;
		if (e.getPageIndex() != null && e.getPageSize() != null) {
            pageRequest = new PageRequest(e.getPageIndex(), e.getPageSize());
        }
        Page<Employe> pages = null;
        pages =employeDao.findAll(sp,pageRequest);
        return pages;
	}
	/**
	 * 根据部门id查询员工
	 * @param departmentId
	 * @return
	 */
    public List<Employe> findByDepartmentId(Long departmentId) {
        return employeDao.findByDepartmentId(departmentId);
    }
    public Employe findByLoginName(String username) {
        return employeDao.findByLoginName(username);
    }
    
    /**
     * 功能描述：查询全部员工
     * @author liukai
     * @return
     */
    public List<Employe> findAll(){
    	Specification<Employe> sp=new Specification<Employe>() {
			@Override
			public Predicate toPredicate(Root<Employe> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				return null;
			}
		};
    	
    	return employeDao.findAll(sp);
    }
    
    /**
     * 根据部门id查询员工
     * @param departmentId
     * @return
     */
    public List<Employe> findByEmployeDepartmentId(Long departmentId) {
        List<Employe> list = employeDao.findByDepartmentId(departmentId);
        List<Employe> listEmploye = new ArrayList<Employe>();
        if(null != list && list.size() > 0){
            for(Employe temp : list){
                if(null != temp ){
                    if(!temp.getIsMt()&& !temp.getIsPm()){
                        listEmploye.add(temp);
                    }
                }
            }
        }
        return listEmploye;
    }

}
