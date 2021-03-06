package com.fox.avisys.controller;

import com.fox.avisys.entity.*;
import com.fox.avisys.mapper.AutoFloor_AVI_PLC_LogMapper;
import com.fox.avisys.service.AVISystemService;
import com.fox.testsys.entity.AutoFloor_Target;
import com.fox.testsys.service.TestAProductService;
import com.fox.testsys.utility.AutoFloorDate;
import com.google.gson.Gson;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author
 * @create 2020-06-08 10:19
 */

@Controller
public class AVISystemCont {

    @Autowired
    AutoFloorDate autoFloorDate;

    @Autowired
    AVISystemService aviSystemService;

    @Autowired
    TestAProductService testAProductService;

    @Autowired
    AutoFloor_AVI_PLC_LogMapper autoFloor_avi_plc_logMapper;

        private static final String TwoTimeType = "two";
        //机种名称
        private static final String LOT_PRODUCT = "Lotus";
        private static final String MAC_PRODUCT = "Raptor";
        private static final String PHA_PRODUCT = "Pha";
        private static final Integer MachinieTypeSum = 4;
        private static final DateFormat df=new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    @RequestMapping("/avisystemcont")
    public  String  AVISystemCont( @RequestParam(value = "FloorName", required = true, defaultValue = "D061F") String FloorName,
                                   @RequestParam(value = "TimeType", required = true, defaultValue = "two") String TypeTime,
                                   @RequestParam(value = "Product", required = true, defaultValue = "All") String Product, Map map){


       Map<String, Object> AutoFloorDateMap = autoFloorDate.AutoFloorDate(TwoTimeType);

       Integer Htime = (Integer) AutoFloorDateMap.get("Htime");
       //当前日期
       Date schedule = (Date) AutoFloorDateMap.get("schedule");
       //昨天日期
       Date YesterdayDate = (Date) AutoFloorDateMap.get("YesterdayDate");

       Date TomorrowDate = (Date) AutoFloorDateMap.get("TomorrowDate");

       Integer Minute = (Integer) AutoFloorDateMap.get("Minute");
       //工作时间
       Integer actionMinuteD = (Integer) AutoFloorDateMap.get("actionMinuteD");
       //運行時間
       Integer TimeGNumber = (Integer) AutoFloorDateMap.get("TimeGNumber");
       /*过24时 时间变量*/
       String StartDateStr = (String) AutoFloorDateMap.get("StartDateStr");
       String EndDateStr = (String) AutoFloorDateMap.get("EndDateStr");
        /*AVI过24时 时间变量*/
       String AviStartDateStr = (String) AutoFloorDateMap.get("AviStartDateStr");
       String AviEndDateStr = (String) AutoFloorDateMap.get("AviEndDateStr");
       //设备状态信息
       List<AutoFloor_Target> LineList = testAProductService.LineData(FloorName, FloorName, schedule, MAC_PRODUCT, TypeTime, "","");
       map.put("LineList",LineList);
       AutoFloor_AVI_MachinieType AVISystemQualityType = aviSystemService.AVISystemQualityType(MachinieTypeSum,LineList, StartDateStr, EndDateStr);
       map.put("AVISystemQualityType",AVISystemQualityType);
       //AVI设备状态
       Map<String, Integer> AVIQualityType = aviSystemService.AVIQualityType(LineList, StartDateStr, EndDateStr);
       map.put("AVIQualityType",AVIQualityType);
       //机故  异常信息
       Map<String, Object> MachineMassageMap = aviSystemService.AVISystemMachineMassage(LineList,FloorName, actionMinuteD, StartDateStr, EndDateStr,AviStartDateStr,AviEndDateStr);
       Double machineRate = (Double)MachineMassageMap.get("MachineRate");
       List<AutoFloor_AVI_Warning> machineMassageList = ( List<AutoFloor_AVI_Warning>)MachineMassageMap.get("MachineMassageList");
       map.put("machineRate",machineRate);
       map.put("machineMassageList",machineMassageList);

       //AVI直通率
        if (Minute>30){Htime+=1;}
        Map<String, Object> AVISystemUPHLineDataMap = aviSystemService.AVISystemUPHLineData(Htime, FloorName);
        List<AutoFloor_AVIUPHLineData> aviuphLineData = (List<AutoFloor_AVIUPHLineData>)AVISystemUPHLineDataMap.get("AVIUPHLineData");
        Double allAVIUPHPassRate = (Double)AVISystemUPHLineDataMap.get("allAVIUPHPassRate");
        Gson gson=new Gson();
       String aviuphLineDataJson = gson.toJson(aviuphLineData);
       map.put("aviuphLineData",aviuphLineDataJson);
       map.put("allAVIUPHPassRate",allAVIUPHPassRate);

       //AVI过杀漏检
       List<AVIOverkillandEscape> aviOverkillandEscapes = aviSystemService.AVIOverkillandEscape(Htime,FloorName, schedule);
       String aviOverkillandEscapesJson = gson.toJson(aviOverkillandEscapes);
       map.put("aviOverkillandEscapes",aviOverkillandEscapesJson);




        //不良分析
       List<AVIBadnessthyAnalyse>aviUnhealthyAnalyse = aviSystemService.aviUnhealthyAnalyseData(LineList, FloorName, AviStartDateStr, AviEndDateStr);

       map.put("aviUnhealthyAnalyse",aviUnhealthyAnalyse);
       map.put("StartDateStr",StartDateStr);
       map.put("EndDateStr",df.format(new Date(System.currentTimeMillis())));
       return "avisys/avisystem";
    }


