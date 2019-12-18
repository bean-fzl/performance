package com.performance.oa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.performance.oa.entity.Project;

public interface ProjectDao  extends PagingAndSortingRepository<Project, Long>, JpaSpecificationExecutor<Project>{

    List<Project> findByPmId(int pmId);

}
