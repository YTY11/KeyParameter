package com.fox.skasafs_sys.service;


import com.fox.skasafs_sys.entity.*;
import com.fox.skasafs_sys.mapper.*;
import com.fox.skasafs_sys.utility.SKASAFSUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author
 * @create 2020-07-06 14:06
 */

@Service
public class SKASAFSSystemMessageAjaxService {
    @Autowired
    AGV_ExceptionMapper agv_exceptionMapper;

    @Autowired
    SKASAFSSystemHomeService skasafsSystemHomeService;

    @Autowired
    AGV_AFS_ExchangeMapper agv_afs_exchangeMapper;

    @Autowired
    AGV_AFS_RGVReCodeMapper agv_afs_rgvReCodeMapper;

    @Autowired
    AGV_AFS_StateMapper agv_afs_stateMapper;

    @Autowired
    AGV_AFS_FeedReCodeMapper agv_afs_feedReCodeMapper;
    @Autowired
    SKASAFSSystemMessageService skasafsSystemMessageService;

    @Autowired
    AutoFloor_SKAS_AFS_ALARMCODEMapper autoFloor_skas_afs_alarmcodeMapper;
    @Autowired
    SKASAFSUtility skasafsUtility;

    private  static  final  Integer DishNum=25;

    public List<AGV_Exception> SKASAFSystemMessageAjaxData(String Machine, String CarNum, String FloorName, String Time, String StartDate, String EndDate){
        List<AGV_Exception> MessageAjaxData=new ArrayList<>();
        if ("C001".equals(CarNum)){
            CarNum="";
        }
            List<AGV_Exception> SKASAFSystemMessageAjaxData=new ArrayList<>();
            List<AGV_Exception> agv_exceptions = agv_exceptionMapper.selectMachineUnMessage(CarNum,FloorName, Time, StartDate, EndDate);
            if ("RGV".equals(Machine)){
                List<String> AGVWorkstationsList =new ArrayList<>();
                List<String> AGVERRORList  =new ArrayList<>();
                RGV_AGV_AjaxAddList(Machine,SKASAFSystemMessageAjaxData, agv_exceptions, AGVWorkstationsList, AGVERRORList);
            }else  if ("AGV".equals(Machine)){
                List<String> AGVWorkstationsList =skasafsSystemHomeService.AGVWorkstationsList();
                List<String> AGVERRORList = skasafsSystemMessageService.ERRORList("AGV");
                RGV_AGV_AjaxAddList(Machine,SKASAFSystemMessageAjaxData, agv_exceptions, AGVWorkstationsList, AGVERRORList);
            }else  if ("BUFFER".equals(Machine)){
                List<AutoFloor_SKAS_AFS_ALARMCODE> BUFFERalarmcodes = autoFloor_skas_afs_alarmcodeMapper.selectAlarmCodeList("B001");
                BUF_AFS_AjaxAddList(Machine, CarNum, SKASAFSystemMessageAjaxData, agv_exceptions, BUFFERalarmcodes);
            }else  if ("AFS".equals(Machine)){
                List<AutoFloor_SKAS_AFS_ALARMCODE> AFSAlarmcodes = autoFloor_skas_afs_alarmcodeMapper.selectAlarmCodeList("S001");
                BUF_AFS_AjaxAddList(Machine, CarNum, SKASAFSystemMessageAjaxData, agv_exceptions, AFSAlarmcodes);
            }


        return SKASAFSystemMessageAjaxData;
    }