    @RequestMapping("/avisystabData")
    public  String  AVISystemtabData( @RequestParam(value = "FloorName", required = true, defaultValue = "D061F") String FloorName,
                                   @RequestParam(value = "TimeType", required = true, defaultValue = "two") String TypeTime,
                                   @RequestParam(value = "Product", required = true, defaultValue = "All") String Product, Map map){


        Map<String, Object> AutoFloorDateMap = autoFloorDate.AutoFloorDate(TwoTimeType);

        Integer Htime = (Integer) AutoFloorDateMap.get("Htime");
        //当前日期
        Date schedule = (Date) AutoFloorDateMap.get("schedule");
        //昨天日期
        Date YesterdayDate = (Date) AutoFloorDateMap.get("YesterdayDate");

        Date TomorrowDate = (Date) AutoFloorDateMap.get("TomorrowDate");

        Integer Minute = (Integer) AutoFloorDateMap.get("Minute");
        //工作时间
        Integer actionMinuteD = (Integer) AutoFloorDateMap.get("actionMinuteD");
        //運行時間
        Integer TimeGNumber = (Integer) AutoFloorDateMap.get("TimeGNumber");
        /*过24时 时间变量*/
        String StartDateStr = (String) AutoFloorDateMap.get("StartDateStr");
        String EndDateStr = (String) AutoFloorDateMap.get("EndDateStr");
        /*AVI过24时 时间变量*/
        String AviStartDateStr = (String) AutoFloorDateMap.get("AviStartDateStr");
        String AviEndDateStr = (String) AutoFloorDateMap.get("AviEndDateStr");
        //设备状态信息
        List<AutoFloor_Target> LineList = testAProductService.LineData(FloorName, FloorName, schedule, MAC_PRODUCT, TypeTime, "","");
        map.put("LineList",LineList);
        AutoFloor_AVI_MachinieType AVISystemQualityType = aviSystemService.AVISystemQualityType(MachinieTypeSum,LineList, StartDateStr, EndDateStr);
        map.put("AVISystemQualityType",AVISystemQualityType);
        //AVI设备状态
        Map<String, Integer> AVIQualityType = aviSystemService.AVIQualityType(LineList, StartDateStr, EndDateStr);
        map.put("AVIQualityType",AVIQualityType);
        //机故  异常信息
        Map<String, Object> MachineMassageMap = aviSystemService.AVISystemMachineMassage(LineList,FloorName, actionMinuteD, StartDateStr, EndDateStr,AviStartDateStr,AviEndDateStr);
        Double machineRate = (Double)MachineMassageMap.get("MachineRate");
        List<AutoFloor_AVI_Warning> machineMassageList = ( List<AutoFloor_AVI_Warning>)MachineMassageMap.get("MachineMassageList");
        map.put("machineRate",machineRate);
        map.put("machineMassageList",machineMassageList);

        //AVI直通率
        if (Minute>30){Htime+=1;}
        Map<String, Object> AVISystemUPHLineDataMap = aviSystemService.AVISystemUPHLineData(Htime, FloorName);
        List<AutoFloor_AVIUPHLineData> aviuphLineData = (List<AutoFloor_AVIUPHLineData>)AVISystemUPHLineDataMap.get("AVIUPHLineData");
        Double allAVIUPHPassRate = (Double)AVISystemUPHLineDataMap.get("allAVIUPHPassRate");
        Gson gson=new Gson();
        String aviuphLineDataJson = gson.toJson(aviuphLineData);
        map.put("aviuphLineData",aviuphLineDataJson);
        map.put("allAVIUPHPassRate",allAVIUPHPassRate);

        //AVI过杀漏检
        List<AVIOverkillandEscape> aviOverkillandEscapes = aviSystemService.AVIOverkillandEscape(Htime,FloorName, schedule);
        String aviOverkillandEscapesJson = gson.toJson(aviOverkillandEscapes);
        map.put("aviOverkillandEscapes",aviOverkillandEscapesJson);

        //不良分析
        List<AVIBadnessthyAnalyse>aviUnhealthyAnalyse = aviSystemService.aviUnhealthyAnalyseData(LineList, FloorName, AviStartDateStr, AviEndDateStr);

        map.put("aviUnhealthyAnalyse",aviUnhealthyAnalyse);
        map.put("StartDateStr",StartDateStr);
        map.put("EndDateStr",df.format(new Date(System.currentTimeMillis())));
        return "avisys/avisystem::aviTabData";
    }

}
