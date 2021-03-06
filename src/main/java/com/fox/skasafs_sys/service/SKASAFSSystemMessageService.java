package com.fox.skasafs_sys.service;


import com.fox.skasafs_sys.entity.*;
import com.fox.skasafs_sys.mapper.*;
import com.fox.skasafs_sys.utility.SKASAFSUtility;
import com.fox.testsys.utility.TimeUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author
 * @create 2020-06-17 11:25
 */
@Service
public class SKASAFSSystemMessageService {

    @Autowired
    AGV_AFS_ExchangeMapper agv_afs_exchangeMapper;

    @Autowired
    AGV_ExceptionMapper agv_exceptionMapper;

    @Autowired
    AGV_AFS_RGVReCodeMapper agv_afs_rgvReCodeMapper;

    @Autowired
    SKASAFSSystemHomeService skasafsSystemHomeService;

    @Autowired
    AGV_AFS_FeedReCodeMapper agv_afs_feedReCodeMapper;

    @Autowired
    SKASAFSSystemMessageAjaxService skasafsSystemMessageAjaxService;

    @Autowired
    TimeUtility timeUtility;

    @Autowired
    SKASAFSUtility skasafsUtility;
    @Autowired
    AutoFloor_SKAS_AFS_ALARMCODEMapper autoFloor_skas_afs_alarmcodeMapper;

    private  static  final  Integer DishNum=25;

    DateFormat format = new SimpleDateFormat("yyyy/MM/dd");

