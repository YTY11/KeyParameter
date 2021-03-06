package com.fox.qualitysys.entity;

import java.math.BigDecimal;
import java.util.Date;

public class AutoFloor_Fail_Retest {
    private BigDecimal id;

    private String floor;

    private String product;

    private String lineName;

    private String sn;

    private String stationName;

    private Date firstFailTime;

    private Date secondTestTime;

    private BigDecimal secondTestResult;

    private Date thirdTestTime;

    private BigDecimal thirdTestResult;

    private String retestFinish;

    private Date retestFinishTime;

    private String retestTimeOut;

    private Date updateTime;

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor == null ? null : floor.trim();
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product == null ? null : product.trim();
    }

    public String getLineName() {
        return lineName;
    }

    public void setLineName(String lineName) {
        this.lineName = lineName == null ? null : lineName.trim();
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn == null ? null : sn.trim();
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName == null ? null : stationName.trim();
    }

    public Date getFirstFailTime() {
        return firstFailTime;
    }

    public void setFirstFailTime(Date firstFailTime) {
        this.firstFailTime = firstFailTime;
    }

    public Date getSecondTestTime() {
        return secondTestTime;
    }

    public void setSecondTestTime(Date secondTestTime) {
        this.secondTestTime = secondTestTime;
    }

    public BigDecimal getSecondTestResult() {
        return secondTestResult;
    }

    public void setSecondTestResult(BigDecimal secondTestResult) {
        this.secondTestResult = secondTestResult;
    }

    public Date getThirdTestTime() {
        return thirdTestTime;
    }

    public void setThirdTestTime(Date thirdTestTime) {
        this.thirdTestTime = thirdTestTime;
    }

    public BigDecimal getThirdTestResult() {
        return thirdTestResult;
    }

    public void setThirdTestResult(BigDecimal thirdTestResult) {
        this.thirdTestResult = thirdTestResult;
    }

    public String getRetestFinish() {
        return retestFinish;
    }

    public void setRetestFinish(String retestFinish) {
        this.retestFinish = retestFinish == null ? null : retestFinish.trim();
    }

    public Date getRetestFinishTime() {
        return retestFinishTime;
    }

    public void setRetestFinishTime(Date retestFinishTime) {
        this.retestFinishTime = retestFinishTime;
    }

    public String getRetestTimeOut() {
        return retestTimeOut;
    }

    public void setRetestTimeOut(String retestTimeOut) {
        this.retestTimeOut = retestTimeOut == null ? null : retestTimeOut.trim();
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}