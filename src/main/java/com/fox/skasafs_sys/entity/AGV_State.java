package com.fox.skasafs_sys.entity;

import java.math.BigDecimal;
import java.util.Date;

public class AGV_State {
    private BigDecimal id;

    private String agv;

    private String rfid;

    private String task;

    private String runstate;

    private String speed;

    private String abnormal;

    private String batterystate;

    private String state;

    private String network;

    private String workstation;

    private Date updateTime;

    private String floor;

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public String getAgv() {
        return agv;
    }

    public void setAgv(String agv) {
        this.agv = agv == null ? null : agv.trim();
    }

    public String getRfid() {
        return rfid;
    }

    public void setRfid(String rfid) {
        this.rfid = rfid == null ? null : rfid.trim();
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task == null ? null : task.trim();
    }

    public String getRunstate() {
        return runstate;
    }

    public void setRunstate(String runstate) {
        this.runstate = runstate == null ? null : runstate.trim();
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed == null ? null : speed.trim();
    }

    public String getAbnormal() {
        return abnormal;
    }

    public void setAbnormal(String abnormal) {
        this.abnormal = abnormal == null ? null : abnormal.trim();
    }

    public String getBatterystate() {
        return batterystate;
    }

    public void setBatterystate(String batterystate) {
        this.batterystate = batterystate == null ? null : batterystate.trim();
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state == null ? null : state.trim();
    }

    public String getNetwork() {
        return network;
    }

    public void setNetwork(String network) {
        this.network = network == null ? null : network.trim();
    }

    public String getWorkstation() {
        return workstation;
    }

    public void setWorkstation(String workstation) {
        this.workstation = workstation == null ? null : workstation.trim();
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor == null ? null : floor.trim();
    }
}