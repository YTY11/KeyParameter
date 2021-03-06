package com.fox.skasafs_sys.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class AGV_Exception {
    private BigDecimal id;

    private String device;

    private String error;

    private String errorDescribe;

    private Date starttime;

    private Date endtime;

    private String cost;

    private Double Minutecost;

    private String workstation;

    private Date updateTime;

    private String floor;

    private String rfid;

    private String timeSlot;

    private String DateTime;


}