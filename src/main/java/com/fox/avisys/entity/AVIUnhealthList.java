package com.fox.avisys.entity;

import lombok.Data;

/**
 * @author
 * @create 2020-11-09 17:28
 */
@Data
public class AVIUnhealthList {

    private  Integer missingPaste;

    private  Integer residualGlue;

    private  Integer deviation;

    private  Integer breakage;

    private  Integer residualTin;

    private  Integer swelling;

    private  Integer fold;

    private  Integer leakage_Glue;

    private  Integer dirty;

    private  Integer snOmission;

    private  Integer vacancy;

    public AVIUnhealthList(Integer missingPaste, Integer residualGlue, Integer deviation, Integer breakage, Integer residualTin, Integer swelling, Integer fold, Integer leakage_glue, Integer dirty,Integer snOmission,Integer vacancy) {
        this.missingPaste=missingPaste;
        this.residualGlue=residualGlue;
        this.deviation=deviation;
        this.breakage=breakage;
        this.residualTin=residualTin;
        this.swelling=swelling;
        this.fold=fold;
        this.leakage_Glue=leakage_glue;
        this.dirty=dirty;
        this.snOmission= snOmission;
        this.vacancy=vacancy;

    }
}

