package com.fox.alarmsys.controller;


import com.fox.alarmsys.entity.AlarmEnquiriesEntity;
import com.fox.alarmsys.entity.AlarmProjectEntity;
import com.fox.alarmsys.mapper.AutoFloor_Test_ExceptionMapper;
import com.fox.alarmsys.service.AlarmEnquiriesService;
import com.fox.qualitysys.entity.AutoFloor_Key_CheckNG;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author
 * @create 2020-06-06 8:27
 */
@Controller
public class AlarmEnquiriesContr {

    @Autowired
    AlarmEnquiriesService alarmEnquiriesService;

    @Autowired
    AutoFloor_Test_ExceptionMapper autoFloor_test_exceptionMapper;



    @RequestMapping("alarmenquiries")
    public  String  AlarmEnquiriesContr(@RequestParam(value = "floorName",required = true,defaultValue = "D061F") String FloorName,
                                        @RequestParam(value = "lineName",required = true,defaultValue = "D061FARF01") String LineName,
                                        @RequestParam(value = "productName",required = true,defaultValue = "Maven") String ProductName,
                                        @RequestParam(value = "startDate",required = true,defaultValue = "") String StartDate,
                                        @RequestParam(value = "endDate",required = true,defaultValue = "") String EndDate,
                                        @RequestParam(value = "Unit",required = true,defaultValue = "") String Unit,
                                        @RequestParam(value = "Priority",required = true,defaultValue = "") String Priority,
                                        Map map){
    List<AutoFloor_Key_CheckNG> searchNGMessage=new ArrayList<>();
    Map<String, Object> alarmEnquiriesSearchMap = alarmEnquiriesService.alarmEnquiriesSearchData("default");
    Set<String> floorSet = (Set<String>) alarmEnquiriesSearchMap.get("floorSet");
    List<String>lineList = (List<String>) alarmEnquiriesSearchMap.get("lineList");
    Set<String> productSet = (Set<String>) alarmEnquiriesSearchMap.get("productSet");
    List<AlarmProjectEntity> alarmTypeList = (List<AlarmProjectEntity>)alarmEnquiriesSearchMap.get("alarmTypeList");
    List<AlarmProjectEntity> machineFaultList = (List<AlarmProjectEntity>) alarmEnquiriesSearchMap.get("machineFaultList");


    map.put("alarmTypeList",alarmTypeList);
    map.put("machineFaultList",machineFaultList);
    map.put("Unit",Unit);
    map.put("FloorName",FloorName);
    map.put("LineName",LineName);
    map.put("ProductName",ProductName);
    map.put("StartDate",StartDate);
    map.put("EndDate",EndDate);
    map.put("searchNGMessage",searchNGMessage);
    map.put("floorSet",floorSet);
    map.put("lineList",lineList);
    map.put("productSet",productSet);
    return "alarmsys/alarmenquiries";
    }


    @RequestMapping(value = "alarmenquiriesfrom",method = RequestMethod.POST)
    public String AlarmEnquiriesFromData(@RequestBody @Valid AlarmEnquiriesEntity alarmEnquiriesEntity, BindingResult bindingResult, Map map){

        String alarmTypeName = alarmEnquiriesEntity.getAlarmTypeName();
        String machineFaultName = alarmEnquiriesEntity.getMachineFaultName();
        String flag = alarmEnquiriesEntity.getFlag();
        String FloorName = alarmEnquiriesEntity.getFloorName();
        String LineName = alarmEnquiriesEntity.getLineName();
        String StartDate = alarmEnquiriesEntity.getStartDate();
        String EndDate = alarmEnquiriesEntity.getEndDate();
        String Unit = alarmEnquiriesEntity.getUnit();
        String Priority = alarmEnquiriesEntity.getPriority();
        String ProductName = alarmEnquiriesEntity.getProductName();


        List<AutoFloor_Key_CheckNG> searchNGMessage=new ArrayList<>();
        String  EnquiriesLineName="";
        if (LineName.length()>5){
              EnquiriesLineName= LineName.substring(0,5)+LineName.substring(LineName.length()-2);
        }
        Map<String, Object> alarmEnquiriesSearchMap = alarmEnquiriesService.alarmEnquiriesSearchData("default");
        Set<String> floorSet = (Set<String>) alarmEnquiriesSearchMap.get("floorSet");
        List<String> lineList = (List<String>) alarmEnquiriesSearchMap.get("lineList");
        Set<String> productSet = (Set<String>) alarmEnquiriesSearchMap.get("productSet");

        if (Unit.equals("EE")){
            searchNGMessage= alarmEnquiriesService.alarmEnquiriesTeData(alarmTypeName,machineFaultName,flag,EnquiriesLineName,FloorName,StartDate,EndDate,Priority,"EE",Unit);
        }else if (Unit.equals("TE")){
            searchNGMessage=alarmEnquiriesService.alarmEnquiriesTeData(alarmTypeName,machineFaultName,flag,LineName,FloorName,StartDate,EndDate,Priority,"TEST",Unit);
        }else if (Unit.equals("FAC")){
            searchNGMessage=alarmEnquiriesService.alarmEnquiriesTeData(alarmTypeName,machineFaultName,flag,LineName,FloorName,StartDate,EndDate,Priority,"FAC",Unit);
        }


        map.put("Unit",Unit);
        map.put("FloorName",FloorName);
        map.put("LineName",LineName);
        map.put("ProductName",ProductName);
        map.put("StartDate",StartDate);
        map.put("EndDate",EndDate);
        map.put("searchNGMessage",searchNGMessage);
        map.put("floorSet",floorSet);
        map.put("lineList",lineList);
        map.put("productSet",productSet);
        return "alarmsys/alarmenquiries::fromdata";
    }




}
