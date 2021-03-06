package com.fox.skasafs_sys.entity;

import lombok.Data;

/**
 * @author
 * @create 2020-06-17 14:13
 */
@Data
public class AGVLineData {


    private String  AGVDayList;

    private Integer StoreIssueTarget;

    private Integer StoreIssueAction;

    private Double  AchievingRateAction;

    private Double  MachineFaultRateTarget;

    private Double  MachineFaultRateAction;

}
