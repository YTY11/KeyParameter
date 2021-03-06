package com.fox.testsys.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class AutoFloor_Machine_Detail {
    private BigDecimal id;

    private String tArea;

    private String tFloor;

    private String tProduct;

    private String lineName;

    private String stationName;

    private String machineNo;

    private Date inputTime;

    private Date updateTime;

    private String p2;

    private String p3;

    private String p4;

    private String p5;

    private String p6;

    private Integer MachineSum;

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public String gettArea() {
        return tArea;
    }

    public void settArea(String tArea) {
        this.tArea = tArea == null ? null : tArea.trim();
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

    public String getLineName() {
        return lineName;
    }

    public void setLineName(String lineName) {
        this.lineName = lineName == null ? null : lineName.trim();
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

    public Date getInputTime() {
        return inputTime;
    }

    public void setInputTime(Date inputTime) {
        this.inputTime = inputTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
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