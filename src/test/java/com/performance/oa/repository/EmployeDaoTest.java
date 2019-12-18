package com.performance.oa.repository;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;

import com.performance.oa.entity.Employe;
import com.performance.oa.query.EmployeQuery;
import com.performance.oa.service.employe.EmployeService;
public class EmployeDaoTest extends BaseTest {
    @Autowired
    EmployeDao employeDao;
    
    @Autowired
    EmployeService employeService;
    /***
     * 导入员工
     */
    @Test
    public void testAdd() {
        Employe entity = new Employe();
        entity.setCode("121l");
        entity.setCreateTime(Calendar.getInstance().getTime());
        entity.setDepartmentId(21l);
        entity.setDepartmentName("aaa2");
        entity.setPosition("abc2");
        entity.setName("test2");
        entity.setEmail("aaa@performance.com");
        entity.setEmploymentDate(Calendar.getInstance().getTime());
        entity.setIsMt(true);
        entity.setIsPm(true);
        entity.setLoginName("aaa");
        entity.setMobile("1231231321");
        entity.setPassword("aaa");
        entity = employeDao.save(entity);
        System.out.println("id .>>>>> " + entity.getId());
    }
    /***
     * 搜索员工
     */
    @Test
    public void testSelect(){
    	 Specification<Employe> sp=new Specification<Employe>() {
			@Override
			public Predicate toPredicate(Root<Employe> root,
					CriteriaQuery<?> query, CriteriaBuilder cb) {
				// TODO Auto-generated method stub
				List<Predicate> list=new ArrayList<>();
				SimpleDateFormat f=new SimpleDateFormat("yy-MM-dd");
				Predicate p1=cb.and(cb.lessThanOrEqualTo(cb.substring(root.get("employmentDate").as(String.class),1,10 ),"2015-12-30"),
						cb.greaterThanOrEqualTo(cb.substring(root.get("employmentDate").as(String.class),1,10 ),"2015-12-02"));
				/*list.add(p1);
				Predicate p2=cb.equal(root.get("name").as(String.class),"test01");
				list.add(p2);
				Predicate[] ps = new Predicate[list.size()];
                query.where(cb.and(list.toArray(ps)));
				return  query.getGroupRestriction();*/
				query.where(p1);
				return query.getRestriction();
			}
		};
		Page<Employe> list=employeDao.findAll(sp,new PageRequest(0,5,new Sort(Direction.ASC,"createTime")));
		System.out.println("size >>>>>>>" +list.getSize());
		System.out.println("getNum >>>>" + list.getNumber());
		for(Employe e:list){
			System.out.println("id >>>>>>>>" +e.getId());
		}
    }
    
    @Test
    public void findOne(){
    	System.out.println("name>>>>>>>>"+employeDao.findOne(2l).getName());
    }
    
    @Test
    public void find(){
    	EmployeQuery e=new EmployeQuery();
    	Page<Employe> es=employeService.find(e);
    	for(Employe e1:es){
    		System.out.println("id>>>>>"+e1.getId());
    	}
    }
    
    @Test
    public void passwdReset(){
    	Employe e=employeDao.findOne(1l);
    	e.setPassword("aaaaa");
    	employeDao.save(e);
    }
}
