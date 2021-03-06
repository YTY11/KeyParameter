package com.fox.testsys.entity;

import lombok.Data;

/**
 * @author
 * @create 2019-08-20 19:14
 */
@Data
public class SumNumber {

    private  String LineName;
    private  String Product;
    private Integer DTarget;
    private Integer Pre_DFUsum;
    private Integer DFUsum;
    private Integer FCTsum;
    private Integer S1sum;
    private Integer S2sum;
    private Integer S3sum;
    private Integer S4sum;
    private Integer CCTsum;
    private Integer W1sum;
    private Integer W2sum;
    private Integer UWBsum;
    private Integer SCONDsum;
    private Integer WIFIASSOsum;
    private Integer MARSCONDsum;
    private Integer UPHTimesum;
    private String  Time_Slot;
    private Integer Cell_A;
    private Integer Cell_B;
    private Integer Cell_C;
    private Integer Cell_D;
    private Integer Cell_E;
    private Integer Cell_F;
    private Integer Cell_G;
    private Integer Cell_H;

    private Integer Cell_AFix;
    private Integer Cell_BFix;
    private Integer Cell_CFix;
    private Integer Cell_DFix;
    private Integer Cell_EFix;
    private Integer Cell_FFix;
    private Integer Cell_GFix;
    private Integer Cell_HFix;

    private Integer Pre_DFUamount;
    private Integer DFUamount;
    private Integer FCTamount;
    private Integer S1amount;
    private Integer S2amount;
    private Integer S3amount;
    private Integer S4amount;
    private Integer CCTamount;
    private Integer W1amount;
    private Integer W2amount;
    private Integer UWBamount;
    private Integer SCONDamount;
    private Integer WIFIASSOamount;
    private Integer MARSCONDamount;

    private String Pre_DFUMachine;
    private String DFUMachine;
    private String FCTMachine;
    private String S1Machine;
    private String S2Machine;
    private String S3Machine;
    private String S4Machine;
    private String CCTMachine;
    private String W1Machine;
    private String W2Machine;
    private String UWBMachine;
    private String SCONDMachine;
    private Integer WIFIASSOMachine;
    private Integer MARSCONDMachine;

    private Integer actionPre_DFUsum;
    private Integer actionDFUsum;
    private Integer actionFCTsum;
    private Integer actionS1sum;
    private Integer actionS2sum;
    private Integer actionS3sum;
    private Integer actionS4sum;
    private Integer actionCCTsum;
    private Integer actionW1sum;
    private Integer actionW2sum;
    private Integer actionUWBsum;
    private Integer actionSCONDsum;
    private Integer actionWIFIASSOsum;
    private Integer actionMARSCONDsum;

    private Integer actionUPHTimesum;

    private Integer ActionDataTarget;

    private Double RateCell_A;
    private Double RateCell_B;
    private Double RateCell_C;
    private Double RateCell_D;
    private Double RateCell_E;
    private Double RateCell_F;
    private Double RateCell_G;
    private Double RateCell_H;

    private Double waitCell_A;
    private Double waitCell_B;
    private Double waitCell_C;
    private Double waitCell_D;
    private Double waitCell_E;
    private Double waitCell_F;
    private Double waitCell_G;
    private Double waitCell_H;

    private Double Misdetet_A;
    private Double Misdetet_B;
    private Double Misdetet_C;
    private Double Misdetet_D;
    private Double Misdetet_E;
    private Double Misdetet_F;
    private Double Misdetet_G;
    private Double Misdetet_H;

    private Double RatePre_DFU;
    private Double RateDFU;
    private Double RateFCT;
    private Double RateS1;
    private Double RateS2;
    private Double RateS3;
    private Double RateS4;
    private Double RateCCT;
    private Double RateW1;
    private Double RateW2;
    private Double RateUWB;
    private Double RateSCOND;
    private Double RateWIFIASSO;
    private Double RateMARSCOND;

    private Integer Pre_DFU_OP;
    private Integer DFU_OP;
    private Integer FCT_OP;
    private Integer S1_OP;
    private Integer S2_OP;
    private Integer S3_OP;
    private Integer S4_OP;
    private Integer CCT_OP;
    private Integer W1_OP;
    private Integer W2_OP;
    private Integer UWB_OP;
    private Integer SCOND_OP;
    private Integer WIFIASSO_OP;
    private Integer MARSCOND_OP;

