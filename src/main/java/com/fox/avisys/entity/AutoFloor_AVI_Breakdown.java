package com.fox.avisys.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class AutoFloor_AVI_Breakdown {

    private BigDecimal id;

    private String floor;

    private String product;

    private String linename;

    private Date starttime;

    private Date endtime;

    private String timeelapsed;

    private String lighttype;

    private String ttype;

    private String issuedesc;

    private String repairaction;

    private String p1;

    private String p2;

    private String p3;

    private String p4;

    private String ERRORTimeString;



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

    public String getTimeelapsed() {
        return timeelapsed;
    }

    public void setTimeelapsed(String timeelapsed) {
        this.timeelapsed = timeelapsed == null ? null : timeelapsed.trim();
    }

    public String getLighttype() {
        return lighttype;
    }

    public void setLighttype(String lighttype) {
        this.lighttype = lighttype == null ? null : lighttype.trim();
    }

    public String getTtype() {
        return ttype;
    }

    public void setTtype(String ttype) {
        this.ttype = ttype == null ? null : ttype.trim();
    }

    public String getIssuedesc() {
        return issuedesc;
    }

    public void setIssuedesc(String issuedesc) {
        this.issuedesc = issuedesc == null ? null : issuedesc.trim();
    }

    public String getRepairaction() {
        return repairaction;
    }

    public void setRepairaction(String repairaction) {
        this.repairaction = repairaction == null ? null : repairaction.trim();
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