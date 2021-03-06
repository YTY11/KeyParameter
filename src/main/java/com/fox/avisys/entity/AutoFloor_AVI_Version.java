package com.fox.avisys.entity;

import java.math.BigDecimal;

public class AutoFloor_AVI_Version {
    private BigDecimal id;

    private String area;

    private String floor;

    private String product;

    private String linename;

    private String workstation;

    private String machinetype;

    private String keyPmsEn;

    private String keyPmsCh;

    private String spec;

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

    public String getWorkstation() {
        return workstation;
    }

    public void setWorkstation(String workstation) {
        this.workstation = workstation == null ? null : workstation.trim();
    }

    public String getMachinetype() {
        return machinetype;
    }

    public void setMachinetype(String machinetype) {
        this.machinetype = machinetype == null ? null : machinetype.trim();
    }

    public String getKeyPmsEn() {
        return keyPmsEn;
    }

    public void setKeyPmsEn(String keyPmsEn) {
        this.keyPmsEn = keyPmsEn == null ? null : keyPmsEn.trim();
    }

    public String getKeyPmsCh() {
        return keyPmsCh;
    }

    public void setKeyPmsCh(String keyPmsCh) {
        this.keyPmsCh = keyPmsCh == null ? null : keyPmsCh.trim();
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec == null ? null : spec.trim();
    }
}