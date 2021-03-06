package com.fox.alarmsys.entity;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author
 * @create 2020-09-19 11:26
 */
@Data
public class AlarmModelingtoolTabData {

    private  BigDecimal Id;

    private  Integer Flag;

    private  String FloorName;       //樓層

    private  String LineName;       //綫體

    private  String Station;        //工站

    private  String MachineNO;      //機臺號

    private  String ExceptionType;  //機故類型

    private  Double MisdetectRate;  //最近4小時誤測率

    private  String ExceptionStep;  //異常步驟

    private  String ExceptionPre;   //異常預判

    private  String RepairContent;  //維修内容

    private  String ExceptionTime;   //異常時間
}
