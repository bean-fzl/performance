package com.ync365.oa.service.projectChange;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ync365.commons.utils.StringUtils;

import com.ync365.oa.entity.ProjectChange;
import com.ync365.oa.query.ProjectChangeQuery;
import com.ync365.oa.repository.ProjectChangeDao;


@Component
@Transactional
public class ProjectChangeService {

    private static Logger logger = LoggerFactory.getLogger(ProjectChangeService.class);
    
    @Autowired
    private ProjectChangeDao projectChangeDao;
    
    public List<ProjectChange> findListByProId(int projectId){
        List<ProjectChange> projectChangeList = projectChangeDao.findByProjectId(projectId);
        return projectChangeList;
    }

    public Page<ProjectChange> findAll(final ProjectChangeQuery q) {
        Specification<ProjectChange> sp = new Specification<ProjectChange>() {
            @Override
            public Predicate toPredicate(Root<ProjectChange> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> list = new ArrayList<>();
                if (q.getProjectId() != null) {
                    list.add(cb.equal(root.get("projectId").as(Integer.class), q.getProjectId()));
                }
                
                if (q.getEmployeId() != null) {
                    list.add(cb.equal(root.get("employeId").as(Integer.class), q.getEmployeId()));
                }
                Predicate[] ps = new Predicate[list.size()];
                query.where(cb.and(list.toArray(ps)));
                if (StringUtils.isNotEmpty(q.getSort())) {
                    query.orderBy(cb.desc(root.get(q.getSort())));
                }
                return query.getGroupRestriction();
            }
        };
        PageRequest pageRequest = null;
        if (q.getPageIndex() != null && q.getPageSize() != null) {
            pageRequest = new PageRequest(q.getPageIndex(), q.getPageSize());
        }
        Page<ProjectChange> pageList = projectChangeDao.findAll(sp, pageRequest);
        return pageList;
    }
}