    public  List<AGVTabData>  SKASAFSSystemMessageData(String Quality, Double WorkingHours, Integer actionMinuteD, String FloorName, String StartDate, String EndDate){
        List<String> TIME_SLOTList = new ArrayList<>();
        if (timeUtility.Timequantum("08:30", "20:29")) {
            TIME_SLOTList = DayTIME_SLOT();
        } else {
            TIME_SLOTList = NightTIME_SLOT();
        }
        List<AGV_AFS_RGVReCode> RGVStoreIssueQuantityList = agv_afs_rgvReCodeMapper.selectStoreIssueQuantity(FloorName,"", "", StartDate, EndDate);
        List<AGVTabData> TabDataList=new ArrayList<>();
        List<AGV_Exception> rgv_exceptions = agv_exceptionMapper.selectMachineTimeSlotCost(FloorName, StartDate, EndDate);
        List<AGV_AFS_Exchange> AGVStoreIssueQuantityList = agv_afs_exchangeMapper.selectStoreIssue(FloorName, StartDate, EndDate);
        List<String> WorkstationsList=new ArrayList<>();
        if ("RGV".equals(Quality)){
            WorkstationsList=skasafsSystemHomeService.RGVWorkstationsList();
        }else  if ("AGV".equals(Quality)){
            WorkstationsList=skasafsSystemHomeService.AGVWorkstationsList();
        }

        List<String> ERRORList = ERRORList(Quality);
        AGVTabData ALLTabData=new AGVTabData();;
        TabDataList.add(0,ALLTabData);
        int ALLFaultTime=0;
        int ALLStoreIssueTarget=0;
        int ALLStoreIssueAction=0;
        double ALLAchievingRateAction=0;
        double ALLMachineFaultRate=0;
        int ALLERROR_TuoGui=0;
        int ALLERROR_PengZhuang=0;
        int ALLERROR_ZuDang=0;
        int ALLERROR_Jiting=0;
        int ALLERROR_ChaoShi=0;
        for (String TIME_SLOT : TIME_SLOTList) {
            AGVTabData TabData=new AGVTabData();
            int StoreIssueTarget=0;
            int StoreIssueAction=0;
            double AchievingRateAction=0;
            int FaultTime=0;
            double MachineFaultRate;
            int ERROR_TuoGui=0;
            int ERROR_PengZhuang=0;
            int ERROR_ZuDang=0;
            int ERROR_Jiting=0;
            int ERROR_ChaoShi=0;
            if ("RGV".equals(Quality)){
                for (AGV_AFS_RGVReCode agv_afs_rgvReCode : RGVStoreIssueQuantityList) {
                    String timeSlot = agv_afs_rgvReCode.getTimeSlot();
                    if (timeSlot.equals(TIME_SLOT)){
                        String target = agv_afs_rgvReCode.getTarget();
                        String actual = agv_afs_rgvReCode.getActual();
                        Boolean targetBoolean = skasafsUtility.ConvertDouble(target);
                        if (targetBoolean){ StoreIssueTarget+=Integer.parseInt(target);}
                        Boolean actualBoolean = skasafsUtility.ConvertDouble(actual);
                        if (actualBoolean){ StoreIssueAction+=Integer.parseInt(actual);}
                    }
                }
                for (AGV_Exception agv_exception : rgv_exceptions) {
                    String timeSlot = agv_exception.getTimeSlot();
                    String agverror = agv_exception.getError();
                    for (String  Workstations : WorkstationsList) {
                        String workstation = agv_exception.getWorkstation();
                        if (workstation.equals(Workstations)){
                            if (TIME_SLOT.equals(timeSlot)){
                                String cost = agv_exception.getCost();
                                int intcost= Integer.parseInt(cost);
                                for (String ERROR : ERRORList)  {
                                    if (ERROR.equals("TuoGui")){
                                        if (ERROR.equals(agverror)){
                                            ERROR_TuoGui++;
                                            FaultTime +=intcost;
                                        }
                                    }else if (ERROR.equals("PengZhuang")){
                                        if (ERROR.equals(agverror)){
                                            ERROR_PengZhuang++;
                                            FaultTime +=intcost;
                                        }
                                    }else if (ERROR.equals("Obstruct(ZuDang)")){
                                        if (ERROR.equals(agverror)){
                                            ERROR_ZuDang++;
                                            FaultTime +=intcost;
                                        }
                                    }else if (ERROR.equals("Jiting")){
                                        if (ERROR.equals(agverror)){
                                            ERROR_Jiting++;
                                            FaultTime +=intcost;
                                        }
                                    }else {
                                        if (ERROR.equals(agverror)){
                                            ERROR_ChaoShi++;
                                            FaultTime +=intcost;
                                        }
                                    }
                                }
                            }
                        }

                    }
                }
            }else if ("AGV".equals(Quality)){
                for (AGV_AFS_RGVReCode agv_afs_rgvReCode : RGVStoreIssueQuantityList) {
                    String timeSlot = agv_afs_rgvReCode.getTimeSlot();
                    if (timeSlot.equals(TIME_SLOT)){
                        StoreIssueTarget++;
                    }
                }
                for (AGV_AFS_Exchange agv_afs_exchange : AGVStoreIssueQuantityList) {
                    String timeSlot = agv_afs_exchange.getTimeSlot();
                    if (timeSlot.equals(TIME_SLOT)){
                        String agvtobuf = agv_afs_exchange.getAgvtobuf();
                        Boolean intBoolean = skasafsUtility.ConvertDouble(agvtobuf);
                        if (intBoolean){
                            StoreIssueAction+= Integer.parseInt(agvtobuf);
                        }
                    }
                }
                for (AGV_Exception agv_exception : rgv_exceptions) {
                    String timeSlot = agv_exception.getTimeSlot();
                    String agverror = agv_exception.getError();
                    for (String  Workstations : WorkstationsList) {
                        String workstation = agv_exception.getWorkstation();
                        if (workstation.equals(Workstations)){
                            if (TIME_SLOT.equals(timeSlot)){
                                String cost = agv_exception.getCost();
                                Boolean costBoolean = skasafsSystemMessageAjaxService.StringDouble(cost);
                                if (costBoolean){
                                    int intCost = Integer.parseInt(cost);
                                    for (String ERROR : ERRORList)  {
                                        if (ERROR.equals("TuoGui")){
                                            if (ERROR.equals(agverror)){
                                                ERROR_TuoGui++;
                                                FaultTime +=intCost;
                                            }
                                        }else if (ERROR.equals("PengZhuang")){
                                            if (ERROR.equals(agverror)){
                                                ERROR_PengZhuang++;
                                                FaultTime +=intCost;
                                            }
                                        }else if (ERROR.equals("ZuDang")){
                                            if (ERROR.equals(agverror)){
                                                ERROR_ZuDang++;
                                                FaultTime +=intCost;
                                            }
                                        }else if (ERROR.equals("JiTing")){
                                            if (ERROR.equals(agverror)){
                                                ERROR_Jiting++;
                                                FaultTime +=intCost;
                                            }
                                        }else {
                                            if (ERROR.equals(agverror)){
                                                ERROR_ChaoShi++;
                                                FaultTime +=intCost;
                                            }
                                        }
                                    }
                                }

                            }
                        }
                    }
                }
            }

            MachineFaultRate=(double) FaultTime/(60*60);
            BigDecimal MachineFaultRatebig=new BigDecimal(MachineFaultRate*100);
            MachineFaultRate=MachineFaultRatebig.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
            ALLFaultTime+=FaultTime;
            ALLERROR_TuoGui+=ERROR_TuoGui;
            ALLERROR_PengZhuang+=ERROR_PengZhuang;
            ALLERROR_ZuDang+=ERROR_ZuDang;
            ALLERROR_Jiting+=ERROR_Jiting;
            ALLERROR_ChaoShi+=ERROR_ChaoShi;
            TabData.setMachineFaultRateAction(MachineFaultRate);
            TabData.setERROR_TuoGui(ERROR_TuoGui);
            TabData.setERROR_PengZhuang(ERROR_PengZhuang);
            TabData.setERROR_ZuDang(ERROR_ZuDang);
            TabData.setERROR_Jiting(ERROR_Jiting);
            TabData.setERROR_ChaoShi(ERROR_ChaoShi);
            TabData.setMachineFaultTime(FaultTime/60);
            if (StoreIssueTarget!=0){AchievingRateAction=StoreIssueAction/(double)StoreIssueTarget;}
            BigDecimal AchievingRateActionBig=new BigDecimal(AchievingRateAction*100);
            AchievingRateAction=AchievingRateActionBig.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
            TabData.setStoreIssueTarget(StoreIssueTarget);
            ALLStoreIssueTarget+=StoreIssueTarget;
            TabData.setStoreIssueAction(StoreIssueAction);
            ALLStoreIssueAction+=StoreIssueAction;
            TabData.setAchievingRateAction(AchievingRateAction);
            TabData.setTime(TIME_SLOT);
            TabDataList.add(TabData);
        }
        ALLMachineFaultRate=(double) ALLFaultTime/(actionMinuteD*60);
        if (actionMinuteD>=720){
            ALLMachineFaultRate=(double) ALLFaultTime/(720*60);
        }
        //達成率
        if (ALLStoreIssueTarget!=0){ ALLAchievingRateAction=ALLStoreIssueAction/(double)ALLStoreIssueTarget;}
        BigDecimal ALLAchievingRateActionBig=new BigDecimal(ALLAchievingRateAction*100);
        ALLAchievingRateAction=ALLAchievingRateActionBig.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
        //機故率
        BigDecimal ALLMachineFaultRatebig=new BigDecimal(ALLMachineFaultRate*100);
        ALLMachineFaultRate=ALLMachineFaultRatebig.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();


        ALLTabData.setTime("TTL");
        ALLTabData.setStoreIssueTarget(ALLStoreIssueTarget);
        ALLTabData.setStoreIssueAction(ALLStoreIssueAction);
        ALLTabData.setAchievingRateAction(ALLAchievingRateAction);
        ALLTabData.setMachineFaultRateAction(ALLMachineFaultRate);
        ALLTabData.setERROR_TuoGui(ALLERROR_TuoGui);
        ALLTabData.setERROR_PengZhuang(ALLERROR_PengZhuang);
        ALLTabData.setERROR_ZuDang(ALLERROR_ZuDang);
        ALLTabData.setERROR_Jiting(ALLERROR_Jiting);
        ALLTabData.setERROR_ChaoShi(ALLERROR_ChaoShi);
        ALLTabData.setMachineFaultTime(ALLFaultTime/60);
        TabDataList.set(0,ALLTabData);
         return TabDataList;
    }

