package com.performance.oa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.performance.oa.entity.Efficiency;

public interface EfficiencyDao extends PagingAndSortingRepository<Efficiency, Long>, JpaSpecificationExecutor<Efficiency>  {

    List<Efficiency> findByProjectId(Integer projectId);

    List<Efficiency> findByEmployeId(int intValue);

    List<Efficiency> findByProjectIdAndEmployeId(Integer projectId, Integer employeId);

}
