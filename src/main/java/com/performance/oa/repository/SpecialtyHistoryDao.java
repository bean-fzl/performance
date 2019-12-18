package com.performance.oa.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import com.performance.oa.entity.SpecialtyHistory;

public interface SpecialtyHistoryDao extends PagingAndSortingRepository<SpecialtyHistory, Long>,JpaSpecificationExecutor<SpecialtyHistory> {
	

}
