package com.fox.alarmsys.controller;


import com.fox.alarmsys.entity.SoftwareMonitoring;
import com.fox.alarmsys.service.AlarmEnquiriesService;
import com.fox.alarmsys.service.AlarmSoftwareMonioringService;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author
 * @create 2019-11-01 15:44
 */
@Log4j
@Controller
public class AlarmSoftwareMonitoringContr {

    @Autowired
    AlarmSoftwareMonioringService alarmSoftwareMonioringService;
    @Autowired
    AlarmEnquiriesService alarmEnquiriesService;

    @RequestMapping("/alarmsoftwaremonitoring")
    public String AlarmSoftwareMonitoring(@RequestParam(value = "floorName",required = true,defaultValue = "D061F") String FloorName, Map map){
        List<SoftwareMonitoring> softwareMonitorings = alarmSoftwareMonioringService.AlarmSoftwareMonitoring(FloorName);
        Map<String, Object> alarmEnquiriesSearchMap = alarmEnquiriesService.alarmEnquiriesSearchData("default");
        Set<String> floorSet = (Set<String>) alarmEnquiriesSearchMap.get("floorSet");
        List<String>lineList = (List<String>) alarmEnquiriesSearchMap.get("lineList");
        Set<String> productSet = (Set<String>) alarmEnquiriesSearchMap.get("productSet");

        map.put("FloorName",FloorName);
        map.put("floorSet",floorSet);
        map.put("lineList",lineList);
        map.put("productSet",productSet);
        map.put("softwareData",softwareMonitorings);
        return "alarmsys/alarmsoftwaremonitoring";
    }
}
