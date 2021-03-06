package com.fox.testsys.entity;

import java.math.BigDecimal;
import java.util.Date;

public class AUTOFLOOR_Robot_Efficiency {
    private BigDecimal id;

    private String floor;

    private String product;

    private String lineName;

    private BigDecimal slantTime;

    private BigDecimal slantCount;

    private BigDecimal waitTime;

    private Date updateTime;

    private  String Time;


    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
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

    public BigDecimal getSlantTime() {
        return slantTime;
    }

    public void setSlantTime(BigDecimal slantTime) {
        this.slantTime = slantTime;
    }

    public BigDecimal getSlantCount() {
        return slantCount;
    }

    public void setSlantCount(BigDecimal slantCount) {
        this.slantCount = slantCount;
    }

    public BigDecimal getWaitTime() {
        return waitTime;
    }

    public void setWaitTime(BigDecimal waitTime) {
        this.waitTime = waitTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "AUTOFLOOR_Robot_Efficiency{" +
                "id=" + id +
                ", floor='" + floor + '\'' +
                ", product='" + product + '\'' +
                ", lineName='" + lineName + '\'' +
                ", slantTime=" + slantTime +
                ", slantCount=" + slantCount +
                ", waitTime=" + waitTime +
                ", updateTime=" + updateTime +
                ", Time='" + Time + '\'' +
                '}';
    }
}