    public  List<AGVLineData>  SKASAFSSystemMessageLineData(String Quality, Date schedule, String FloorName){
        List<AGVLineData> AGVLineDataList=new ArrayList<>();
        List<String> DayDateList = DayDataList(schedule);
        Collections.reverse(DayDateList);
        List<String> WorkstationsList;
        if (Quality.equals("RGV")){
            WorkstationsList=skasafsSystemHomeService.RGVWorkstationsList();
        }else {
            WorkstationsList=skasafsSystemHomeService.AGVWorkstationsList();
        }
        for (String DayDate : DayDateList) {
            AGVLineData agvLineData=new AGVLineData();
            String StartDate=DayDate+" 00:00:00";
            String EndDate  =DayDate+" 23:59:59";


            int FaultTime=0;
            double MachineFaultRate=0;
            int StoreIssueTarget=0;
            int StoreIssueAction=0;
            double AchievingRateAction=0;
            List<AGV_AFS_RGVReCode> RGVStoreIssueQuantityList = agv_afs_rgvReCodeMapper.selectStoreIssueQuantity(FloorName,"", "", StartDate, EndDate);
            List<AGV_AFS_Exchange> AGVStoreIssueQuantityList = agv_afs_exchangeMapper.selectStoreIssue(FloorName, StartDate, EndDate);
            if ("RGV".equals(Quality)){
                //产能
                for (AGV_AFS_RGVReCode agv_afs_rgvReCode : RGVStoreIssueQuantityList) {
                    String target = agv_afs_rgvReCode.getTarget();
                    String actual = agv_afs_rgvReCode.getActual();
                    Boolean targetBoolean = skasafsUtility.ConvertDouble(target);
                    if (targetBoolean){ StoreIssueTarget+=Integer.parseInt(target);}
                    Boolean actualBoolean = skasafsUtility.ConvertDouble(actual);
                    if (actualBoolean){ StoreIssueAction+=Integer.parseInt(actual);}
                }
            }else if ("AGV".equals(Quality)){
                //产能
                StoreIssueTarget=RGVStoreIssueQuantityList.size();
                for (AGV_AFS_Exchange agv_afs_exchange : AGVStoreIssueQuantityList) {
                        String agvtobuf = agv_afs_exchange.getAgvtobuf();
                        Boolean intBoolean = skasafsUtility.ConvertDouble(agvtobuf);
                        if (intBoolean){
                            StoreIssueAction+= Integer.parseInt(agvtobuf);
                        }
                }
            }

            //达成率
            if (StoreIssueTarget!=0){AchievingRateAction=StoreIssueAction/(double)StoreIssueTarget;}
            BigDecimal AchievingRateActionBig=new BigDecimal(AchievingRateAction*100);
            AchievingRateAction=AchievingRateActionBig.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();

            //机故率
            List<AGV_Exception> agv_exceptions = agv_exceptionMapper.selectMachineTimeSlotCost(FloorName, StartDate, EndDate);
            List<String> ERRORList = ERRORList(Quality);
            FaultTime=skasafsSystemHomeService.getAGV_RGVMachineFaultTime(WorkstationsList,agv_exceptions,FaultTime,ERRORList);
            MachineFaultRate=(double) FaultTime/(24*60*60);
            BigDecimal MachineFaultRatebig=new BigDecimal(MachineFaultRate*100);
            MachineFaultRate=MachineFaultRatebig.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
            String Day = DayDate.substring(5);
            agvLineData.setAGVDayList(Day);
            agvLineData.setStoreIssueTarget(StoreIssueTarget);
            agvLineData.setStoreIssueAction(StoreIssueAction);
            agvLineData.setAchievingRateAction(AchievingRateAction);
            agvLineData.setMachineFaultRateTarget(5.0);
            agvLineData.setMachineFaultRateAction(MachineFaultRate);
            AGVLineDataList.add(agvLineData);
        }
        return AGVLineDataList;

    }

