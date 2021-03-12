package com.fox.keyParameter.controller;

import com.fox.alarmsys.entity.AlarmSystemTitle;
import com.fox.alarmsys.entity.AutoFloor_Test_Exception;
import com.fox.alarmsys.mapper.AutoFloor_Test_ExceptionMapper;
import com.fox.alarmsys.service.AlarmEnquiriesService;
import com.fox.alarmsys.service.AlarmSystemService;
import com.fox.keyParameter.entity.AutoFloorKeyCheckup;
import com.fox.keyParameter.entity.AutofloorKeyCheckupHis;
import com.fox.keyParameter.entity.AutofloorTarget;
import com.fox.keyParameter.mapper.AutofloorKeyCheckupHisMapper;
import com.fox.keyParameter.service.AutoFloorKeyCheckupService;
import com.fox.keyParameter.service.AutofloorTargetService;
import com.fox.qualitysys.entity.AutoFloor_Key_CheCkUp;
import com.fox.testsys.utility.AutoFloorDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author Yang TanYing
 * @Description:
 * @create 2021-02-26 17:14
 */

@Controller
public class keyParameterController {
    @Autowired
    AlarmSystemService alarmSystemService;

    @Autowired
    AutoFloorDate autoFloorDate;

    @Autowired
    AlarmEnquiriesService alarmEnquiriesService;

    @Autowired
    AutoFloor_Test_ExceptionMapper autoFloor_test_exceptionMapper;

    private static final String TwoTimeType = "two";

    DateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    DateFormat Timeformat = new SimpleDateFormat("MM/dd HH:mm:ss");

    @RequestMapping("getKeyHtml")//主页监控 默认显示关键参数
    public   String  AlarmSystemContr(@RequestParam(value = "FloorName",required = true,defaultValue = "D061F") String FloorName,
                                      @RequestParam(value = "LineName",required = true,defaultValue = "") String LineName,
                                      @RequestParam(value = "Product",required = true,defaultValue = "KeyParameter")  String Product ,Map map){

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
        if ("KeyParameter".equals(Product)){//关键参数
            //Tab
            autoFloor_key_checkNGS = alarmSystemService.AlarmSystemData(FloorName, StartDate, EndDate,"ALL");
            //Title
            List<AutoFloor_Key_CheCkUp> autoFloor_Key_exceptions = alarmSystemService.AlarmSystemData(FloorName, StartDate, EndDate,"ALL");
            alarmSystemTitles = alarmSystemService.AlarmSystemKeyTitleData(autoFloor_Key_exceptions,Product);
        }else if ("Equipment".equals(Product)){//Test设备运行
            //Tab
            List<String> DisableException_TypeList = alarmSystemService.DisableException_Type(Product);
            autoFloor_key_checkNGS =alarmSystemService.AlarmSystemTestTabData(FloorName,StartDate, EndDate,"TEST",Product,DisableException_TypeList,"ALL","ALL","D061FARF01");
            //Title
            autoFloor_test_exceptions = autoFloor_test_exceptionMapper.selectTestExceptionList(FloorName,"D061FARF01", StartDate, EndDate,"TEST",DisableException_TypeList);
            autoFloor_test_exceptions.addAll(autoFloor_test_exceptionMapper.selectRobotTestExceptionList(FloorName,"D061FARF01", StartDate, EndDate,"TEST",DisableException_TypeList));
            if ("K051FARF08".equals(LineName)){
                autoFloor_test_exceptions.addAll(autoFloor_test_exceptionMapper.selectK05CCD_Data("D061FARF01", StartDate, EndDate));
            }
            alarmSystemTitles = alarmSystemService.AlarmSystemTitleData(autoFloor_test_exceptions,Product);
        }else if ("Equipment_SMT".equals(Product)){//SMT设备运行
            //Tab
            List<String> DisableException_TypeList = alarmSystemService.DisableException_Type(Product);
            autoFloor_key_checkNGS =alarmSystemService.AlarmSystemTestTabData(FloorName, StartDate, EndDate,"SMT",Product,DisableException_TypeList,"ALL","ALL",LineName);
            //Title
            autoFloor_test_exceptions = autoFloor_test_exceptionMapper.selectNOTTestExceptionList(FloorName, StartDate, EndDate,"SMT",DisableException_TypeList);
            alarmSystemTitles = alarmSystemService.AlarmSystemTitleData(autoFloor_test_exceptions,Product);
        }else if ("SKASandAFS".equals(Product)){//SKAS&AFS
            //Tab
            List<String> DisableException_TypeList = alarmSystemService.DisableException_Type(Product);
            autoFloor_key_checkNGS =alarmSystemService.AlarmSystemTestTabData(FloorName, StartDate, EndDate,"SKAS",Product,DisableException_TypeList,"ALL","ALL",LineName);
            //Title
            autoFloor_test_exceptions = autoFloor_test_exceptionMapper.selectNOTTestExceptionList(FloorName, StartDate, EndDate,"SKAS",DisableException_TypeList);
            alarmSystemTitles = alarmSystemService.AlarmSystemTitleData(autoFloor_test_exceptions,Product);
        }else if ("Equipment_AVI".equals(Product)){//AVI
            //Tab
            List<String> DisableException_TypeList = alarmSystemService.DisableException_Type(Product);
            autoFloor_key_checkNGS =alarmSystemService.AlarmSystemTestTabData(FloorName, StartDate, EndDate,"AVI",Product,DisableException_TypeList,"ALL","ALL",LineName);
            //Title
            autoFloor_test_exceptions = autoFloor_test_exceptionMapper.selectNOTTestExceptionList(FloorName, StartDate, EndDate,"AVI",DisableException_TypeList);
            alarmSystemTitles = alarmSystemService.AlarmSystemTitleData(autoFloor_test_exceptions,Product);
        }else if ("HotAirGuns".equals(Product)){//熱風槍
            //Tab
            List<String> DisableException_TypeList = alarmSystemService.DisableException_Type(Product);
            autoFloor_key_checkNGS =alarmSystemService.AlarmSystemTestTabData(FloorName, StartDate, EndDate,"HOTAIRGUN",Product,DisableException_TypeList,"ALL","ALL",LineName);
            //Title
            autoFloor_test_exceptions = autoFloor_test_exceptionMapper.selectNOTTestExceptionList(FloorName, StartDate, EndDate,"HOTAIRGUN",DisableException_TypeList);
            alarmSystemTitles = alarmSystemService.AlarmSystemTitleData(autoFloor_test_exceptions,Product);
        }

        Map<String, Object> alarmEnquiriesSearchMap = alarmEnquiriesService.alarmEnquiriesSearchData(Product);
        Set<String> floorSet = (Set<String>) alarmEnquiriesSearchMap.get("floorSet");
        List<String>lineList = (List<String>) alarmEnquiriesSearchMap.get("lineList");
        Set<String> productSet = (Set<String>) alarmEnquiriesSearchMap.get("productSet");

        map.put("floorSet",floorSet);
        map.put("lineList",lineList);
        map.put("productSet",productSet);
        map.put("FloorName",FloorName);
        map.put("LineName",LineName);
        map.put("alarmSystemTitles",alarmSystemTitles);
        map.put("Product",Product);
        map.put("autoFloor_key_checkNGS",autoFloor_key_checkNGS);
        map.put("alarmSystemTitlesMap",alarmSystemTitlesMap);
        map.put("StartDate",StartDate);
        map.put("EndDate",EndDate);
        return "keyParameter";
    }