    public void BUF_AFS_AjaxAddList(String Machine, String CarNum, List<AGV_Exception> SKASAFSystemMessageAjaxData, List<AGV_Exception> agv_exceptions, List<AutoFloor_SKAS_AFS_ALARMCODE> BUFFERalarmcodes) {
        for (AutoFloor_SKAS_AFS_ALARMCODE autoFloor_skas_afs_alarmcode : BUFFERalarmcodes) {
            String FalutType = autoFloor_skas_afs_alarmcode.getFalutType();
            String alarmCode = autoFloor_skas_afs_alarmcode.getAlarmCode();
            for (AGV_Exception rgv_exception : agv_exceptions) {
                AGV_Exception SKASAFSystemMessage=new AGV_Exception();
                String cost = rgv_exception.getCost();
                String device = rgv_exception.getDevice();
                String errorCode = rgv_exception.getError();
                String timeSlot = rgv_exception.getTimeSlot();
                SKASAFSystemMessage.setTimeSlot(timeSlot);
                if(device.equals(CarNum)){
                    Boolean costBoolean = StringDouble(cost);
                    if (costBoolean){
                        int intcost=Integer.parseInt(cost);
                        if (alarmCode.equals(errorCode)){
                            if (FalutType.equals("卡料異常")){
                                SKASAFSystemMessage.setErrorDescribe(FalutType);
                                SKASAFSystemMessage.setMinutecost(Double.parseDouble(cost));
                                SKASAFSystemMessage.setWorkstation(Machine);
                                SKASAFSystemMessageAjaxData.add(SKASAFSystemMessage);
                            }
                            else if (FalutType.equals("CCD異常")){
                                SKASAFSystemMessage.setErrorDescribe(FalutType);
                                SKASAFSystemMessage.setMinutecost(Double.parseDouble(cost));
                                SKASAFSystemMessage.setWorkstation(Machine);
                                SKASAFSystemMessageAjaxData.add(SKASAFSystemMessage);
                            }
                            else if (FalutType.equals("上料卡料")){
                                SKASAFSystemMessage.setErrorDescribe(FalutType);
                                SKASAFSystemMessage.setMinutecost(Double.parseDouble(cost));
                                SKASAFSystemMessage.setWorkstation(Machine);
                                SKASAFSystemMessageAjaxData.add(SKASAFSystemMessage);
                            }
                            else if (FalutType.equals("接料偏位")){
                                SKASAFSystemMessage.setErrorDescribe(FalutType);
                                SKASAFSystemMessage.setMinutecost(Double.parseDouble(cost));
                                SKASAFSystemMessage.setWorkstation(Machine);
                                SKASAFSystemMessageAjaxData.add(SKASAFSystemMessage);
                            }
                            else if (FalutType.equals("上料 卡料")){
                                SKASAFSystemMessage.setErrorDescribe(FalutType);
                                SKASAFSystemMessage.setMinutecost(Double.parseDouble(cost));
                                SKASAFSystemMessage.setWorkstation(Machine);
                                SKASAFSystemMessageAjaxData.add(SKASAFSystemMessage);
                            }
                            else if (FalutType.equals("急停")){
                                SKASAFSystemMessage.setErrorDescribe(FalutType);
                                SKASAFSystemMessage.setMinutecost(Double.parseDouble(cost));
                                SKASAFSystemMessage.setWorkstation(Machine);
                                SKASAFSystemMessageAjaxData.add(SKASAFSystemMessage);
                            }
                            else if (FalutType.equals("阻擋")){
                                SKASAFSystemMessage.setErrorDescribe(FalutType);
                                SKASAFSystemMessage.setMinutecost(Double.parseDouble(cost));
                                SKASAFSystemMessage.setWorkstation(Machine);
                                SKASAFSystemMessageAjaxData.add(SKASAFSystemMessage);
                            }
                            else if (FalutType.equals("下料卡料")){
                                SKASAFSystemMessage.setErrorDescribe(FalutType);
                                SKASAFSystemMessage.setMinutecost(Double.parseDouble(cost));
                                SKASAFSystemMessage.setWorkstation(Machine);
                                SKASAFSystemMessageAjaxData.add(SKASAFSystemMessage);
                            }
                        }
                    }
                }

            }
        }
    }

