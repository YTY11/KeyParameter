package com.fox.alarmsys.entity;

import lombok.Data;

/**
 * @author
 * @create 2020-08-17 19:01
 */
@Data
public class SoftwareMonitoring {

    private  String LineName;
    private  String MachineType;
    private  Integer Pre_AP;
    private  Integer Post_AP;
    private  Integer Roater;
    private  Integer RF_Cell01;
    private  Integer RF_Cell02;
    private  Integer RF_Cell03;
    private  Integer RF_Cell04;
    private  Integer RF_Cell05;
    private  Integer RF_Cell06;
    private  Integer RF_Cell07;
    private  Integer RF_Cell08;
    private  Integer RF_Module;
    private  Integer CCD_ASSORT;
    private  Integer CCD_Module1;
    private  Integer CCD_Module2;
    private  Integer CCD_Module3;
    private  Integer PD_Cell;
    private  String  UpDateTime;
}
