package com.fox.skasafs_sys.entity;

import lombok.Data;

/**
 * @author
 * @create 2020-07-21 9:21
 */
@Data
public class BUFFERTabData {

    private String Time;

    private Integer StoreIssueTarget;

    private Integer StoreIssueAction;

    private Double  AchievingRateAction;

    private Double  YieldRateTarget;

    private Double  YieldRateAction;

    private Double  MachineFaultRateTarget;

    private Double  MachineFaultRateAction;

    private Integer ERROR_TuoGui;

    private Integer ERROR_PengZhuang;

    private Integer ERROR_ZuDang;

    private Integer ERROR_Jiting;

    private Integer ERROR_ChaoShi;

    private Integer ERROR_CCDYiChang;

    private Integer ERROR_ShangLiaoKaLiao;

    private Integer ERROR_JieLiaoPianWei;

    private Integer ERROR_ShangLiaoDuiJieKaLiao;

    private Integer ERROR_XiaLiaoKaLiao;

    private Integer ERROR_KaLiaoYiChang;

    private Integer  MachineFaultTime;

}
