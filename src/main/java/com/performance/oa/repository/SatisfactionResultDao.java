package com.ync365.oa.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.ync365.oa.entity.SatisfactionResult;

public interface SatisfactionResultDao extends PagingAndSortingRepository<SatisfactionResult, Long>, JpaSpecificationExecutor<SatisfactionResult> {
	
}
