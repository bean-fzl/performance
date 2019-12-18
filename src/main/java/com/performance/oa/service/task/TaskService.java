/*******************************************************************************
 * Copyright (c) 2005, 2014 springside.github.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *******************************************************************************/
package com.ync365.oa.service.task;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.ync365.oa.bo.SpecialtyJXBo;
import com.ync365.oa.entity.Department;
import com.ync365.oa.entity.EfficiencyResult;
import com.ync365.oa.entity.Employe;
import com.ync365.oa.entity.LeaderAssessment;
import com.ync365.oa.entity.PerformanceEvaluation;
import com.ync365.oa.service.department.DepartmentService;
import com.ync365.oa.service.efficiencyResult.EfficiencyResultService;
import com.ync365.oa.service.employe.EmployeService;
import com.ync365.oa.service.leaderAssessment.LeaderAssessmentService;
import com.ync365.oa.service.pecontroller.PeControllerService;
import com.ync365.oa.service.performanceevaluation.PerformanceEvaluationService;
import com.ync365.oa.service.properties.PropertiesService;
import com.ync365.oa.service.satisfactionresult.SatisfactionResultService;
import com.ync365.oa.service.specialty.SpecialtyJXService;

@Transactional
public class TaskService {
    private Logger log = LoggerFactory.getLogger(TaskService.class);
    @Autowired
    private PropertiesService s;
    @Autowired
    private PeControllerService peControllerService;
    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private SatisfactionResultService satisfactionResultService;
    SimpleDateFormat f = new SimpleDateFormat("yyyy-MM");
    @Autowired
    private EmployeService employeService;
    @Autowired
    private PerformanceEvaluationService performanceEvaluationService;
    @Autowired
    private EfficiencyResultService efficiencyResultService;
    @Autowired
    private LeaderAssessmentService assessmentService;
    @Autowired
    private SpecialtyJXService specialtyJXService;

    /**
     * 每月1日0点执行
     * @Title: doNotifyMT
     * @Description: 
     * @author: duan.zhao.qian
     * @date: 2015年12月8日 上午9:08:18
     * @version: 
     *
     *
     */
    public void doNotifyMT() {
        log.info("doNotifyMT >> 每月1号0点执行");
        List<Department> list = departmentService.getAll();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -1);
        for (Department department : list) {
            peControllerService.checkIsProcessed(department.getId(), department.getName(),
                    f.format(calendar.getTime()));
        }
        //生成月度客户评价
        satisfactionResultService.calculatorSatisfactionResult();
        //生成月度上级评价
        assessmentService.createLeaderAssessment();
        //生成月度员工效能
        efficiencyResultService.calcEfficiencyResult();
        //生成专业评价
        specialtyJXService.initSpecialtyJXList();
        //        System.out.println("doNotifyMT >>> " + Calendar.getInstance().getTime().toLocaleString());
        //        System.out.println(PropertiesService.EFFICIENCY_SCORE);
        //        System.out.println(PropertiesService.LEADER_ASSESSMENT_SCORE);
        //        System.out.println(PropertiesService.SATISFACTION_SCORE);
        //        System.out.println(PropertiesService.SPECIALTY_SCORE);
    }

    /**
     * 每月4号0点执行
     * @Title: doCalculateScore
     * @Description: 
     * @author: duan.zhao.qian
     * @date: 2015年12月8日 上午9:08:42
     * @version: 
     *
     *
     */
    public void doCalculateScore() {
        log.info("doCalculateScore >> 每月4号0点执行");
        //计算员工绩效
        List<Department> listDepartment = departmentService.getAll();
        for (Department department : listDepartment) {
            List<Employe> listEmp = employeService.findByDepartmentId(department.getId());
            for (Employe employe : listEmp) {
                if (employe.getIsMt() || employe.getIsPm()) {
                    continue;
                }
                double sum = 0;
                PerformanceEvaluation performanceEvaluation = new PerformanceEvaluation();
                performanceEvaluation.setBeEvaluatedId(employe.getId());
                performanceEvaluation.setBeEvaluatedName(employe.getName());
                performanceEvaluation.setCreateTime(Calendar.getInstance().getTime());
                performanceEvaluation.setDepartmentId(department.getId());
                performanceEvaluation.setDepartmentName(department.getName());
                EfficiencyResult efficiencyResult = efficiencyResultService.findEfficiencyResult(employe.getId(),
                        Calendar.getInstance().getTime());
                if (efficiencyResult != null) {
                    performanceEvaluation.setEfficiencyScore(efficiencyResult.getEfficiencyTotalScore());
                    sum += performanceEvaluation.getEfficiencyScore();
                }
                LeaderAssessment lead = assessmentService.findByEmployeIdAndDate(employe.getId(),
                        Calendar.getInstance().getTime());
                if ((lead.getAction1_Score() == null) || (lead.getAction2_Score() == null)
                        || (lead.getAction3_Score() == null) || (lead.getAction4_Score() == null)
                        || (lead.getAction5_Score() == null)) {
                    performanceEvaluation.setLeaderAssessmentScore((double) 0l);
                } else {
                    performanceEvaluation
                            .setLeaderAssessmentScore((double) (lead.getAction1_Score() + lead.getAction2_Score()
                                    + lead.getAction3_Score() + lead.getAction4_Score() + lead.getAction5_Score()));
                }

                sum += performanceEvaluation.getLeaderAssessmentScore();
                performanceEvaluation.setSatisfactionScore(satisfactionResultService
                        .getScoreByDateEmployeId(employe.getId(), Calendar.getInstance().getTime()));
                sum += performanceEvaluation.getSatisfactionScore();
                SpecialtyJXBo bo = specialtyJXService.getSpecialtyJXByEmployeId(employe.getId(),
                        Calendar.getInstance().getTime());
                performanceEvaluation.setSpecialtyScore(bo.getTotalScore());
                sum += performanceEvaluation.getSpecialtyScore();
                performanceEvaluation.setTotalScore(sum);
                performanceEvaluation.setTotalGrade("");
                performanceEvaluation.setUpdateTime(Calendar.getInstance().getTime());
                performanceEvaluationService.update(performanceEvaluation);
            }
        }
        //        System.out.println("doCalculateScore >>> " + Calendar.getInstance().getTime().toLocaleString());
    }

    /**
     * 每分钟更新配置
     * @Title: doSetProperties
     * @Description: 
     * @author: duan.zhao.qian
     * @date: 2015年12月8日 上午9:10:21
     * @version: 
     *
     *
     */
    public void doSetProperties() {
        s.setProperties();
    }
}
