package com.performance.oa.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.performance.oa.entity.EfficiencyResult;


public interface EfficiencyResultDao extends PagingAndSortingRepository<EfficiencyResult, Long>,JpaSpecificationExecutor<EfficiencyResult>{

}
