package com.fox.avisys.service;


import com.fox.avisys.entity.*;
import com.fox.avisys.mapper.*;
import com.fox.qualitysys.mapper.AutoFloor_Key_KeyPMSMapper;
import com.fox.testsys.entity.AutoFloor_Target;
import com.fox.testsys.utility.TaskService;
import com.fox.testsys.utility.TimeUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author
 * @create 2020-06-16 14:24
 */
@Service
public class AVISystemService {

    @Autowired
    AutoFloor_AVI_BreakdownMapper autoFloor_avi_breakdownMapper;

    @Autowired
    AutoFloor_AVI_TrackingMapper autoFloor_avi_trackingMapper;

    @Autowired
    AutoFloor_Key_KeyPMSMapper autoFloor_key_keyPMSMapper;

    @Autowired
    AutoFloor_AVI_WarningMapper autoFloor_avi_warningMapper;

    @Autowired
    AutoFloor_AVI_VersionMapper autoFloor_avi_versionMapper;
    @Autowired
    IPD_SFC_INFO_VMI4Mapper ipd_sfc_info_vmi4Mapper;
    @Autowired
    AutoFloor_AVI_PLC_LogMapper autoFloor_avi_plc_logMapper;

    @Autowired
    private  AutoFloor_AVI_TOP1Mapper autoFloor_avi_top1Mapper;

    @Autowired
    private  AutoFloor_AVI_TOP2Mapper autoFloor_avi_top2Mapper;

    @Autowired
    TaskService taskService;
    @Autowired
    TimeUtility timeUtility;

    DateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    DateFormat Aviformat = new SimpleDateFormat("yyyyMMdd");

    private  static   int MachineSum=4;

    public AutoFloor_AVI_MachinieType AVISystemQualityType(Integer MachineTypeSum, List<AutoFloor_Target> LineList, String StartDate, String EndDate){
        AutoFloor_AVI_MachinieType autoFloor_avi_machinieType=new AutoFloor_AVI_MachinieType();
        int MachineHealthTypeNum=0;
        int MachinewarningTypeNum=0;
        int MachineunusualTypeNum=0;
        int MachineFalutTypeNum=0;
        for (AutoFloor_Target autoFloor_target : LineList) {
            int topHealth=0;
            int topwarning=0;
            int topFalut=0;
            int topUnusua=0;
            String lineName = autoFloor_target.getLineName();

            AutoFloor_AVI_Warning Top1_avi_machines = autoFloor_avi_warningMapper.selectMachinieType( lineName,"TOP1",StartDate, EndDate);
            AutoFloor_AVI_Warning Top2_avi_machines = autoFloor_avi_warningMapper.selectMachinieType( lineName,"TOP2",StartDate, EndDate);
            AutoFloor_AVI_PLC_Log TOP2UnusualStatus = autoFloor_avi_plc_logMapper.selectUnusualStatus(lineName, "TOP2");
            if (Top1_avi_machines!=null){
                String TOP1Type = Top1_avi_machines.getWarning();
                if ("OK".equals(TOP1Type)){
                    topHealth++;
                }else if ("CLOSE".equals(TOP1Type)){
                    topFalut++;
                }else if ("QUIT".equals(TOP1Type)){
                    topFalut++;
                }else{
                    topwarning++;
                }
            }
            if (Top2_avi_machines!=null){
                String TOP2Type = Top2_avi_machines.getWarning();
                if ("OK".equals(TOP2Type)){
                    topHealth++;
                }else if ("CLOSE".equals(TOP2Type)){
                    topFalut++;
                }else if ("QUIT".equals(TOP2Type)){
                    topFalut++;
                }else {
                    topwarning++;
                }
            }


            if (TOP2UnusualStatus!=null) {
                Date TOP2inputtime = TOP2UnusualStatus.getInputtime();
                Date TOP2sysDate = TOP2UnusualStatus.getSysDate();
                if (TOP2sysDate.getTime() - TOP2inputtime.getTime() > (7 * 60 * 1000)) {
                    topHealth= 0;
                    topwarning = 0;
                    topFalut = 0;
                    topUnusua++;
                    if (TOP2sysDate.getTime() - TOP2inputtime.getTime() > (72 * 60 * 60 * 1000)) {
                        topUnusua=0;
                        MachineSum = MachineSum - 1;
                    }
                }
            }
            ///////////////////
            if ("D061FARF03".equals(lineName)||"D061FARF04".equals(lineName)){
                topHealth=2;
                topwarning=0;
                topFalut=0;
                topUnusua=0;
            }
            ////////////////////

            if (topHealth>1){
                MachineHealthTypeNum++;
            }

            if (topwarning>0){
                topFalut=0;
                MachinewarningTypeNum++;
            }

            if (topFalut>0){
                MachineFalutTypeNum++;
            }

            if (topUnusua>0){
                MachineunusualTypeNum++;
            }
        }

        Double MachineRate=0.0;
        if (MachineSum!=0){
            MachineRate=(double)MachineHealthTypeNum/4;
        }

        BigDecimal MachineRatebig=new BigDecimal(MachineRate*100);
        MachineRate=MachineRatebig.setScale(1,BigDecimal.ROUND_HALF_UP).doubleValue();
        autoFloor_avi_machinieType.setMachineTypeSum(MachineTypeSum);
        autoFloor_avi_machinieType.setMachineHealthTypeNum(MachineHealthTypeNum);
        autoFloor_avi_machinieType.setMachinewarningTypeNum(MachinewarningTypeNum);
        autoFloor_avi_machinieType.setMachineFalutNum(MachineFalutTypeNum);
        autoFloor_avi_machinieType.setMachineunusualTypeNum(MachineunusualTypeNum);
        autoFloor_avi_machinieType.setMachineRate(MachineRate);

        return autoFloor_avi_machinieType;
    }

