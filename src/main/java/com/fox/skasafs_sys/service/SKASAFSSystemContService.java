package com.fox.skasafs_sys.service;


import com.fox.skasafs_sys.entity.*;
import com.fox.skasafs_sys.mapper.*;
import com.fox.skasafs_sys.utility.SKASAFSUtility;
import com.fox.testsys.utility.TaskService;
import com.fox.testsys.utility.TimeUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author
 * @create 2020-08-04 18:05
 */
@Service
public class SKASAFSSystemContService {

    @Autowired
    AGV_AFS_StateMapper agv_afs_stateMapper;

    @Autowired
    TimeUtility timeUtility;

    DateFormat SKASAFSformat = new SimpleDateFormat("yyyy-MM-dd");

    DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @Autowired
    AGV_ExceptionMapper agv_exceptionMapper;

    @Autowired
    AutoFloor_SKAS_AFS_ALARMCODEMapper autoFloor_skas_afs_alarmcodeMapper;

    @Autowired
    SKASAFSSystemHomeService skasafsSystemHomeService;

    @Autowired
    SKASAFSSystemMessageAjaxService skasafsSystemMessageAjaxService;

    @Autowired
    SKASAFSSystemMessageService skasafsSystemMessageService;

    @Autowired
    AGV_AFS_RGVReCodeMapper agv_afs_rgvReCodeMapper;

    @Autowired
    AGV_AFS_ExchangeMapper agv_afs_exchangeMapper;

    @Autowired
    AGV_AFS_FeedReCodeMapper agv_afs_feedReCodeMapper;

    @Autowired
    TaskService taskService;

    @Autowired
    SKASAFSUtility skasafsUtility;

    @Autowired
    AGV_StateMapper agv_stateMapper;

    private static  final Integer MachineSum=18;

    DateFormat Skasformat = new SimpleDateFormat("yyyy/MM/dd");

    public AGV_AFS_State SKASAFSMachineStateData(Map<String, Integer> SysStateMapData){


        AGV_AFS_State agv_afs_StateData=new AGV_AFS_State();

        int MachineHealth=0;  //健康
        int MachineWarning=0;  //预警
        int MachineUnusual=0;  //维修
        int MachineLostConnection=0; //掉线

        Integer RGVTyep = SysStateMapData.get("RGV");
        switch (RGVTyep){
            case 0:
                MachineLostConnection++;
                break;
            case 1:
                MachineHealth++;
                break;
            case 2:
                //MachineUnusual++;
                MachineHealth++;
                break;
            case 3:
                //ps:->强制修改邏輯
                //MachineWarning++;
                MachineHealth++;
                break;
        }
        Integer AGVTyep = SysStateMapData.get("AGV");
        switch (AGVTyep){
            case 0:
                MachineLostConnection++;
                break;
            case 1:
                MachineHealth++;
                break;
            case 2:
                //MachineUnusual++;
                MachineHealth++;
                break;
            case 3:
                //ps:->强制修改邏輯
                //MachineWarning++;
                MachineHealth++;
                break;
        }
        for (int i = 1; i <=8; i++) {
            Integer BufType = SysStateMapData.get("B00" + i);

            Integer AfsType = SysStateMapData.get("S00" + i);
            if (BufType!=null){
                switch (BufType){
                    case 0:
                        MachineLostConnection++;
                        break;
                    case 1:
                        MachineHealth++;
                        break;
                    case 2:
                        //MachineUnusual++;
                        MachineHealth++;
                        break;
                    case 3:
                        //ps:->强制修改邏輯
                        //MachineWarning++;
                        MachineHealth++;
                        break;
                }
            }
            if (AfsType!=null){
                switch (AfsType){
                    case 0:
                        MachineLostConnection++;
                        break;
                    case 1:
                        MachineHealth++;
                        break;
                    case 2:
                        //MachineUnusual++;
                        MachineHealth++;
                        break;
                    case 3:
                        //ps:->强制修改邏輯
                        //MachineWarning++;
                        MachineHealth++;
                        break;
                }
            }

        }

        MachineLostConnection=MachineSum-(MachineHealth+MachineWarning+MachineUnusual);
        agv_afs_StateData.setMachineSum(MachineSum);
        agv_afs_StateData.setMachineHealth(MachineHealth);
        agv_afs_StateData.setMachineWarning(MachineWarning);
        agv_afs_StateData.setMachineUnusual(MachineUnusual);
        agv_afs_StateData.setMachineLostConnection(MachineLostConnection);
        double MachineRate=0;
        if (MachineHealth+MachineWarning+MachineUnusual!=0){
            MachineRate= (MachineHealth+MachineWarning)/(double)(MachineHealth+MachineWarning+MachineUnusual);
            BigDecimal MachineRateBig=new BigDecimal(MachineRate*100);
            MachineRate=MachineRateBig.setScale(0,BigDecimal.ROUND_HALF_UP).doubleValue();
        }
        agv_afs_StateData.setMachineRate(MachineRate);
        return agv_afs_StateData;
    }

