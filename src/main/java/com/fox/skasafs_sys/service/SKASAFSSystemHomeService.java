package com.fox.skasafs_sys.service;


import com.fox.skasafs_sys.entity.*;
import com.fox.skasafs_sys.mapper.*;
import com.fox.skasafs_sys.utility.SKASAFSUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author
 * @create 2020-06-11 10:24
 */


@Service
public class SKASAFSSystemHomeService {

    @Autowired
    AGV_AFS_ExchangeMapper agv_afs_exchangeMapper;

    @Autowired
    AGV_ExceptionMapper agv_exceptionMapper;

    @Autowired
    AGV_AFS_RGVReCodeMapper agv_afs_rgvReCodeMapper;

    @Autowired
    AGV_AFS_StateMapper agv_afs_stateMapper;

    @Autowired
    AGV_AFS_FeedReCodeMapper agv_afs_feedReCodeMapper;

    @Autowired
    SKASAFSUtility skasafsUtility;

    @Autowired
    AutoFloor_SKAS_AFS_ALARMCODEMapper  autoFloor_skas_afs_alarmcodeMapper;
    @Autowired
    SKASAFSSystemMessageService skasafsSystemMessageService;
    @Autowired
    SKASAFSSystemMessageAjaxService skasafsSystemMessageAjaxService;

    private  static  final  Integer DishNum=25;

    private  static  final  String Proudct="D54" ;

    public SKAS_AFSData RGV_AGVSystemData(String Quality, String FloorName, String StartDate, String EndDate, Double WorkingHours, Integer actionMinuteD){
        int actionSecond =actionMinuteD*60;
        if (actionMinuteD>=720){
            Double TargetSecond=(720*60*60.0);
            actionSecond=TargetSecond.intValue();
        }
        if (WorkingHours>0){
            actionSecond=WorkingHours.intValue();
        }

        SKAS_AFSData skas_afsData=new SKAS_AFSData();
        int StoreIssueTarget=0;
        int StoreIssueAction=0;
        double AchievingRateTarget=0;
        double AchievingRateAction=0;
        double MachineRateTarget=0;
        double MachineRateAction=0;
        List<AGV_Exception> agv_exceptions = agv_exceptionMapper.selectMachineFaultTime(FloorName,StartDate,EndDate);
        if ("RGV".equals(Quality)){
            List<AGV_AFS_RGVReCode> RGVStoreIssueQuantityList = agv_afs_rgvReCodeMapper.selectStoreIssueQuantity(FloorName,"", "", StartDate, EndDate);
            Map<String, Integer> storeIssueQuantityMap = storeIssueQuantity(RGVStoreIssueQuantityList, StoreIssueTarget, StoreIssueAction);
            StoreIssueTarget = storeIssueQuantityMap.get("StoreIssueTarget");
            StoreIssueAction = storeIssueQuantityMap.get("StoreIssueAction");
        }else if ("AGV".equals(Quality)){
            List<AGV_AFS_RGVReCode> RGVStoreIssueQuantityList = agv_afs_rgvReCodeMapper.selectStoreIssueQuantity(FloorName,"", "", StartDate, EndDate);
            StoreIssueTarget = RGVStoreIssueQuantityList.size();
            List<AGV_AFS_Exchange> AGVStoreIssueQuantityList = agv_afs_exchangeMapper.selectStoreIssue(FloorName, StartDate, EndDate);
            for (AGV_AFS_Exchange agv_afs_exchange : AGVStoreIssueQuantityList) {
                String agvtobuf = agv_afs_exchange.getAgvtobuf();
                Boolean intBoolean = skasafsUtility.ConvertDouble(agvtobuf);
                if (intBoolean){
                    StoreIssueAction+= Integer.parseInt(agvtobuf);
                }
            }
            List<String> WorkstationsList =AGVWorkstationsList();
            MachineRateAction=agv_afsMachineRate(Quality,WorkstationsList,agv_exceptions,actionSecond,MachineRateAction);
        }


        if (StoreIssueTarget!=0){AchievingRateAction=StoreIssueAction/(double)StoreIssueTarget;}
        BigDecimal AchievingRateActionBig=new BigDecimal(AchievingRateAction*100);
        AchievingRateAction=AchievingRateActionBig.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
        skas_afsData.setProudct(Proudct);
        skas_afsData.setLineName(Quality);
        skas_afsData.setStoreIssueTarget(StoreIssueTarget);
        skas_afsData.setStoreIssueAction(StoreIssueAction);
        skas_afsData.setAchievingRateTarget(AchievingRateTarget);
        skas_afsData.setAchievingRateAction(AchievingRateAction);
        skas_afsData.setMachineRateTarget(MachineRateTarget);
        skas_afsData.setMachineRateAction(MachineRateAction);
        return  skas_afsData;
    }

