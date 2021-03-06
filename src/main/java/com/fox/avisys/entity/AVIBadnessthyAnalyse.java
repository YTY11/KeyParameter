package com.fox.avisys.entity;

import lombok.Data;

/**
 * @author
 * @@create 2020-12-02 14:42
 */
@Data
public class AVIBadnessthyAnalyse {

    private String badnessName;

    private Integer badnessValue;

    private Double badnessValueRate;

    public AVIBadnessthyAnalyse(String badnessName, Integer badnessValue, Double badnessValueRate) {
        this.badnessName = badnessName;
        this.badnessValue = badnessValue;
        this.badnessValueRate = badnessValueRate;
    }
}
