package com.performance.oa.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import com.performance.oa.entity.Specialty;

public interface SpecialtyDao extends PagingAndSortingRepository<Specialty, Long>,JpaSpecificationExecutor<Specialty> {
	

}