    public   List<SKAS_AFSData>  BUFFER_AFCSystemData(String Quality,String FloorName,String StartDate,String EndDate,Double WorkingHours,Integer actionMinuteD){
        int actionSecond =actionMinuteD*60;
        if (actionMinuteD>=720){
            Double TargetSecond=(720*60*60.0);
            actionSecond=TargetSecond.intValue();
        }
        if (WorkingHours>0){
            actionSecond=WorkingHours.intValue();
        }
        List<SKAS_AFSData> Buffer_AFCDataList=new ArrayList<>();
        List<String> CarNumList=new ArrayList<>();
        CarNumList = StoreIssueNumList();
        List<String> WorkstationsList =new ArrayList<>();;
        List<AGV_AFS_Exchange> BUFFERStoreIssueList = agv_afs_exchangeMapper.selectStoreIssue(FloorName, StartDate, EndDate);
        List<AGV_AFS_FeedReCode> AFSStoreIssueList = agv_afs_feedReCodeMapper.selectAFSStoreIssue(FloorName, StartDate, EndDate);
        List<AGV_Exception> agv_exceptions = agv_exceptionMapper.selectMachineFaultTime(FloorName,StartDate,EndDate);
        List<AGV_AFS_RGVReCode> RGVStoreIssueQuantityList = agv_afs_rgvReCodeMapper.selectStoreIssueQuantity(FloorName,"", "", StartDate, EndDate);
            for (String CarNum : CarNumList) {
                int StoreIssueTarget=0;
                int StoreIssueAction=0;
                double AchievingRateTarget=0;
                double AchievingRateAction=0;
                double MachineRateTarget=0;
                double MachineRateAction=0;
                SKAS_AFSData skas_afsData=new SKAS_AFSData();
                if ("BUFFER".equals(Quality)){
                    WorkstationsList =BufferNumList();
                    int agvDishNum=0;
                    int bufferDishNum=0;
                    for (AGV_AFS_Exchange agv_afs_exchange : BUFFERStoreIssueList) {
                        String linename = agv_afs_exchange.getLinename();
                        if (CarNum.equals(linename)){
                            String agvtobuf = agv_afs_exchange.getAgvtobuf();
                            String buftoafs = agv_afs_exchange.getBuftoafs();
                            if (agvtobuf.equals("1")){agvDishNum++; }
                            if (buftoafs.equals("1")){bufferDishNum++;}
                        }
                    }
                    for (String Workstations : WorkstationsList) {
                        String Workstationssub = Workstations.substring(1);
                        String CarNumsub = CarNum.substring(1);
                        if (Workstationssub.equals(CarNumsub)){
                            MachineRateAction = BUFFER_AFSMachineRate(Quality,Workstations, agv_exceptions, actionSecond, MachineRateAction);
                        }
                    }
                    StoreIssueTarget=agvDishNum;
                    StoreIssueAction=bufferDishNum;
                    CarNum="B"+CarNum.substring(1);

                }else if ("AFS".equals(Quality)){
                    WorkstationsList =AFSNumList();
                    int AFSTarget=0;
                    int afsDishNum=0;
                    for (AGV_AFS_RGVReCode agv_afs_rgvReCode : RGVStoreIssueQuantityList) {
                        String linename = agv_afs_rgvReCode.getLinename();
                        if (CarNum.equals(linename)){
                            String actual = agv_afs_rgvReCode.getActual();
                            Boolean aBoolean = skasafsSystemMessageAjaxService.StringDouble(actual);
                            if (aBoolean){
                                AFSTarget+=Integer.parseInt(actual);
                            }
                        }
                    }
                    for (AGV_AFS_FeedReCode agv_afs_feedReCode : AFSStoreIssueList) {
                        String linename = agv_afs_feedReCode.getLinename();
                        if (CarNum.equals(linename)){afsDishNum++;}
                    }
                    for (String Workstations : WorkstationsList) {
                        String Workstationssub = Workstations.substring(1);
                        String CarNumsub = CarNum.substring(1);
                        if (Workstationssub.equals(CarNumsub)){
                            MachineRateAction = BUFFER_AFSMachineRate(Quality,Workstations, agv_exceptions, actionSecond, MachineRateAction);
                        }
                    }
                    StoreIssueTarget=AFSTarget;
                    StoreIssueAction=afsDishNum;
                    CarNum="S"+CarNum.substring(1);
                }
                if (StoreIssueTarget!=0){AchievingRateAction=(double)StoreIssueAction/StoreIssueTarget;}
                BigDecimal AchievingRateActionBig=new BigDecimal(AchievingRateAction*100);
                AchievingRateAction=AchievingRateActionBig.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
                skas_afsData.setProudct(Proudct);
                skas_afsData.setLineName(CarNum);
                skas_afsData.setStoreIssueTarget(StoreIssueTarget);
                skas_afsData.setStoreIssueAction(StoreIssueAction);
                skas_afsData.setAchievingRateTarget(AchievingRateTarget);
                skas_afsData.setAchievingRateAction(AchievingRateAction);
                skas_afsData.setMachineRateTarget(MachineRateTarget);
                skas_afsData.setMachineRateAction(MachineRateAction);
                Buffer_AFCDataList.add(skas_afsData);
            }

        return  Buffer_AFCDataList;
    }