    public void RGV_AGV_AjaxAddList(String Machine,List<AGV_Exception> SKASAFSystemMessageAjaxData, List<AGV_Exception> agv_exceptions, List<String> AGVWorkstationsList, List<String> AGVERRORList) {
        for (AGV_Exception agv_exception : agv_exceptions) {
            String timeSlot = agv_exception.getTimeSlot();
            String cost = agv_exception.getCost();
            AGV_Exception SKASAFSystemMessage=new AGV_Exception();
            SKASAFSystemMessage.setTimeSlot(timeSlot);
            String agverror = agv_exception.getError();
            for (String  Workstations : AGVWorkstationsList) {
                String workstation = agv_exception.getWorkstation();
                if (workstation.equals(Workstations)){
                    int intcost= Integer.parseInt(cost);
                    Boolean costBoolean = StringDouble(cost);
                    if (costBoolean){
                        for (String ERROR : AGVERRORList)  {
                            if (ERROR.equals("TuoGui")){
                                if (ERROR.equals(agverror)){
                                    SKASAFSystemMessage.setErrorDescribe("脫軌");
                                    SKASAFSystemMessage.setMinutecost(Double.parseDouble(cost));
                                    SKASAFSystemMessage.setWorkstation(Machine);
                                    SKASAFSystemMessageAjaxData.add(SKASAFSystemMessage);
                                }
                            }else if (ERROR.equals("PengZhuang")){
                                if (ERROR.equals(agverror)){
                                    SKASAFSystemMessage.setErrorDescribe("碰撞");
                                    SKASAFSystemMessage.setMinutecost(Double.parseDouble(cost));
                                    SKASAFSystemMessage.setWorkstation(Machine);
                                    SKASAFSystemMessageAjaxData.add(SKASAFSystemMessage);
                                }
                            }else if (ERROR.equals("ZuDang")){
                                if (ERROR.equals(agverror)){
                                    SKASAFSystemMessage.setErrorDescribe("阻擋");
                                    SKASAFSystemMessage.setMinutecost(Double.parseDouble(cost));
                                    SKASAFSystemMessage.setWorkstation(Machine);
                                    SKASAFSystemMessageAjaxData.add(SKASAFSystemMessage);
                                }
                            }else if (ERROR.equals("Jiting")){
                                if (ERROR.equals(agverror)){
                                    SKASAFSystemMessage.setErrorDescribe("急停");
                                    SKASAFSystemMessage.setMinutecost(Double.parseDouble(cost));
                                    SKASAFSystemMessage.setWorkstation(Machine);
                                    SKASAFSystemMessageAjaxData.add(SKASAFSystemMessage);
                                }
                            }else {
                                if (ERROR.equals(agverror)){
                                    SKASAFSystemMessage.setErrorDescribe("超時");
                                    SKASAFSystemMessage.setMinutecost(Double.parseDouble(cost));
                                    SKASAFSystemMessage.setWorkstation(Machine);
                                    SKASAFSystemMessageAjaxData.add(SKASAFSystemMessage);
                                }
                            }
                        }
                    }

                }

            }
        }
    }

