package com.fox.qualitysys.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class AutoFloor_Key_CheckNG {
    private String OrderNumber;

    private BigDecimal id;

    private String product;

    private String floor;

    private String linename;

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

    private String UpDateTimeStr;

    private Integer NGSum;

    private String Testtime;

    private Integer Flag;

    private Integer DisposeTime;

    private String RepairContent;


}