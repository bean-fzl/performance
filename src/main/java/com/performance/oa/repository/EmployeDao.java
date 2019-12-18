package com.ync365.oa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.ync365.oa.entity.Efficiency;
import com.ync365.oa.entity.Employe;

public interface EmployeDao extends PagingAndSortingRepository<Employe, Long>,JpaSpecificationExecutor<Employe> {

    List<Employe> findByDepartmentId(Long departmentId);

    Employe findByLoginName(String username);

}
