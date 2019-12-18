package com.ync365.oa.bo;

import java.util.Date;

public class SatisfactionBo {
	
	private Long id;

	private Long projectId;
	
	private String projectName;
	
	private Long evaluatedId;
	
	private String evaluatedName;
	
	private Long evaluatedDepartmentId;
	
	private String evaluatedDepartmentName;
	
	private Long beEvaluatedId;
	
	private String beEvaluatedName;
	
	private Long beEvaluatedDepartmentId;
	
	private String beEvaluatedDepartmentName;
	
	private Date evaluatedTime;
	
	private Double score;
	
	private String comment;
	
	private Date createTime;
	
	private Double scoreLimit;
	
	private Boolean havaButton;//判断是否展示评分按钮
	
	private String evaluatedTimeFormate;
	
	public String getEvaluatedTimeFormate() {
		return evaluatedTimeFormate;
	}

	public void setEvaluatedTimeFormate(String evaluatedTimeFormate) {
		this.evaluatedTimeFormate = evaluatedTimeFormate;
	}

	public Boolean getHavaButton() {
		return havaButton;
	}

	public void setHavaButton(Boolean havaButton) {
		this.havaButton = havaButton;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getProjectId() {
		return projectId;
	}

	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public Long getEvaluatedId() {
		return evaluatedId;
	}

	public void setEvaluatedId(Long evaluatedId) {
		this.evaluatedId = evaluatedId;
	}

	public String getEvaluatedName() {
		return evaluatedName;
	}

	public void setEvaluatedName(String evaluatedName) {
		this.evaluatedName = evaluatedName;
	}

	public Long getEvaluatedDepartmentId() {
		return evaluatedDepartmentId;
	}

	public void setEvaluatedDepartmentId(Long evaluatedDepartmentId) {
		this.evaluatedDepartmentId = evaluatedDepartmentId;
	}

	public String getEvaluatedDepartmentName() {
		return evaluatedDepartmentName;
	}

	public void setEvaluatedDepartmentName(String evaluatedDepartmentName) {
		this.evaluatedDepartmentName = evaluatedDepartmentName;
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

	public Long getBeEvaluatedDepartmentId() {
		return beEvaluatedDepartmentId;
	}

	public void setBeEvaluatedDepartmentId(Long beEvaluatedDepartmentId) {
		this.beEvaluatedDepartmentId = beEvaluatedDepartmentId;
	}

	public String getBeEvaluatedDepartmentName() {
		return beEvaluatedDepartmentName;
	}

	public void setBeEvaluatedDepartmentName(String beEvaluatedDepartmentName) {
		this.beEvaluatedDepartmentName = beEvaluatedDepartmentName;
	}

	public Date getEvaluatedTime() {
		return evaluatedTime;
	}

	public void setEvaluatedTime(Date evaluatedTime) {
		this.evaluatedTime = evaluatedTime;
	}

	public Double getScore() {
		return score;
	}

	public void setScore(Double score) {
		this.score = score;
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

	public Double getScoreLimit() {
		return scoreLimit;
	}

	public void setScoreLimit(Double scoreLimit) {
		this.scoreLimit = scoreLimit;
	}
}