    public  Map<String,Object>   AVISystemMachineMassage(List<AutoFloor_Target> LineList,String FloorName,Integer actionMinuteD,String StartDate,String EndDate,String AVIStartDate,String AVIEndDate){
        List<AutoFloor_AVI_Breakdown> autoFloor_avi_breakdowns = autoFloor_avi_breakdownMapper.selectUNMessage(FloorName, StartDate, EndDate);
        double MachineFlutTime=0;
        for (AutoFloor_AVI_Breakdown autoFloor_avi_breakdown : autoFloor_avi_breakdowns) {
            String timeelapsed = autoFloor_avi_breakdown.getTimeelapsed();
            Boolean aBoolean = ValStrList(timeelapsed);
            if (aBoolean){
                MachineFlutTime+= Double.parseDouble(timeelapsed);
            }
        }
        AutoFloor_Target autoFloor_target =new AutoFloor_Target();
        for (int i = 0; i < LineList.size(); i++) {
            AutoFloor_Target LineTarget = LineList.get(i);
            if (i<1){
                autoFloor_target = LineTarget;
            }
            String lineName = LineTarget.getLineName();
            AutoFloor_AVI_Tracking autoFloor_avi_tracking = autoFloor_avi_trackingMapper.selectProgramVersion(FloorName, lineName, AVIStartDate, AVIEndDate);
            if (autoFloor_avi_tracking!=null){
                Date inputTime = autoFloor_avi_tracking.getInputTime();
                long Inputtime = inputTime.getTime();
                try {
                    Date parse = format.parse(StartDate);
                    long StartDateLong = parse.getTime();
                    long AutoTime=Inputtime-StartDateLong;
                    actionMinuteD+= Math.toIntExact(AutoTime / 1000);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        actionMinuteD/=60;
        Double MachineRate=0.0;
        if (actionMinuteD!=0){
            MachineRate=MachineFlutTime/(actionMinuteD);
            BigDecimal  MachineRateBig=new BigDecimal( MachineRate*100);
            MachineRate=MachineRateBig.setScale(1,BigDecimal.ROUND_HALF_UP).doubleValue();
        }
        if (MachineRate<0){MachineRate=0.0;}

        List<AutoFloor_AVI_Warning> autoFloor_avi_warnings = autoFloor_avi_warningMapper.selectMachinieWarning(FloorName, StartDate, EndDate);

        List<AutoFloor_AVI_Warning> MachineMassageList=new ArrayList<>();
        for (int i = 0; i<autoFloor_avi_warnings.size(); i++) {
            int size = autoFloor_avi_warnings.size();
            AutoFloor_AVI_Warning autoFloor_avi_Warning = new AutoFloor_AVI_Warning();
            autoFloor_avi_Warning.setLinename("无数据");
           /* if (size==1){
                if (i<1){
                    autoFloor_avi_Warning = autoFloor_avi_warnings.get(0);
                }
            }else if (size==2){
                if (i<2){
                    autoFloor_avi_Warning = autoFloor_avi_warnings.get(1);
                }
            }else if (size>=3){
            }*/
            autoFloor_avi_Warning = autoFloor_avi_warnings.get(i);
            String EndTimeStr ="";
            if (autoFloor_avi_Warning.getInputtime()!=null){
                Date endtime = autoFloor_avi_Warning.getInputtime();
                EndTimeStr =format.format(endtime);
            }
            autoFloor_avi_Warning.setERRORTimeString(EndTimeStr);
            MachineMassageList.add(autoFloor_avi_Warning);
        }
        Map<String,Object> MachineMassageMap=new HashMap<>();
        MachineMassageMap.put("MachineRate",MachineRate);
        MachineMassageMap.put("MachineMassageList",MachineMassageList);
        return MachineMassageMap;
    }

    public  Boolean ValStrList(String val) {
        Boolean ValStr = null;
        try {
            Double.parseDouble(val);
            ValStr = true;
        } catch (Exception e) {
            ValStr = false;
        }

        return ValStr;
    }

    public  AutoFloor_AVI_MachinieType  AVISystemProgramVersion(Integer MachinieTypeSum,List<AutoFloor_Target> MacLineList,String FloorName,String AviStartTime,String AviEndTime){
        AutoFloor_AVI_MachinieType autoFloor_avi_machinieType=new AutoFloor_AVI_MachinieType();
        List<AutoFloor_AVI_Version> autoFloor_avi_versions = autoFloor_avi_versionMapper.selectAVI_Version();

        Integer OKVersion=0;
        for (AutoFloor_Target autoFloor_target : MacLineList) {
            int dTarget = autoFloor_target.getdTarget().intValue();
            if (dTarget>0){
                String lineName = autoFloor_target.getLineName();
                AutoFloor_AVI_Tracking LineProgramVersion = autoFloor_avi_trackingMapper.selectProgramVersion(FloorName, lineName, AviStartTime, AviEndTime);
                if (LineProgramVersion!=null){
                    String LineVersion = LineProgramVersion.getP37();
                    for (AutoFloor_AVI_Version autoFloor_avi_version : autoFloor_avi_versions) {
                        String linename = autoFloor_avi_version.getLinename();
                        String spec = autoFloor_avi_version.getSpec();
                        if (lineName!=null&&lineName.equals(linename)){
                            if (LineVersion!=null&&LineVersion.equals(spec)){
                                OKVersion++;
                            }
                        }
                    }
                }
            }
        }


       Integer ErrorVersion=MachinieTypeSum-OKVersion;
        if (ErrorVersion<0){
            ErrorVersion=0;
        }
        Double MachineRate=0.0;
        if (MachinieTypeSum!=0){
            MachineRate=ErrorVersion/(double)MachinieTypeSum;
        }
        BigDecimal MachineRateBig=new BigDecimal(MachineRate*100);
        MachineRate=MachineRateBig.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();

        Double MachineHealthRate=OKVersion/(double)MachinieTypeSum;
        BigDecimal MachineHealthRateBig=new BigDecimal(MachineHealthRate*100);
        MachineHealthRate=MachineHealthRateBig.setScale(0,BigDecimal.ROUND_HALF_UP).doubleValue();


        autoFloor_avi_machinieType.setMachineRate(MachineRate);
        autoFloor_avi_machinieType.setMachineHealthRate(MachineHealthRate);
        autoFloor_avi_machinieType.setMachineHealthTypeNum(OKVersion);
        autoFloor_avi_machinieType.setMachineFalutNum(ErrorVersion);
        autoFloor_avi_machinieType.setMachineTypeSum(MachinieTypeSum);
        return autoFloor_avi_machinieType;



    }

    public Map<String, Object> AVISystemUPHLineData(Integer Htime, String FloorName){
        Map<String,Object> AVISystemUPHLineDataMap= new HashMap<>();
        List<AutoFloor_AVIUPHLineData> AVIUPHLineData=new ArrayList<>();

        List<String> TIME_SLOT =new ArrayList<>();
        String DayType="";
        if (timeUtility.Timequantum("08:30", "20:30")) {
            DayType="Day";
            TIME_SLOT =DayTIME_SLOT();
        }else {
            DayType="Night";
            TIME_SLOT =NightTIME_SLOT();
        }
        List<AutoFloor_AVI_TimeSlot> autoFloor_avi_timeSlots = AVITimeSlot((Htime-8),DayType);
        /*获取当前时间*/
        List<java.sql.Date> sqlList = taskService.Schedule();
        /*当前日期*/
        Date schedule = sqlList.get(0);
        /*当前日期减1*/
        Date YesterdayDate = sqlList.get(1);
        /*当前日期加1*/
        Date TomorrowDate = sqlList.get(2);
        String AviTime = Aviformat.format(schedule);
        String AviTomorrowDate = Aviformat.format(TomorrowDate);
        String AviYesterdayDate = Aviformat.format(YesterdayDate);

        int allAVIUPHSum=0;
        int allAVIUPHPass=0;
        int allAVIUPHFail=0;

        for (int i = 0; i < autoFloor_avi_timeSlots.size(); i++) {
            AutoFloor_AVI_TimeSlot AVI_TimeSlot = autoFloor_avi_timeSlots.get(i);
            String Time = TIME_SLOT.get(i);
            String aviEndDate = AVI_TimeSlot.getAVIEndDate();
            String aviStartDate = AVI_TimeSlot.getAVIStartDate();
            String AVISTARTTIME="";
            String AVIENDTIME="";
            if (timeUtility.Timequantum("20:30", "23:59")) {
                switch (Time){
                    case "20:30":
                    case "22:30":
                    case "21:30":
                        AVISTARTTIME=AviTime+aviStartDate;
                        AVIENDTIME=AviTime+aviEndDate;
                        break;
                    case "23:30":
                        AVISTARTTIME=AviTime+aviStartDate;
                        AVIENDTIME=AviTomorrowDate+aviEndDate;
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
                        AVISTARTTIME=AviTomorrowDate+aviStartDate;
                        AVIENDTIME=AviTomorrowDate+aviEndDate;
                        break;
                }
            }
            else if (timeUtility.Timequantum("00:00", "08:30")) {
                switch (Time){
                    case "20:30":
                    case "22:30":
                    case "21:30":
                        AVISTARTTIME=AviYesterdayDate+aviStartDate;
                        AVIENDTIME=AviYesterdayDate+aviEndDate;
                        break;
                    case "23:30":
                        AVISTARTTIME=AviYesterdayDate+aviStartDate;
                        AVIENDTIME=AviTime+aviEndDate;
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
                        AVISTARTTIME=AviTime+aviStartDate;
                        AVIENDTIME=AviTime+aviEndDate;
                        break;
                }
            }
            else {
                AVISTARTTIME=AviTime+aviStartDate;
                AVIENDTIME=AviTime+aviEndDate;
            }

            List<AutoFloor_AVI_Tracking> AVIUPH = autoFloor_avi_trackingMapper.selectAVIUPH(FloorName,AVISTARTTIME, AVIENDTIME);
            List<AutoFloor_AVI_Tracking> AVIRepetitionUPH = autoFloor_avi_trackingMapper.selectAVIRepetitionUPH(FloorName, AVISTARTTIME, AVIENDTIME);
            AutoFloor_AVIUPHLineData autoFloor_aviuphLineData=new AutoFloor_AVIUPHLineData();
            int AVIUPHSum=0;
            int AVIUPHPass=0;
            int AVIUPHFail=0;
            //机器测试
            for (AutoFloor_AVI_Tracking autoFloor_avi_tracking : AVIUPH) {
                String uk = autoFloor_avi_tracking.getUk();
                Boolean aBoolean = ValIntList(uk);
                if (aBoolean){
                    int ukInt = Integer.parseInt(uk);
                    if (ukInt==0){
                        AVIUPHPass++;
                    }else {
                        AVIUPHFail++;
                    }
                }
            }
            //人工复判
            for (AutoFloor_AVI_Tracking autoFloor_avi_tracking : AVIRepetitionUPH) {
                String uk = autoFloor_avi_tracking.getUk();
                Boolean aBoolean = ValIntList(uk);
                if (aBoolean){
                    int ukInt = Integer.parseInt(uk);
                    if (ukInt==0){
                        AVIUPHPass++;
                    }else {
                        AVIUPHFail--;
                    }
                }
            }


            AVIUPHSum=(AVIUPHPass+AVIUPHFail);
            allAVIUPHSum+=AVIUPHSum;
            allAVIUPHPass+=AVIUPHPass;
            allAVIUPHFail+=AVIUPHFail;
            Double AVIUPHPassRate=0.0;
            Double AVIUPHFailRate=0.0;
            if (AVIUPHSum!=0){
                AVIUPHPassRate= AVIUPHPass/(double)AVIUPHSum;
                BigDecimal AVIUPHPassRateBig=new BigDecimal(AVIUPHPassRate*100);
                AVIUPHPassRate=AVIUPHPassRateBig.setScale(1,BigDecimal.ROUND_HALF_UP).doubleValue();

                AVIUPHFailRate= AVIUPHFail/(double)AVIUPHSum;
                BigDecimal AVIUPHFailRateBig=new BigDecimal(AVIUPHFailRate*100);
                AVIUPHFailRate=AVIUPHFailRateBig.setScale(1,BigDecimal.ROUND_HALF_UP).doubleValue();
            }
            autoFloor_aviuphLineData.setTIME_SLOT(Time);
            autoFloor_aviuphLineData.setAVIUPHSum(AVIUPHSum);
            autoFloor_aviuphLineData.setAVIUPHPassRate(AVIUPHPassRate);
            autoFloor_aviuphLineData.setAVIUPHFailRate(AVIUPHFailRate);
            AVIUPHLineData.add( autoFloor_aviuphLineData);
        }
        Double allAVIUPHPassRate=0.00;
        if (allAVIUPHSum!=0){
            allAVIUPHPassRate= allAVIUPHPass/(double)allAVIUPHSum;
        }
        BigDecimal AVIUPHPassRateBig=new BigDecimal(allAVIUPHPassRate*100);
        allAVIUPHPassRate=AVIUPHPassRateBig.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
        AVISystemUPHLineDataMap.put("allAVIUPHPassRate",allAVIUPHPassRate);
        AVISystemUPHLineDataMap.put("AVIUPHLineData",AVIUPHLineData);
        return AVISystemUPHLineDataMap;
    }

    private List<String> DayTIME_SLOT() {
        List<String> DayTime_Slot = new ArrayList<>();
        DayTime_Slot.add("08:30");
        DayTime_Slot.add("09:30");
        DayTime_Slot.add("10:30");
        DayTime_Slot.add("11:30");
        DayTime_Slot.add("12:30");
        DayTime_Slot.add("13:30");
        DayTime_Slot.add("14:30");
        DayTime_Slot.add("15:30");
        DayTime_Slot.add("16:30");
        DayTime_Slot.add("17:30");
        DayTime_Slot.add("18:30");
        DayTime_Slot.add("19:30");
        DayTime_Slot.add("20:30");
        return DayTime_Slot;
    }

    private List<String> NightTIME_SLOT() {
        List<String> NightTime_Slot = new ArrayList<>();
        NightTime_Slot.add("20:30");
        NightTime_Slot.add("21:30");
        NightTime_Slot.add("22:30");
        NightTime_Slot.add("23:30");
        NightTime_Slot.add("00:30");
        NightTime_Slot.add("01:30");
        NightTime_Slot.add("02:30");
        NightTime_Slot.add("03:30");
        NightTime_Slot.add("04:30");
        NightTime_Slot.add("05:30");
        NightTime_Slot.add("06:30");
        NightTime_Slot.add("07:30");
        NightTime_Slot.add("08:30");
        return NightTime_Slot;
    }

    private List<AutoFloor_AVI_TimeSlot>  AVITimeSlot(Integer Htime,String DayType){

        List<AutoFloor_AVI_TimeSlot> TIME_SLOTList=new ArrayList<>();
        if (DayType.equals("Day")){
            for (int i = 0; i < Htime; i++) {
                int DayH=8;
                AutoFloor_AVI_TimeSlot autoFloor_avi_timeSlot=new AutoFloor_AVI_TimeSlot();
                if (DayH+i<=9){
                    autoFloor_avi_timeSlot.setAVIStartDate("0"+(DayH+i)+"3000");
                    autoFloor_avi_timeSlot.setAVIEndDate("0"+(DayH+1+i)+"3000");
                    if (DayH+i==9){
                        autoFloor_avi_timeSlot.setAVIEndDate((DayH+1+i)+"3000");
                    }
                }else {
                    autoFloor_avi_timeSlot.setAVIStartDate((DayH+i)+"3000");
                    autoFloor_avi_timeSlot.setAVIEndDate((DayH+1+i)+"3000");
                }

                TIME_SLOTList.add(autoFloor_avi_timeSlot);
            }
        }else {
            for (int i = 0; i < 13; i++) {
                int NightH=20;
                AutoFloor_AVI_TimeSlot autoFloor_avi_timeSlot=new AutoFloor_AVI_TimeSlot();
                if (NightH+i<=23){
                    autoFloor_avi_timeSlot.setAVIStartDate((NightH+i)+"3000");
                    if ((NightH+1+i)>23){
                        autoFloor_avi_timeSlot.setAVIEndDate("003000");
                    }else {
                        autoFloor_avi_timeSlot.setAVIEndDate((NightH+1+i)+"3000");
                    }
                }else {
                    autoFloor_avi_timeSlot.setAVIStartDate("0"+(i-4)+"3000");
                    autoFloor_avi_timeSlot.setAVIEndDate("0"+(1+i-4)+"3000");
                }
                TIME_SLOTList.add(autoFloor_avi_timeSlot);
            }
        }


        return TIME_SLOTList;
    }

    public  Boolean ValIntList(String val) {
        Boolean ValStr = null;
        try {
            Integer.parseInt(val);
            ValStr = true;
        } catch (Exception e) {
            ValStr = false;
        }

        return ValStr;
    }

    public  Map<String,Integer>  AVIQualityType(List<AutoFloor_Target> LineList,String StartDate, String EndDate){
        Map<String,Integer> AVIQualityTypeMap=new HashMap<>();
        for (AutoFloor_Target autoFloor_target : LineList) {
            Integer  QualityType=null;
            int topHealth=0;
            int topwarning=0;
            int topFalut=0;
            int topUnusua=0;
            int topStop=0;
            String lineName = autoFloor_target.getLineName();
            AutoFloor_AVI_Warning Top1_avi_machines = autoFloor_avi_warningMapper.selectMachinieType( lineName,"TOP1",StartDate, EndDate);
            AutoFloor_AVI_Warning Top2_avi_machines = autoFloor_avi_warningMapper.selectMachinieType( lineName,"TOP2",StartDate, EndDate);
            AutoFloor_AVI_PLC_Log TOP2UnusualStatus = autoFloor_avi_plc_logMapper.selectUnusualStatus(lineName, "TOP2");
            if (Top1_avi_machines!=null){
                String TOP1Type = Top1_avi_machines.getWarning();
                if ("OK".equals(TOP1Type)){
                    topHealth++;
                }else if ("CLOSE".equals(TOP1Type)){
                    topFalut++;
                }else if ("QUIT".equals(TOP1Type)){
                    topFalut++;
                }else{
                    topwarning++;
                }
            }
            if (Top2_avi_machines!=null){
                String TOP2Type = Top2_avi_machines.getWarning();
                if ("OK".equals(TOP2Type)){
                    topHealth++;
                }else if ("CLOSE".equals(TOP2Type)){
                    topFalut++;
                }else if ("QUIT".equals(TOP2Type)){
                    topFalut++;
                }else {
                    topwarning++;
                }
            }
            if (TOP2UnusualStatus!=null) {
                Date TOP2inputtime = TOP2UnusualStatus.getInputtime();
                Date TOP2sysDate = TOP2UnusualStatus.getSysDate();
                if (TOP2sysDate.getTime() - TOP2inputtime.getTime() > (7 * 60 * 1000)) {
                    topHealth= 0;
                    topwarning = 0;
                    topFalut = 0;
                    topUnusua++;
                    if (TOP2sysDate.getTime() - TOP2inputtime.getTime() > (72 * 60 * 60 * 1000)) {
                        topUnusua=0;
                        topStop=2;
                        MachineSum = MachineSum - 1;
                    }
                }
            }else{
                topStop++;
                topStop++;
            }

            if (topHealth>1){ QualityType=1; }

            if (topwarning>0){ topFalut=0;QualityType=2; }

            if (topFalut>0){ QualityType=3; }

            if (topStop>1){ QualityType=null; }

            if (topUnusua>0){ QualityType=0; }

            ///////////////////
            if ("D061FARF03".equals(lineName)||"D061FARF04".equals(lineName)){
                QualityType=1;
            }
            ///////////////
            AVIQualityTypeMap.put(lineName+"TOP1",QualityType);
            AVIQualityTypeMap.put(lineName+"TOP2",QualityType);
        }
        return AVIQualityTypeMap;
    }

    //AVI过杀漏检
    public  List<AVIOverkillandEscape>  AVIOverkillandEscape(Integer Htime,String FloorName, Date scheduleTime){

        List<AVIOverkillandEscape> AVIOverkillandEscapeData=new ArrayList<>();
        List<String> TIME_SLOT =new ArrayList<>();
        String DayType="";
        if (timeUtility.Timequantum("08:30", "20:30")) {
            DayType="Day";
            TIME_SLOT =DayTIME_SLOT();
        }else {
            DayType="Night";
            TIME_SLOT =NightTIME_SLOT();
        }
        List<AutoFloor_AVI_TimeSlot> autoFloor_avi_timeSlots = overkillandTimeSlot(DayType);
        /*获取当前时间*/
        List<java.sql.Date> sqlList = taskService.Schedule();
        /*当前日期减1*/
        Date schedule = sqlList.get(0);
        /*当前日期减1*/
        Date YesterdayDate = sqlList.get(1);
        /*当前日期加1*/
        Date TomorrowDate = sqlList.get(2);
        /*当前日期减2*/
        Date BeforedayDate = sqlList.get(5);
        String AviTime = Aviformat.format(schedule);  //當天
        String AviTomorrowDate = Aviformat.format(TomorrowDate); //當前日期+1天
        String AviYesterdayDate = Aviformat.format(YesterdayDate); //當前日期-1天
        String AviBeforedayDate = Aviformat.format(BeforedayDate); //當前日期-2天
        for (int i = 0; i < autoFloor_avi_timeSlots.size(); i++) {
            AutoFloor_AVI_TimeSlot AVI_TimeSlot = autoFloor_avi_timeSlots.get(i);
            String Time = TIME_SLOT.get(i);
            String aviEndDate = AVI_TimeSlot.getAVIEndDate();
            String aviStartDate = AVI_TimeSlot.getAVIStartDate();
            String AVISTARTTIME="";
            String AVIENDTIME="";
            if (timeUtility.Timequantum("20:30", "23:59")) {
                switch (Time){
                    case "20:30":
                    case "22:30":
                    case "21:30":
                        AVISTARTTIME=AviYesterdayDate+aviStartDate;
                        AVIENDTIME=AviYesterdayDate+aviEndDate;
                        break;
                    case "23:30":
                        AVISTARTTIME=AviYesterdayDate+aviStartDate;
                        AVIENDTIME=AviTime+aviEndDate;
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
                        AVISTARTTIME=AviTime+aviStartDate;
                        AVIENDTIME=AviTime+aviEndDate;
                        break;
                }
            }
            else if (timeUtility.Timequantum("00:00", "08:30")) {
                switch (Time){
                    case "20:30":
                    case "22:30":
                    case "21:30":
                        AVISTARTTIME=AviBeforedayDate+aviStartDate;
                        AVIENDTIME=AviBeforedayDate+aviEndDate;
                        break;
                    case "23:30":
                        AVISTARTTIME=AviBeforedayDate+aviStartDate;
                        AVIENDTIME=AviYesterdayDate+aviEndDate;
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
                        AVISTARTTIME=AviYesterdayDate+aviStartDate;
                        AVIENDTIME=AviYesterdayDate+aviEndDate;
                        break;
                }
            }
            else {
                AVISTARTTIME=AviYesterdayDate+aviStartDate;
                AVIENDTIME=AviYesterdayDate+aviEndDate;
            }

            List<AutoFloor_AVI_Tracking> AVIUKState = autoFloor_avi_trackingMapper.selectAVIUKState(FloorName, AVISTARTTIME, AVIENDTIME);
            List<IPD_SFC_INFO_VMI4> ipd_sfc_info_vmi4s = ipd_sfc_info_vmi4Mapper.selectINFO_VMI4Data(FloorName,AVISTARTTIME, AVIENDTIME);
            AVIOverkillandEscape aviOverkillandEscape=new AVIOverkillandEscape();
            //过杀
            int OverkillNum=0;
            // 漏检
            int EscapeNum=0;
            //投入
            int PutNum=0;
            PutNum=AVIUKState.size();
            //NG 数量
            int NGNum=0;
            for (AutoFloor_AVI_Tracking autoFloor_avi_tracking : AVIUKState) {
                String uk = autoFloor_avi_tracking.getUk();
                if (!"0".equals(uk)) {
                    NGNum++;
                }
            }
            int SnEq=0;
            int VMI4Sum=0;
            for (IPD_SFC_INFO_VMI4 ipd_sfc_info_vmi4 : ipd_sfc_info_vmi4s) {
                String vmi4lineName = ipd_sfc_info_vmi4.getLineName();
                String serialNumber = ipd_sfc_info_vmi4.getSerialNumber();
                String groupName = ipd_sfc_info_vmi4.getGroupName();
                String errorFalg = ipd_sfc_info_vmi4.getErrorFalg();
                if ("VMI4".equals(groupName)){
                    if ("1".equals(errorFalg)){
                        VMI4Sum++;
                        for (AutoFloor_AVI_Tracking autoFloor_avi_tracking : AVIUKState) {
                            String trackinglineName = autoFloor_avi_tracking.getLineName();
                            String uk = autoFloor_avi_tracking.getUk();
                            String sn = autoFloor_avi_tracking.getSn();
                            if (!"0".equals(uk)) {
                                if (trackinglineName != null && trackinglineName.equals(vmi4lineName)) {
                                    if (sn != null && sn.equals(serialNumber)) {
                                        SnEq++;
                                    }
                                }
                            }
                        }
                    }
                }else if ("CA".equals(groupName)){
                    if ("1".equals(errorFalg)){
                        for (AutoFloor_AVI_Tracking autoFloor_avi_tracking : AVIUKState) {
                            String trackinglineName = autoFloor_avi_tracking.getLineName();
                            String uk = autoFloor_avi_tracking.getUk();
                            String sn = autoFloor_avi_tracking.getSn();
                            if ("0".equals(uk)) {
                                if (trackinglineName != null && trackinglineName.equals(vmi4lineName)) {
                                    if (sn != null && sn.equals(serialNumber)) {
                                        EscapeNum++;
                                    }
                                }
                            }
                        }
                    }
                }

            }


            OverkillNum= NGNum-(SnEq);
            double OverkillRate=0;
            double EscapeNumRate=0;
            if (PutNum!=0){
                BigDecimal OverkillBig=new BigDecimal((OverkillNum/(double)PutNum)*100);
                OverkillRate=OverkillBig.setScale(1,BigDecimal.ROUND_HALF_UP).doubleValue();

                BigDecimal EscapeNumBig=new BigDecimal((EscapeNum/(double)PutNum)*100);
                EscapeNumRate=EscapeNumBig.setScale(1,BigDecimal.ROUND_HALF_UP).doubleValue();

            }

            aviOverkillandEscape.setAVIEscapeRate(EscapeNumRate);
            aviOverkillandEscape.setAVIOverkillRate(OverkillRate);
            aviOverkillandEscape.setTIME_SLOT(Time);
            AVIOverkillandEscapeData.add(aviOverkillandEscape);
        }
        return AVIOverkillandEscapeData;
    }

    //不良現象
    public List<AVIBadnessthyAnalyse> aviUnhealthyAnalyseData(List<AutoFloor_Target> LineList, String FloorName, String StartDateStr, String EndDateStr){

        List<AutoFloor_AVI_TOP1> autoFloor_avi_top1List = autoFloor_avi_top1Mapper.selectByLineTop1Data(FloorName, StartDateStr, EndDateStr);
        List<AutoFloor_AVI_TOP2> autoFloor_avi_top2List = autoFloor_avi_top2Mapper.selectByLineTop2Data(FloorName, StartDateStr, EndDateStr);

        Integer missingPasteAll =0;//漏貼
        Integer residualGlueAll =0;//殘膠
        Integer deviationAll=0;//偏位
        Integer breakageAll=0;//破損
        Integer residualTinAll =0;//殘錫
        Integer swellingAll=0;//鼓包
        Integer foldAll=0;//褶皺
        Integer leakage_GlueAll =0;//漏點膠
        Integer dirtyAll=0;//髒污/異物
        Integer snOmissionAll=0;//Sn漏印
        Integer vacancyAll=0;//空位
        Integer failvalue=0;

        for (AutoFloor_Target autoFloor_target : LineList) {
            String lineName = autoFloor_target.getLineName();

            for (AutoFloor_AVI_TOP1 autoFloor_avi_top1 : autoFloor_avi_top1List) {
                String lineNameTOP1 = autoFloor_avi_top1.getLineName();
                if (lineName.equals(lineNameTOP1)){
                    Integer missingPaste =0;//漏貼
                    Integer residualGlue =0;//殘膠
                    Integer deviation=0;//偏位
                    Integer breakage=0;//破損
                    Integer residualTin =0;//殘錫
                    Integer swelling=0;//鼓包
                    Integer fold=0;//褶皺
                    Integer leakage_Glue =0;//漏點膠
                    Integer dirty=0;//髒污/異物
                    Integer snOmission=0;//Sn漏印
                    Integer vacancy=0;//空位
                    AVIUnhealthList aviUnhealthListtop1 = top1Unusual(autoFloor_avi_top1.getP1(), missingPaste, residualGlue, deviation, breakage, residualTin, swelling, fold, leakage_Glue, dirty, snOmission, vacancy);
                    missingPasteAll+= aviUnhealthListtop1.getMissingPaste();
                    residualGlueAll +=aviUnhealthListtop1.getResidualGlue();
                    deviationAll+=aviUnhealthListtop1.getDeviation();
                    breakageAll+=aviUnhealthListtop1.getBreakage();
                    residualTinAll+=aviUnhealthListtop1.getResidualTin();
                    swellingAll+=aviUnhealthListtop1.getSwelling();
                    foldAll+=aviUnhealthListtop1.getFold();
                    leakage_GlueAll+=aviUnhealthListtop1.getLeakage_Glue();
                    dirtyAll+=aviUnhealthListtop1.getDirty();
                    snOmissionAll+=aviUnhealthListtop1.getSnOmission();
                    vacancyAll+=aviUnhealthListtop1.getVacancy();

                    AVIUnhealthList aviUnhealthListtop2 =  top1Unusual(autoFloor_avi_top1.getP2() ,missingPaste,residualGlue,deviation,breakage,residualTin,swelling,fold,leakage_Glue,dirty,snOmission,vacancy);
                    missingPasteAll+= aviUnhealthListtop2.getMissingPaste();
                    residualGlueAll +=aviUnhealthListtop2.getResidualGlue();
                    deviationAll+=aviUnhealthListtop2.getDeviation();
                    breakageAll+=aviUnhealthListtop2.getBreakage();
                    residualTinAll+=aviUnhealthListtop2.getResidualTin();
                    swellingAll+=aviUnhealthListtop2.getSwelling();
                    foldAll+=aviUnhealthListtop2.getFold();
                    leakage_GlueAll+=aviUnhealthListtop2.getLeakage_Glue();
                    dirtyAll+=aviUnhealthListtop2.getDirty();
                    snOmissionAll+=aviUnhealthListtop2.getSnOmission();
                    vacancyAll+=aviUnhealthListtop2.getVacancy();

                    AVIUnhealthList aviUnhealthListtop3 = top1Unusual(autoFloor_avi_top1.getP3(), missingPaste, residualGlue, deviation, breakage, residualTin, swelling, fold, leakage_Glue, dirty, snOmission, vacancy);
                    missingPasteAll+= aviUnhealthListtop3.getMissingPaste();
                    residualGlueAll +=aviUnhealthListtop3.getResidualGlue();
                    deviationAll+=aviUnhealthListtop3.getDeviation();
                    breakageAll+=aviUnhealthListtop3.getBreakage();
                    residualTinAll+=aviUnhealthListtop3.getResidualTin();
                    swellingAll+=aviUnhealthListtop3.getSwelling();
                    foldAll+=aviUnhealthListtop3.getFold();
                    leakage_GlueAll+=aviUnhealthListtop3.getLeakage_Glue();
                    dirtyAll+=aviUnhealthListtop3.getDirty();
                    snOmissionAll+=aviUnhealthListtop3.getSnOmission();
                    vacancyAll+=aviUnhealthListtop3.getVacancy();


                    AVIUnhealthList aviUnhealthListtop4 =top1Unusual(autoFloor_avi_top1.getP4() ,missingPaste,residualGlue,deviation,breakage,residualTin,swelling,fold,leakage_Glue,dirty,snOmission,vacancy);
                    missingPasteAll+= aviUnhealthListtop4.getMissingPaste();
                    residualGlueAll+=aviUnhealthListtop4.getResidualGlue();
                    deviationAll+=aviUnhealthListtop4.getDeviation();
                    breakageAll+=aviUnhealthListtop4.getBreakage();
                    residualTinAll+=aviUnhealthListtop4.getResidualTin();
                    swellingAll+=aviUnhealthListtop4.getSwelling();
                    foldAll+=aviUnhealthListtop4.getFold();
                    leakage_GlueAll+=aviUnhealthListtop4.getLeakage_Glue();
                    dirtyAll+=aviUnhealthListtop4.getDirty();
                    snOmissionAll+=aviUnhealthListtop4.getSnOmission();
                    vacancyAll+=aviUnhealthListtop4.getVacancy();

                    AVIUnhealthList aviUnhealthListtop5 =top1Unusual(autoFloor_avi_top1.getP5() ,missingPaste,residualGlue,deviation,breakage,residualTin,swelling,fold,leakage_Glue,dirty,snOmission,vacancy);
                    missingPasteAll+= aviUnhealthListtop5.getMissingPaste();
                    residualGlueAll +=aviUnhealthListtop5.getResidualGlue();
                    deviationAll+=aviUnhealthListtop5.getDeviation();
                    breakageAll+=aviUnhealthListtop5.getBreakage();
                    residualTinAll+=aviUnhealthListtop5.getResidualTin();
                    swellingAll+=aviUnhealthListtop5.getSwelling();
                    foldAll+=aviUnhealthListtop5.getFold();
                    leakage_GlueAll+=aviUnhealthListtop5.getLeakage_Glue();
                    dirtyAll+=aviUnhealthListtop5.getDirty();
                    snOmissionAll+=aviUnhealthListtop5.getSnOmission();
                    vacancyAll+=aviUnhealthListtop5.getVacancy();

                    AVIUnhealthList aviUnhealthListtop6 =top1Unusual(autoFloor_avi_top1.getP6() ,missingPaste,residualGlue,deviation,breakage,residualTin,swelling,fold,leakage_Glue,dirty,snOmission,vacancy);
                    missingPasteAll+= aviUnhealthListtop6.getMissingPaste();
                    residualGlueAll +=aviUnhealthListtop6.getResidualGlue();
                    deviationAll+=aviUnhealthListtop6.getDeviation();
                    breakageAll+=aviUnhealthListtop6.getBreakage();
                    residualTinAll+=aviUnhealthListtop6.getResidualTin();
                    swellingAll+=aviUnhealthListtop6.getSwelling();
                    foldAll+=aviUnhealthListtop6.getFold();
                    leakage_GlueAll+=aviUnhealthListtop6.getLeakage_Glue();
                    dirtyAll+=aviUnhealthListtop6.getDirty();
                    snOmissionAll+=aviUnhealthListtop6.getSnOmission();
                    vacancyAll+=aviUnhealthListtop6.getVacancy();

                    AVIUnhealthList aviUnhealthListtop7 =top1Unusual(autoFloor_avi_top1.getP7() ,missingPaste,residualGlue,deviation,breakage,residualTin,swelling,fold,leakage_Glue,dirty,snOmission,vacancy);
                    missingPasteAll+= aviUnhealthListtop7.getMissingPaste();
                    residualGlueAll +=aviUnhealthListtop7.getResidualGlue();
                    deviationAll+=aviUnhealthListtop7.getDeviation();
                    breakageAll+=aviUnhealthListtop7.getBreakage();
                    residualTinAll+=aviUnhealthListtop7.getResidualTin();
                    swellingAll+=aviUnhealthListtop7.getSwelling();
                    foldAll+=aviUnhealthListtop7.getFold();
                    leakage_GlueAll+=aviUnhealthListtop7.getLeakage_Glue();
                    dirtyAll+=aviUnhealthListtop7.getDirty();
                    snOmissionAll+=aviUnhealthListtop7.getSnOmission();
                    vacancyAll+=aviUnhealthListtop7.getVacancy();

                    AVIUnhealthList aviUnhealthListtop8 =top1Unusual(autoFloor_avi_top1.getP8() ,missingPaste,residualGlue,deviation,breakage,residualTin,swelling,fold,leakage_Glue,dirty,snOmission,vacancy);
                    missingPasteAll+= aviUnhealthListtop8.getMissingPaste();
                    residualGlueAll +=aviUnhealthListtop8.getResidualGlue();
                    deviationAll+=aviUnhealthListtop8.getDeviation();
                    breakageAll+=aviUnhealthListtop8.getBreakage();
                    residualTinAll+=aviUnhealthListtop8.getResidualTin();
                    swellingAll+=aviUnhealthListtop8.getSwelling();
                    foldAll+=aviUnhealthListtop8.getFold();
                    leakage_GlueAll+=aviUnhealthListtop8.getLeakage_Glue();
                    dirtyAll+=aviUnhealthListtop8.getDirty();
                    snOmissionAll+=aviUnhealthListtop8.getSnOmission();
                    vacancyAll+=aviUnhealthListtop8.getVacancy();

                    AVIUnhealthList aviUnhealthListtop9 =top1Unusual(autoFloor_avi_top1.getP9() ,missingPaste,residualGlue,deviation,breakage,residualTin,swelling,fold,leakage_Glue,dirty,snOmission,vacancy);
                    missingPasteAll+= aviUnhealthListtop9.getMissingPaste();
                    residualGlueAll +=aviUnhealthListtop9.getResidualGlue();
                    deviationAll+=aviUnhealthListtop9.getDeviation();
                    breakageAll+=aviUnhealthListtop9.getBreakage();
                    residualTinAll+=aviUnhealthListtop9.getResidualTin();
                    swellingAll+=aviUnhealthListtop9.getSwelling();
                    foldAll+=aviUnhealthListtop9.getFold();
                    leakage_GlueAll+=aviUnhealthListtop9.getLeakage_Glue();
                    dirtyAll+=aviUnhealthListtop9.getDirty();
                    snOmissionAll+=aviUnhealthListtop9.getSnOmission();
                    vacancyAll+=aviUnhealthListtop9.getVacancy();

                    AVIUnhealthList aviUnhealthListtop10 =top1Unusual(autoFloor_avi_top1.getP10(),missingPaste,residualGlue,deviation,breakage,residualTin,swelling,fold,leakage_Glue,dirty,snOmission,vacancy);
                    missingPasteAll+= aviUnhealthListtop10.getMissingPaste();
                    residualGlueAll +=aviUnhealthListtop10.getResidualGlue();
                    deviationAll+=aviUnhealthListtop10.getDeviation();
                    breakageAll+=aviUnhealthListtop10.getBreakage();
                    residualTinAll+=aviUnhealthListtop10.getResidualTin();
                    swellingAll+=aviUnhealthListtop10.getSwelling();
                    foldAll+=aviUnhealthListtop10.getFold();
                    leakage_GlueAll+=aviUnhealthListtop10.getLeakage_Glue();
                    dirtyAll+=aviUnhealthListtop10.getDirty();
                    snOmissionAll+=aviUnhealthListtop10.getSnOmission();
                    vacancyAll+=aviUnhealthListtop10.getVacancy();

                    AVIUnhealthList aviUnhealthListtop11 =top1Unusual(autoFloor_avi_top1.getP11(),missingPaste,residualGlue,deviation,breakage,residualTin,swelling,fold,leakage_Glue,dirty,snOmission,vacancy);
                    missingPasteAll+= aviUnhealthListtop11.getMissingPaste();
                    residualGlueAll +=aviUnhealthListtop11.getResidualGlue();
                    deviationAll+=aviUnhealthListtop11.getDeviation();
                    breakageAll+=aviUnhealthListtop11.getBreakage();
                    residualTinAll+=aviUnhealthListtop11.getResidualTin();
                    swellingAll+=aviUnhealthListtop11.getSwelling();
                    foldAll+=aviUnhealthListtop11.getFold();
                    leakage_GlueAll+=aviUnhealthListtop11.getLeakage_Glue();
                    dirtyAll+=aviUnhealthListtop11.getDirty();
                    snOmissionAll+=aviUnhealthListtop11.getSnOmission();
                    vacancyAll+=aviUnhealthListtop11.getVacancy();

                    AVIUnhealthList aviUnhealthListtop12 =top1Unusual(autoFloor_avi_top1.getP12(),missingPaste,residualGlue,deviation,breakage,residualTin,swelling,fold,leakage_Glue,dirty,snOmission,vacancy);
                    missingPasteAll+= aviUnhealthListtop12.getMissingPaste();
                    residualGlueAll +=aviUnhealthListtop12.getResidualGlue();
                    deviationAll+=aviUnhealthListtop12.getDeviation();
                    breakageAll+=aviUnhealthListtop12.getBreakage();
                    residualTinAll+=aviUnhealthListtop12.getResidualTin();
                    swellingAll+=aviUnhealthListtop12.getSwelling();
                    foldAll+=aviUnhealthListtop12.getFold();
                    leakage_GlueAll+=aviUnhealthListtop12.getLeakage_Glue();
                    dirtyAll+=aviUnhealthListtop12.getDirty();
                    snOmissionAll+=aviUnhealthListtop12.getSnOmission();
                    vacancyAll+=aviUnhealthListtop12.getVacancy();

                    AVIUnhealthList aviUnhealthListtop13 =top1Unusual(autoFloor_avi_top1.getP13(),missingPaste,residualGlue,deviation,breakage,residualTin,swelling,fold,leakage_Glue,dirty,snOmission,vacancy);
                    missingPasteAll+= aviUnhealthListtop13.getMissingPaste();
                    residualGlueAll +=aviUnhealthListtop13.getResidualGlue();
                    deviationAll+=aviUnhealthListtop13.getDeviation();
                    breakageAll+=aviUnhealthListtop13.getBreakage();
                    residualTinAll+=aviUnhealthListtop13.getResidualTin();
                    swellingAll+=aviUnhealthListtop13.getSwelling();
                    foldAll+=aviUnhealthListtop13.getFold();
                    leakage_GlueAll+=aviUnhealthListtop13.getLeakage_Glue();
                    dirtyAll+=aviUnhealthListtop13.getDirty();
                    snOmissionAll+=aviUnhealthListtop13.getSnOmission();
                    vacancyAll+=aviUnhealthListtop13.getVacancy();

                    AVIUnhealthList aviUnhealthListtop14 =top1Unusual(autoFloor_avi_top1.getP14(),missingPaste,residualGlue,deviation,breakage,residualTin,swelling,fold,leakage_Glue,dirty,snOmission,vacancy);
                    missingPasteAll+= aviUnhealthListtop14.getMissingPaste();
                    residualGlueAll +=aviUnhealthListtop14.getResidualGlue();
                    deviationAll+=aviUnhealthListtop14.getDeviation();
                    breakageAll+=aviUnhealthListtop14.getBreakage();
                    residualTinAll+=aviUnhealthListtop14.getResidualTin();
                    swellingAll+=aviUnhealthListtop14.getSwelling();
                    foldAll+=aviUnhealthListtop14.getFold();
                    leakage_GlueAll+=aviUnhealthListtop14.getLeakage_Glue();
                    dirtyAll+=aviUnhealthListtop14.getDirty();
                    snOmissionAll+=aviUnhealthListtop14.getSnOmission();
                    vacancyAll+=aviUnhealthListtop14.getVacancy();

                    AVIUnhealthList aviUnhealthListtop15 =top1Unusual(autoFloor_avi_top1.getP15(),missingPaste,residualGlue,deviation,breakage,residualTin,swelling,fold,leakage_Glue,dirty,snOmission,vacancy);
                    missingPasteAll+= aviUnhealthListtop15.getMissingPaste();
                    residualGlueAll +=aviUnhealthListtop15.getResidualGlue();
                    deviationAll+=aviUnhealthListtop15.getDeviation();
                    breakageAll+=aviUnhealthListtop15.getBreakage();
                    residualTinAll+=aviUnhealthListtop15.getResidualTin();
                    swellingAll+=aviUnhealthListtop15.getSwelling();
                    foldAll+=aviUnhealthListtop15.getFold();
                    leakage_GlueAll+=aviUnhealthListtop15.getLeakage_Glue();
                    dirtyAll+=aviUnhealthListtop15.getDirty();
                    snOmissionAll+=aviUnhealthListtop15.getSnOmission();
                    vacancyAll+=aviUnhealthListtop15.getVacancy();

                    AVIUnhealthList aviUnhealthListtop16 = top1Unusual(autoFloor_avi_top1.getP16(),missingPaste,residualGlue,deviation,breakage,residualTin,swelling,fold,leakage_Glue,dirty,snOmission,vacancy);
                    missingPasteAll+= aviUnhealthListtop16.getMissingPaste();
                    residualGlueAll +=aviUnhealthListtop16.getResidualGlue();
                    deviationAll+=aviUnhealthListtop16.getDeviation();
                    breakageAll+=aviUnhealthListtop16.getBreakage();
                    residualTinAll+=aviUnhealthListtop16.getResidualTin();
                    swellingAll+=aviUnhealthListtop16.getSwelling();
                    foldAll+=aviUnhealthListtop16.getFold();
                    leakage_GlueAll+=aviUnhealthListtop16.getLeakage_Glue();
                    dirtyAll+=aviUnhealthListtop16.getDirty();
                    snOmissionAll+=aviUnhealthListtop16.getSnOmission();
                    vacancyAll+=aviUnhealthListtop16.getVacancy();

                    AVIUnhealthList aviUnhealthListtop17 = top1Unusual(autoFloor_avi_top1.getP17(),missingPaste,residualGlue,deviation,breakage,residualTin,swelling,fold,leakage_Glue,dirty,snOmission,vacancy);
                    missingPasteAll+= aviUnhealthListtop17.getMissingPaste();
                    residualGlueAll +=aviUnhealthListtop17.getResidualGlue();
                    deviationAll+=aviUnhealthListtop17.getDeviation();
                    breakageAll+=aviUnhealthListtop17.getBreakage();
                    residualTinAll+=aviUnhealthListtop17.getResidualTin();
                    swellingAll+=aviUnhealthListtop17.getSwelling();
                    foldAll+=aviUnhealthListtop17.getFold();
                    leakage_GlueAll+=aviUnhealthListtop17.getLeakage_Glue();
                    dirtyAll+=aviUnhealthListtop17.getDirty();
                    snOmissionAll+=aviUnhealthListtop17.getSnOmission();
                    vacancyAll+=aviUnhealthListtop17.getVacancy();

                    AVIUnhealthList aviUnhealthListtop18 = top1Unusual(autoFloor_avi_top1.getP18(),missingPaste,residualGlue,deviation,breakage,residualTin,swelling,fold,leakage_Glue,dirty,snOmission,vacancy);
                    missingPasteAll+= aviUnhealthListtop18.getMissingPaste();
                    residualGlueAll +=aviUnhealthListtop18.getResidualGlue();
                    deviationAll+=aviUnhealthListtop18.getDeviation();
                    breakageAll+=aviUnhealthListtop18.getBreakage();
                    residualTinAll+=aviUnhealthListtop18.getResidualTin();
                    swellingAll+=aviUnhealthListtop18.getSwelling();
                    foldAll+=aviUnhealthListtop18.getFold();
                    leakage_GlueAll+=aviUnhealthListtop18.getLeakage_Glue();
                    dirtyAll+=aviUnhealthListtop18.getDirty();
                    snOmissionAll+=aviUnhealthListtop18.getSnOmission();
                    vacancyAll+=aviUnhealthListtop18.getVacancy();

                    AVIUnhealthList aviUnhealthListtop19 = top1Unusual(autoFloor_avi_top1.getP19(),missingPaste,residualGlue,deviation,breakage,residualTin,swelling,fold,leakage_Glue,dirty,snOmission,vacancy);
                    missingPasteAll+= aviUnhealthListtop19.getMissingPaste();
                    residualGlueAll +=aviUnhealthListtop19.getResidualGlue();
                    deviationAll+=aviUnhealthListtop19.getDeviation();
                    breakageAll+=aviUnhealthListtop19.getBreakage();
                    residualTinAll+=aviUnhealthListtop19.getResidualTin();
                    swellingAll+=aviUnhealthListtop19.getSwelling();
                    foldAll+=aviUnhealthListtop19.getFold();
                    leakage_GlueAll+=aviUnhealthListtop19.getLeakage_Glue();
                    dirtyAll+=aviUnhealthListtop19.getDirty();
                    snOmissionAll+=aviUnhealthListtop19.getSnOmission();
                    vacancyAll+=aviUnhealthListtop19.getVacancy();

                    AVIUnhealthList aviUnhealthListtop20 = top1Unusual(autoFloor_avi_top1.getP20(),missingPaste,residualGlue,deviation,breakage,residualTin,swelling,fold,leakage_Glue,dirty,snOmission,vacancy);
                    missingPasteAll+= aviUnhealthListtop20.getMissingPaste();
                    residualGlueAll +=aviUnhealthListtop20.getResidualGlue();
                    deviationAll+=aviUnhealthListtop20.getDeviation();
                    breakageAll+=aviUnhealthListtop20.getBreakage();
                    residualTinAll+=aviUnhealthListtop20.getResidualTin();
                    swellingAll+=aviUnhealthListtop20.getSwelling();
                    foldAll+=aviUnhealthListtop20.getFold();
                    leakage_GlueAll+=aviUnhealthListtop20.getLeakage_Glue();
                    dirtyAll+=aviUnhealthListtop20.getDirty();
                    snOmissionAll+=aviUnhealthListtop20.getSnOmission();
                    vacancyAll+=aviUnhealthListtop20.getVacancy();

                    AVIUnhealthList aviUnhealthListtop21  = top1Unusual(autoFloor_avi_top1.getP21(), missingPaste, residualGlue, deviation, breakage, residualTin, swelling, fold, leakage_Glue, dirty, snOmission, vacancy);
                    missingPasteAll+= aviUnhealthListtop21.getMissingPaste();
                    residualGlueAll +=aviUnhealthListtop21.getResidualGlue();
                    deviationAll+=aviUnhealthListtop21.getDeviation();
                    breakageAll+=aviUnhealthListtop21.getBreakage();
                    residualTinAll+=aviUnhealthListtop21.getResidualTin();
                    swellingAll+=aviUnhealthListtop21.getSwelling();
                    foldAll+=aviUnhealthListtop21.getFold();
                    leakage_GlueAll+=aviUnhealthListtop21.getLeakage_Glue();
                    dirtyAll+=aviUnhealthListtop21.getDirty();
                    snOmissionAll+=aviUnhealthListtop21.getSnOmission();
                    vacancyAll+=aviUnhealthListtop21.getVacancy();

                    AVIUnhealthList aviUnhealthListtop22  = top1Unusual(autoFloor_avi_top1.getP22(), missingPaste, residualGlue, deviation, breakage, residualTin, swelling, fold, leakage_Glue, dirty, snOmission, vacancy);
                    missingPasteAll+= aviUnhealthListtop22.getMissingPaste();
                    residualGlueAll +=aviUnhealthListtop22.getResidualGlue();
                    deviationAll+=aviUnhealthListtop22.getDeviation();
                    breakageAll+=aviUnhealthListtop22.getBreakage();
                    residualTinAll+=aviUnhealthListtop22.getResidualTin();
                    swellingAll+=aviUnhealthListtop22.getSwelling();
                    foldAll+=aviUnhealthListtop22.getFold();
                    leakage_GlueAll+=aviUnhealthListtop22.getLeakage_Glue();
                    dirtyAll+=aviUnhealthListtop22.getDirty();
                    snOmissionAll+=aviUnhealthListtop22.getSnOmission();
                    vacancyAll+=aviUnhealthListtop22.getVacancy();

                    AVIUnhealthList aviUnhealthListtop23  = top1Unusual(autoFloor_avi_top1.getP23(), missingPaste, residualGlue, deviation, breakage, residualTin, swelling, fold, leakage_Glue, dirty, snOmission, vacancy);
                    missingPasteAll+= aviUnhealthListtop23.getMissingPaste();
                    residualGlueAll +=aviUnhealthListtop23.getResidualGlue();
                    deviationAll+=aviUnhealthListtop23.getDeviation();
                    breakageAll+=aviUnhealthListtop23.getBreakage();
                    residualTinAll+=aviUnhealthListtop23.getResidualTin();
                    swellingAll+=aviUnhealthListtop23.getSwelling();
                    foldAll+=aviUnhealthListtop23.getFold();
                    leakage_GlueAll+=aviUnhealthListtop23.getLeakage_Glue();
                    dirtyAll+=aviUnhealthListtop23.getDirty();
                    snOmissionAll+=aviUnhealthListtop23.getSnOmission();
                    vacancyAll+=aviUnhealthListtop23.getVacancy();

                    AVIUnhealthList aviUnhealthListtop24  = top1Unusual(autoFloor_avi_top1.getP24(), missingPaste, residualGlue, deviation, breakage, residualTin, swelling, fold, leakage_Glue, dirty, snOmission, vacancy);
                    missingPasteAll+= aviUnhealthListtop24.getMissingPaste();
                    residualGlueAll +=aviUnhealthListtop24.getResidualGlue();
                    deviationAll+=aviUnhealthListtop24.getDeviation();
                    breakageAll+=aviUnhealthListtop24.getBreakage();
                    residualTinAll+=aviUnhealthListtop24.getResidualTin();
                    swellingAll+=aviUnhealthListtop24.getSwelling();
                    foldAll+=aviUnhealthListtop24.getFold();
                    leakage_GlueAll+=aviUnhealthListtop24.getLeakage_Glue();
                    dirtyAll+=aviUnhealthListtop24.getDirty();
                    snOmissionAll+=aviUnhealthListtop24.getSnOmission();
                    vacancyAll+=aviUnhealthListtop24.getVacancy();

                    AVIUnhealthList aviUnhealthListtop25  = top1Unusual(autoFloor_avi_top1.getP25(), missingPaste, residualGlue, deviation, breakage, residualTin, swelling, fold, leakage_Glue, dirty, snOmission, vacancy);
                    missingPasteAll+= aviUnhealthListtop25.getMissingPaste();
                    residualGlueAll +=aviUnhealthListtop25.getResidualGlue();
                    deviationAll+=aviUnhealthListtop25.getDeviation();
                    breakageAll+=aviUnhealthListtop25.getBreakage();
                    residualTinAll+=aviUnhealthListtop25.getResidualTin();
                    swellingAll+=aviUnhealthListtop25.getSwelling();
                    foldAll+=aviUnhealthListtop25.getFold();
                    leakage_GlueAll+=aviUnhealthListtop25.getLeakage_Glue();
                    dirtyAll+=aviUnhealthListtop25.getDirty();
                    snOmissionAll+=aviUnhealthListtop25.getSnOmission();
                    vacancyAll+=aviUnhealthListtop25.getVacancy();

                    AVIUnhealthList aviUnhealthListtop26  = top1Unusual(autoFloor_avi_top1.getP26(), missingPaste, residualGlue, deviation, breakage, residualTin, swelling, fold, leakage_Glue, dirty, snOmission, vacancy);
                    missingPasteAll+= aviUnhealthListtop26.getMissingPaste();
                    residualGlueAll +=aviUnhealthListtop26.getResidualGlue();
                    deviationAll+=aviUnhealthListtop26.getDeviation();
                    breakageAll+=aviUnhealthListtop26.getBreakage();
                    residualTinAll+=aviUnhealthListtop26.getResidualTin();
                    swellingAll+=aviUnhealthListtop26.getSwelling();
                    foldAll+=aviUnhealthListtop26.getFold();
                    leakage_GlueAll+=aviUnhealthListtop26.getLeakage_Glue();
                    dirtyAll+=aviUnhealthListtop26.getDirty();
                    snOmissionAll+=aviUnhealthListtop26.getSnOmission();
                    vacancyAll+=aviUnhealthListtop26.getVacancy();
                }

            }
            for (AutoFloor_AVI_TOP2 autoFloor_avi_top2 : autoFloor_avi_top2List) {
                String lineNameTOP2 = autoFloor_avi_top2.getLineName();
                if (lineName.equals(lineNameTOP2)){
                    residualGlueAll += top2Fail(autoFloor_avi_top2.getP1());
                    residualGlueAll += top2Fail(autoFloor_avi_top2.getP2());
                    residualGlueAll += top2Fail(autoFloor_avi_top2.getP3());
                    residualGlueAll += top2Fail(autoFloor_avi_top2.getP4());
                    residualGlueAll += top2Fail(autoFloor_avi_top2.getP5());
                    residualGlueAll += top2Fail(autoFloor_avi_top2.getP6());
                    residualGlueAll += top2Fail(autoFloor_avi_top2.getP7());
                    residualGlueAll += top2Fail(autoFloor_avi_top2.getP8());
                    residualGlueAll += top2Fail(autoFloor_avi_top2.getP9());
                    residualGlueAll += top2Fail(autoFloor_avi_top2.getP10());
                    residualGlueAll += top2Fail(autoFloor_avi_top2.getP11());
                    residualGlueAll += top2Fail(autoFloor_avi_top2.getP12());
                    residualGlueAll += top2Fail(autoFloor_avi_top2.getP13());
                    residualGlueAll += top2Fail(autoFloor_avi_top2.getP14());
                    residualGlueAll += top2Fail(autoFloor_avi_top2.getP15());
                    residualGlueAll += top2Fail(autoFloor_avi_top2.getP16());
                    residualGlueAll += top2Fail(autoFloor_avi_top2.getP17());
                    residualGlueAll += top2Fail(autoFloor_avi_top2.getP18());
                    residualGlueAll += top2Fail(autoFloor_avi_top2.getP19());
                    residualGlueAll += top2Fail(autoFloor_avi_top2.getP20());
                    residualGlueAll += top2Fail(autoFloor_avi_top2.getP21());
                }
            }
        }
        Integer allSum=missingPasteAll+residualGlueAll+deviationAll+breakageAll+residualTinAll+swellingAll+foldAll+leakage_GlueAll+dirtyAll+snOmissionAll+vacancyAll;
        List<AVIBadnessthyAnalyse> aviBadnessthyAnalyseList=new ArrayList<>();
        double missingPasteRate=0;
        double residualGlueRate=0;
        double deviationRate=0;
        double breakageRate=0;
        double residualTinRate=0;
        double swellingRate=0;
        double foldRate=0;
        double leakage_GlueRate=0;
        double dirtyRate=0;
        double snOmissionRate=0;
        double vacancyRate=0;
        if (allSum!=0)
        {
            missingPasteRate =  new BigDecimal((missingPasteAll / (double)allSum) * 100).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            residualGlueRate =new BigDecimal((residualGlueAll/(double)allSum)*100).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
            deviationRate =new BigDecimal((deviationAll/(double)allSum)*100).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
            breakageRate =new BigDecimal((breakageAll/(double)allSum)*100).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
            residualTinRate =new BigDecimal((residualTinAll/(double)allSum)*100).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
            swellingRate =new BigDecimal((swellingAll/(double)allSum)*100).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
            foldRate =new BigDecimal((foldAll/(double)allSum)*100).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
            leakage_GlueRate =new BigDecimal((leakage_GlueAll/(double)allSum)*100).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
            dirtyRate =new BigDecimal((dirtyAll/(double)allSum)*100).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
            snOmissionRate =new BigDecimal((snOmissionAll/(double)allSum)*100).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
            vacancyRate =new BigDecimal((vacancyAll/(double)allSum)*100).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
        }
        aviBadnessthyAnalyseList.add(new AVIBadnessthyAnalyse("漏貼",  missingPasteAll,missingPasteRate));
        aviBadnessthyAnalyseList.add(new AVIBadnessthyAnalyse("殘膠",  residualGlueAll,residualGlueRate));
        aviBadnessthyAnalyseList.add(new AVIBadnessthyAnalyse("偏位",  deviationAll,deviationRate));
        aviBadnessthyAnalyseList.add(new AVIBadnessthyAnalyse("破損",  breakageAll,breakageRate));
        aviBadnessthyAnalyseList.add(new AVIBadnessthyAnalyse("殘錫",  residualTinAll,residualTinRate));
        aviBadnessthyAnalyseList.add(new AVIBadnessthyAnalyse("鼓包",  swellingAll,swellingRate));
        aviBadnessthyAnalyseList.add(new AVIBadnessthyAnalyse("褶皺",  foldAll,foldRate));
        aviBadnessthyAnalyseList.add(new AVIBadnessthyAnalyse("漏點膠", leakage_GlueAll,leakage_GlueRate));
        aviBadnessthyAnalyseList.add(new AVIBadnessthyAnalyse("異物",  dirtyAll,dirtyRate));
        aviBadnessthyAnalyseList.add(new AVIBadnessthyAnalyse("Sn漏印",snOmissionAll,snOmissionRate));
        aviBadnessthyAnalyseList.add(new AVIBadnessthyAnalyse("空位",  vacancyAll,vacancyRate));
        aviBadnessthyAnalyseList = aviBadnessthyAnalyseList.stream().sorted(Comparator.comparing(AVIBadnessthyAnalyse::getBadnessValue).reversed()).collect(Collectors.toList());
        return aviBadnessthyAnalyseList;
    }

    private  AVIUnhealthList top1Unusual(String pKey,Integer missingPaste,Integer residualGlue,Integer deviation,Integer breakage,Integer residualTin,
                                 Integer swelling,Integer fold,Integer leakage_Glue,Integer dirty,Integer snOmission,Integer vacancy){
        Integer failValue=0;
        String[] pKeySplit = pKey.split("-");
        for (int i = 0; i < pKeySplit.length; i++) {
            String value = pKeySplit[pKeySplit.length-1];//12 數值碼的12位 判斷pass fail；
            if (value.length()>1){
                String  valueSub = value.substring(value.length() - 1); //取最後一位個位
                Integer prevalue=0;
                Integer add_Together = valueIntAdd_Together(value, prevalue);//取最後一位之前的
                if (add_Together!=null){prevalue+= add_Together;}
                if (valueSub.equals(prevalue+"")){
                    String  preKeyArry = pKeySplit[i]; //12 數值碼的前11位
                    String  preKeyvalueSub = preKeyArry.substring(preKeyArry.length() - 1);
                    Integer preKeyarryValue=0;
                    preKeyarryValue+= valueIntAdd_Together(preKeyArry, preKeyarryValue);//取千位十位
                    if (preKeyvalueSub.equals(preKeyarryValue+"")){
                        failValue++;
                        switch (i){
                            case 0:
                                missingPaste++;
                                break;
                            case 1:
                                residualGlue++;
                                break;
                            case 2:
                                deviation++;
                                break;
                            case 3:
                                breakage++;
                                break;
                            case 4:
                                residualTin++;
                                break;
                            case 5:
                                swelling++;
                                break;
                            case 6:
                                fold++;
                                break;
                            case 7:
                                leakage_Glue++;
                                break;
                            case 8:
                                dirty++;
                                break;
                            case 9:
                                snOmission++;
                                break;
                            case 10:
                                vacancy++;
                                break;
                        }
                    }
                }
            }
        }

        AVIUnhealthList aviUnhealthList=new AVIUnhealthList(missingPaste,residualGlue,deviation,breakage,residualTin,swelling,fold,leakage_Glue,dirty,snOmission,vacancy);

        return aviUnhealthList;
    }

    private Integer valueIntAdd_Together(String value, Integer prevalue){
        for (int j = 1; j <= value.length(); j++) {   //只取倒數第二位 和 倒數第4位
            if (j>1&&j!=3){
                String substring = value.substring(value.length()-j,value.length()-(j-1));
                Boolean intBoolean = IntegerTry(substring);
                if (intBoolean)
                {
                    Integer prevalueInteger = Integer.parseInt(substring);
                    prevalue+= prevalueInteger;
                }
            }
        }
        return  prevalue;
    }


    private  Integer top2Fail(String pKey){
        Integer failValue=0;
        if (pKey.length()>1){
            String oneKey = pKey.substring(0, 1);
            String twoKey =pKey.substring(1,2);
            if (oneKey.equals(twoKey)){
                failValue=1;
            }
        }

        return failValue;
    }

    private Boolean  IntegerTry(String str){
        Boolean IntegerTry=false;

        try {
            Integer.parseInt(str);
            IntegerTry=true;
        }catch (Exception e){

        }



        return IntegerTry;
    }

    private List<AutoFloor_AVI_TimeSlot> overkillandTimeSlot(String DayType){
        List<AutoFloor_AVI_TimeSlot> TIME_SLOTList=new ArrayList<>();
        if (DayType.equals("Day")){
            for (int i = 8; i < 20; i++) {
                AutoFloor_AVI_TimeSlot autoFloor_avi_timeSlot=new AutoFloor_AVI_TimeSlot();
                if (i<=9){
                    autoFloor_avi_timeSlot.setAVIStartDate("0"+(i)+"3000");
                    autoFloor_avi_timeSlot.setAVIEndDate("0"+(1+i)+"3000");
                    if (i==9){
                        autoFloor_avi_timeSlot.setAVIEndDate((1+i)+"3000");
                    }
                }else {
                    autoFloor_avi_timeSlot.setAVIStartDate((i)+"3000");
                    autoFloor_avi_timeSlot.setAVIEndDate((1+i)+"3000");
                }
                TIME_SLOTList.add(autoFloor_avi_timeSlot);
            }
        }else {

            for (int i = 20; i < 23; i++) {
                AutoFloor_AVI_TimeSlot autoFloor_avi_timeSlot=new AutoFloor_AVI_TimeSlot();
                autoFloor_avi_timeSlot.setAVIStartDate((i)+"3000");
                autoFloor_avi_timeSlot.setAVIEndDate((1+i)+"3000");
                TIME_SLOTList.add(autoFloor_avi_timeSlot);
            }
            AutoFloor_AVI_TimeSlot autoFloor_avi_timeSlot23=new AutoFloor_AVI_TimeSlot();
            autoFloor_avi_timeSlot23.setAVIStartDate("233000");
            autoFloor_avi_timeSlot23.setAVIEndDate("003000");
            TIME_SLOTList.add( autoFloor_avi_timeSlot23);
            for (int i = 0; i < 8; i++) {
                AutoFloor_AVI_TimeSlot autoFloor_avi_timeSlot=new AutoFloor_AVI_TimeSlot();
                autoFloor_avi_timeSlot.setAVIStartDate("0"+(i)+"3000");
                autoFloor_avi_timeSlot.setAVIEndDate("0"+(1+i)+"3000");
                TIME_SLOTList.add(autoFloor_avi_timeSlot);
            }
        }
        return TIME_SLOTList;
    }
}
