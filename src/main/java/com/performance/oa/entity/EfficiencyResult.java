package com.ync365.oa.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name= "efficiency_result")
public class EfficiencyResult extends IdEntity{
	private String employeCode;
	private String employeName;
	private Integer employeId;
	private Integer departmentId;
	private String departmentName;
	private Integer projectCount;
	private Integer planHours;
	private Integer actualHours;
	private Integer outputHours;
	private Integer basicHours;
	private Double loadRate;
	private Double efficiencyPercentage;
	private Double efficiencyTotalScore;
	private Date createTime;
	public String getEmployeCode() {
		return employeCode;
	}
	public void setEmployeCode(String employeCode) {
		this.employeCode = employeCode;
	}
	public String getEmployeName() {
		return employeName;
	}
	public void setEmployeName(String employeName) {
		this.employeName = employeName;
	}
	public Integer getDepartmentId() {
		return departmentId;
	}
	public void setDepartmentId(Integer departmentId) {
		this.departmentId = departmentId;
	}
	public String getDepartmentName() {
		return departmentName;
	}
	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}
	public Integer getProjectCount() {
		return projectCount;
	}
	public void setProjectCount(Integer projectCount) {
		this.projectCount = projectCount;
	}
	public Integer getPlanHours() {
		return planHours;
	}
	public void setPlanHours(Integer planHours) {
		this.planHours = planHours;
	}
	public Integer getActualHours() {
		return actualHours;
	}
	public void setActualHours(Integer actualHours) {
		this.actualHours = actualHours;
	}
	public Integer getOutputHours() {
		return outputHours;
	}
	public void setOutputHours(Integer outputHours) {
		this.outputHours = outputHours;
	}
	public Integer getBasicHours() {
		return basicHours;
	}
	public void setBasicHours(Integer basicHours) {
		this.basicHours = basicHours;
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
	public Double getEfficiencyTotalScore() {
		return efficiencyTotalScore;
	}
	public void setEfficiencyTotalScore(Double efficiencyTotalScore) {
		this.efficiencyTotalScore = efficiencyTotalScore;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Integer getEmployeId() {
		return employeId;
	}
	public void setEmployeId(Integer employeId) {
		this.employeId = employeId;
	}
}
