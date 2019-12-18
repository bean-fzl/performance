package com.performance.oa.repository;


import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.performance.oa.entity.LeaderAssessment;

public interface LeaderAssessmentDao extends PagingAndSortingRepository<LeaderAssessment, Long>, JpaSpecificationExecutor<LeaderAssessment> {
}
