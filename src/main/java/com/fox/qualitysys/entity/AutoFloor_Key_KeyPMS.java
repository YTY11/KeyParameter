package com.fox.qualitysys.entity;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AutoFloor_Key_KeyPMS {
    private BigDecimal id;

    private String workstation;

    private String machinetype;

    private String keyPmsEn;

    private String keyPmsCh;

    private String spec;

    private BigDecimal specMin;

    private BigDecimal specMax;

    private String componenttype;

    private String product;

    private String floor;

    private String area;

    private String linename;

    private String linename2;

    private String processing;


}