package com.fox.skasafs_sys.entity;

import java.math.BigDecimal;
import java.util.Date;

public class AGV_AFS_FeedReCode {

    private BigDecimal id;

    private String floor;

    private String workstation;

    private String linename;

    private String tablex;

    private String slot;

    private String feedid;

    private String auto;

    private Date starttime;

    private Date endtime;

    private String cost;

    private Date updateTime;

    private String timeSlot;

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

    public String getLinename() {
        return linename;
    }

    public void setLinename(String linename) {
        this.linename = linename == null ? null : linename.trim();
    }

    public String getTablex() {
        return tablex;
    }

    public void setTablex(String tablex) {
        this.tablex = tablex == null ? null : tablex.trim();
    }

    public String getSlot() {
        return slot;
    }

    public void setSlot(String slot) {
        this.slot = slot == null ? null : slot.trim();
    }

    public String getFeedid() {
        return feedid;
    }

    public void setFeedid(String feedid) {
        this.feedid = feedid == null ? null : feedid.trim();
    }

    public String getAuto() {
        return auto;
    }

    public void setAuto(String auto) {
        this.auto = auto == null ? null : auto.trim();
    }

    public Date getStarttime() {
        return starttime;
    }

    public void setStarttime(Date starttime) {
        this.starttime = starttime;
    }

    public Date getEndtime() {
        return endtime;
    }

    public void setEndtime(Date endtime) {
        this.endtime = endtime;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost == null ? null : cost.trim();
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(String timeSlot) {
        this.timeSlot = timeSlot == null ? null : timeSlot.trim();
    }
}