    public List<AFSLineData> SKASAFSMachineRate(String FloorName, Date schedules, Date TomorrowDates){
        List<String> TIME_SLOT =new ArrayList<>();
        String DayType="";
        if (timeUtility.Timequantum("08:30", "20:30")) {
            DayType="Day";
            TIME_SLOT =DayTIME_SLOT();
        }else {
            DayType="Night";
            TIME_SLOT =NightTIME_SLOT();
        }

        List<AFSLineData>  afsLineDataList=new ArrayList<>();
        List<AFSLineData> SKASAFSTimeSlot = SKASAFSTimeSlot(DayType);

        /*获取当前时间*/
        List<java.sql.Date> sqlList = taskService.Schedule();
        /*当前日期*/
        Date schedule = sqlList.get(0);
        /*当前日期减1*/
        Date YesterdayDate = sqlList.get(1);
        /*当前日期加1*/
        Date TomorrowDate = sqlList.get(2);
        String SkasTime = Skasformat.format(schedule);
        String SkasTomorrowDate = Skasformat.format(TomorrowDate);
        String SkasYesterdayDate = Skasformat.format(YesterdayDate);
        for (int i = 0; i < SKASAFSTimeSlot.size(); i++) {
            AFSLineData afsLineData=new AFSLineData();
            AFSLineData lineData=SKASAFSTimeSlot.get(i);
            String SkasStartDate = lineData.getSkASAFSStartDate();
            String SkasEndDate = lineData.getSkASAFSEndDate();
            String Time = TIME_SLOT.get(i);
            System.out.println(Time);
            String SKASSTARTTIME="";
            String SKASENDTIME="";
            if (timeUtility.Timequantum("20:30", "23:59")) {
                switch (Time){
                    case "20:30":
                    case "22:30":
                    case "21:30":
                        SKASSTARTTIME=SkasTime+" "+SkasStartDate;
                        SKASENDTIME=SkasTime+" "+SkasEndDate;
                        break;
                    case "23:30":
                        SKASSTARTTIME=SkasTime+" "+SkasStartDate;
                        SKASENDTIME=SkasTomorrowDate+" "+SkasEndDate;
                        break;
                    case "00:30":
                    case "01:30":
                    case "02:30":
                    case "03:30":
                    case "04:30":
                    case "05:30":
                    case "06:30":
                    case "07:30":
                    case "08:30":
                        SKASSTARTTIME=SkasTomorrowDate+" "+SkasStartDate;
                        SKASENDTIME=SkasTomorrowDate+" "+SkasEndDate;
                        break;
                } }
            else if (timeUtility.Timequantum("00:00", "08:30")) {
                switch (Time){
                    case "20:30":
                    case "22:30":
                    case "21:30":
                        SKASSTARTTIME=SkasYesterdayDate+" "+SkasStartDate;
                        SKASENDTIME=SkasYesterdayDate+" "+SkasEndDate;
                        break;
                    case "23:30":
                        SKASSTARTTIME=SkasYesterdayDate+" "+SkasStartDate;
                        SKASENDTIME=SkasTime+" "+SkasEndDate;
                        break;
                    case "00:30":
                    case "01:30":
                    case "02:30":
                    case "03:30":
                    case "04:30":
                    case "05:30":
                    case "06:30":
                    case "07:30":
                    case "08:30":
                        SKASSTARTTIME=SkasTime+" "+SkasStartDate;
                        SKASENDTIME=SkasTime+" "+SkasEndDate;
                        break;
                }
            }
            else {
               SKASSTARTTIME=SkasTime+" "+SkasStartDate;
               SKASENDTIME=SkasTime+" "+SkasEndDate; }

            List<AGV_Exception> agv_exceptions = agv_exceptionMapper.selectMachineFaultTime(FloorName, SKASSTARTTIME, SKASENDTIME);

            Double MachineRate;
            int MachineCost=0;
            double MachineHTime=(60*60)*2;
            //RGV機故時間
            List<String> RGVWorkstationsList =new ArrayList<>();
            List<String> RGVERRORList = new ArrayList<>();
            int RGVMachineCost=skasafsSystemHomeService.getAGV_RGVMachineFaultTime(RGVWorkstationsList,agv_exceptions,MachineCost,RGVERRORList);
            //AGV機故時間
            // AGV機故固定分類
            List<String> AGVWorkstationsList =skasafsSystemHomeService.AGVWorkstationsList();
            List<String> AGVERRORList = skasafsSystemMessageService.ERRORList("AGV");
            int AGVMachineCost=skasafsSystemHomeService.getAGV_RGVMachineFaultTime(AGVWorkstationsList,agv_exceptions,MachineCost,AGVERRORList);
            List<String> CarNumList = skasafsSystemHomeService.StoreIssueNumList();

            List<AutoFloor_SKAS_AFS_ALARMCODE> BUFFERalarmcodes = autoFloor_skas_afs_alarmcodeMapper.selectAlarmCodeList("B001");

            List<AutoFloor_SKAS_AFS_ALARMCODE> AFSAlarmcodes = autoFloor_skas_afs_alarmcodeMapper.selectAlarmCodeList("S001");

            List<String> BufferNumCar = skasafsSystemHomeService.BufferNumList();
            int BUFMachineCost=0;
            for (String Car : BufferNumCar) {
                //BUF 機故時間
                BUFMachineCost+=skasafsSystemHomeService.getBuf_AfsMachineFaultTime(Car, agv_exceptions, MachineCost, BUFFERalarmcodes);
            }
            List<String> AFSNumCar = skasafsSystemHomeService.AFSNumList();
            int AFSMachineCost=0;
            for (String Car : AFSNumCar) {
                //AFS 機故時間
                AFSMachineCost+=skasafsSystemHomeService.getBuf_AfsMachineFaultTime(Car, agv_exceptions, MachineCost, AFSAlarmcodes);
            }
            MachineCost=RGVMachineCost+AGVMachineCost+BUFMachineCost+AFSMachineCost;
            MachineRate=MachineCost/MachineHTime;
            BigDecimal MachineRateBig=new BigDecimal(MachineRate*100);
            MachineRate=MachineRateBig.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
            afsLineData.setDateTime(Time);
           Boolean timequantum08 = timeUtility.Timequantum("08:30", "20:30");
            if (timequantum08){
                System.out.println("08:30:00"+"-------"+ SkasStartDate);
                Boolean timequantum = timeUtility.presentTimequantum("08:30:00", SkasStartDate);
                System.out.println(timequantum);
            }else {
                Boolean timequantum = timeUtility.presentTimequantum("20:30:00", SkasEndDate);

            }
            if (MachineRate<=0){MachineRate=null;}
            afsLineData.setMachineRate(MachineRate);
            afsLineDataList.add(afsLineData);
        }
        return afsLineDataList;
    }

