package com.fox.testsys.entity;

import java.math.BigDecimal;
import java.util.Date;

public class AutoFloor_Slant_Count {
    private BigDecimal id;

    private String floor;

    private String product;

    private String lineName;

    private String slantLocal;

    private String panelPcs;

    private String pos;

    private String leftRight;

    private BigDecimal slantCount;

    private Date slantDate;

    private String shift;

    private Date beginTime;

    private Date endTime;

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

    public String getSlantLocal() {
        return slantLocal;
    }

    public void setSlantLocal(String slantLocal) {
        this.slantLocal = slantLocal == null ? null : slantLocal.trim();
    }

    public String getPanelPcs() {
        return panelPcs;
    }

    public void setPanelPcs(String panelPcs) {
        this.panelPcs = panelPcs == null ? null : panelPcs.trim();
    }

    public String getPos() {
        return pos;
    }

    public void setPos(String pos) {
        this.pos = pos == null ? null : pos.trim();
    }

    public String getLeftRight() {
        return leftRight;
    }

    public void setLeftRight(String leftRight) {
        this.leftRight = leftRight == null ? null : leftRight.trim();
    }

    public BigDecimal getSlantCount() {
        return slantCount;
    }

    public void setSlantCount(BigDecimal slantCount) {
        this.slantCount = slantCount;
    }

    public Date getSlantDate() {
        return slantDate;
    }

    public void setSlantDate(Date slantDate) {
        this.slantDate = slantDate;
    }

    public String getShift() {
        return shift;
    }

    public void setShift(String shift) {
        this.shift = shift == null ? null : shift.trim();
    }

    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}