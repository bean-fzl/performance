package com.performance.oa.bo;

import java.util.Date;

public class EfficiencyRecordBo {

    /**
     * 效能主键id
     */
    private Integer efficiencyIds;
    
    /**
     * 部门名称 
     */
    private Integer departmentId;
    
    /**
     * 员工姓名 
     */
    private Integer employeId;
    
    /**
     * 计划时长
     */
    private Integer planHours;

    /**
     * 任务月份
     */
    private Integer month;

    /**
     * 计划开始时间
     */
    private Date planBeginTime;
    
    /**
     * 计划结束时间
     */
    private Date planEndTime;

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }

    public Integer getEmployeId() {
        return employeId;
    }

    public void setEmployeId(Integer employeId) {
        this.employeId = employeId;
    }

    public Integer getPlanHours() {
        return planHours;
    }

    public void setPlanHours(Integer planHours) {
        this.planHours = planHours;
    }

    public Date getPlanBeginTime() {
        return planBeginTime;
    }

    public void setPlanBeginTime(Date planBeginTime) {
        this.planBeginTime = planBeginTime;
    }

    public Date getPlanEndTime() {
        return planEndTime;
    }

    public void setPlanEndTime(Date planEndTime) {
        this.planEndTime = planEndTime;
    }

    public Integer getEfficiencyIds() {
        return efficiencyIds;
    }

    public void setEfficiencyIds(Integer efficiencyIds) {
        this.efficiencyIds = efficiencyIds;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

}