    private Double MisdetetPre_DFU;
    private Double MisdetetDFU;
    private Double MisdetetFCT;
    private Double MisdetetS1;
    private Double MisdetetS2;
    private Double MisdetetS3;
    private Double MisdetetS4;
    private Double MisdetetCCT;
    private Double MisdetetW1;
    private Double MisdetetW2;
    private Double MisdetetUWB;
    private Double MisdetetSCOND;
    private Double MisdetetWIFIASSO;
    private Double MisdetetMARSCOND;

    public String getTime_Slot() {
        return Time_Slot;
    }

    public void setTime_Slot(String time_Slot) {
        Time_Slot = time_Slot;
    }

    public Double getMisdetetDFU() {
        return MisdetetDFU;
    }

    public void setMisdetetDFU(Double misdetetDFU) {
        MisdetetDFU = misdetetDFU;
    }

    public Double getMisdetetFCT() {
        return MisdetetFCT;
    }

    public void setMisdetetFCT(Double misdetetFCT) {
        MisdetetFCT = misdetetFCT;
    }

    public Double getMisdetetS1() {
        return MisdetetS1;
    }

    public void setMisdetetS1(Double misdetetS1) {
        MisdetetS1 = misdetetS1;
    }

    public Double getMisdetetS2() {
        return MisdetetS2;
    }

    public void setMisdetetS2(Double misdetetS2) {
        MisdetetS2 = misdetetS2;
    }

    public Double getMisdetetS3() {
        return MisdetetS3;
    }

    public void setMisdetetS3(Double misdetetS3) {
        MisdetetS3 = misdetetS3;
    }

    public Double getMisdetetCCT() {
        return MisdetetCCT;
    }

    public void setMisdetetCCT(Double misdetetCCT) {
        MisdetetCCT = misdetetCCT;
    }

    public Double getMisdetetW1() {
        return MisdetetW1;
    }

    public void setMisdetetW1(Double misdetetW1) {
        MisdetetW1 = misdetetW1;
    }

    public Double getMisdetetW2() {
        return MisdetetW2;
    }

    public void setMisdetetW2(Double misdetetW2) {
        MisdetetW2 = misdetetW2;
    }

    public Double getMisdetetUWB() {
        return MisdetetUWB;
    }

    public void setMisdetetUWB(Double misdetetUWB) {
        MisdetetUWB = misdetetUWB;
    }

    public Double getMisdetetSCOND() {
        return MisdetetSCOND;
    }

    public void setMisdetetSCOND(Double misdetetSCOND) {
        MisdetetSCOND = misdetetSCOND;
    }

    public Double getMisdetet_A() {
        return Misdetet_A;
    }

    public void setMisdetet_A(Double misdetet_A) {
        Misdetet_A = misdetet_A;
    }

    public Double getMisdetet_B() {
        return Misdetet_B;
    }

    public void setMisdetet_B(Double misdetet_B) {
        Misdetet_B = misdetet_B;
    }

    public Double getMisdetet_C() {
        return Misdetet_C;
    }

    public void setMisdetet_C(Double misdetet_C) {
        Misdetet_C = misdetet_C;
    }

    public Double getMisdetet_D() {
        return Misdetet_D;
    }

    public void setMisdetet_D(Double misdetet_D) {
        Misdetet_D = misdetet_D;
    }

    public Double getMisdetet_E() {
        return Misdetet_E;
    }

    public void setMisdetet_E(Double misdetet_E) {
        Misdetet_E = misdetet_E;
    }

    public Double getMisdetet_F() {
        return Misdetet_F;
    }

    public void setMisdetet_F(Double misdetet_F) {
        Misdetet_F = misdetet_F;
    }

    public Double getMisdetet_G() {
        return Misdetet_G;
    }

    public void setMisdetet_G(Double misdetet_G) {
        Misdetet_G = misdetet_G;
    }

    public Double getMisdetet_H() {
        return Misdetet_H;
    }

    public void setMisdetet_H(Double misdetet_H) {
        Misdetet_H = misdetet_H;
    }

    public Integer getDFU_OP() {
        return DFU_OP;
    }

