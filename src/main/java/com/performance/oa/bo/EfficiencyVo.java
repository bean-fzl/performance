package com.performance.oa.bo;

import com.performance.oa.entity.Efficiency;
import com.performance.oa.entity.IdEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

public class EfficiencyVo extends Efficiency {

    private Integer month;

    private Integer projectCount;

    private Integer employeCount;

    private Integer departmentCount;

    private Integer planHourCount;

    private Integer actualHourCount;

    private Integer outputHourCount;

    @Override
    public Integer getMonth() {
        return month;
    }

    @Override
    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getProjectCount() {
        return projectCount;
    }

    public void setProjectCount(Integer projectCount) {
        this.projectCount = projectCount;
    }

    public Integer getEmployeCount() {
        return employeCount;
    }

    public void setEmployeCount(Integer employeCount) {
        this.employeCount = employeCount;
    }

    public Integer getPlanHourCount() {
        return planHourCount;
    }

    public void setPlanHourCount(Integer planHourCount) {
        this.planHourCount = planHourCount;
    }

    public Integer getActualHourCount() {
        return actualHourCount;
    }

    public void setActualHourCount(Integer actualHourCount) {
        this.actualHourCount = actualHourCount;
    }

    public Integer getOutputHourCount() {
        return outputHourCount;
    }

    public void setOutputHourCount(Integer outputHourCount) {
        this.outputHourCount = outputHourCount;
    }

    public Integer getDepartmentCount() {
        return departmentCount;
    }

    public void setDepartmentCount(Integer departmentCount) {
        this.departmentCount = departmentCount;
    }
}
