package com.fox.keyParameter.entity;

import java.math.BigDecimal;
import java.util.Date;

public class AutofloorTarget {
    private Short id;

    private Date targetDate;

    private String tClass;

    private String tFloor;

    private String lineName;

    private String tProduct;

    private Date inputTime;

    private BigDecimal dTarget;

    private BigDecimal workingHours;

    private BigDecimal hTarget;

    private String typeTarget;

    private String tArea;

    private String p1;

    private String p2;

    private String p3;

    private String p4;

    private String p5;

    private String p6;

    public Short getId() {
        return id;
    }

    public void setId(Short id) {
        this.id = id;
    }

    public Date getTargetDate() {
        return targetDate;
    }

    public void setTargetDate(Date targetDate) {
        this.targetDate = targetDate;
    }

    public String gettClass() {
        return tClass;
    }

    public void settClass(String tClass) {
        this.tClass = tClass == null ? null : tClass.trim();
    }

    public String gettFloor() {
        return tFloor;
    }

    public void settFloor(String tFloor) {
        this.tFloor = tFloor == null ? null : tFloor.trim();
    }

    public String getLineName() {
        return lineName;
    }

    public void setLineName(String lineName) {
        this.lineName = lineName == null ? null : lineName.trim();
    }

    public String gettProduct() {
        return tProduct;
    }

    public void settProduct(String tProduct) {
        this.tProduct = tProduct == null ? null : tProduct.trim();
    }

    public Date getInputTime() {
        return inputTime;
    }

    public void setInputTime(Date inputTime) {
        this.inputTime = inputTime;
    }

    public BigDecimal getdTarget() {
        return dTarget;
    }

    public void setdTarget(BigDecimal dTarget) {
        this.dTarget = dTarget;
    }

    public BigDecimal getWorkingHours() {
        return workingHours;
    }

    public void setWorkingHours(BigDecimal workingHours) {
        this.workingHours = workingHours;
    }

    public BigDecimal gethTarget() {
        return hTarget;
    }

    public void sethTarget(BigDecimal hTarget) {
        this.hTarget = hTarget;
    }

    public String getTypeTarget() {
        return typeTarget;
    }

    public void setTypeTarget(String typeTarget) {
        this.typeTarget = typeTarget == null ? null : typeTarget.trim();
    }

    public String gettArea() {
        return tArea;
    }

    public void settArea(String tArea) {
        this.tArea = tArea == null ? null : tArea.trim();
    }

    public String getP1() {
        return p1;
    }

    public void setP1(String p1) {
        this.p1 = p1 == null ? null : p1.trim();
    }

    public String getP2() {
        return p2;
    }

    public void setP2(String p2) {
        this.p2 = p2 == null ? null : p2.trim();
    }

    public String getP3() {
        return p3;
    }

    public void setP3(String p3) {
        this.p3 = p3 == null ? null : p3.trim();
    }

    public String getP4() {
        return p4;
    }

    public void setP4(String p4) {
        this.p4 = p4 == null ? null : p4.trim();
    }

    public String getP5() {
        return p5;
    }

    public void setP5(String p5) {
        this.p5 = p5 == null ? null : p5.trim();
    }

    public String getP6() {
        return p6;
    }

    public void setP6(String p6) {
        this.p6 = p6 == null ? null : p6.trim();
    }
}