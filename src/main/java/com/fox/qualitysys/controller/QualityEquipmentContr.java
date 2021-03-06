package com.fox.qualitysys.controller;


import com.fox.alarmsys.mapper.AutoFloor_Test_ExceptionMapper;
import com.fox.qualitysys.entity.QualityequipmentUN;
import com.fox.qualitysys.service.QualityEquipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author
 * @create 2020-04-22 15:38
 */
@Controller
public class QualityEquipmentContr {

    @Autowired
    QualityEquipmentService qualityEquipmentService;

    @Autowired
    AutoFloor_Test_ExceptionMapper autoFloor_test_exceptionMapper;

    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @RequestMapping("qualityequipment")
    public String QualityEquipmentContr(@RequestParam(value = "FloorName", required = false, defaultValue = "D061F") String FloorName, Map map) {
        List<QualityequipmentUN> ALLLineList = qualityEquipmentService.ALLLineList(FloorName);
        long EndTimeLong = autoFloor_test_exceptionMapper.DBDate().getTime();
        String EndDate = df.format(new Date(EndTimeLong+10*60*1000));
        String StartDate = df.format(new Date(EndTimeLong- 60 * 60 * 1000));
        Map<String, Object> ALLFloorLineMap = qualityEquipmentService.ALLFloorLineList(FloorName, StartDate, EndDate);
        List<QualityequipmentUN> qualityequipmentUNList = ( List<QualityequipmentUN>)ALLFloorLineMap.get("qualityequipmentUNList");
        Map<String, List<QualityequipmentUN>> qualityequipmentUNMap = (Map<String, List<QualityequipmentUN>>) ALLFloorLineMap.get("QualityequipmentUNMap");
        map.put("ALLLineList", ALLLineList);
        map.put("qualityequipmentUNList",qualityequipmentUNList);
        map.put("qualityequipmentUNMap",qualityequipmentUNMap);
        return "quality/qualityequipment";
    }

    @RequestMapping("Equipment")
    public String SPIEquipmentContr(@RequestParam(value = "FloorName", required = false, defaultValue = "D061F") String FloorName, Map map) {
        List<QualityequipmentUN> ALLLineList = qualityEquipmentService.ALLLineList(FloorName);
        long EndTimeLong = autoFloor_test_exceptionMapper.DBDate().getTime();
        String EndDate = df.format(new Date(EndTimeLong+10*60*1000));
        String StartDate = df.format(new Date(EndTimeLong-60*60*1000));
        Map<String, Object> ALLFloorLineMap = qualityEquipmentService.ALLFloorLineList(FloorName, StartDate, EndDate);
        List<QualityequipmentUN> qualityequipmentUNList = ( List<QualityequipmentUN>)ALLFloorLineMap.get("qualityequipmentUNList");
        Map<String, List<QualityequipmentUN>> qualityequipmentUNMap = (Map<String, List<QualityequipmentUN>>) ALLFloorLineMap.get("QualityequipmentUNMap");
        map.put("ALLLineList", ALLLineList);
        map.put("qualityequipmentUNList",qualityequipmentUNList);
        map.put("qualityequipmentUNMap",qualityequipmentUNMap);

        return "quality/qualityequipment::Equipment";
    }

}