    public List<BUFFERTabData> BUFFERAFSSystemMessageData(String Quality,String CarNum,Double WorkingHours,Integer actionMinuteD,String FloorName,String StartDate,String EndDate){
        List<String> TIME_SLOTList = new ArrayList<>();
        if (timeUtility.Timequantum("08:30", "20:29")) {
            TIME_SLOTList = DayTIME_SLOT();
        } else {
            TIME_SLOTList = NightTIME_SLOT();
        }
        List<BUFFERTabData> TabDataList=new ArrayList<>();
        List<AGV_Exception> rgv_exceptions = agv_exceptionMapper.selectMachineTimeSlotCost(FloorName, StartDate, EndDate);


        List<AutoFloor_SKAS_AFS_ALARMCODE> autoFloor_skas_afs_alarmcodes ;
        String DeviceCarNum=CarNum;
        CarNum="C"+CarNum.substring(1);

        if (Quality.equals("BUFFER")){
            autoFloor_skas_afs_alarmcodes = autoFloor_skas_afs_alarmcodeMapper.selectAlarmCodeList("B001");
        }else {
            autoFloor_skas_afs_alarmcodes = autoFloor_skas_afs_alarmcodeMapper.selectAlarmCodeList("S001");
        }

        List<AGV_AFS_Exchange> BUFFERStoreIssueQuantityList = agv_afs_exchangeMapper.selectCarStoreIssue(FloorName,CarNum, StartDate, EndDate);
        List<AGV_AFS_FeedReCode> AFSfeedReCodes = agv_afs_feedReCodeMapper.selectCarAFSStoreIssue(FloorName, CarNum, StartDate, EndDate);
        List<AGV_AFS_RGVReCode> RGVStoreIssueQuantityList = agv_afs_rgvReCodeMapper.selectStoreIssueQuantity(FloorName,"", CarNum, StartDate, EndDate);

        BUFFERTabData ALLTabData=new BUFFERTabData();;
        TabDataList.add(0,ALLTabData);
        int ALLFaultTime=0;
        int ALLStoreIssueTarget=0;
        int ALLStoreIssueAction=0;
        double ALLAchievingRateAction=0;
        double ALLMachineFaultRate=0;
        int ALLERROR_ZuDang=0;
        int ALLERROR_Jiting=0;
        int ALLERROR_CCDYiChang=0;
        int ALLERROR_ShangLiaoKaLiao=0;
        int ALLERROR_JieLiaoPianWei=0;
        int ALLERROR_ShangLiaoDuiJieKaLiao=0;
        int ALLERROR_XiaLiaoKaLiao=0;
        int ALLERROR_KaLiaoYiChang=0;

        for (String TIME_SLOT : TIME_SLOTList) {
            BUFFERTabData TabData=new BUFFERTabData();
            int StoreIssueTarget=0;
            int StoreIssueAction=0;
            double AchievingRateAction=0;
            int FaultTime=0;
            double MachineFaultRate;
            int ERROR_ZuDang=0;
            int ERROR_Jiting=0;
            int ERROR_CCDYiChang=0;
            int ERROR_ShangLiaoKaLiao=0;
            int ERROR_JieLiaoPianWei=0;
            int ERROR_ShangLiaoDuiJieKaLiao=0;
            int ERROR_XiaLiaoKaLiao=0;
            int ERROR_KaLiaoYiChang=0;
            if ("BUFFER".equals(Quality)){
                for (AGV_AFS_Exchange agv_afs_exchange : BUFFERStoreIssueQuantityList) {
                    String timeSlot = agv_afs_exchange.getTimeSlot();
                    if (timeSlot.equals(TIME_SLOT)){
                        String agvtobuf = agv_afs_exchange.getAgvtobuf();
                        Boolean intBoolean = skasafsUtility.ConvertDouble(agvtobuf);
                        if (intBoolean){
                            StoreIssueTarget+= Integer.parseInt(agvtobuf);
                        }
                        String Buftoafs = agv_afs_exchange.getBuftoafs();
                        Boolean BufBoolean = skasafsUtility.ConvertDouble(Buftoafs);
                        if (BufBoolean){
                            StoreIssueAction+= Integer.parseInt(Buftoafs);
                        }
                    }
                }
            }else  if ("AFS".equals(Quality)){
                for (AGV_AFS_RGVReCode agv_afs_rgvReCode : RGVStoreIssueQuantityList) {
                    String actual = agv_afs_rgvReCode.getActual();
                    String timeSlot = agv_afs_rgvReCode.getTimeSlot();
                    Boolean TarBoolean = skasafsUtility.ConvertDouble(actual);
                    if (timeSlot.equals(TIME_SLOT)) {
                        if (TarBoolean) {
                            StoreIssueTarget += Integer.parseInt(actual);
                        }
                    }
                }

                for (AGV_AFS_FeedReCode afSfeedReCode : AFSfeedReCodes) {
                    String timeSlot = afSfeedReCode.getTimeSlot();
                    if (timeSlot.equals(TIME_SLOT)){
                        StoreIssueAction++;
                    }
                }

            }

            for (AutoFloor_SKAS_AFS_ALARMCODE autoFloor_skas_afs_alarmcode : autoFloor_skas_afs_alarmcodes) {
                String FalutType = autoFloor_skas_afs_alarmcode.getFalutType();
                String alarmCode = autoFloor_skas_afs_alarmcode.getAlarmCode();
                for (AGV_Exception rgv_exception : rgv_exceptions) {
                    String timeSlot = rgv_exception.getTimeSlot();
                    String cost = rgv_exception.getCost();
                    String device = rgv_exception.getDevice();
                    String errorCode = rgv_exception.getError();
                    if (TIME_SLOT.equals(timeSlot)){
                        if (DeviceCarNum.equals(device)){
                            Boolean costBoolean = skasafsSystemMessageAjaxService.StringDouble(cost);
                            if (costBoolean){
                                int intcost=Integer.parseInt(cost);
                                if (alarmCode.equals(errorCode)){
                                    if (FalutType.equals("卡料異常")){
                                        ERROR_KaLiaoYiChang++;
                                        ALLERROR_KaLiaoYiChang++;
                                        FaultTime += intcost;
                                    }
                                    else if (FalutType.equals("CCD異常")){
                                        ERROR_CCDYiChang++;
                                        ALLERROR_CCDYiChang++;
                                        FaultTime += intcost;
                                    }
                                    else if (FalutType.equals("上料卡料")){
                                        ERROR_ShangLiaoKaLiao++;
                                        ALLERROR_ShangLiaoKaLiao++;
                                        FaultTime += intcost;
                                    }
                                    else if (FalutType.equals("接料偏位")){
                                        ERROR_JieLiaoPianWei++;
                                        ALLERROR_JieLiaoPianWei++;
                                        FaultTime += intcost;
                                    }
                                    else if (FalutType.equals("上料 卡料")){
                                        ERROR_ShangLiaoDuiJieKaLiao++;
                                        ALLERROR_ShangLiaoDuiJieKaLiao++;
                                        FaultTime += intcost;
                                    }
                                    else if (FalutType.equals("急停")){
                                        ERROR_Jiting++;
                                        ALLERROR_Jiting++;
                                        FaultTime += intcost;
                                    }
                                    else if (FalutType.equals("阻擋")){
                                        ERROR_ZuDang++;
                                        ALLERROR_ZuDang++;
                                        FaultTime += intcost;
                                    }
                                    else if (FalutType.equals("下料卡料")){
                                        ERROR_XiaLiaoKaLiao++;
                                        ALLERROR_XiaLiaoKaLiao++;
                                        FaultTime += intcost;
                                    }

                                }
                            }

                        }
                    }

                }
            }


            ALLFaultTime+=FaultTime;
            MachineFaultRate=(double) FaultTime/(60*60);
            BigDecimal MachineFaultRatebig=new BigDecimal(MachineFaultRate*100);
            MachineFaultRate=MachineFaultRatebig.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();

            ALLMachineFaultRate+=MachineFaultRate;
            TabData.setMachineFaultRateAction(MachineFaultRate);
            TabData.setERROR_ZuDang(ERROR_ZuDang);
            TabData.setERROR_Jiting(ERROR_Jiting);
            TabData.setERROR_KaLiaoYiChang(ERROR_KaLiaoYiChang);
            TabData.setERROR_CCDYiChang(ERROR_CCDYiChang);
            TabData.setERROR_ShangLiaoKaLiao(ERROR_ShangLiaoKaLiao);
            TabData.setERROR_ShangLiaoDuiJieKaLiao(ERROR_ShangLiaoDuiJieKaLiao);
            TabData.setERROR_XiaLiaoKaLiao(ERROR_XiaLiaoKaLiao);
            TabData.setERROR_JieLiaoPianWei(ERROR_JieLiaoPianWei);
            TabData.setMachineFaultTime(FaultTime/(60*60));
            if (StoreIssueTarget!=0){AchievingRateAction=StoreIssueAction/(double)StoreIssueTarget;}
            BigDecimal AchievingRateActionBig=new BigDecimal(AchievingRateAction*100);
            AchievingRateAction=AchievingRateActionBig.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
            TabData.setStoreIssueTarget(StoreIssueTarget);
            ALLStoreIssueTarget+=StoreIssueTarget;
            TabData.setStoreIssueAction(StoreIssueAction);
            ALLStoreIssueAction+=StoreIssueAction;
            TabData.setAchievingRateAction(AchievingRateAction);
            TabData.setTime(TIME_SLOT);
            TabDataList.add(TabData);
        }
        ALLMachineFaultRate=(double) ALLFaultTime/(actionMinuteD*60);
        if (actionMinuteD>=720){
            ALLMachineFaultRate=(double) ALLFaultTime/(720*60);
        }
        BigDecimal ALLMachineFaultRatebig=new BigDecimal(ALLMachineFaultRate*100);
        ALLMachineFaultRate=ALLMachineFaultRatebig.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
        if (ALLStoreIssueTarget!=0){ ALLAchievingRateAction=ALLStoreIssueAction/(double)ALLStoreIssueTarget;}
        BigDecimal ALLAchievingRateActionBig=new BigDecimal(ALLAchievingRateAction*100);
        ALLAchievingRateAction=ALLAchievingRateActionBig.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
        ALLTabData.setTime("TTL");
        ALLTabData.setStoreIssueTarget(ALLStoreIssueTarget);
        ALLTabData.setStoreIssueAction(ALLStoreIssueAction);
        ALLTabData.setAchievingRateAction(ALLAchievingRateAction);
        ALLTabData.setMachineFaultRateAction(ALLMachineFaultRate);
        ALLTabData.setERROR_ZuDang(ALLERROR_ZuDang);
        ALLTabData.setERROR_Jiting(ALLERROR_Jiting);
        ALLTabData.setERROR_KaLiaoYiChang(ALLERROR_KaLiaoYiChang);
        ALLTabData.setERROR_CCDYiChang(ALLERROR_CCDYiChang);
        ALLTabData.setERROR_ShangLiaoKaLiao(ALLERROR_ShangLiaoKaLiao);
        ALLTabData.setERROR_ShangLiaoDuiJieKaLiao(ALLERROR_ShangLiaoDuiJieKaLiao);
        ALLTabData.setERROR_XiaLiaoKaLiao(ALLERROR_XiaLiaoKaLiao);
        ALLTabData.setERROR_JieLiaoPianWei(ALLERROR_JieLiaoPianWei);
        ALLTabData.setMachineFaultTime(ALLFaultTime/60);
        TabDataList.set(0,ALLTabData);
        return TabDataList;
    }

