package com.performance.oa.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.performance.oa.entity.Department;

public interface DepartmentDao extends PagingAndSortingRepository<Department, Long>, JpaSpecificationExecutor<Department>{
	
}
