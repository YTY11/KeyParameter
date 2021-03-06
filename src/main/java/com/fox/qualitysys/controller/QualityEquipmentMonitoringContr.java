package com.fox.qualitysys.controller;


import com.fox.qualitysys.entity.AutoFloor_ESW_Monitor;
import com.fox.qualitysys.service.QualityEquipmentMonitoringService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author
 * @create 2020-04-23 13:47
 */
@Controller
public class QualityEquipmentMonitoringContr {

    @Autowired
    QualityEquipmentMonitoringService qualityEquipmentMonitoringService;


    @RequestMapping("qualityequipmentmonitoring")
    public String QualityEquipmentMonitoringContr(@RequestParam(value = "FloorName", required = false, defaultValue = "D061F") String FloorName,
                                                  @RequestParam(value = "Product", required = false, defaultValue = "Macan") String Product,
                                                  Map map) {
        List<String>FloorList=new ArrayList<>();
        FloorList.add("D061FS01");
        FloorList.add("D061FS02");
        List<AutoFloor_ESW_Monitor> autoFloor_esw_monitors = qualityEquipmentMonitoringService.QualityEquipmentMonitoringData(FloorName, Product);
        map.put("FloorList",FloorList);
        map.put("esw_monitors",autoFloor_esw_monitors);
        return "quality/qualityequipmentmonitoring";
    }

    @RequestMapping("quipmentmonitoring")
    public String QualityMonitoringContr(@RequestParam(value = "FloorName", required = false, defaultValue = "D061F") String FloorName,
                                         @RequestParam(value = "Product", required = false, defaultValue = "Macan") String Product,
                                         Map map) {
        List<String>FloorList=new ArrayList<>();
        FloorList.add("D061FS01");
        FloorList.add("D061FS02");
        List<AutoFloor_ESW_Monitor> autoFloor_esw_monitors = qualityEquipmentMonitoringService.QualityEquipmentMonitoringData(FloorName, Product);
        map.put("FloorList",FloorList);
        map.put("esw_monitors",autoFloor_esw_monitors);
        return "quality/qualityequipmentmonitoring::quipmentmonitoring";

    }
}