    public void setDFU_OP(Integer DFU_OP) {
        this.DFU_OP = DFU_OP;
    }

    public Integer getFCT_OP() {
        return FCT_OP;
    }

    public void setFCT_OP(Integer FCT_OP) {
        this.FCT_OP = FCT_OP;
    }

    public Integer getS1_OP() {
        return S1_OP;
    }

    public void setS1_OP(Integer s1_OP) {
        S1_OP = s1_OP;
    }

    public Integer getS2_OP() {
        return S2_OP;
    }

    public void setS2_OP(Integer s2_OP) {
        S2_OP = s2_OP;
    }

    public Integer getS3_OP() {
        return S3_OP;
    }

    public void setS3_OP(Integer s3_OP) {
        S3_OP = s3_OP;
    }

    public Integer getCCT_OP() {
        return CCT_OP;
    }

    public void setCCT_OP(Integer CCT_OP) {
        this.CCT_OP = CCT_OP;
    }

    public Integer getW1_OP() {
        return W1_OP;
    }

    public void setW1_OP(Integer w1_OP) {
        W1_OP = w1_OP;
    }

    public Integer getW2_OP() {
        return W2_OP;
    }

    public void setW2_OP(Integer w2_OP) {
        W2_OP = w2_OP;
    }

    public Integer getUWB_OP() {
        return UWB_OP;
    }

    public void setUWB_OP(Integer UWB_OP) {
        this.UWB_OP = UWB_OP;
    }

    public Integer getSCOND_OP() {
        return SCOND_OP;
    }

    public void setSCOND_OP(Integer SCOND_OP) {
        this.SCOND_OP = SCOND_OP;
    }

    public Double getWaitCell_A() {
        return waitCell_A;
    }

    public void setWaitCell_A(Double waitCell_A) {
        this.waitCell_A = waitCell_A;
    }

    public Double getWaitCell_B() {
        return waitCell_B;
    }

    public void setWaitCell_B(Double waitCell_B) {
        this.waitCell_B = waitCell_B;
    }

    public Double getWaitCell_C() {
        return waitCell_C;
    }

    public void setWaitCell_C(Double waitCell_C) {
        this.waitCell_C = waitCell_C;
    }

    public Double getWaitCell_D() {
        return waitCell_D;
    }

    public void setWaitCell_D(Double waitCell_D) {
        this.waitCell_D = waitCell_D;
    }

    public Double getWaitCell_E() {
        return waitCell_E;
    }

    public void setWaitCell_E(Double waitCell_E) {
        this.waitCell_E = waitCell_E;
    }

    public Double getWaitCell_F() {
        return waitCell_F;
    }

    public void setWaitCell_F(Double waitCell_F) {
        this.waitCell_F = waitCell_F;
    }

    public Double getWaitCell_G() {
        return waitCell_G;
    }

    public void setWaitCell_G(Double waitCell_G) {
        this.waitCell_G = waitCell_G;
    }

    public Double getWaitCell_H() {
        return waitCell_H;
    }

    public void setWaitCell_H(Double waitCell_H) {
        this.waitCell_H = waitCell_H;
    }

    public Double getRateDFU() {
        return RateDFU;
    }

    public void setRateDFU(Double rateDFU) {
        RateDFU = rateDFU;
    }

    public Double getRateFCT() {
        return RateFCT;
    }

    public void setRateFCT(Double rateFCT) {
        RateFCT = rateFCT;
    }

    public Double getRateS1() {
        return RateS1;
    }

    public void setRateS1(Double rateS1) {
        RateS1 = rateS1;
    }

    public Double getRateS2() {
        return RateS2;
    }

    public void setRateS2(Double rateS2) {
        RateS2 = rateS2;
    }

    public Double getRateS3() {
        return RateS3;
    }

    public void setRateS3(Double rateS3) {
        RateS3 = rateS3;
    }

    public Double getRateCCT() {
        return RateCCT;
    }

    public void setRateCCT(Double rateCCT) {
        RateCCT = rateCCT;
    }

    public Double getRateW1() {
        return RateW1;
    }

    public void setRateW1(Double rateW1) {
        RateW1 = rateW1;
    }

    public Double getRateW2() {
        return RateW2;
    }