    public  List<AGV_Exception>   SKASAFSexceptionDescribe(String FloorName, String StartDate,String EndDate){
        List<AGV_Exception>  SKASAFSexceptionList=new ArrayList<>();
        List<AutoFloor_SKAS_AFS_ALARMCODE> AlarmCodeList = autoFloor_skas_afs_alarmcodeMapper.selectAlarmCodeList("");
        List<AGV_Exception> agv_exceptions = agv_exceptionMapper.selectMachineMessage(FloorName, StartDate, EndDate);
        for (int i = 0; i < agv_exceptions.size(); i++) {
            int size = agv_exceptions.size();
            AGV_Exception agv_exception = new AGV_Exception();
            AGV_Exception Sexception=new AGV_Exception();
            Sexception.setDevice("无数据");
            /*if (size==1){
                if (i<1){
                    agv_exception = agv_exceptions.get(0);
                }
            }else if (size==2){
                if (i<2){
                    agv_exception = agv_exceptions.get(1);
                }
            }else if (size>=3){
            }*/
            agv_exception = agv_exceptions.get(i);
            String device = agv_exception.getDevice();
            String workstation = agv_exception.getWorkstation();
            String error = agv_exception.getError();
            Date starttime = agv_exception.getStarttime();
            String cost = agv_exception.getCost();
            String DateTime ="";
            for (AutoFloor_SKAS_AFS_ALARMCODE autoFloor_skas_afs_alarmcode : AlarmCodeList) {
                String alarmCode = autoFloor_skas_afs_alarmcode.getAlarmCode();
                String falutType = autoFloor_skas_afs_alarmcode.getFalutType();
                if (error!=null&&error.equals(alarmCode)){
                    error=falutType;
                }
            }
            if (starttime!=null){
                DateTime = this.format.format(starttime);
            }
            Sexception.setDevice(device);
            Sexception.setWorkstation(workstation);
            Sexception.setError(error);
            Sexception.setDateTime(DateTime);
            Sexception.setCost(cost);
            SKASAFSexceptionList.add(Sexception);
        }
        return SKASAFSexceptionList;
    }

