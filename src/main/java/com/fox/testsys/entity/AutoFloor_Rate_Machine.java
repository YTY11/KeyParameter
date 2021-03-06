package com.fox.testsys.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class AutoFloor_Rate_Machine {
    private BigDecimal id;

    private String floor;

    private String lineName;

    private String product;

    private String stationName;

    private String machineNo;

    private BigDecimal input;

    private BigDecimal pass;

    private BigDecimal fail;

    private BigDecimal retest;

    private BigDecimal unknown;

    private BigDecimal firstPass;

    private Date startTime;

    private Date endTime;

    private String timeSlot;

    private Date updateTime;

    private BigDecimal failCount;

    private BigDecimal failRetest;

    private Double FPYRate;

    private Double YieldRate;

    private Double MisdetetRate;

    private Double UnknownRate;

    private String UNMessage;

    public Double getFPYRate() {
        return FPYRate;
    }

    public void setFPYRate(Double FPYRate) {
        this.FPYRate = FPYRate;
    }

    public Double getYieldRate() {
        return YieldRate;
    }

    public void setYieldRate(Double yieldRate) {
        YieldRate = yieldRate;
    }

    public Double getMisdetetRate() {
        return MisdetetRate;
    }

    public void setMisdetetRate(Double misdetetRate) {
        MisdetetRate = misdetetRate;
    }

    public Double getUnknownRate() {
        return UnknownRate;
    }

    public void setUnknownRate(Double unknownRate) {
        UnknownRate = unknownRate;
    }

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

    public String getLineName() {
        return lineName;
    }

    public void setLineName(String lineName) {
        this.lineName = lineName == null ? null : lineName.trim();
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product == null ? null : product.trim();
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName == null ? null : stationName.trim();
    }

    public String getMachineNo() {
        return machineNo;
    }

    public void setMachineNo(String machineNo) {
        this.machineNo = machineNo == null ? null : machineNo.trim();
    }

    public BigDecimal getInput() {
        return input;
    }

    public void setInput(BigDecimal input) {
        this.input = input;
    }

    public BigDecimal getPass() {
        return pass;
    }

    public void setPass(BigDecimal pass) {
        this.pass = pass;
    }

    public BigDecimal getFail() {
        return fail;
    }

    public void setFail(BigDecimal fail) {
        this.fail = fail;
    }

    public BigDecimal getRetest() {
        return retest;
    }

    public void setRetest(BigDecimal retest) {
        this.retest = retest;
    }

    public BigDecimal getUnknown() {
        return unknown;
    }

    public void setUnknown(BigDecimal unknown) {
        this.unknown = unknown;
    }

    public BigDecimal getFirstPass() {
        return firstPass;
    }

    public void setFirstPass(BigDecimal firstPass) {
        this.firstPass = firstPass;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(String timeSlot) {
        this.timeSlot = timeSlot == null ? null : timeSlot.trim();
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public BigDecimal getFailCount() {
        return failCount;
    }

    public void setFailCount(BigDecimal failCount) {
        this.failCount = failCount;
    }

    public BigDecimal getFailRetest() {
        return failRetest;
    }

    public void setFailRetest(BigDecimal failRetest) {
        this.failRetest = failRetest;
    }
}