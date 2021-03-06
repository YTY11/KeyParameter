package com.fox.alarmsys.entity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 自動樓關鍵checkuphis
 *
 * @author wu
 * @date 2020/10/14
 */
public class AutoFloor_Key_CheckUP_HIS {
    private BigDecimal id;

    private String product;

    private String floor;

    private String linename;

    private String workstation;

    private String machinetype;

    private String keyPmsEn;

    private String keyPmsCh;

    private String spec;

    private String specMin;

    private String specMax;

    private String keyVal;

    private String checkResult;

    private String testtime;

    private Date updateTime;

    private String componenttype;

    private BigDecimal flag;

    private String repairContent;

    private Date repairEndTime;

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product == null ? null : product.trim();
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor == null ? null : floor.trim();
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

    public String getSpecMin() {
        return specMin;
    }

    public void setSpecMin(String specMin) {
        this.specMin = specMin == null ? null : specMin.trim();
    }

    public String getSpecMax() {
        return specMax;
    }

    public void setSpecMax(String specMax) {
        this.specMax = specMax == null ? null : specMax.trim();
    }

    public String getKeyVal() {
        return keyVal;
    }

    public void setKeyVal(String keyVal) {
        this.keyVal = keyVal == null ? null : keyVal.trim();
    }

    public String getCheckResult() {
        return checkResult;
    }

    public void setCheckResult(String checkResult) {
        this.checkResult = checkResult == null ? null : checkResult.trim();
    }

    public String getTesttime() {
        return testtime;
    }

    public void setTesttime(String testtime) {
        this.testtime = testtime == null ? null : testtime.trim();
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getComponenttype() {
        return componenttype;
    }

    public void setComponenttype(String componenttype) {
        this.componenttype = componenttype == null ? null : componenttype.trim();
    }

    public BigDecimal getFlag() {
        return flag;
    }

    public void setFlag(BigDecimal flag) {
        this.flag = flag;
    }

    public String getRepairContent() {
        return repairContent;
    }

    public void setRepairContent(String repairContent) {
        this.repairContent = repairContent == null ? null : repairContent.trim();
    }

    public Date getRepairEndTime() {
        return repairEndTime;
    }

    public void setRepairEndTime(Date repairEndTime) {
        this.repairEndTime = repairEndTime;
    }
}