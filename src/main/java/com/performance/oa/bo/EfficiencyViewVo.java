package com.ync365.oa.bo;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.ync365.oa.entity.Employe;


public class EfficiencyViewVo {

    private Long id;
    /**
     * 员工姓名
     */
    private String employeName;
    
    /**
     * 部门名称
     */
    private String departmentName;
    
    /**
     * 员工标号
     */
    private String employeCode; 
    
    /**
     * 员工id
     */
    private Integer employeId;
    
    /**
     * 部门id
     */
    private Integer departmentId;
    
    /**
     * 项目id
     */
    private Integer projectId;
    
    /**
     * 项目名称
     */
    private String projectName;
    
    /**
     * 计划开始时间
     */
    private Date planBeginTime;
    
    /**
     * 计划结束时间啊
     */
    private Date planEndTime;
    
    /**
     * 计划 时长
     */
    private Integer planHours;
    
    /**
     * 实际 开始时间
     */
    private Date actualBeginTime;
    
    /**
     * 实际  结束时间
     */
    private Date actualEndTime;
    
    /**
     * 实际时长
     */
    private Integer actualHours ;
    
    /**
     * 产出开始时间
     */
    private Date outputBeginTime;
    
    /**
     * 产出 结束时间
     */
    private Date  outputEndTime;
    
    /**
     * 产出时长
     */
    private Integer outputHours;
    
    /**
     * 
     */
    private String pm;
    
    /**
     * 创建时间
     */
    private Date createTime;
    
    /**
     * 效率百分比
     */
    private Double efficiencyPercentage;
    
    /**
     * 负荷率百分比
     */
    private Double loadRate;
    
    private List<Employe> employeList;

    public String getEmployeName() {
        return employeName;
    }

    public void setEmployeName(String employeName) {
        this.employeName = employeName;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getEmployeCode() {
        return employeCode;
    }

    public void setEmployeCode(String employeCode) {
        this.employeCode = employeCode;
    }

    public Integer getEmployeId() {
        return employeId;
    }

    public void setEmployeId(Integer employeId) {
        this.employeId = employeId;
    }

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
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

    public Integer getPlanHours() {
        return planHours;
    }

    public void setPlanHours(Integer planHours) {
        this.planHours = planHours;
    }

    public Date getActualBeginTime() {
        return actualBeginTime;
    }

    public void setActualBeginTime(Date actualBeginTime) {
        this.actualBeginTime = actualBeginTime;
    }

    public Date getActualEndTime() {
        return actualEndTime;
    }

    public void setActualEndTime(Date actualEndTime) {
        this.actualEndTime = actualEndTime;
    }

    public Integer getActualHours() {
        return actualHours;
    }

    public void setActualHours(Integer actualHours) {
        this.actualHours = actualHours;
    }

    public Date getOutputBeginTime() {
        return outputBeginTime;
    }

    public void setOutputBeginTime(Date outputBeginTime) {
        this.outputBeginTime = outputBeginTime;
    }

    public Date getOutputEndTime() {
        return outputEndTime;
    }

    public void setOutputEndTime(Date outputEndTime) {
        this.outputEndTime = outputEndTime;
    }

    public Integer getOutputHours() {
        return outputHours;
    }

    public void setOutputHours(Integer outputHours) {
        this.outputHours = outputHours;
    }

    public String getPm() {
        return pm;
    }

    public void setPm(String pm) {
        this.pm = pm;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Double getLoadRate() {
        return loadRate;
    }

    public void setLoadRate(Double loadRate) {
        this.loadRate = loadRate;
    }

    public Double getEfficiencyPercentage() {
        return efficiencyPercentage;
    }

    public void setEfficiencyPercentage(Double efficiencyPercentage) {
        this.efficiencyPercentage = efficiencyPercentage;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Employe> getEmployeList() {
        return employeList;
    }

    public void setEmployeList(List<Employe> employeList) {
        this.employeList = employeList;
    }

    
    
}
