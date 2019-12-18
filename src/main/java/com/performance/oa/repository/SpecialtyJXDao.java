package com.performance.oa.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import com.performance.oa.entity.SpecialtyJX;

public interface SpecialtyJXDao extends PagingAndSortingRepository<SpecialtyJX, Long>,JpaSpecificationExecutor<SpecialtyJX>{

}
