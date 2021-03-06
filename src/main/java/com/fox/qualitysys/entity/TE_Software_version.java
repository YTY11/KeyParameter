package com.fox.qualitysys.entity;

import java.math.BigDecimal;
import java.util.Date;

public class TE_Software_version {
    private BigDecimal id;

    private Short status;

    private String product;

    private String station;

    private String stationOverlay;

    private String programMaster;

    private String programAssistant;

    private String version;

    private String scriptName;

    private String scriptMDate;

    private Date addTime;

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

    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station == null ? null : station.trim();
    }

    public String getStationOverlay() {
        return stationOverlay;
    }

    public void setStationOverlay(String stationOverlay) {
        this.stationOverlay = stationOverlay == null ? null : stationOverlay.trim();
    }

    public String getProgramMaster() {
        return programMaster;
    }

    public void setProgramMaster(String programMaster) {
        this.programMaster = programMaster == null ? null : programMaster.trim();
    }

    public String getProgramAssistant() {
        return programAssistant;
    }

    public void setProgramAssistant(String programAssistant) {
        this.programAssistant = programAssistant == null ? null : programAssistant.trim();
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version == null ? null : version.trim();
    }

    public String getScriptName() {
        return scriptName;
    }

    public void setScriptName(String scriptName) {
        this.scriptName = scriptName == null ? null : scriptName.trim();
    }

    public String getScriptMDate() {
        return scriptMDate;
    }

    public void setScriptMDate(String scriptMDate) {
        this.scriptMDate = scriptMDate == null ? null : scriptMDate.trim();
    }

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }
}