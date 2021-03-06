package com.fox.alarmsys.controller;


import com.fox.alarmsys.entity.AlarmSystemTitle;
import com.fox.alarmsys.entity.AutoFloor_Test_Exception;
import com.fox.alarmsys.mapper.AutoFloor_Test_ExceptionMapper;
import com.fox.alarmsys.service.AlarmEnquiriesService;
import com.fox.alarmsys.service.AlarmFACSystemService;
import com.fox.qualitysys.entity.AutoFloor_Key_CheCkUp;
import com.fox.testsys.utility.AutoFloorDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**`
 * @author
 * @create 2020-06-06 8:27
 */

@Controller
public class AlarmSystemFACContr {



    @Autowired
    private AutoFloorDate autoFloorDate;

    @Autowired
    private AlarmEnquiriesService alarmEnquiriesService;

    @Autowired
    private AlarmFACSystemService alarmFACSystemService;

    @Autowired
    private AutoFloor_Test_ExceptionMapper autoFloor_test_exceptionMapper;

    private static final String TwoTimeType = "two";

    DateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    DateFormat Timeformat = new SimpleDateFormat("MM/dd HH:mm:ss");

    @RequestMapping("alarmFACSystem")//主页监控 默认显示关键参数
    public   String  AlarmSystemContr(@RequestParam(value = "FloorName",required = true,defaultValue = "") String FloorName,
                                      @RequestParam(value = "LineName",required = true,defaultValue = "") String LineName,
                                      @RequestParam(value = "Product",required = true,defaultValue = "FacilityManager")  String Product ,Map map){

        List<AutoFloor_Key_CheCkUp> autoFloor_key_checkNGS =new ArrayList<>();
        List<AlarmSystemTitle> alarmSystemTitles =new ArrayList<>();
        String StartDate="";
        String EndDate="";
        Map<String, Object> AutoFloorDateMap = autoFloorDate.AutoFloorDate(TwoTimeType);
        /*过24时 时间变量*/
        StartDate = (String) AutoFloorDateMap.get("StartDateStr");
        EndDate = (String) AutoFloorDateMap.get("EndDateStr");
        Map<String,List<AlarmSystemTitle> >alarmSystemTitlesMap=new HashMap<>();
        List<AutoFloor_Test_Exception> autoFloor_test_exceptions;
        //Tabs
        List<String> DisableException_TypeList = alarmFACSystemService.DisableException_Type(Product);
        autoFloor_key_checkNGS =alarmFACSystemService.AlarmSystemTestTabData(FloorName, StartDate, EndDate,"FAC",Product,DisableException_TypeList,"ALL","ALL",LineName);
        //Title
        autoFloor_test_exceptions = autoFloor_test_exceptionMapper.selectFACTestExceptionList(FloorName, StartDate, EndDate,"FAC",DisableException_TypeList);
        alarmSystemTitles = alarmFACSystemService.AlarmSystemTitleData(autoFloor_test_exceptions,Product,FloorName, StartDate, EndDate);
        alarmSystemTitlesMap = alarmFACSystemService.FACAlarmSystemTitleData(autoFloor_test_exceptions,FloorName,Product,StartDate,EndDate);

        Map<String, List<String>> FACAreaAndFloorMap = alarmEnquiriesService.alarmSystemFACAreaAndFloor();
        List<String> floorList = FACAreaAndFloorMap.get("FloorList");
        List<String> areaList = FACAreaAndFloorMap.get("areaList");


        map.put("floorList",floorList);
        map.put("areaList",areaList);
        map.put("FloorName",FloorName);
        map.put("LineName",LineName);
        map.put("alarmSystemTitles",alarmSystemTitles);
        map.put("Product",Product);
        map.put("autoFloor_key_checkNGS",autoFloor_key_checkNGS);
        map.put("alarmSystemTitlesMap",alarmSystemTitlesMap);
        map.put("StartDate",StartDate);
        map.put("EndDate",EndDate);
        return "alarmsys/alarmFACSystem";
    }


    @RequestMapping("alarmFACSystemdata")//Tab 异步内容
    public   String  AlarmSystemDataContr(@RequestParam(value = "FloorName",required = true,defaultValue = "") String FloorName,
                                          @RequestParam(value = "LineName",required = true,defaultValue = "") String LineName,
                                      @RequestParam(value = "Product",required = true,defaultValue = "FacilityManager")  String Product ,
                                      @RequestParam(value = "MachineType",required = false,defaultValue = "ALL")  String MachineType ,
                                      @RequestParam(value = "PriorityOrFlg",required = false,defaultValue = "ALL")  String PriorityOrFlg ,Map map){

        List<AutoFloor_Key_CheCkUp> autoFloor_key_checkNGS =new ArrayList<>();
        String StartDate="";
        String EndDate="";
        Map<String, Object> AutoFloorDateMap = autoFloorDate.AutoFloorDate(TwoTimeType);
        /*过24时 时间变量*/
        StartDate = (String) AutoFloorDateMap.get("StartDateStr");
        EndDate = (String) AutoFloorDateMap.get("EndDateStr");
        List<String> DisableException_TypeList = alarmFACSystemService.DisableException_Type(Product);
        autoFloor_key_checkNGS =alarmFACSystemService.AlarmSystemTestTabData(FloorName, StartDate, EndDate,"FAC",Product,DisableException_TypeList,MachineType,PriorityOrFlg,LineName);


        map.put("FloorName",FloorName);
        map.put("Product",Product);
        map.put("autoFloor_key_checkNGS",autoFloor_key_checkNGS);
        map.put("StartDate",StartDate);
        map.put("EndDate",EndDate);
        return "alarmsys/alarmFACSystem :: alarmsystemdata";
    }

    @RequestMapping("alarmFACSystemtetitledata")//title 异步内容
    public   String  AlarmSystemTitleDataContr(@RequestParam(value = "FloorName",required = true,defaultValue = "") String FloorName,
                                               @RequestParam(value = "LineName",required = true,defaultValue = "") String LineName,
                                          @RequestParam(value = "Product",required = true,defaultValue = "FacilityManager")  String Product ,Map map){

        List<AutoFloor_Key_CheCkUp> autoFloor_key_checkNGS =new ArrayList<>();
        String StartDate="";
        String EndDate="";
        Map<String, Object> AutoFloorDateMap = autoFloorDate.AutoFloorDate(TwoTimeType);
        /*过24时 时间变量*/
        StartDate = (String) AutoFloorDateMap.get("StartDateStr");
        EndDate = (String) AutoFloorDateMap.get("EndDateStr");
        List<String> DisableException_TypeList = alarmFACSystemService.DisableException_Type(Product);
        List<AutoFloor_Test_Exception> autoFloor_test_exceptions;
        List<AlarmSystemTitle> alarmSystemTitles=new ArrayList<>();
        autoFloor_test_exceptions = autoFloor_test_exceptionMapper.selectFACTestExceptionList(FloorName, StartDate, EndDate,"FAC",DisableException_TypeList);
        alarmSystemTitles = alarmFACSystemService.AlarmSystemTitleData(autoFloor_test_exceptions,Product,FloorName, StartDate, EndDate);


        map.put("FloorName",FloorName);
        map.put("alarmSystemTitles",alarmSystemTitles);
        map.put("Product",Product);
        map.put("StartDate",StartDate);
        map.put("EndDate",EndDate);
        return "alarmsys/alarmFACSystem :: alarmsystemtitledata";
    }

    @RequestMapping("alarmFACSystemtetitle2data")//title 异步内容
    public   String  AlarmSystemTitle2DataContr(@RequestParam(value = "FloorName",required = true,defaultValue = "") String FloorName,
                                               @RequestParam(value = "LineName",required = true,defaultValue = "") String LineName,
                                               @RequestParam(value = "Product",required = true,defaultValue = "FacilityManager")  String Product ,Map map){

        List<AutoFloor_Key_CheCkUp> autoFloor_key_checkNGS =new ArrayList<>();
        String StartDate="";
        String EndDate="";
        Map<String, Object> AutoFloorDateMap = autoFloorDate.AutoFloorDate(TwoTimeType);
        /*过24时 时间变量*/
        StartDate = (String) AutoFloorDateMap.get("StartDateStr");
        EndDate = (String) AutoFloorDateMap.get("EndDateStr");
        List<String> DisableException_TypeList = alarmFACSystemService.DisableException_Type(Product);
        List<AutoFloor_Test_Exception> autoFloor_test_exceptions;
        Map<String,List<AlarmSystemTitle> >alarmSystemTitlesMap=new HashMap<>();
        autoFloor_test_exceptions = autoFloor_test_exceptionMapper.selectFACTestExceptionList(FloorName, StartDate, EndDate,"FAC",DisableException_TypeList);
        alarmSystemTitlesMap = alarmFACSystemService.FACAlarmSystemTitleData(autoFloor_test_exceptions,FloorName,Product,StartDate,EndDate);

        map.put("FloorName",FloorName);
        map.put("alarmSystemTitlesMap",alarmSystemTitlesMap);
        map.put("Product",Product);
        map.put("StartDate",StartDate);
        map.put("EndDate",EndDate);
        return "alarmsys/alarmFACSystem :: alarmsystemtitle2data";
    }


}
