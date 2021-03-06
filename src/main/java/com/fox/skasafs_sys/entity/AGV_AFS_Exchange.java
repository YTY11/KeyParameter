package com.fox.skasafs_sys.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class AGV_AFS_Exchange {
    private BigDecimal id;

    private String floor;

    private String workstation;

    private String linename;

    private String agvtobuf;

    private String buftoafs;

    private Date starttime;

    private Date endtime;

    private String cost;

    private Date updateTime;

    private String timeSlot;


}