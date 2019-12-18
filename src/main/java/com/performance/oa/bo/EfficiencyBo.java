package com.performance.oa.bo;

import java.util.List;

public class EfficiencyBo {
    
    /**
     * 项目id
     */
    private long proId;

    /**
     * 项目名称
     */
    private String projectNameBo;

    /**
     * 工作属性
     */
    private Integer taskType;

    /**
     * 工作感想
     */
    private String workFeeling;


    private List<EfficiencyRecordBo> efficiencyRecordBo;

    public long getProId() {
        return proId;
    }

    public void setProId(long proId) {
        this.proId = proId;
    }

    public String getProjectNameBo() {
        return projectNameBo;
    }

    public void setProjectNameBo(String projectNameBo) {
        this.projectNameBo = projectNameBo;
    }

    public List<EfficiencyRecordBo> getEfficiencyRecordBo() {
        return efficiencyRecordBo;
    }

    public void setEfficiencyRecordBo(List<EfficiencyRecordBo> efficiencyRecordBo) {
        this.efficiencyRecordBo = efficiencyRecordBo;
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
}
