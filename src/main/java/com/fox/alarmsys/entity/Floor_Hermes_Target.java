package com.fox.alarmsys.entity;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Floor_Hermes_Target {
    private BigDecimal id;

    private String area;

    private String hermesFloor;

    private BigDecimal hermesTargetMax;

    private BigDecimal hermesTargetMin;

    private String hermesStatioName;

    private String hermesFunctionName;

    private String hermesStatioSn;

    private String hermesFlag;

    private String hermesMac;

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public String getHermesFloor() {
        return hermesFloor;
    }

    public void setHermesFloor(String hermesFloor) {
        this.hermesFloor = hermesFloor == null ? null : hermesFloor.trim();
    }

    public BigDecimal getHermesTargetMax() {
        return hermesTargetMax;
    }

    public void setHermesTargetMax(BigDecimal hermesTargetMax) {
        this.hermesTargetMax = hermesTargetMax;
    }

    public BigDecimal getHermesTargetMin() {
        return hermesTargetMin;
    }

    public void setHermesTargetMin(BigDecimal hermesTargetMin) {
        this.hermesTargetMin = hermesTargetMin;
    }

    public String getHermesStatioName() {
        return hermesStatioName;
    }

    public void setHermesStatioName(String hermesStatioName) {
        this.hermesStatioName = hermesStatioName == null ? null : hermesStatioName.trim();
    }

    public String getHermesFunctionName() {
        return hermesFunctionName;
    }

    public void setHermesFunctionName(String hermesFunctionName) {
        this.hermesFunctionName = hermesFunctionName == null ? null : hermesFunctionName.trim();
    }

    public String getHermesStatioSn() {
        return hermesStatioSn;
    }

    public void setHermesStatioSn(String hermesStatioSn) {
        this.hermesStatioSn = hermesStatioSn == null ? null : hermesStatioSn.trim();
    }

    public String getHermesFlag() {
        return hermesFlag;
    }

    public void setHermesFlag(String hermesFlag) {
        this.hermesFlag = hermesFlag == null ? null : hermesFlag.trim();
    }

    public String getHermesMac() {
        return hermesMac;
    }

    public void setHermesMac(String hermesMac) {
        this.hermesMac = hermesMac == null ? null : hermesMac.trim();
    }
}