package com.performance.oa.service.project;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.performance.oa.bo.EfficiencyProjectBo;
import com.performance.oa.entity.Efficiency;
import com.performance.oa.entity.Employe;
import com.performance.oa.entity.Project;
import com.performance.oa.query.ProjectQuery;
import com.performance.oa.repository.EfficiencyDao;
import com.performance.oa.repository.EmployeDao;
import com.performance.oa.repository.ProjectDao;
import com.performance.oa.service.account.ShiroDbRealm.ShiroUser;

@Component
@Transactional
public class ProjectService {

    private static Logger logger = LoggerFactory.getLogger(ProjectService.class);

    @Autowired
    private ProjectDao projectDao;

    @Autowired
    private EfficiencyDao efficiencyDao;

    @Autowired
    private EmployeDao employeDao;

    public Project findById(int proId) {
        return projectDao.findOne(new Long(proId));
    }

    public void delById(Long proId) {
        projectDao.delete(proId);
    }

    /*
     * 查看项目列表
     * */
    public List<Project> find(final ProjectQuery p) {
        Specification<Project> sp = new Specification<Project>() {
            @Override
            public Predicate toPredicate(Root<Project> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                // TODO Auto-generated method stub
                List<Predicate> list = new ArrayList<>();
                if (p.getId() != null) {
                    list.add(cb.equal(root.get("id").as(Long.class), p.getId()));
                }
                if (StringUtils.isNotEmpty(p.getName())) {
                    list.add(cb.like(root.get("name").as(String.class), "%" + p.getName() + "%"));
                }
                if (StringUtils.isNotEmpty(p.getPm())) {
                    list.add(cb.like(root.get("pm").as(String.class), "%" + p.getPm() + "%"));
                }
                if (p.getState() != null) {
                    list.add(cb.equal(root.get("state").as(Integer.class), p.getState()));
                }
                if (p.getProjectBeginTime() != null) {
                    list.add(cb.equal(root.get("projectBeginTime").as(Date.class), p.getProjectBeginTime()));
                }
                if (p.getProjectEndTime() != null) {
                    list.add(cb.equal(root.get("projectEndTime").as(Date.class), p.getProjectEndTime()));
                }

                Predicate[] ps = new Predicate[list.size()];
                query.where(cb.and(list.toArray(ps)));
                if (StringUtils.isNotEmpty(p.getSort())) {
                    query.orderBy(cb.desc(root.get(p.getSort())));
                }
                return query.getGroupRestriction();
            }
        };

        List<Project> plist = projectDao.findAll(sp);
        if (StringUtils.isNotEmpty(p.getProjectPersonnel())) {
            List<Project> list = new ArrayList<Project>();
            for (Project project : plist) {
                String[] strs = project.getProjectPersonnel().split(",");
                for (String s : strs) {
                    if (s.equals(p.getProjectPersonnel())) {
                        list.add(project);
                        break;
                    }
                }
            }
            return list;
        } else {
            return plist;
        }
    }

    /**
     * 功能描述：添加分页功能
     * @author liukai
     * @param p
     * @return
     */
    public Page<Project> findQuery(final ProjectQuery p) {
        Specification<Project> sp = new Specification<Project>() {
            @Override
            public Predicate toPredicate(Root<Project> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> list = new ArrayList<>();
                if (p.getId() != null) {
                    list.add(cb.equal(root.get("id").as(Long.class), p.getId()));
                }
                if (StringUtils.isNotEmpty(p.getName())) {
                    list.add(cb.like(root.get("name").as(String.class), "%" + p.getName() + "%"));
                }
                if (StringUtils.isNotEmpty(p.getPm())) {
                    list.add(cb.like(root.get("pm").as(String.class), "%" + p.getPm() + "%"));
                }
                if (p.getState() != null) {
                    list.add(cb.equal(root.get("state").as(Integer.class), p.getState()));
                }
                if (p.getProjectBeginTime() != null) {
                    list.add(cb.equal(root.get("projectBeginTime").as(Date.class), p.getProjectBeginTime()));
                }
                if (p.getProjectEndTime() != null) {
                    list.add(cb.equal(root.get("projectEndTime").as(Date.class), p.getProjectEndTime()));
                }

                Predicate[] ps = new Predicate[list.size()];
                query.where(cb.and(list.toArray(ps)));
                if (StringUtils.isNotEmpty(p.getSort())) {
                    query.orderBy(cb.desc(root.get(p.getSort())));
                }
                return query.getGroupRestriction();
            }
        };

        PageRequest pageRequest = null;
        if (p.getPageIndex() != null && p.getPageSize() != null) {
            pageRequest = new PageRequest(p.getPageIndex(), p.getPageSize());
        }
        Page<Project> pages = projectDao.findAll(sp, pageRequest);
        ;

        List<Project> plist = pages.getContent();

        if (StringUtils.isNotEmpty(p.getProjectPersonnel())) {
            List<Project> list = new ArrayList<Project>();
            for (Project project : plist) {
                String[] strs = project.getProjectPersonnel().split(",");
                for (String s : strs) {
                    if (s.equals(p.getProjectPersonnel())) {
                        list.add(project);
                        break;
                    }
                }
            }

            Page<Project> pageList = new PageImpl<Project>(list, pageRequest, p.getPageSize());
            return pageList;
        } else {
            return pages;
        }
    }

