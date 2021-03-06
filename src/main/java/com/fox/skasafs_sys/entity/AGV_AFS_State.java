package com.fox.skasafs_sys.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class AGV_AFS_State {
    private BigDecimal id;

    private String floor;

    private String workstation;

    private String devicename;

    private String state;

    private String abnormal;

    private Date updateTime;

    private Integer MachineSum;

    private Integer MachineHealth;

    private Integer MachineWarning;

    private Integer MachineUnusual;

    private Integer MachineLostConnection;

    private Double MachineRate;



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
        this.floor = floor == null ? null : floor.trim();
    }

    public String getWorkstation() {
        return workstation;
    }

    public void setWorkstation(String workstation) {
        this.workstation = workstation == null ? null : workstation.trim();
    }

    public String getDevicename() {
        return devicename;
    }

    public void setDevicename(String devicename) {
        this.devicename = devicename == null ? null : devicename.trim();
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state == null ? null : state.trim();
    }

    public String getAbnormal() {
        return abnormal;
    }

    public void setAbnormal(String abnormal) {
        this.abnormal = abnormal == null ? null : abnormal.trim();
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}