package com.performance.oa.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.performance.oa.entity.SatisfactionResult;

public interface SatisfactionResultDao extends PagingAndSortingRepository<SatisfactionResult, Long>, JpaSpecificationExecutor<SatisfactionResult> {
	
}
