package com.performance.commons.utils;

public enum TaskType {
    /**
     * 1、行政管理工作任务 2、党群管理工作任务 3 财务管理工作任务 4 人事管理工作任务 5 安全管理工作任务 6 通信业务管理工作任务 7 信息化业务管理工作任务
     */
    PRODUCT_TASK(1,"行政管理工作任务"),
    POLITICAL_TASK(2,"党群管理工作任务"),
    FINANCE_TASK(3,"财务管理工作任务"),
    PERSONNEL_TASK(4,"人事管理工作任务"),
    SAFETY_TASK(5,"安全管理工作任务"),
    DOC_TASK(6,"通信业务管理工作任务"),
    INFORMATION_TASK(7,"信息化业务管理工作任务");

    private final int value;
    private final String content;

    private TaskType(int value ,String content){
        this.content=content;
        this.value=value;
    }
    public int v(){
        return this.value;
    }
    public String c(){
        return this.content;
    }
}
