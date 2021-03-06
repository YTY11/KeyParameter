package com.fox.avisys.entity;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author
 * @create 2020-11-09 9:09
 */

@Data
public class AVIUnhealthyAnalyse {

    private Integer missingPasteValue;

    private Integer residualGlueValue;

    private Integer deviationValue;

    private Integer breakageValue;

    private Integer residualTinValue;

    private Integer swellingValue;

    private Integer foldValue;

    private Integer leakage_GlueValue;

    private Integer dirtyValue;

    private Integer snOmissionValue;

    private Integer vacancyValue;

    private BigDecimal missingPasteRate;

    private BigDecimal residualGlueRate;

    private BigDecimal deviationRate;

    private BigDecimal breakageRate;

    private BigDecimal residualTinRate;

    private BigDecimal swellingRate;

    private BigDecimal foldRate;

    private BigDecimal leakage_GlueRate;

    private BigDecimal dirtyRate;

    private BigDecimal snOmissionRate;

    private BigDecimal vacancyRate;
}
