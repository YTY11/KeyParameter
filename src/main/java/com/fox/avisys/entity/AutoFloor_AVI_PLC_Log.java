package com.fox.avisys.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class AutoFloor_AVI_PLC_Log {
    private BigDecimal id;

    private String floor;

    private String product;

    private String linename;

    private Date inputtime;

    private Date SysDate;



    private String topx;

    private String stationinfo;

    private String comstatus;

    private String contents;

    private String p1;

    private String p2;

    private String p3;

    private String p4;



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

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product == null ? null : product.trim();
    }

    public String getLinename() {
        return linename;
    }

    public void setLinename(String linename) {
        this.linename = linename == null ? null : linename.trim();
    }

    public Date getInputtime() {
        return inputtime;
    }

    public void setInputtime(Date inputtime) {
        this.inputtime = inputtime;
    }

    public String getTopx() {
        return topx;
    }

    public void setTopx(String topx) {
        this.topx = topx == null ? null : topx.trim();
    }

    public String getStationinfo() {
        return stationinfo;
    }

    public void setStationinfo(String stationinfo) {
        this.stationinfo = stationinfo == null ? null : stationinfo.trim();
    }

    public String getComstatus() {
        return comstatus;
    }

    public void setComstatus(String comstatus) {
        this.comstatus = comstatus == null ? null : comstatus.trim();
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents == null ? null : contents.trim();
    }

    public String getP1() {
        return p1;
    }

    public void setP1(String p1) {
        this.p1 = p1 == null ? null : p1.trim();
    }

    public String getP2() {
        return p2;
    }

    public void setP2(String p2) {
        this.p2 = p2 == null ? null : p2.trim();
    }

    public String getP3() {
        return p3;
    }

    public void setP3(String p3) {
        this.p3 = p3 == null ? null : p3.trim();
    }

    public String getP4() {
        return p4;
    }

    public void setP4(String p4) {
        this.p4 = p4 == null ? null : p4.trim();
    }
}