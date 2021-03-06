package com.fox.qualitysys.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class TE_Base_Station {
    private BigDecimal id;

    private Short status;

    private String product;

    private String line;

    private String station;

    private String stationNo;

    private String stationIp;

    private String statusType;

    private Date addTime;

    private String equipment;

    private BigDecimal fixFlag;

    private String sLine;

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public Short getStatus() {
        return status;
    }

    public void setStatus(Short status) {
        this.status = status;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product == null ? null : product.trim();
    }

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line == null ? null : line.trim();
    }

    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station == null ? null : station.trim();
    }

    public String getStationNo() {
        return stationNo;
    }

    public void setStationNo(String stationNo) {
        this.stationNo = stationNo == null ? null : stationNo.trim();
    }

    public String getStationIp() {
        return stationIp;
    }

    public void setStationIp(String stationIp) {
        this.stationIp = stationIp == null ? null : stationIp.trim();
    }

    public String getStatusType() {
        return statusType;
    }

    public void setStatusType(String statusType) {
        this.statusType = statusType == null ? null : statusType.trim();
    }

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    public String getEquipment() {
        return equipment;
    }

    public void setEquipment(String equipment) {
        this.equipment = equipment == null ? null : equipment.trim();
    }

    public BigDecimal getFixFlag() {
        return fixFlag;
    }

    public void setFixFlag(BigDecimal fixFlag) {
        this.fixFlag = fixFlag;
    }
}