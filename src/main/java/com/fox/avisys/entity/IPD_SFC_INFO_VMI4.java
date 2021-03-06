package com.fox.avisys.entity;

public class IPD_SFC_INFO_VMI4 {
    private String serialNumber;

    private String groupName;

    private String inStationTime;

    private String moNumber;

    private String modelName;

    private String lineName;

    private String errorFalg;

    private String fresh;

    private String day;

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber == null ? null : serialNumber.trim();
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName == null ? null : groupName.trim();
    }

    public String getInStationTime() {
        return inStationTime;
    }

    public void setInStationTime(String inStationTime) {
        this.inStationTime = inStationTime == null ? null : inStationTime.trim();
    }

    public String getMoNumber() {
        return moNumber;
    }

    public void setMoNumber(String moNumber) {
        this.moNumber = moNumber == null ? null : moNumber.trim();
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName == null ? null : modelName.trim();
    }

    public String getLineName() {
        return lineName;
    }

    public void setLineName(String lineName) {
        this.lineName = lineName == null ? null : lineName.trim();
    }

    public String getErrorFalg() {
        return errorFalg;
    }

    public void setErrorFalg(String errorFalg) {
        this.errorFalg = errorFalg == null ? null : errorFalg.trim();
    }

    public String getFresh() {
        return fresh;
    }

    public void setFresh(String fresh) {
        this.fresh = fresh == null ? null : fresh.trim();
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day == null ? null : day.trim();
    }
}