    @RequestMapping("alarmsystemdataKey")//Tab 异步内容
    public   String  AlarmSystemDataContr(@RequestParam(value = "FloorName",required = true,defaultValue = "D061F") String FloorName,
                                          @RequestParam(value = "LineName",required = true,defaultValue = "") String LineName,
                                          @RequestParam(value = "Product",required = true,defaultValue = "KeyParameter")  String Product ,
                                          @RequestParam(value = "MachineType",required = false)  String MachineType ,
                                          @RequestParam(value = "PriorityOrFlg",required = false)  String PriorityOrFlg ,Map map){

        List<AutoFloor_Key_CheCkUp> autoFloor_key_checkNGS =new ArrayList<>();
        String StartDate="";
        String EndDate="";
        Map<String, Object> AutoFloorDateMap = autoFloorDate.AutoFloorDate(TwoTimeType);
        /*过24时 时间变量*/
        StartDate = (String) AutoFloorDateMap.get("StartDateStr");
        EndDate = (String) AutoFloorDateMap.get("EndDateStr");

        if ("KeyParameter".equals(Product)){//关键参数
            autoFloor_key_checkNGS = alarmSystemService.AlarmSystemData(FloorName, StartDate, EndDate,PriorityOrFlg);
        }else if ("Equipment".equals(Product)){//Test设备运行
            if ("".equals(LineName)){LineName="D061FARF01";}
            List<String> DisableException_TypeList = alarmSystemService.DisableException_Type(Product);
            autoFloor_key_checkNGS =alarmSystemService.AlarmSystemTestTabData(FloorName, StartDate, EndDate,"TEST",Product,DisableException_TypeList,MachineType,PriorityOrFlg,LineName);
        }else if ("Equipment_SMT".equals(Product)){//SMT设备运行
            List<String> DisableException_TypeList = alarmSystemService.DisableException_Type(Product);
            autoFloor_key_checkNGS =alarmSystemService.AlarmSystemTestTabData(FloorName, StartDate, EndDate,"SMT",Product,DisableException_TypeList,MachineType,PriorityOrFlg,LineName);
        }else if ("SKASandAFS".equals(Product)){//SKAS&AFS
            List<String> DisableException_TypeList = alarmSystemService.DisableException_Type(Product);
            autoFloor_key_checkNGS =alarmSystemService.AlarmSystemTestTabData(FloorName, StartDate, EndDate,"SKAS",Product,DisableException_TypeList,MachineType,PriorityOrFlg,LineName);
        }else if ("Equipment_AVI".equals(Product)){//AVI
            //Tab
            List<String> DisableException_TypeList = alarmSystemService.DisableException_Type(Product);
            autoFloor_key_checkNGS =alarmSystemService.AlarmSystemTestTabData(FloorName, StartDate, EndDate,"AVI",Product,DisableException_TypeList,MachineType,PriorityOrFlg,LineName);
        }else if ("HotAirGuns".equals(Product)){//熱風槍
            //Tab
            List<String> DisableException_TypeList = alarmSystemService.DisableException_Type(Product);
            autoFloor_key_checkNGS =alarmSystemService.AlarmSystemTestTabData(FloorName, StartDate, EndDate,"HOTAIRGUN",Product,DisableException_TypeList,MachineType,PriorityOrFlg,LineName);
        }

        map.put("FloorName",FloorName);
        map.put("Product",Product);
        map.put("autoFloor_key_checkNGS",autoFloor_key_checkNGS);
        map.put("StartDate",StartDate);
        map.put("EndDate",EndDate);
        return "keyParameter :: alarmsystemdata";
    }