    public void setRateW2(Double rateW2) {
        RateW2 = rateW2;
    }

    public Double getRateUWB() {
        return RateUWB;
    }

    public void setRateUWB(Double rateUWB) {
        RateUWB = rateUWB;
    }

    public Double getRateSCOND() {
        return RateSCOND;
    }

    public void setRateSCOND(Double rateSCOND) {
        RateSCOND = rateSCOND;
    }

    public Double getRateCell_A() {
        return RateCell_A;
    }

    public void setRateCell_A(Double rateCell_A) {
        RateCell_A = rateCell_A;
    }

    public Double getRateCell_B() {
        return RateCell_B;
    }

    public void setRateCell_B(Double rateCell_B) {
        RateCell_B = rateCell_B;
    }

    public Double getRateCell_C() {
        return RateCell_C;
    }

    public void setRateCell_C(Double rateCell_C) {
        RateCell_C = rateCell_C;
    }

    public Double getRateCell_D() {
        return RateCell_D;
    }

    public void setRateCell_D(Double rateCell_D) {
        RateCell_D = rateCell_D;
    }

    public Double getRateCell_E() {
        return RateCell_E;
    }

    public void setRateCell_E(Double rateCell_E) {
        RateCell_E = rateCell_E;
    }

    public Double getRateCell_F() {
        return RateCell_F;
    }

    public void setRateCell_F(Double rateCell_F) {
        RateCell_F = rateCell_F;
    }

    public Double getRateCell_G() {
        return RateCell_G;
    }

    public void setRateCell_G(Double rateCell_G) {
        RateCell_G = rateCell_G;
    }

    public Double getRateCell_H() {
        return RateCell_H;
    }

    public void setRateCell_H(Double rateCell_H) {
        RateCell_H = rateCell_H;
    }

    public Integer getActionDataTarget() {
        return ActionDataTarget;
    }

    public void setActionDataTarget(Integer actionDataTarget) {
        ActionDataTarget = actionDataTarget;
    }

    public Integer getCell_AFix() {
        return Cell_AFix;
    }

    public void setCell_AFix(Integer cell_AFix) {
        Cell_AFix = cell_AFix;
    }

    public Integer getCell_BFix() {
        return Cell_BFix;
    }

    public void setCell_BFix(Integer cell_BFix) {
        Cell_BFix = cell_BFix;
    }

    public Integer getCell_CFix() {
        return Cell_CFix;
    }

    public void setCell_CFix(Integer cell_CFix) {
        Cell_CFix = cell_CFix;
    }

    public Integer getCell_DFix() {
        return Cell_DFix;
    }

    public void setCell_DFix(Integer cell_DFix) {
        Cell_DFix = cell_DFix;
    }

    public Integer getCell_EFix() {
        return Cell_EFix;
    }

    public void setCell_EFix(Integer cell_EFix) {
        Cell_EFix = cell_EFix;
    }

    public Integer getCell_FFix() {
        return Cell_FFix;
    }

    public void setCell_FFix(Integer cell_FFix) {
        Cell_FFix = cell_FFix;
    }

    public Integer getCell_GFix() {
        return Cell_GFix;
    }

    public void setCell_GFix(Integer cell_GFix) {
        Cell_GFix = cell_GFix;
    }

    public Integer getCell_HFix() {
        return Cell_HFix;
    }

    public void setCell_HFix(Integer cell_HFix) {
        Cell_HFix = cell_HFix;
    }

    public Integer getDTarget() {
        return DTarget;
    }

    public void setDTarget(Integer DTarget) {
        this.DTarget = DTarget;
    }

    public String getDFUMachine() {
        return DFUMachine;
    }

    public void setDFUMachine(String DFUMachine) {
        this.DFUMachine = DFUMachine;
    }

    public String getFCTMachine() {
        return FCTMachine;
    }

    public void setFCTMachine(String FCTMachine) {
        this.FCTMachine = FCTMachine;
    }

    public String getS1Machine() {
        return S1Machine;
    }

    public void setS1Machine(String s1Machine) {
        S1Machine = s1Machine;
    }

    public String getS2Machine() {
        return S2Machine;
    }

    public void setS2Machine(String s2Machine) {
        S2Machine = s2Machine;
    }

