package com.performance.oa.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;


@Entity
@Table(name = "project")
public class Project extends IdEntity{

    /**
     * 名称
     */
    private String name;
    
    /**
     * 参加人员
     */
    private String projectPersonnel;

    /**
     * 工作属性
     */
    private Integer taskType;

    /**
     * 工作感想
     */
    private String workFeeling;

    /**
     * 
     */
    private String pm;
    
    /**
     * 项目开始 时间
     */
    private Date projectBeginTime;
    
    /**
     *项目结束时间
     */
    private Date projectEndTime;
    
    /**
     * 状态:0初始化，1已完成
     */
    private Integer state;
    
    /**
     * 创建时间
     */
    private Date createTime;
    
    /**
     * 项目管理员id
     */
    private Integer pmId;

    @Transient
    private Integer taskAll;

    @Transient
    private Integer taskOver;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProjectPersonnel() {
        return projectPersonnel;
    }

    public void setProjectPersonnel(String projectPersonnel) {
        this.projectPersonnel = projectPersonnel;
    }

    public String getPm() {
        return pm;
    }

    public void setPm(String pm) {
        this.pm = pm;
    }

    public Date getProjectBeginTime() {
        return projectBeginTime;
    }

    public void setProjectBeginTime(Date projectBeginTime) {
        this.projectBeginTime = projectBeginTime;
    }

    public Date getProjectEndTime() {
        return projectEndTime;
    }

    public void setProjectEndTime(Date projectEndTime) {
        this.projectEndTime = projectEndTime;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getPmId() {
        return pmId;
    }

    public void setPmId(Integer pmId) {
        this.pmId = pmId;
    }

    public Integer getTaskType() {
        return taskType;
    }

    public void setTaskType(Integer taskType) {
        this.taskType = taskType;
    }

    public String getWorkFeeling() {
        return workFeeling;
    }

    public void setWorkFeeling(String workFeeling) {
        this.workFeeling = workFeeling;
    }

    public Integer getTaskAll() {
        return taskAll;
    }

    public void setTaskAll(Integer taskAll) {
        this.taskAll = taskAll;
    }

    public Integer getTaskOver() {
        return taskOver;
    }

    public void setTaskOver(Integer taskOver) {
        this.taskOver = taskOver;
    }
}
