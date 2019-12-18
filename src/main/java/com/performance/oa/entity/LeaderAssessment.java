package com.ync365.oa.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "leader_assessment")
public class LeaderAssessment extends IdEntity{
	private int departmentId;	//部门id
	private String departmentName;	//部门名称
	private String mt;			//评论人
	private Long beEvaluatedId;	//被评论人id		
	private String beEvaluatedName;		//被评论人名称
	private Long action1_Score;		
	private Long action2_Score;
	private Long action3_Score;
	private Long action4_Score;
	private Long action5_Score;
	private String comment;		//评语
	private Date evaluatedTime;	//评论时间
	private Date createTime;	//创建时间
	private int totalScore;		//总分
	
	
	public int getDepartmentId() {
		return departmentId;
	}
	public void setDepartmentId(int departmentId) {
		this.departmentId = departmentId;
	}
	public String getDepartmentName() {
		return departmentName;
	}
	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}
	public String getMt() {
		return mt;
	}
	public void setMt(String mt) {
		this.mt = mt;
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
	
	public Long getAction1_Score() {
		return action1_Score;
	}
	public void setAction1_Score(Long action1_Score) {
		this.action1_Score = action1_Score;
	}
	
	public Long getAction2_Score() {
		return action2_Score;
	}
	public void setAction2_Score(Long action2_Score) {
		this.action2_Score = action2_Score;
	}
	public Long getAction3_Score() {
		return action3_Score;
	}
	public void setAction3_Score(Long action3_Score) {
		this.action3_Score = action3_Score;
	}
	public Long getAction4_Score() {
		return action4_Score;
	}
	public void setAction4_Score(Long action4_Score) {
		this.action4_Score = action4_Score;
	}
	public Long getAction5_Score() {
		return action5_Score;
	}
	public void setAction5_Score(Long action5_Score) {
		this.action5_Score = action5_Score;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public Date getEvaluatedTime() {
		return evaluatedTime;
	}
	public void setEvaluatedTime(Date evaluatedTime) {
		this.evaluatedTime = evaluatedTime;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public int getTotalScore() {
		return totalScore;
	}
	public void setTotalScore(int totalScore) {
		this.totalScore = totalScore;
	}
	
}
