package com.fox.alarmsys.controller;


import com.fox.alarmsys.entity.AutoFloor_Visrdo_Target;
import com.fox.alarmsys.service.AlarmSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * 警報设置contr
 *
 * @author wu
 * @create 2020-10-16 16:43
 * @date 2020/10/16
 */


@Controller
public class AlarmSettingContr {

    @Autowired
    private AlarmSettingService alarmSettingService;


    /**
     * 警報o2设置contr
     */
   @GetMapping("/alarmo2setting")
   public String  AlarmO2SettingContr(Map map){
       Map<String, List> AlarmO2SettingMap = alarmSettingService.AlarmO2SettingContr();
        List floorList = AlarmO2SettingMap.get("floorList");
        List linenameList = AlarmO2SettingMap.get("linenameList");
        List machineTypeList = AlarmO2SettingMap.get("machineTypeList");
        List machineTypelineList = AlarmO2SettingMap.get("machineTypelineList");

       List<AutoFloor_Visrdo_Target> quiriesTabData =  alarmSettingService.AlarmO2SettingContrQuiries("", "", "");

        int kill=0;
       map.put("machineTypelineList",machineTypelineList);
       map.put("kill",kill);
       map.put("quiriesTabData",quiriesTabData);
       map.put("floorList",floorList);
       map.put("linenameList",linenameList);
       map.put("machineTypeList",machineTypeList);
       return "alarmsys/alarmsetting";
   }


    /**
     * 警報o2设置查詢
     */
    @GetMapping("/alarmo2settingenquiries")
    public String   AlarmO2SettingEnquiries(@RequestParam(value = "floorName")String floorName,
                                          @RequestParam(value = "lineName")String lineName,
                                          @RequestParam(value = "machineTypeName")String machineTypeName,Map map){
        List<AutoFloor_Visrdo_Target> quiriesTabData = alarmSettingService.AlarmO2SettingContrQuiries(floorName, lineName, machineTypeName);

        int kill=1;
        map.put("kill",kill);
        map.put("quiriesTabData",quiriesTabData);
        return "alarmsys/alarmsetting::quiriestab";
    }





}
