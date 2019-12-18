package com.performance.oa.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.performance.oa.entity.PerformanceEvaluation;

public interface PerformanceEvaluationDao extends PagingAndSortingRepository<PerformanceEvaluation, Long>, JpaSpecificationExecutor<PerformanceEvaluation> {

}
