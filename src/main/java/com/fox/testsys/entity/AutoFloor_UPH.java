package com.fox.testsys.entity;

import java.math.BigDecimal;
import java.util.Date;


public class AutoFloor_UPH {
    private BigDecimal id;

    private String floor;

    private String lineName;

    private String product;

    private String time_slot;

    private String station_name;

    private Integer StationSum;

    private Integer StationActionSum;

    private Integer StationActionTarget;

    private BigDecimal uph;

    private Date startTime;

    private Date endTime;



    public Integer getStationActionSum() {
        return StationActionSum;
    }

    public void setStationActionSum(Integer stationActionSum) {
        StationActionSum = stationActionSum;
    }

    public Integer getStationActionTarget() {
        return StationActionTarget;
    }

    public void setStationActionTarget(Integer stationActionTarget) {
        StationActionTarget = stationActionTarget;
    }

    public Integer getStationSum() {
        return StationSum;
    }

    public void setStationSum(Integer stationSum) {
        StationSum = stationSum;
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
        this.floor = floor;
    }

    public String getLineName() {
        return lineName;
    }

    public void setLineName(String lineName) {
        this.lineName = lineName;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getTime_slot() {
        return time_slot;
    }

    public void setTime_slot(String time_slot) {
        this.time_slot = time_slot;
    }

    public String getStation_name() {
        return station_name;
    }

    public void setStation_name(String station_name) {
        this.station_name = station_name;
    }

    public BigDecimal getUph() {
        return uph;
    }

    public void setUph(BigDecimal uph) {
        this.uph = uph;
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

    @Override
    public String toString() {
        return "AutoFloor_UPH{" +
                "id=" + id +
                ", floor='" + floor + '\'' +
                ", lineName='" + lineName + '\'' +
                ", product='" + product + '\'' +
                ", time_slot='" + time_slot + '\'' +
                ", station_name='" + station_name + '\'' +
                ", StationSum=" + StationSum +
                ", StationActionSum=" + StationActionSum +
                ", StationActionTarget=" + StationActionTarget +
                ", uph=" + uph +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }
}