    private List<String> DayTIME_SLOT() {
        List<String> DayTime_Slot = new ArrayList<>();
        DayTime_Slot.add("08:30");
        DayTime_Slot.add("10:30");
        DayTime_Slot.add("12:30");
        DayTime_Slot.add("14:30");
        DayTime_Slot.add("16:30");
        DayTime_Slot.add("18:30");
        DayTime_Slot.add("20:30");
        return DayTime_Slot;
    }

    private List<String> NightTIME_SLOT() {
        List<String> NightTime_Slot = new ArrayList<>();
        NightTime_Slot.add("20:30");
        NightTime_Slot.add("22:30");
        NightTime_Slot.add("00:30");
        NightTime_Slot.add("02:30");
        NightTime_Slot.add("04:30");
        NightTime_Slot.add("06:30");
        NightTime_Slot.add("08:30");
        return NightTime_Slot;
    }

    private List<AFSLineData>  SKASAFSTimeSlot(String DayType){
        List<AFSLineData> TIME_SLOTList=new ArrayList<>();
        if (DayType.equals("Day")){
            for (int i = 0; i < 13; i+=2) {
                int DayH=8;
                AFSLineData afsLineDatatimeSlot=new AFSLineData();
                if (DayH+i<=9){
                    afsLineDatatimeSlot.setSkASAFSStartDate("0"+(DayH+i)+":30:00");
                    afsLineDatatimeSlot.setSkASAFSEndDate((DayH+2+i)+":30:00");
                    if (DayH+i==9){
                        afsLineDatatimeSlot.setSkASAFSEndDate((DayH+2+i)+":30:00");
                    }
                }else {
                    afsLineDatatimeSlot.setSkASAFSStartDate((DayH+i)+":30:00");
                    afsLineDatatimeSlot.setSkASAFSEndDate((DayH+2+i)+":30:00");
                }

                TIME_SLOTList.add(afsLineDatatimeSlot);
            }
        }else {
            for (int i = 0; i < 13; i+=2) {
                int NightH=20;
                AFSLineData afsLineDatatimeSlot=new AFSLineData();
                if (NightH+i<=23){
                    afsLineDatatimeSlot.setSkASAFSStartDate((NightH+i)+":30:00");
                    if (NightH+2+i==24){
                        afsLineDatatimeSlot.setSkASAFSEndDate("00:30:00");
                    }else {
                        afsLineDatatimeSlot.setSkASAFSEndDate((NightH+2+i)+":30:00");
                    }
                }else {
                    afsLineDatatimeSlot.setSkASAFSStartDate("0"+(i-4)+":30:00");
                    afsLineDatatimeSlot.setSkASAFSEndDate("0"+(2+i-4)+":30:00");
                }
                TIME_SLOTList.add(afsLineDatatimeSlot);
            }
        }


        return TIME_SLOTList;
    }

