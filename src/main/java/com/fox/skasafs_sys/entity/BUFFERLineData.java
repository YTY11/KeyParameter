package com.fox.skasafs_sys.entity;

import lombok.Data;

/**
 * @author
 * @create 2020-07-21 9:21
 */
@Data
public class BUFFERLineData {
    private String  BUFFERDayList;

    private Integer StoreIssueTarget;

    private Integer StoreIssueAction;

    private Double  AchievingRateAction;

    private Double  MachineFaultRateTarget;

    private Double  MachineFaultRateAction;

}