    public Map<String,List<BUFFERLineData>> BUFFERAFSSystemMessageLineData(String Quality,String CarNum,Date schedule,String FloorName){

        Map<String,List<BUFFERLineData>> BUFFERAFSLineDataMap=new HashMap<>();

        List<String> DayDateList = DayDataList(schedule);
        Collections.reverse(DayDateList);
        List<AutoFloor_SKAS_AFS_ALARMCODE> autoFloor_skas_afs_alarmcodes =new ArrayList<>();
        List<String> DeviceCarNumList=new ArrayList<>();
        if ("BUFFER".equals(Quality)){
            DeviceCarNumList=skasafsSystemHomeService.BufferNumList();
        }else if ("AFS".equals(Quality)){
            DeviceCarNumList=skasafsSystemHomeService.AFSNumList();
        }
        List<String> CarNumList = skasafsSystemHomeService.StoreIssueNumList();

        for (String Car : CarNumList) {
            List<BUFFERLineData> BUFFERAFSLineDataList=new ArrayList<>();
            for (String DayDate : DayDateList) {
                BUFFERLineData BUFFERAFSLineData=new BUFFERLineData();
                String StartDate=DayDate+" 00:00:00";
                String EndDate  =DayDate+" 23:59:59";
                int FaultTime=0;
                double MachineFaultRate=0;
                int StoreIssueTarget=0;
                int StoreIssueAction=0;
                double AchievingRateAction=0;
                    if ("BUFFER".equals(Quality)){
                        List<AGV_AFS_Exchange> BUFFERStoreIssueQuantityList = agv_afs_exchangeMapper.selectStoreIssue(FloorName, StartDate, EndDate);
                        //产能
                        for (AGV_AFS_Exchange agv_afs_Exchange : BUFFERStoreIssueQuantityList) {
                            String linename = agv_afs_Exchange.getLinename();
                            if (linename.equals(Car)){
                                String target = agv_afs_Exchange.getAgvtobuf();
                                String actual = agv_afs_Exchange.getBuftoafs();
                                Boolean targetBoolean = skasafsUtility.ConvertDouble(target);
                                if (targetBoolean){ StoreIssueTarget+=Integer.parseInt(target);}
                                Boolean actualBoolean = skasafsUtility.ConvertDouble(actual);
                                if (actualBoolean){ StoreIssueAction+=Integer.parseInt(actual);}
                            }
                        }
                    }else if ("AFS".equals(Quality)){
                        List<AGV_AFS_FeedReCode> agv_afs_feedReCodes = agv_afs_feedReCodeMapper.selectAFSStoreIssue(FloorName, StartDate, EndDate);
                        List<AGV_AFS_RGVReCode> RGVStoreIssueQuantityList = agv_afs_rgvReCodeMapper.selectStoreIssueQuantity(FloorName,"", "", StartDate, EndDate);
                        //产能
                        for (AGV_AFS_RGVReCode agv_afs_rgvReCode : RGVStoreIssueQuantityList) {
                            String linename = agv_afs_rgvReCode.getLinename();
                            if (linename .equals(Car)){
                                String actual = agv_afs_rgvReCode.getActual();
                                Boolean actualBoolean = skasafsUtility.ConvertDouble(actual);
                                if (actualBoolean){ StoreIssueTarget+=Integer.parseInt(actual);}
                            }
                        }
                        for (AGV_AFS_FeedReCode agv_afs_feedReCode : agv_afs_feedReCodes) {
                            String linename = agv_afs_feedReCode.getLinename();
                            if (linename.equals(Car)){
                                StoreIssueAction++;
                            }
                        }
                    }
                    //达成率
                    if (StoreIssueTarget!=0){AchievingRateAction=StoreIssueAction/(double)StoreIssueTarget;}
                    BigDecimal AchievingRateActionBig=new BigDecimal(AchievingRateAction*100);
                    AchievingRateAction=AchievingRateActionBig.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
                    //机故率
                    List<AGV_Exception> agv_exceptions = agv_exceptionMapper.selectMachineTimeSlotCost(FloorName, StartDate, EndDate);
                    FaultTime= skasafsSystemHomeService.getBuf_AfsMachineFaultTime(Car, agv_exceptions, FaultTime, autoFloor_skas_afs_alarmcodes);

                    MachineFaultRate=(double) FaultTime/(24*60*60);
                    BigDecimal MachineFaultRatebig=new BigDecimal(MachineFaultRate*100);
                    MachineFaultRate=MachineFaultRatebig.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
                    String Day = DayDate.substring(5);
                    BUFFERAFSLineData.setBUFFERDayList(Day);
                    BUFFERAFSLineData.setStoreIssueTarget(StoreIssueTarget);
                    BUFFERAFSLineData.setStoreIssueAction(StoreIssueAction);
                    BUFFERAFSLineData.setAchievingRateAction(AchievingRateAction);
                    BUFFERAFSLineData.setMachineFaultRateTarget(5.0);
                    BUFFERAFSLineData.setMachineFaultRateAction(MachineFaultRate);
                    BUFFERAFSLineDataList.add(BUFFERAFSLineData);
            }
            BUFFERAFSLineDataMap.put(Car,BUFFERAFSLineDataList);
        }

        return BUFFERAFSLineDataMap;
    }

