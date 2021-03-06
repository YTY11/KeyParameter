package com.fox.alarmsys.entity;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 警報查詢entity
 *
 * @author wu
 * @create 2020-10-15 11:04
 * @date 2020/10/15
 */

@Data
public class AlarmEnquiriesEntity {

    @NotNull
    private String floorName;

    @NotNull
    private String lineName;

    @NotNull
    private String productName;

    @NotNull
    private String startDate;

    @NotNull
    private String endDate;

    @NotNull
    private String unit;

    @NotNull
    private String priority;

    @NotNull
    private String flag;

    @NotNull
    private String alarmTypeName;

    @NotNull
    private String machineFaultName;

}
