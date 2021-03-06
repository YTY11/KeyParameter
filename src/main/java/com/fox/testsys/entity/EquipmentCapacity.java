package com.fox.testsys.entity;

public class EquipmentCapacity {


    private  String  LineName;

    private  Integer TestEquipmentAllNum;

    private  Integer TestEquipmentHealthNum;

    private  Double  TestEquipmentHealthRate;

    private  Integer TestRobotAllNum;

    private  Integer TestRobotHealthNum;

    private  Double  TestRobotHealthRate;

    private  Integer DTarget;

    public Integer getDTarget() {
        return DTarget;
    }

    public void setDTarget(Integer DTarget) {
        this.DTarget = DTarget;
    }

    public String getLineName() {
        return LineName;
    }

    public void setLineName(String lineName) {
        LineName = lineName;
    }

    public Integer getTestEquipmentAllNum() {
        return TestEquipmentAllNum;
    }

    public void setTestEquipmentAllNum(Integer testEquipmentAllNum) {
        TestEquipmentAllNum = testEquipmentAllNum;
    }

    public Integer getTestEquipmentHealthNum() {
        return TestEquipmentHealthNum;
    }

    public void setTestEquipmentHealthNum(Integer testEquipmentHealthNum) {
        TestEquipmentHealthNum = testEquipmentHealthNum;
    }

    public Double getTestEquipmentHealthRate() {
        return TestEquipmentHealthRate;
    }

    public void setTestEquipmentHealthRate(Double testEquipmentHealthRate) {
        TestEquipmentHealthRate = testEquipmentHealthRate;
    }

    public Integer getTestRobotAllNum() {
        return TestRobotAllNum;
    }

    public void setTestRobotAllNum(Integer testRobotAllNum) {
        TestRobotAllNum = testRobotAllNum;
    }

    public Integer getTestRobotHealthNum() {
        return TestRobotHealthNum;
    }

    public void setTestRobotHealthNum(Integer testRobotHealthNum) {
        TestRobotHealthNum = testRobotHealthNum;
    }

    public Double getTestRobotHealthRate() {
        return TestRobotHealthRate;
    }

    public void setTestRobotHealthRate(Double testRobotHealthRate) {
        TestRobotHealthRate = testRobotHealthRate;
    }
}
