package com.fox.testsys.entity;

import java.math.BigDecimal;
import java.util.Date;

public class AutoFloor_CCDData {
    private BigDecimal id;

    private String area;

    private String floor;

    private String product;

    private String linename;

    private String equipmentid;

    private String runstate;

    private String moduleaction;

    private String sn;

    private String testresult;

    private String scanresult;

    private String errorcode;

    private String errordesc;

    private String sensorstate;

    private Date inputTime;

    private Date errorstarttime;

    private Date errorendtime;

    private String p3;

    private String p4;

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

    public String getEquipmentid() {
        return equipmentid;
    }

    public void setEquipmentid(String equipmentid) {
        this.equipmentid = equipmentid == null ? null : equipmentid.trim();
    }

    public String getRunstate() {
        return runstate;
    }

    public void setRunstate(String runstate) {
        this.runstate = runstate == null ? null : runstate.trim();
    }

    public String getModuleaction() {
        return moduleaction;
    }

    public void setModuleaction(String moduleaction) {
        this.moduleaction = moduleaction == null ? null : moduleaction.trim();
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn == null ? null : sn.trim();
    }

    public String getTestresult() {
        return testresult;
    }

    public void setTestresult(String testresult) {
        this.testresult = testresult == null ? null : testresult.trim();
    }

    public String getScanresult() {
        return scanresult;
    }

    public void setScanresult(String scanresult) {
        this.scanresult = scanresult == null ? null : scanresult.trim();
    }

    public String getErrorcode() {
        return errorcode;
    }

    public void setErrorcode(String errorcode) {
        this.errorcode = errorcode == null ? null : errorcode.trim();
    }

    public String getErrordesc() {
        return errordesc;
    }

    public void setErrordesc(String errordesc) {
        this.errordesc = errordesc == null ? null : errordesc.trim();
    }

    public String getSensorstate() {
        return sensorstate;
    }

    public void setSensorstate(String sensorstate) {
        this.sensorstate = sensorstate == null ? null : sensorstate.trim();
    }

    public Date getInputTime() {
        return inputTime;
    }

    public void setInputTime(Date inputTime) {
        this.inputTime = inputTime;
    }

    public Date getErrorstarttime() {
        return errorstarttime;
    }

    public void setErrorstarttime(Date errorstarttime) {
        this.errorstarttime = errorstarttime;
    }

    public Date getErrorendtime() {
        return errorendtime;
    }

    public void setErrorendtime(Date errorendtime) {
        this.errorendtime = errorendtime;
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