    public String getS3Machine() {
        return S3Machine;
    }

    public void setS3Machine(String s3Machine) {
        S3Machine = s3Machine;
    }

    public String getCCTMachine() {
        return CCTMachine;
    }

    public void setCCTMachine(String CCTMachine) {
        this.CCTMachine = CCTMachine;
    }

    public String getW1Machine() {
        return W1Machine;
    }

    public void setW1Machine(String w1Machine) {
        W1Machine = w1Machine;
    }

    public String getW2Machine() {
        return W2Machine;
    }

    public void setW2Machine(String w2Machine) {
        W2Machine = w2Machine;
    }

    public String getUWBMachine() {
        return UWBMachine;
    }

    public void setUWBMachine(String UWBMachine) {
        this.UWBMachine = UWBMachine;
    }

    public String getSCONDMachine() {
        return SCONDMachine;
    }

    public void setSCONDMachine(String SCONDMachine) {
        this.SCONDMachine = SCONDMachine;
    }

    public String getLineName() {
        return LineName;
    }

    public void setLineName(String lineName) {
        LineName = lineName;
    }

    public String getProduct() {
        return Product;
    }

    public void setProduct(String product) {
        Product = product;
    }

    public Integer getCell_A() {
        return Cell_A;
    }

    public void setCell_A(Integer cell_A) {
        Cell_A = cell_A;
    }

    public Integer getCell_B() {
        return Cell_B;
    }

    public void setCell_B(Integer cell_B) {
        Cell_B = cell_B;
    }

    public Integer getCell_C() {
        return Cell_C;
    }

    public void setCell_C(Integer cell_C) {
        Cell_C = cell_C;
    }

    public Integer getCell_D() {
        return Cell_D;
    }

    public void setCell_D(Integer cell_D) {
        Cell_D = cell_D;
    }

    public Integer getCell_E() {
        return Cell_E;
    }

    public void setCell_E(Integer cell_E) {
        Cell_E = cell_E;
    }

    public Integer getCell_F() {
        return Cell_F;
    }

    public void setCell_F(Integer cell_F) {
        Cell_F = cell_F;
    }

    public Integer getCell_G() {
        return Cell_G;
    }

    public void setCell_G(Integer cell_G) {
        Cell_G = cell_G;
    }

    public Integer getCell_H() {
        return Cell_H;
    }

    public void setCell_H(Integer cell_H) {
        Cell_H = cell_H;
    }

    public Integer getDFUsum() {
        return DFUsum;
    }

    public void setDFUsum(Integer DFUsum) {
        this.DFUsum = DFUsum;
    }

    public Integer getFCTsum() {
        return FCTsum;
    }

    public void setFCTsum(Integer FCTsum) {
        this.FCTsum = FCTsum;
    }

    public Integer getS1sum() {
        return S1sum;
    }

    public void setS1sum(Integer s1sum) {
        S1sum = s1sum;
    }

    public Integer getS2sum() {
        return S2sum;
    }

    public void setS2sum(Integer s2sum) {
        S2sum = s2sum;
    }

    public Integer getS3sum() {
        return S3sum;
    }

    public void setS3sum(Integer s3sum) {
        S3sum = s3sum;
    }

    public Integer getCCTsum() {
        return CCTsum;
    }

    public void setCCTsum(Integer CCTsum) {
        this.CCTsum = CCTsum;
    }

    public Integer getW1sum() {
        return W1sum;
    }

    public void setW1sum(Integer w1sum) {
        W1sum = w1sum;
    }

    public Integer getW2sum() {
        return W2sum;
    }

    public void setW2sum(Integer w2sum) {
        W2sum = w2sum;
    }

    public Integer getUWBsum() {
        return UWBsum;
    }

    public void setUWBsum(Integer UWBsum) {
        this.UWBsum = UWBsum;
    }

    public Integer getSCONDsum() {
        return SCONDsum;
    }

    public void setSCONDsum(Integer SCONDsum) {
        this.SCONDsum = SCONDsum;
    }

    public Integer getUPHTimesum() {
        return UPHTimesum;
    }

    public void setUPHTimesum(Integer UPHTimesum) {
        this.UPHTimesum = UPHTimesum;
    }