    public Map<String,Integer>   SAKSAFSSysStateMapData(String FloorName,String StartDate,String EndDate,Integer actionMinuteD){
        HashMap<String,Integer> StateMapData=new HashMap<>();
        List<AGV_AFS_State> agv_afs_states = agv_afs_stateMapper.selectCarState(FloorName, StartDate, EndDate);
        AGV_State agv_state = agv_stateMapper.selectAGVState(StartDate, EndDate);
        int actionSecond =actionMinuteD*60;
        if (actionMinuteD>=720){
            Double TargetSecond=(720*60*60.0);
            actionSecond=TargetSecond.intValue();
        }
        int AGVState=0;
        int RGVState=0;
        //BUF AFS
        List<AGV_AFS_RGVReCode> RGVStoreIssueQuantityList = agv_afs_rgvReCodeMapper.selectStoreIssueQuantity(FloorName,"", "", StartDate, EndDate);
        List<AGV_AFS_FeedReCode> AFSStoreIssueList = agv_afs_feedReCodeMapper.selectAFSStoreIssue(FloorName, StartDate, EndDate);
        List<AGV_Exception> agv_exceptions = agv_exceptionMapper.selectMachineFaultTime(FloorName,StartDate,EndDate);
        List<AGV_AFS_Exchange> BUFFERStoreIssueList = agv_afs_exchangeMapper.selectStoreIssue(FloorName, StartDate, EndDate);

        for (int i = 1; i <=8; i++) {
            int BufCarState=0;
            int AfsCarState=0;
            String BufCarName="B00"+i;
            String AfsCarName="S00"+i;
            String CarNum="C00"+i;
            //BUF
            for (AGV_AFS_State agv_afs_state : agv_afs_states) {
                String devicename = agv_afs_state.getDevicename();
                String state = agv_afs_state.getState();
                if (BufCarName.equals(devicename)){
                    if (state.equals("1")){
                        BufCarState=1;
                    }else if (state.equals("2")){
                        BufCarState=2;
                    }else if (state.equals("3")){
                        BufCarState=1;
                    }
                }
                // 产能 机故 不达标 判断预警
                if (BufCarState==1){
                    List<String> WorkstationsList = skasafsSystemHomeService.BufferNumList();
                    int agvDishNum=0;
                    int bufferDishNum=0;
                    double MachineRateAction=0;
                    int StoreIssueTarget=0;
                    int StoreIssueAction=0;

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
                            MachineRateAction = skasafsSystemHomeService.BUFFER_AFSMachineRate("BUFFER",Workstations, agv_exceptions, actionSecond, MachineRateAction);
                        }
                    }
                    if (MachineRateAction>5){
                        //ps:->强制修改邏輯
                        //BufCarState=3;
                        BufCarState=1;
                    }
                    StoreIssueTarget=agvDishNum;
                    StoreIssueAction=bufferDishNum;
                    double AchievingRateAction=0;
                    if (StoreIssueTarget!=0){AchievingRateAction=(double)StoreIssueAction/StoreIssueTarget;}
                    BigDecimal AchievingRateActionBig=new BigDecimal(AchievingRateAction*100);
                    AchievingRateAction=AchievingRateActionBig.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
                    if (StoreIssueTarget>StoreIssueAction){
                        //ps:->强制修改邏輯
                        //BufCarState=3;
                        BufCarState=1;
                    }

                }
                //ps:->强行修改
                if (BufCarState>1){BufCarState=1;}
                StateMapData.put(BufCarName,BufCarState);


                ///AFS
                if (AfsCarName.equals(devicename)){
                    if (state.equals("1")){
                        AfsCarState=1;
                    }else if (state.equals("2")){
                        AfsCarState=2;
                    }else if (state.equals("3")){
                        AfsCarState=1;
                    }
                }
                // 产能 机故 不达标 判断预警
                if (AfsCarState==1){
                    List<String> WorkstationsList =skasafsSystemHomeService.AFSNumList();
                    int AFSTarget=0;
                    int afsDishNum=0;
                    double MachineRateAction=0;
                    int StoreIssueTarget=0;
                    int StoreIssueAction=0;

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
                            MachineRateAction = skasafsSystemHomeService.BUFFER_AFSMachineRate("AFS",Workstations, agv_exceptions, actionSecond, MachineRateAction);
                        }
                    }

