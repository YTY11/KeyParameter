package com.fox.qualitysys.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class Warning_List_Slot {
    private BigDecimal id;

    private String sLine;

    private String product;

    private String line;

    private String station;

    private String stationNo;

    private BigDecimal slot;

    private String warningCase;

    private String warningDetail;

    private Date addTime;

    private Date closeTime;

    private Short quitFlag;

    private Short urlOk;

    private String urlMsg;

    private Integer MachineSum;


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

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line == null ? null : line.trim();
    }

    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station == null ? null : station.trim();
    }

    public String getStationNo() {
        return stationNo;
    }

    public void setStationNo(String stationNo) {
        this.stationNo = stationNo == null ? null : stationNo.trim();
    }

    public BigDecimal getSlot() {
        return slot;
    }

    public void setSlot(BigDecimal slot) {
        this.slot = slot;
    }

    public String getWarningCase() {
        return warningCase;
    }

    public void setWarningCase(String warningCase) {
        this.warningCase = warningCase == null ? null : warningCase.trim();
    }

    public String getWarningDetail() {
        return warningDetail;
    }

    public void setWarningDetail(String warningDetail) {
        this.warningDetail = warningDetail == null ? null : warningDetail.trim();
    }

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    public Date getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(Date closeTime) {
        this.closeTime = closeTime;
    }

    public Short getQuitFlag() {
        return quitFlag;
    }

    public void setQuitFlag(Short quitFlag) {
        this.quitFlag = quitFlag;
    }

    public Short getUrlOk() {
        return urlOk;
    }

    public void setUrlOk(Short urlOk) {
        this.urlOk = urlOk;
    }

    public String getUrlMsg() {
        return urlMsg;
    }

    public void setUrlMsg(String urlMsg) {
        this.urlMsg = urlMsg == null ? null : urlMsg.trim();
    }
}