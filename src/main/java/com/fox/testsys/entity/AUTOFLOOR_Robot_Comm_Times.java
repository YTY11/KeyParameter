package com.fox.testsys.entity;

import java.math.BigDecimal;
import java.util.Date;

public class AUTOFLOOR_Robot_Comm_Times {
    private BigDecimal id;

    private String floor;

    private String product;

    private String lineName;

    private BigDecimal times;

    private Date startTime;

    private Date endTime;

    private String timeSlot;

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

    public BigDecimal getTimes() {
        return times;
    }

    public void setTimes(BigDecimal times) {
        this.times = times;
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

    @Override
    public String toString() {
        return "AUTOFLOOR_Robot_Comm_Times{" +
                "id=" + id +
                ", floor='" + floor + '\'' +
                ", product='" + product + '\'' +
                ", lineName='" + lineName + '\'' +
                ", times=" + times +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", timeSlot='" + timeSlot + '\'' +
                ", updateTime=" + updateTime +
                '}';
    }
}