    public Map<String,Integer> storeIssueQuantity(List<AGV_AFS_RGVReCode> StoreIssueQuantityList,Integer StoreIssueTarget,Integer StoreIssueAction){
        Map<String,Integer> storeIssueQuantityMap=new HashMap<>();

        for (AGV_AFS_RGVReCode agv_afs_rgvReCode : StoreIssueQuantityList) {
            String target = agv_afs_rgvReCode.getTarget();
            Boolean targetBoolean = skasafsUtility.ConvertDouble(target);
            if (targetBoolean){StoreIssueTarget+=Integer.parseInt(target);}
            String actual = agv_afs_rgvReCode.getActual();
            Boolean actualBoolean = skasafsUtility.ConvertDouble(actual);
            if (actualBoolean){StoreIssueAction+=Integer.parseInt(actual);}
        }
        storeIssueQuantityMap.put("StoreIssueTarget",StoreIssueTarget);
        storeIssueQuantityMap.put("StoreIssueAction",StoreIssueAction);

        return storeIssueQuantityMap;
    }

    public Double  agv_afsMachineRate(String Quality,List<String> WorkstationsList, List<AGV_Exception> agv_exceptions,Integer actionSecond,Double MachineRateAction){
        int MachineFaultTime=0;
        List<String> ERRORList = skasafsSystemMessageService.ERRORList(Quality);
        MachineFaultTime = getAGV_RGVMachineFaultTime(WorkstationsList, agv_exceptions, MachineFaultTime, ERRORList);
        if (actionSecond!=0){MachineRateAction=MachineFaultTime/(double)actionSecond;}
        BigDecimal MachineFaultRateBig=new BigDecimal(MachineRateAction*100);
        MachineRateAction=MachineFaultRateBig.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
        return MachineRateAction;

    }

    public int getAGV_RGVMachineFaultTime(List<String> WorkstationsList, List<AGV_Exception> agv_exceptions, int machineFaultTime, List<String> ERRORList) {
        for (AGV_Exception agv_exception : agv_exceptions) {
            String agverror = agv_exception.getError();
            for (String  Workstations : WorkstationsList) {
                String workstation = agv_exception.getWorkstation();
                if (workstation!=null&&workstation.equals(Workstations)){
                        String cost = agv_exception.getCost();
                    Boolean costBoolean = skasafsSystemMessageAjaxService.StringDouble(cost);
                    int intcost= Integer.parseInt(cost);
                    if (costBoolean){
                        for (String ERROR : ERRORList)  {
                            if (ERROR.equals("TuoGui")){
                                if (ERROR.equals(agverror)){
                                    machineFaultTime +=intcost;
                                }
                            }else if (ERROR.equals("PengZhuang")){
                                if (ERROR.equals(agverror)){
                                    machineFaultTime +=intcost;
                                }
                            }else if (ERROR.equals("ZuDang")){
                                if (ERROR.equals(agverror)){
                                    machineFaultTime +=intcost;
                                }
                            }else if (ERROR.equals("Jiting")){
                                if (ERROR.equals(agverror)){
                                    machineFaultTime +=intcost;
                                }
                            }else {
                                if (ERROR.equals(agverror)){
                                    machineFaultTime +=intcost;
                                }
                            }
                        }
                    }
                }

            }
        }
        return machineFaultTime;
    }

