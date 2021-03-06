package com.fox.skasafs_sys.entity;

import lombok.Data;

/**
 * @author
 * @create 2020-06-17 14:13
 */
@Data
public class AGVTabData {

    private String Time;

    private Integer StoreIssueTarget;

    private Integer StoreIssueAction;

    private Double  AchievingRateAction;

    private Double  YieldRateTarget;

    private Double  YieldRateAction;

    private Double  MachineFaultRateTarget;

    private Double  MachineFaultRateAction;

    private Integer ERROR_TuoGui;

    private Integer ERROR_PengZhuang;

    private Integer ERROR_ZuDang;

    private Integer ERROR_Jiting;

    private Integer ERROR_ChaoShi;

    private Integer  MachineFaultTime;

}
