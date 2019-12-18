package com.performance.oa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.performance.oa.entity.Employe;

public interface EmployeDao extends PagingAndSortingRepository<Employe, Long>,JpaSpecificationExecutor<Employe> {

    List<Employe> findByDepartmentId(Long departmentId);

    Employe findByLoginName(String username);

}
