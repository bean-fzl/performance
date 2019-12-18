package com.performance.oa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.performance.oa.entity.PeController;

public interface PeControllerDao
        extends PagingAndSortingRepository<PeController, Long>, JpaSpecificationExecutor<PeController> {
    PeController findByPeDateDepartment(String peDateDepartment);

    Page<PeController> findByDepartmentId(Long departmentId,Pageable pageable);
}
