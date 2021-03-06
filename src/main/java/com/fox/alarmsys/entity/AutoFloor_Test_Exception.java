package com.fox.alarmsys.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 自動樓测试异常
 *
 * @author wu
 * @date 2020/10/14
 */
@Data
public class AutoFloor_Test_Exception {

    private BigDecimal id;

    private String floor;

    private String linename;

    private String machine;

    private String exceptionType;

    private String exceptionDescribe;

    private String priority;

    private Date exceptionTime;

    private Date updateTime;

    private Integer flag;

    private String repairPropose;

    private String repairContent;

    private String empName;

}