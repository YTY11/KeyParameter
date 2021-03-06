package com.fox.avisys.entity;

import lombok.Data;

/**
 * @author
 * @create 2020-06-20 15:02
 */
@Data
public class AutoFloor_AVIUPHLineData {

    private  String  TIME_SLOT;

    private  Integer AVIUPHSum;

    private  Double AVIUPHPassRate;

    private  Double AVIUPHFailRate;


}
