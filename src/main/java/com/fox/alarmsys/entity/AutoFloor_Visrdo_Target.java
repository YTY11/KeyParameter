package com.fox.alarmsys.entity;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AutoFloor_Visrdo_Target {
    private BigDecimal id;

    private String area;

    private String floor;

    private String product;

    private String linename;

    private String machinetype;

    private String zoneNum;

    private Long zoneTargetMax;

    private Long zoneTargetMin;

    private Long flag;

    private String p1;

    private String p2;

    private String p3;

    private String p4;

    private String p5;

    private Long zoneWarningTargetMax;

    private Long zoneWarningTargetMin;

    private String name;

    private String value;

    

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area == null ? null : area.trim();
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

    public String getMachinetype() {
        return machinetype;
    }

    public void setMachinetype(String machinetype) {
        this.machinetype = machinetype == null ? null : machinetype.trim();
    }

    public String getZoneNum() {
        return zoneNum;
    }

    public void setZoneNum(String zoneNum) {
        this.zoneNum = zoneNum == null ? null : zoneNum.trim();
    }

    public Long getZoneTargetMax() {
        return zoneTargetMax;
    }

    public void setZoneTargetMax(Long zoneTargetMax) {
        this.zoneTargetMax = zoneTargetMax;
    }

    public Long getZoneTargetMin() {
        return zoneTargetMin;
    }

    public void setZoneTargetMin(Long zoneTargetMin) {
        this.zoneTargetMin = zoneTargetMin;
    }

    public Long getFlag() {
        return flag;
    }

    public void setFlag(Long flag) {
        this.flag = flag;
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

    public String getP5() {
        return p5;
    }

    public void setP5(String p5) {
        this.p5 = p5 == null ? null : p5.trim();
    }

    public Long getZoneWarningTargetMax() {
        return zoneWarningTargetMax;
    }

    public void setZoneWarningTargetMax(Long zoneWarningTargetMax) {
        this.zoneWarningTargetMax = zoneWarningTargetMax;
    }

    public Long getZoneWarningTargetMin() {
        return zoneWarningTargetMin;
    }

    public void setZoneWarningTargetMin(Long zoneWarningTargetMin) {
        this.zoneWarningTargetMin = zoneWarningTargetMin;
    }
}