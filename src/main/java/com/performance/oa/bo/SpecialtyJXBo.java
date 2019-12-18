package com.ync365.oa.bo;

import java.util.Date;
import java.util.List;
import com.ync365.oa.entity.SpecialtyJX;

public class SpecialtyJXBo {
	private Long departmentId;
	private String departmentName;
	private String mt;
	private Double totalScore;//总分
	private Long evaluateId;
	private String evaluateName;
	private Long beEvaluatedId;//被评价人id
	private String beEvaluatedName;//被评价人姓名
	private String comment;//评语
	private Date createTime;
	private Date evaluateTime;//评价时间
	private Date updateTime;//修改时间
	private List<SpecialtyJX> specialtyJXList;
	public Long getDepartmentId() {
		return departmentId;
	}
	public void setDepartmentId(Long departmentId) {
		this.departmentId = departmentId;
	}
	public String getDepartmentName() {
		return departmentName;
	}
	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}
	public Double getTotalScore() {
		return totalScore;
	}
	public void setTotalScore(Double totalScore) {
		this.totalScore = totalScore;
	}
	public Long getEvaluateId() {
		return evaluateId;
	}
	public void setEvaluateId(Long evaluateId) {
		this.evaluateId = evaluateId;
	}
	public String getEvaluateName() {
		return evaluateName;
	}
	public void setEvaluateName(String evaluateName) {
		this.evaluateName = evaluateName;
	}
	public Long getBeEvaluatedId() {
		return beEvaluatedId;
	}
	public void setBeEvaluatedId(Long beEvaluatedId) {
		this.beEvaluatedId = beEvaluatedId;
	}
	public String getBeEvaluatedName() {
		return beEvaluatedName;
	}
	public void setBeEvaluatedName(String beEvaluatedName) {
		this.beEvaluatedName = beEvaluatedName;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getEvaluateTime() {
		return evaluateTime;
	}
	public void setEvaluateTime(Date evaluateTime) {
		this.evaluateTime = evaluateTime;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public List<SpecialtyJX> getSpecialtyJXList() {
		return specialtyJXList;
	}
	public void setSpecialtyJXList(List<SpecialtyJX> specialtyJXList) {
		this.specialtyJXList = specialtyJXList;
	}
	public String getMt() {
		return mt;
	}
	public void setMt(String mt) {
		this.mt = mt;
	}
	
}
