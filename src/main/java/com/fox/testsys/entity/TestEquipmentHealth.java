package com.fox.testsys.entity;

import java.math.BigDecimal;

/**
 * @author
 * @create 2020-05-12 8:23
 */
public class TestEquipmentHealth {

    private BigDecimal dTarget;

    private String LineName;
    //偏位率
    private Double RobotNoNormalRate;
    //误测率
    private Double MisdetetRate;
    //机故率
    private Double MachineRate;
    //超时待确认
    private Double TimeOutRate;

    public Double getMachineRate() {
        return MachineRate;
    }

    public void setMachineRate(Double machineRate) {
        MachineRate = machineRate;
    }

    public Double getTimeOutRate() {
        return TimeOutRate;
    }

    public void setTimeOutRate(Double timeOutRate) {
        TimeOutRate = timeOutRate;
    }

    public String getLineName() {
        return LineName;
    }

    public void setLineName(String lineName) {
        LineName = lineName;
    }

    public Double getRobotNoNormalRate() {
        return RobotNoNormalRate;
    }

    public void setRobotNoNormalRate(Double robotNoNormalRate) {
        RobotNoNormalRate = robotNoNormalRate;
    }

    public Double getMisdetetRate() {
        return MisdetetRate;
    }

    public void setMisdetetRate(Double misdetetRate) {
        MisdetetRate = misdetetRate;
    }

    public BigDecimal getdTarget() {
        return dTarget;
    }

    public void setdTarget(BigDecimal dTarget) {
        this.dTarget = dTarget;
    }
}
