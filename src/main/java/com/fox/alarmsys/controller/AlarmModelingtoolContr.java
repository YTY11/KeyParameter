package com.fox.alarmsys.controller;


import com.fox.alarmsys.entity.AlarmModelingtoolData;
import com.fox.alarmsys.entity.AlarmModelingtoolTabData;
import com.fox.alarmsys.service.AlarmEnquiriesService;
import com.fox.alarmsys.service.AlarmModelingtoolService;
import com.fox.testsys.utility.AutoFloorDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author
 * @create 2020-09-18 11:24
 */
@Controller
public class AlarmModelingtoolContr {
    @Autowired
    private AutoFloorDate autoFloorDate;

    @Autowired
    private AlarmEnquiriesService alarmEnquiriesService;

    @Autowired
    private AlarmModelingtoolService alarmModelingtoolService;

    private static final String TwoTimeType = "two";


    @RequestMapping("/alarmmodelingcontr")
    public String  AlarmModelingContr(@RequestParam(value = "LineName",required = true,defaultValue = "D061FARF01")String LineName ,Map map){
        Map<String, Object> AutoFloorDateMap = autoFloorDate.AutoFloorDate(TwoTimeType);
        /*过24时 时间变量*/
        String StartDate = (String) AutoFloorDateMap.get("StartDateStr");
        String EndDate = (String) AutoFloorDateMap.get("EndDateStr");
        Map<String, Object> alarmEnquiriesSearchMap = alarmEnquiriesService.alarmEnquiriesSearchData("default");
        Set<String> floorSet = (Set<String>) alarmEnquiriesSearchMap.get("floorSet");
        List<String> lineList = (List<String>) alarmEnquiriesSearchMap.get("lineList");
        Set<String> productSet = (Set<String>) alarmEnquiriesSearchMap.get("productSet");
        List<AlarmModelingtoolData> alarmModelingtoolData = alarmModelingtoolService.AlarmModelingtoolData(LineName,StartDate,EndDate);
        List<AlarmModelingtoolTabData> alarmModelingtoolTabData =new ArrayList<>();

        map.put("alarmModelingtoolTabData",alarmModelingtoolTabData);
        map.put("LineName",LineName);
        map.put("alarmModelingtoolData",alarmModelingtoolData);
        map.put("StartDate",StartDate);
        map.put("EndDate",EndDate);
        map.put("floorSet",floorSet);
        map.put("lineList",lineList);
        map.put("productSet",productSet);
        return "alarmsys/alarmmodelingtool";
    }

    @RequestMapping("/alarmmodelingtitilcontr")
    public String  AlarmModelingTitilContr(@RequestParam(value = "FloorName",required = true,defaultValue = "D061F")String FloorName ,
                                           @RequestParam(value = "LineName",required = true,defaultValue = "D061FARF01")String LineName,Map map){
        Map<String, Object> AutoFloorDateMap = autoFloorDate.AutoFloorDate(TwoTimeType);
        /*过24时 时间变量*/
        String StartDate = (String) AutoFloorDateMap.get("StartDateStr");
        String EndDate = (String) AutoFloorDateMap.get("EndDateStr");
        List<AlarmModelingtoolData> alarmModelingtoolData = alarmModelingtoolService.AlarmModelingtoolData(LineName,StartDate,EndDate);

        map.put("LineName",LineName);
        map.put("alarmModelingtoolData",alarmModelingtoolData);
        return "alarmsys/alarmmodelingtool::alarmmodelingtitilcontr";
    }

    @RequestMapping("/alarmmodelingtabcontr")
    public String  AlarmModelingTabContr(@RequestParam(value = "FloorName",required = true,defaultValue = "D061F")String FloorName ,
                                         @RequestParam(value = "LineName",required = true,defaultValue = "D061FARF01")String LineName,
                                         @RequestParam(value = "StationName",required = true,defaultValue = "")String StationName, Map map){
        Map<String, Object> AutoFloorDateMap = autoFloorDate.AutoFloorDate(TwoTimeType);
        /*过24时 时间变量*/
        String StartDate = (String) AutoFloorDateMap.get("StartDateStr");
        String EndDate = (String) AutoFloorDateMap.get("EndDateStr");
        List<AlarmModelingtoolTabData> alarmModelingtoolTabData = alarmModelingtoolService.AlarmModelingtoolTabData(FloorName, LineName, StartDate, EndDate, StationName);


        map.put("alarmModelingtoolTabData",alarmModelingtoolTabData);
        return "alarmsys/alarmmodelingtool::alarmmodelingtabcontr";
    }
}