    @RequestMapping("alarmsystemtetitledataKey")//title 异步内容
    public   String  AlarmSystemTitleDataContr(@RequestParam(value = "FloorName",required = true,defaultValue = "D061F") String FloorName,
                                               @RequestParam(value = "LineName",required = true,defaultValue = "") String LineName,
                                               @RequestParam(value = "Product",required = true,defaultValue = "KeyParameter")  String Product ,Map map){

        List<AutoFloor_Key_CheCkUp> autoFloor_key_checkNGS =new ArrayList<>();
        String StartDate="";
        String EndDate="";
        Map<String, Object> AutoFloorDateMap = autoFloorDate.AutoFloorDate(TwoTimeType);
        /*过24时 时间变量*/
        StartDate = (String) AutoFloorDateMap.get("StartDateStr");
        EndDate = (String) AutoFloorDateMap.get("EndDateStr");
        List<String> DisableException_TypeList = alarmSystemService.DisableException_Type(Product);
        List<AutoFloor_Test_Exception> autoFloor_test_exceptions;
        List<AlarmSystemTitle> alarmSystemTitles=new ArrayList<>();

        if ("KeyParameter".equals(Product)) {//关键参数
            List<AutoFloor_Key_CheCkUp> autoFloor_Key_exceptions = alarmSystemService.AlarmSystemData(FloorName, StartDate, EndDate,"ALL");
            alarmSystemTitles = alarmSystemService.AlarmSystemKeyTitleData(autoFloor_Key_exceptions,Product);
        }else if ("Equipment".equals(Product)) {//Test设备运行
            if ("".equals(LineName)){LineName="D061FARF01";}
            autoFloor_test_exceptions = autoFloor_test_exceptionMapper.selectTestExceptionList(FloorName,LineName, StartDate, EndDate,"TEST",DisableException_TypeList);
            autoFloor_test_exceptions.addAll(autoFloor_test_exceptionMapper.selectRobotTestExceptionList(FloorName,LineName, StartDate, EndDate,"TEST",DisableException_TypeList));
            if ("K051FARF08".equals(LineName)){ autoFloor_test_exceptions.addAll(autoFloor_test_exceptionMapper.selectK05CCD_Data(LineName, StartDate, EndDate)); }
            alarmSystemTitles = alarmSystemService.AlarmSystemTitleData(autoFloor_test_exceptions,Product);
        }else if ("Equipment_SMT".equals(Product)){//SMT设备运行
            autoFloor_test_exceptions = autoFloor_test_exceptionMapper.selectNOTTestExceptionList(FloorName, StartDate, EndDate,"SMT",DisableException_TypeList);
            alarmSystemTitles = alarmSystemService.AlarmSystemTitleData(autoFloor_test_exceptions,Product);
        }else if ("SKASandAFS".equals(Product)){//SKAS&AFS
            autoFloor_test_exceptions = autoFloor_test_exceptionMapper.selectSkasExceptionList(FloorName,LineName,StartDate, EndDate,"SKAS",DisableException_TypeList);
            alarmSystemTitles = alarmSystemService.AlarmSystemTitleData(autoFloor_test_exceptions,Product);
        }else if ("Equipment_AVI".equals(Product)){//AVI
            //Title
            autoFloor_test_exceptions = autoFloor_test_exceptionMapper.selectSkasExceptionList(FloorName,LineName,StartDate, EndDate,"AVI",DisableException_TypeList);
            alarmSystemTitles = alarmSystemService.AlarmSystemTitleData(autoFloor_test_exceptions,Product);
        }else if ("HotAirGuns".equals(Product)){//熱風槍
            //Title
            autoFloor_test_exceptions = autoFloor_test_exceptionMapper.selectNOTTestExceptionList(FloorName, StartDate, EndDate,"HOTAIRGUN",DisableException_TypeList);
            alarmSystemTitles = alarmSystemService.AlarmSystemTitleData(autoFloor_test_exceptions,Product);
        }

        map.put("FloorName",FloorName);
        map.put("alarmSystemTitles",alarmSystemTitles);
        map.put("Product",Product);
        map.put("StartDate",StartDate);
        map.put("EndDate",EndDate);
        return "keyParameter :: alarmsystemtitledata";
    }




}
