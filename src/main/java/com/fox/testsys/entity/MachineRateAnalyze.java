package com.fox.testsys.entity;

import lombok.Data;

/**
 * @author
 * @create 2020-05-22 10:33
 */
@Data
public class MachineRateAnalyze {
    private String LineName;

    private Integer MachineTime;

    private Double  TTLMachineRate;

    private Double  NoNormalRate;

    private Double  EquipmentRate;

    private Double  AcrossGroupRate;

    private Double  OtherRate;

    private String  UnusualMessage;

    private String  UnusualPrognosis;





}