    public Integer getDFUamount() {
        return DFUamount;
    }

    public void setDFUamount(Integer DFUamount) {
        this.DFUamount = DFUamount;
    }

    public Integer getFCTamount() {
        return FCTamount;
    }

    public void setFCTamount(Integer FCTamount) {
        this.FCTamount = FCTamount;
    }

    public Integer getS1amount() {
        return S1amount;
    }

    public void setS1amount(Integer s1amount) {
        S1amount = s1amount;
    }

    public Integer getS2amount() {
        return S2amount;
    }

    public void setS2amount(Integer s2amount) {
        S2amount = s2amount;
    }

    public Integer getS3amount() {
        return S3amount;
    }

    public void setS3amount(Integer s3amount) {
        S3amount = s3amount;
    }

    public Integer getCCTamount() {
        return CCTamount;
    }

    public void setCCTamount(Integer CCTamount) {
        this.CCTamount = CCTamount;
    }

    public Integer getW1amount() {
        return W1amount;
    }

    public void setW1amount(Integer w1amount) {
        W1amount = w1amount;
    }

    public Integer getW2amount() {
        return W2amount;
    }

    public void setW2amount(Integer w2amount) {
        W2amount = w2amount;
    }

    public Integer getUWBamount() {
        return UWBamount;
    }

    public void setUWBamount(Integer UWBamount) {
        this.UWBamount = UWBamount;
    }

    public Integer getSCONDamount() {
        return SCONDamount;
    }

    public void setSCONDamount(Integer SCONDamount) {
        this.SCONDamount = SCONDamount;
    }

    public Integer getActionDFUsum() {
        return actionDFUsum;
    }

    public void setActionDFUsum(Integer actionDFUsum) {
        this.actionDFUsum = actionDFUsum;
    }

    public Integer getActionFCTsum() {
        return actionFCTsum;
    }

    public void setActionFCTsum(Integer actionFCTsum) {
        this.actionFCTsum = actionFCTsum;
    }

    public Integer getActionS1sum() {
        return actionS1sum;
    }

    public void setActionS1sum(Integer actionS1sum) {
        this.actionS1sum = actionS1sum;
    }

    public Integer getActionS2sum() {
        return actionS2sum;
    }

    public void setActionS2sum(Integer actionS2sum) {
        this.actionS2sum = actionS2sum;
    }

    public Integer getActionS3sum() {
        return actionS3sum;
    }

    public void setActionS3sum(Integer actionS3sum) {
        this.actionS3sum = actionS3sum;
    }

    public Integer getActionCCTsum() {
        return actionCCTsum;
    }

    public void setActionCCTsum(Integer actionCCTsum) {
        this.actionCCTsum = actionCCTsum;
    }

    public Integer getActionW1sum() {
        return actionW1sum;
    }

    public void setActionW1sum(Integer actionW1sum) {
        this.actionW1sum = actionW1sum;
    }

    public Integer getActionW2sum() {
        return actionW2sum;
    }

    public void setActionW2sum(Integer actionW2sum) {
        this.actionW2sum = actionW2sum;
    }

    public Integer getActionUWBsum() {
        return actionUWBsum;
    }

    public void setActionUWBsum(Integer actionUWBsum) {
        this.actionUWBsum = actionUWBsum;
    }

    public Integer getActionSCONDsum() {
        return actionSCONDsum;
    }

    public void setActionSCONDsum(Integer actionSCONDsum) {
        this.actionSCONDsum = actionSCONDsum;
    }

    public Integer getActionUPHTimesum() {
        return actionUPHTimesum;
    }

    public void setActionUPHTimesum(Integer actionUPHTimesum) {
        this.actionUPHTimesum = actionUPHTimesum;
    }

    @Override
    public String toString() {
        return "SumNumber{" +
                "DFUamount=" + DFUamount +
                ", FCTamount=" + FCTamount +
                ", S1amount=" + S1amount +
                ", S2amount=" + S2amount +
                ", S3amount=" + S3amount +
                ", CCTamount=" + CCTamount +
                ", W1amount=" + W1amount +
                ", W2amount=" + W2amount +
                ", UWBamount=" + UWBamount +
                ", SCONDamount=" + SCONDamount +
                '}';
    }
}
