package com.fox.qualitysys.entity;

import lombok.Data;

/**
 * @author
 * @create 2020-05-25 11:27
 */

@Data
public class EquipmentSub {

    private String MachineType;

    private String Key_Pms_EN;

    private String Key_Pms_CH;

    private String Spec;

    private Double Spec_Min;

    private Double Spec_Max;

    private String ComponentType;

    private String Val;

    private Double ValDou;

    private Integer UnJudge;

    private String warningCase;

    private String sLine;

    private String station;
}
