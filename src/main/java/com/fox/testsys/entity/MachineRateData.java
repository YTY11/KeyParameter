package com.fox.testsys.entity;


import lombok.Data;

@Data
public class MachineRateData {

    private String LineName;

    private String StationNameStr;

    private String CellName;

    private Integer AutoTime;

    private Integer MachineTime;

    private Double  MachineRate;

    private Integer NormalNum;

    private Double  NormalRate;


}
