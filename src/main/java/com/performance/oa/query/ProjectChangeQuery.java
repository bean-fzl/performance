package com.ync365.oa.query;

import java.util.Date;

public class ProjectChangeQuery {

    private Long id;
    /**
     * 更改之前
     */
    private String changeBefore;
    
    /**
     * 更改之后
     */
    private String changeAfter;
    
    /**
     * 时间
     */
    private Date createTime;
    
    /**
     * 操作员姓名
     */
    private String optName;
    
    /**
     * 相关的项目id
     */
    private Integer projectId;
    
    /**
     * 相关对应项目人员id
     */
    private Integer efficiencyId;
    
    private Integer employeId;
    
    
    private Integer pageIndex;
    private Integer pageSize;
    private String sort;

    public String getChangeBefore() {
        return changeBefore;
    }

    public void setChangeBefore(String changeBefore) {
        this.changeBefore = changeBefore;
    }

    public String getChangeAfter() {
        return changeAfter;
    }

    public void setChangeAfter(String changeAfter) {
        this.changeAfter = changeAfter;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getOptName() {
        return optName;
    }

    public void setOptName(String optName) {
        this.optName = optName;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public Integer getEfficiencyId() {
        return efficiencyId;
    }

    public void setEfficiencyId(Integer efficiencyId) {
        this.efficiencyId = efficiencyId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Integer getEmployeId() {
        return employeId;
    }

    public void setEmployeId(Integer employeId) {
        this.employeId = employeId;
    }

   
    
}
