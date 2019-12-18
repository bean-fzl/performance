package com.ync365.oa.query;

import java.util.Date;

public class SatisfactionResultQuery {
	
	private Long id;

	private Long beEvaluatedDepartmentId;

	private String beEvaluatedDepartmentName;

	private Long beEvaluatedId;

	private String beEvaluatedName;

	private Date createTime;

	private Double score;

	private Long projectNum;
	
	private Integer pageIndex;

	private Integer pageSize;

	private String sort;
	
	private String sortType;
	
	private String dateReturn;
	
	private Date evaluatedTime;
	
	public Date getEvaluatedTime() {
		return evaluatedTime;
	}

	public void setEvaluatedTime(Date evaluatedTime) {
		this.evaluatedTime = evaluatedTime;
	}

	public String getDateReturn() {
		return dateReturn;
	}

	public void setDateReturn(String dateReturn) {
		this.dateReturn = dateReturn;
	}

	public Long getProjectNum() {
		return projectNum;
	}

	public void setProjectNum(Long projectNum) {
		this.projectNum = projectNum;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Double getScore() {
		return score;
	}

	public void setScore(Double score) {
		this.score = score;
	}

	public Integer getPageIndex() {
		return pageIndex;
	}

	public void setPageIndex(Integer pageIndex) {
		this.pageIndex = pageIndex;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getSortType() {
		return sortType;
	}

	public void setSortType(String sortType) {
		this.sortType = sortType;
	}
	
	
}