    public Double  BUFFER_AFSMachineRate(String Quality,String Device, List<AGV_Exception> agv_exceptions,Integer actionSecond,Double MachineRateAction){
        int MachineFaultTime=0;
        List<AutoFloor_SKAS_AFS_ALARMCODE> autoFloor_skas_afs_alarmcodes ;
        String DeviceCarNum="";
        if (Quality.equals("BUFFER")){
            autoFloor_skas_afs_alarmcodes = autoFloor_skas_afs_alarmcodeMapper.selectAlarmCodeList("B001");
        }else {
            autoFloor_skas_afs_alarmcodes = autoFloor_skas_afs_alarmcodeMapper.selectAlarmCodeList("S001");
        }


        MachineFaultTime = getBuf_AfsMachineFaultTime(Device, agv_exceptions, MachineFaultTime, autoFloor_skas_afs_alarmcodes);
        if (actionSecond!=0){MachineRateAction=MachineFaultTime/(double)actionSecond;}
        BigDecimal MachineFaultRateBig=new BigDecimal(MachineRateAction*100);
        MachineRateAction=MachineFaultRateBig.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
        return MachineRateAction;

    }

    public int getBuf_AfsMachineFaultTime(String Device, List<AGV_Exception> agv_exceptions, int machineFaultTime, List<AutoFloor_SKAS_AFS_ALARMCODE> autoFloor_skas_afs_alarmcodes) {
        for (AutoFloor_SKAS_AFS_ALARMCODE autoFloor_skas_afs_alarmcode : autoFloor_skas_afs_alarmcodes) {
            String FalutType = autoFloor_skas_afs_alarmcode.getFalutType();
            String alarmCode = autoFloor_skas_afs_alarmcode.getAlarmCode();
            for (AGV_Exception rgv_exception : agv_exceptions) {
                String cost = rgv_exception.getCost();
                String device = rgv_exception.getDevice();
                String errorCode = rgv_exception.getError();
                if(device.equals(Device)){
                        Boolean costBoolean = skasafsSystemMessageAjaxService.StringDouble(cost);
                        if (costBoolean){
                            int intcost=Integer.parseInt(cost);
                            if (alarmCode.equals(errorCode)){
                                if (FalutType.equals("卡料異常")){
                                    machineFaultTime += intcost;
                                }
                                else if (FalutType.equals("CCD異常")){
                                    machineFaultTime += intcost;
                                }
                                else if (FalutType.equals("上料卡料")){
                                    machineFaultTime += intcost;
                                }
                                else if (FalutType.equals("接料偏位")){
                                    machineFaultTime += intcost;
                                }
                                else if (FalutType.equals("上料 卡料")){
                                    machineFaultTime += intcost;
                                }
                                else if (FalutType.equals("急停")){
                                    machineFaultTime += intcost;
                                }
                                else if (FalutType.equals("阻擋")){
                                    machineFaultTime += intcost;
                                }
                                else if (FalutType.equals("下料卡料")){
                                    machineFaultTime += intcost;
                                }
                            }
                        }
                }

            }
        }
        return machineFaultTime;
    }

    public  List<String>   AGVWorkstationsList(){
        List<String> AGVWorkstationsList=new ArrayList<>();
        AGVWorkstationsList.add("KT-AFS");
        return AGVWorkstationsList;
    }

    public  List<String>   RGVWorkstationsList(){
        List<String> RGVWorkstationsList=new ArrayList<>();
        RGVWorkstationsList.add("");
        RGVWorkstationsList.add("");
        RGVWorkstationsList.add("");

        return RGVWorkstationsList;
    }

    public List<String> BufferNumList(){
        List<String> BufferNumList=new ArrayList<>();
        for (int i = 1; i <=8; i++) {
            BufferNumList.add("B00"+i);
        }
        return BufferNumList;
    }

    public List<String> AFSNumList (){
        List<String> AFSNumList=new ArrayList<>();
        for (int i = 1; i <=8; i++) {
            AFSNumList.add("S00"+i);
        }
        return AFSNumList;
    }

    public List<String> StoreIssueNumList(){

        List<String> StoreIssueNumList=new ArrayList<>();
        for (int i = 1; i <=8; i++) {
            StoreIssueNumList.add("C00"+i);
        }
        return StoreIssueNumList;
    }


}
