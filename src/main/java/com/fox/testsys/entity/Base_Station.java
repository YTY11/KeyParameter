package com.fox.testsys.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class Base_Station {

    private BigDecimal id;

    private Short status;

    private String product;

    private String line;

    private String station;

    private String stationno;

    private String station_no;

    private String stationip;

    private String statustype;

    private Integer LineStatusNum;

    private Date addTime;

    private String equipment;

    private Integer ttl;

    private Integer onlines;

    private Double  normals;

    private Integer warnings;

    private Integer debugs;

    private Integer offlines;

    private String Result;


    public Integer getLineStatusNum() {
        return LineStatusNum;
    }

    public void setLineStatusNum(Integer lineStatusNum) {
        LineStatusNum = lineStatusNum;
    }

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

    public String getStationno() {
        return stationno;
    }

    public void setStationno(String stationno) {
        this.stationno = stationno == null ? null : stationno.trim();
    }

    public String getStationip() {
        return stationip;
    }

    public void setStationip(String stationip) {
        this.stationip = stationip == null ? null : stationip.trim();
    }

    public String getStatustype() {
        return statustype;
    }

    public void setStatustype(String statustype) {
        this.statustype = statustype == null ? null : statustype.trim();
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
}