    private  List<String> DayDataList(Date schedule){
        List<String> DayDateList=new ArrayList<>();
        for (int i = 1; i < 8; i++) {
            long Time = schedule.getTime() - (i*24 * 60 * 60 * 1000);
            Date date=new Date(Time);
            String format = this.format.format(date);
            DayDateList.add(format);
        }
        return DayDateList;
    }

    private List<String> DayTIME_SLOT() {
        List<String> DayTime_Slot = new ArrayList<>();
        DayTime_Slot.add("8:30~9:30");
        DayTime_Slot.add("9:30~10:30");
        DayTime_Slot.add("10:30~11:30");
        DayTime_Slot.add("11:30~12:30");
        DayTime_Slot.add("12:30~13:30");
        DayTime_Slot.add("13:30~14:30");
        DayTime_Slot.add("14:30~15:30");
        DayTime_Slot.add("15:30~16:30");
        DayTime_Slot.add("16:30~17:30");
        DayTime_Slot.add("17:30~18:30");
        DayTime_Slot.add("18:30~19:30");
        DayTime_Slot.add("19:30~20:30");
        return DayTime_Slot;
    }

    private List<String> NightTIME_SLOT() {
        List<String> NightTime_Slot = new ArrayList<>();
        NightTime_Slot.add("20:30~21:30");
        NightTime_Slot.add("21:30~22:30");
        NightTime_Slot.add("22:30~23:30");
        NightTime_Slot.add("23:30~00:30");
        NightTime_Slot.add("0:30~1:30");
        NightTime_Slot.add("1:30~2:30");
        NightTime_Slot.add("2:30~3:30");
        NightTime_Slot.add("3:30~4:30");
        NightTime_Slot.add("4:30~5:30");
        NightTime_Slot.add("5:30~6:30");
        NightTime_Slot.add("6:30~7:30");
        NightTime_Slot.add("7:30~8:30");
        return NightTime_Slot;
    }

    public List<String> ERRORList(String Machine) {


        List<String> ERRORList = new ArrayList<>();
            //超時
            ERRORList.add("ChaoShi");
            //脫軌
            ERRORList.add("TuoGui");
            //碰撞
            ERRORList.add("PengZhuang");
            //阻擋
            ERRORList.add("ZuDang");
            //急停
            ERRORList.add("JiTing");

        return ERRORList;
    }

}
