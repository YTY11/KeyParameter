package com.fox.avisys.entity;

import java.math.BigDecimal;
import java.util.Date;

public class AutoFloor_AVI_Machine {
    private BigDecimal id;

    private String tFloor;

    private String tProduct;

    private String tLine;

    private String tTime;

    private String tAviwarningstate;

    private String tWarningstate;

    private String p1;

    private String p2;

    private BigDecimal flag;

    private Date time;

    private Date inputTime;

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public String gettFloor() {
        return tFloor;
    }

    public void settFloor(String tFloor) {
        this.tFloor = tFloor == null ? null : tFloor.trim();
    }

    public String gettProduct() {
        return tProduct;
    }

    public void settProduct(String tProduct) {
        this.tProduct = tProduct == null ? null : tProduct.trim();
    }

    public String gettLine() {
        return tLine;
    }

    public void settLine(String tLine) {
        this.tLine = tLine == null ? null : tLine.trim();
    }

    public String gettTime() {
        return tTime;
    }

    public void settTime(String tTime) {
        this.tTime = tTime == null ? null : tTime.trim();
    }

    public String gettAviwarningstate() {
        return tAviwarningstate;
    }

    public void settAviwarningstate(String tAviwarningstate) {
        this.tAviwarningstate = tAviwarningstate == null ? null : tAviwarningstate.trim();
    }

    public String gettWarningstate() {
        return tWarningstate;
    }

    public void settWarningstate(String tWarningstate) {
        this.tWarningstate = tWarningstate == null ? null : tWarningstate.trim();
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

    public BigDecimal getFlag() {
        return flag;
    }

    public void setFlag(BigDecimal flag) {
        this.flag = flag;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Date getInputTime() {
        return inputTime;
    }

    public void setInputTime(Date inputTime) {
        this.inputTime = inputTime;
    }
}