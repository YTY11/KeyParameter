package com.fox.qualitysys.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class AutoFloor_Key_CheCkUp {
    private BigDecimal id;

    private Integer OrderNumber;

    private String product;

    private String floor;

    private String linename;

    private String linename2;

    private String workstation;

    private String machinetype;

    private String keyPmsEn;

    private String keyPmsCh;

    private String spec;

    private String specMin;

    private String specMax;

    private String keyVal;

    private String checkResult;

    private String testtime;

    private Date updateTime;

    private Date RepairEndTime;

    private String ExcEndTime;

    private  String comPonentType;

    private String Priority;

    private Integer Flag;

    private Integer DisposeTime;

    private String ExceptionType;

    private String StrupdateTime;

    private String Responsibilities;

    private String MaintainSuggest;

    private String MaintainDetails;

    private String RepairPropose;

    private String RepairContent;

    private String processing;

    private String empName;
}