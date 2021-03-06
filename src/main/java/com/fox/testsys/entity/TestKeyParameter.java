package com.fox.testsys.entity;

public class TestKeyParameter {

    private String LineName;

    private  Integer DTarget;

    private Double  WipasRate;

    private Double  PathlossRate;

    public String getLineName() {
        return LineName;
    }

    public void setLineName(String lineName) {
        LineName = lineName;
    }

    public Integer getDTarget() {
        return DTarget;
    }

    public void setDTarget(Integer DTarget) {
        this.DTarget = DTarget;
    }

    public Double getWipasRate() {
        return WipasRate;
    }

    public void setWipasRate(Double wipasRate) {
        WipasRate = wipasRate;
    }

    public Double getPathlossRate() {
        return PathlossRate;
    }

    public void setPathlossRate(Double pathlossRate) {
        PathlossRate = pathlossRate;
    }
}
