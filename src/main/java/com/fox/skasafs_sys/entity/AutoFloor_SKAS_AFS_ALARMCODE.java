package com.fox.skasafs_sys.entity;

import java.math.BigDecimal;
import java.util.Date;

public class AutoFloor_SKAS_AFS_ALARMCODE {
    private BigDecimal id;

    private String device;

    private String alarmCode;

    private String alarmMessage;

    private String falutType;

    private Date updateTime;

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device == null ? null : device.trim();
    }

    public String getAlarmCode() {
        return alarmCode;
    }

    public void setAlarmCode(String alarmCode) {
        this.alarmCode = alarmCode == null ? null : alarmCode.trim();
    }

    public String getAlarmMessage() {
        return alarmMessage;
    }

    public void setAlarmMessage(String alarmMessage) {
        this.alarmMessage = alarmMessage == null ? null : alarmMessage.trim();
    }

    public String getFalutType() {
        return falutType;
    }

    public void setFalutType(String falutType) {
        this.falutType = falutType == null ? null : falutType.trim();
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}