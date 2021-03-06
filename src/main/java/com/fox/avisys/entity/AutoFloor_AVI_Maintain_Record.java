package com.fox.avisys.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class AutoFloor_AVI_Maintain_Record {
    private BigDecimal id;

    private String floor;

    private String linename;

    private String machine;

    private String exceptionType;

    private String exceptionDescribe;

    private String repairPropose;

    private String priority;

    private Date exceptionTime;

    private Date updateTime;

    private String elapsedTime;

    private String flag;

    private String type;

    private String repairContent;

    private String employee;

    public AutoFloor_AVI_Maintain_Record(String floor, String linename, String machine, String exceptionType, String exceptionDescribe, String repairPropose, String priority, Date exceptionTime, Date updateTime, String elapsedTime, String flag, String type, String repairContent, String employee) {
        this.floor = floor;
        this.linename = linename;
        this.machine = machine;
        this.exceptionType = exceptionType;
        this.exceptionDescribe = exceptionDescribe;
        this.repairPropose = repairPropose;
        this.priority = priority;
        this.exceptionTime = exceptionTime;
        this.updateTime = updateTime;
        this.elapsedTime = elapsedTime;
        this.flag = flag;
        this.type = type;
        this.repairContent = repairContent;
        this.employee = employee;
    }
}