    /**
     * 根据项目经理id查询出相关项目
     * @param pmId
     * @return
     */
    public List<Project> findByPmId(int pmId) {
        return projectDao.findByPmId(pmId);
    }

    public List<Project> findAll() {
        return (List<Project>) projectDao.findAll();
    }

    /**
     * 项目管理
     * @return
     */
    public Page<EfficiencyProjectBo> findAllPro(final ProjectQuery q) {
        List<EfficiencyProjectBo> list_eff_pro = new ArrayList<EfficiencyProjectBo>();

        Specification<Project> sp = new Specification<Project>() {
            @Override
            public Predicate toPredicate(Root<Project> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> list = new ArrayList<>();
                if (StringUtils.isNotEmpty(q.getName())) {
                    list.add(cb.equal(root.get("name").as(String.class), q.getName()));
                }
                if (StringUtils.isNotEmpty(q.getPm())) {
                    list.add(cb.equal(root.get("pm").as(String.class), q.getPm()));
                }

                if (q.getId() != null) {
                    list.add(cb.equal(root.get("id").as(Integer.class), q.getId()));
                }
                if (q.getState() != null) {
                    list.add(cb.equal(root.get("state").as(Integer.class), q.getState()));
                }
                if(q.getProjectPersonnel()!=null){
                    list.add(cb.like(root.get("projectPersonnel").as(String.class), "%"+q.getProjectPersonnel()+"%"));
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
        Page<Project> pageList = projectDao.findAll(sp, pageRequest);

        List<Project> pro_list = pageList.getContent();

        long tatol = pageList.getTotalElements();
        if (null != pro_list && pro_list.size() > 0) {
            for (Project t_pro : pro_list) {
                /**beging*/
                if (null != t_pro && null != t_pro.getId()) {
                    EfficiencyProjectBo bo = findEfficiencyProjectByProId(t_pro.getId());
                    if(q.getPlanHoursSearch()!=null&&q.getPlanHoursSearch()!=bo.getPlanHoursAll()){
                        continue;
                    }
                    if(q.getActualHoursSearch()!=null&&q.getActualHoursSearch()!=bo.getActualHoursAll()){
                        continue;
                    }if(q.getOutputHoursSearch()!=null&&q.getOutputHoursSearch()!=bo.getOutputHoursAll()){
                        continue;
                    }
                    list_eff_pro.add(bo);
                }
            }
        }
        Page<EfficiencyProjectBo> page = new PageImpl<EfficiencyProjectBo>(list_eff_pro, pageRequest, tatol);
        return page;
    }

    public EfficiencyProjectBo findEfficiencyProjectByProId(Long proId) {
        int a = 0;
        int b = 0;
        int c = 0;
        EfficiencyProjectBo eff_pro = new EfficiencyProjectBo();
        Project pro = (Project) projectDao.findOne(proId);
        List<Efficiency> eff_list = null;
        /**beging*/
        if (null != pro && null != pro.getId()) {
            eff_list = (List<Efficiency>) efficiencyDao.findByProjectId(proId.intValue());
            if (null != eff_list && eff_list.size() > 0) {
                for (Efficiency temp : eff_list) {
                    if (null != temp.getPlanHours()) {
                        a += temp.getPlanHours();
                    }
                    if (null != temp.getActualHours()) {
                        b += temp.getActualHours();
                    }
                    if (null != temp.getOutputHours()) {
                        c += temp.getOutputHours();
                    }
                }
            }
        }
        /**结束*/
        eff_pro.setActualHoursAll(b);
        eff_pro.setPlanHoursAll(a);
        eff_pro.setOutputHoursAll(c);
        eff_pro.setName(pro.getName());
        eff_pro.setPm(pro.getPm());
        eff_pro.setProjectPersonnel(pro.getProjectPersonnel());
        eff_pro.setState(pro.getState());
        eff_pro.setProId(pro.getId());

        return eff_pro;
    }

    /**
     * 更加登录信息获取项目列表
     * @param user
     * @return
     */
    public List<Project> findProjectById(ShiroUser user) {
        //创建list
        List<Project> projectList = new ArrayList<Project>();
        List<Efficiency> efficiencyList = null;
        //根据登录人员id查询登录员工信息
        Employe employe = employeDao.findOne(user.id);

        if (null != employe && null != employe.getIsPm() && null != employe.getIsMt()) {
            if (employe.getIsPm()) {
                projectList = projectDao.findByPmId(user.id.intValue());
            } else if (!employe.getIsMt() && !employe.getIsPm()) {
                efficiencyList = efficiencyDao.findByEmployeId(employe.getId().intValue());
                if (null != efficiencyList && efficiencyList.size() > 0) {
                    for (Efficiency temp : efficiencyList) {
                        if (null != temp.getProjectId()) {
                            Project project = projectDao.findOne(temp.getProjectId().longValue());
                            if (null != project) {
                                if (!projectList.contains(project)) {
                                    projectList.add(project);
                                }
                            }
                        }
                    }
                }
            }
        }
        if(projectList.size()>0){
            projectList.forEach(project->{
                List<Efficiency> efficiencies = efficiencyDao.findByProjectId(project.getId().intValue());
                if(efficiencies!=null && efficiencies.size()>0){
                    project.setTaskAll(efficiencies.size());
                    Long taskOver = efficiencies.stream().filter(e->e.getOutputHours()!=null).count();
                    project.setTaskOver(taskOver.intValue());
                }
            });
        }
        return projectList;
    }

}