                    if (MachineRateAction>5){
                        //ps:->强制修改邏輯
                        //AfsCarState=3;
                        AfsCarState=1;
                    }
                    StoreIssueTarget=AFSTarget;
                    StoreIssueAction=afsDishNum;
                    double AchievingRateAction=0;
                    if (StoreIssueTarget!=0){AchievingRateAction=(double)StoreIssueAction/StoreIssueTarget;}
                    BigDecimal AchievingRateActionBig=new BigDecimal(AchievingRateAction*100);
                    AchievingRateAction=AchievingRateActionBig.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();

                    if (StoreIssueTarget>StoreIssueAction){
                        //ps:->强制修改邏輯
                        //AfsCarState=3;
                        AfsCarState=1;
                    }
                }

            }


            //ps:->强行修改
            if (AfsCarState>1){AfsCarState=1;}
            StateMapData.put(AfsCarName,AfsCarState);

        }


        //AGV
        if (agv_state!=null){
            String state = agv_state.getState();
            if ("Online".equals(state)){
                AGVState=1;
            }else if ("Offline".equals(state)){
                AGVState=0;
            }
        }
        if (AGVState==1){
            double MachineRateAction=0;
            int StoreIssueTarget=0;
            int StoreIssueAction=0;

            StoreIssueTarget = RGVStoreIssueQuantityList.size();
            List<AGV_AFS_Exchange> AGVStoreIssueQuantityList = agv_afs_exchangeMapper.selectStoreIssue(FloorName, StartDate, EndDate);
            for (AGV_AFS_Exchange agv_afs_exchange : AGVStoreIssueQuantityList) {
                String agvtobuf = agv_afs_exchange.getAgvtobuf();
                Boolean intBoolean = skasafsUtility.ConvertDouble(agvtobuf);
                if (intBoolean){
                    StoreIssueAction+= Integer.parseInt(agvtobuf);
                }
            }
            List<String> WorkstationsList =skasafsSystemHomeService.AGVWorkstationsList();
            MachineRateAction=skasafsSystemHomeService.agv_afsMachineRate("AGV",WorkstationsList,agv_exceptions,actionSecond,MachineRateAction);
            if (MachineRateAction>5){
                //ps:->强制修改邏輯
                //AGVState=3;
                AGVState=1;
            }
            double AchievingRateAction=0;
            if (StoreIssueTarget!=0){AchievingRateAction=(double)StoreIssueAction/StoreIssueTarget;}
            BigDecimal AchievingRateActionBig=new BigDecimal(AchievingRateAction*100);
            AchievingRateAction=AchievingRateActionBig.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
            if (StoreIssueTarget>StoreIssueAction){
                //ps:->强制修改邏輯
                //AGVState=3;
                AGVState=1;
            }
        }


        StateMapData.put("RGV",RGVState);
        StateMapData.put("AGV",AGVState);


        return StateMapData;
    }


}