    public List<SKAS_AFSData> SKAS_AFSBarData(String FloorName, String StartDate, String EndDate){
        List<SKAS_AFSData>  SKAS_AFSBarData=new ArrayList<>();
        //RGV發料
        SKAS_AFSData RGVskas_afsData=new SKAS_AFSData();
        int RGVStoreIssueTarget=0;
        int RGVStoreIssueAction=0;
        List<AGV_AFS_RGVReCode> RGVStoreIssueQuantityList = agv_afs_rgvReCodeMapper.selectStoreIssueQuantity(FloorName,"", "", StartDate, EndDate);
        Map<String, Integer> RGVstoreIssueQuantityMap = skasafsSystemHomeService.storeIssueQuantity(RGVStoreIssueQuantityList,RGVStoreIssueTarget, RGVStoreIssueAction);
        RGVStoreIssueTarget = RGVstoreIssueQuantityMap.get("StoreIssueTarget");
        RGVStoreIssueAction = RGVstoreIssueQuantityMap.get("StoreIssueAction");
        RGVskas_afsData.setProudct("RGV");
        RGVskas_afsData.setStoreIssueTarget(RGVStoreIssueTarget);
        RGVskas_afsData.setStoreIssueAction(RGVStoreIssueAction);
        SKAS_AFSBarData.add(RGVskas_afsData);
        //AGV發料 //AGV發料Target =Rgv條數  Action=Agvtobuf交換數量
        SKAS_AFSData AGVskas_afsData=new SKAS_AFSData();
        int AGVStoreIssueTarget=0;
        int AGVStoreIssueAction=0;
        List<AGV_AFS_Exchange> AGVStoreIssueQuantityList = agv_afs_exchangeMapper.selectStoreIssue(FloorName, StartDate, EndDate);
        for (AGV_AFS_Exchange agv_afs_exchange : AGVStoreIssueQuantityList) {
            String agvtobuf = agv_afs_exchange.getAgvtobuf();
            Boolean intBoolean = skasafsUtility.ConvertDouble(agvtobuf);
            if (intBoolean){
                AGVStoreIssueAction+= Integer.parseInt(agvtobuf);
            }
        }
        AGVStoreIssueTarget=RGVStoreIssueQuantityList.size();
        AGVskas_afsData.setProudct("AGV");
        AGVskas_afsData.setStoreIssueTarget(AGVStoreIssueTarget);
        AGVskas_afsData.setStoreIssueAction(AGVStoreIssueAction);
        SKAS_AFSBarData.add(AGVskas_afsData);



        //暫存倉備料
        List<String> CarNumList = skasafsSystemHomeService.StoreIssueNumList();
        List<AGV_AFS_Exchange> BUFFERStoreIssueList = agv_afs_exchangeMapper.selectStoreIssue(FloorName, StartDate, EndDate);
        SKAS_AFSData BUFFERskas_afsData=new SKAS_AFSData();
        int BUFFERStoreIssueTarget=0;
        int BUFFERStoreIssueAction=0;
        for (String CarNum : CarNumList) {
            int agvDishNum = 0;
            int bufferDishNum = 0;
            for (AGV_AFS_Exchange agv_afs_exchange : BUFFERStoreIssueList) {
                String linename = agv_afs_exchange.getLinename();
                if (CarNum.equals(linename)) {
                    String agvtobuf = agv_afs_exchange.getAgvtobuf();
                    String buftoafs = agv_afs_exchange.getBuftoafs();
                    if (agvtobuf.equals("1")) {
                        agvDishNum++;
                    }
                    if (buftoafs.equals("1")) {
                        bufferDishNum++;
                    }
                }
            }
            BUFFERStoreIssueTarget += agvDishNum;
            BUFFERStoreIssueAction += bufferDishNum;
        }
        BUFFERskas_afsData.setProudct("暂存仓");
        BUFFERskas_afsData.setStoreIssueTarget(BUFFERStoreIssueTarget);
        BUFFERskas_afsData.setStoreIssueAction(BUFFERStoreIssueAction);
        SKAS_AFSBarData.add(BUFFERskas_afsData);



        //AFS接料量 //AFSTar = RGV 實際數量
        List<AGV_AFS_FeedReCode> AFSStoreIssueList = agv_afs_feedReCodeMapper.selectAFSStoreIssue(FloorName, StartDate, EndDate);
        SKAS_AFSData AFSRskas_afsData=new SKAS_AFSData();
        int AFSStoreIssueAction=0;
        int AFSStoreIssueTarget=0;
        for (String CarNum : CarNumList) {
                int afsDishNum=0;
                for (AGV_AFS_FeedReCode agv_afs_feedReCode : AFSStoreIssueList) {
                    String linename = agv_afs_feedReCode.getLinename();
                    if (CarNum.equals(linename)){afsDishNum++;}
                }
            AFSStoreIssueAction+=afsDishNum;
        }
        AFSStoreIssueTarget=RGVStoreIssueAction;

        AFSRskas_afsData.setProudct("AFS");
        AFSRskas_afsData.setStoreIssueTarget(AFSStoreIssueTarget);
        AFSRskas_afsData.setStoreIssueAction(AFSStoreIssueAction);
        SKAS_AFSBarData.add(AFSRskas_afsData);


        return SKAS_AFSBarData;
    }

    public  Boolean  StringDouble(String  str){
    try {
            Double.parseDouble(str);
            return true;
        } catch (Exception e) {

            return false;
        }
    }


}
