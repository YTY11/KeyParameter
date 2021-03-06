package com.fox.qualitysys.controller;


import com.fox.alarmsys.mapper.AutoFloor_Test_ExceptionMapper;
import com.fox.qualitysys.entity.AutoFloor_Key_CheCkUp;
import com.fox.qualitysys.entity.EquipmentSub;
import com.fox.qualitysys.mapper.AutoFloor_Key_KeyPMSMapper;
import com.fox.qualitysys.service.QualityEquipmentSubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author
 * @create 2020-04-23 13:47
 */
@Controller
public class QualityEquipmentSubContr {

    @Autowired
    private QualityEquipmentSubService qualityEquipmentSubService;

    @Autowired
    private AutoFloor_Test_ExceptionMapper autoFloor_test_exceptionMapper;

    @Autowired
    private AutoFloor_Key_KeyPMSMapper autoFloor_key_keyPMSMapper;

    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @RequestMapping("qualityequipmentsub")
    public String QualityEquipmentSubContr(@RequestParam(value = "WorkStation", required = false, defaultValue = "REFLOW") String WorkStation,
                                           @RequestParam(value = "LineName", required = false, defaultValue = "D061FS01") String LineName,
                                           @RequestParam(value = "Type", required = false, defaultValue = "0") Integer Type,
                                           Map map) {
        String FloorName = LineName.substring(0, 3) + "-" + LineName.substring(3, 5);
        String StationLineName = LineName.substring(0, 3) + "-" + LineName.substring(3, 5) + "AT-" + LineName.substring(LineName.length() - 2, LineName.length());
        List<EquipmentSub> equipmentSubs = new ArrayList<>();
        List<AutoFloor_Key_CheCkUp>  QualityEquipmentSub=new ArrayList<>();
        long EndTimeLong = autoFloor_test_exceptionMapper.DBDate().getTime();
        String EndDate = df.format(new Date(EndTimeLong+10*60*1000));
        String StartDate = df.format(new Date(EndTimeLong- 60 * 60 * 1000));

        String processing ="";
        if (Type == 0) {
            QualityEquipmentSub= qualityEquipmentSubService.QualityEquipmentSubData(WorkStation, LineName,StartDate,EndDate);
        } else if (Type == 1) {
            String WarningCase = "SW ERROR";
            equipmentSubs = qualityEquipmentSubService.TestUnStationData(WorkStation, WarningCase, FloorName, StationLineName);
            List<EquipmentSub> TestUndfStationData = qualityEquipmentSubService.TestUndfStationData(WorkStation, FloorName, StationLineName);
            equipmentSubs.addAll(TestUndfStationData);
        } else if (Type == 2) {
            String WarningCase = "Pathloss ERROR";
            equipmentSubs = qualityEquipmentSubService.TestUnStationData(WorkStation, WarningCase, FloorName, StationLineName);

        }
        if (Type>0){processing="TEST";}



        map.put("QualityEquipmentSub",QualityEquipmentSub);
        map.put("Type",Type);
        map.put("WorkStation", WorkStation);
        map.put("LineName", LineName);
        map.put("processing",processing);
        map.put("equipmentSubs", equipmentSubs);
        return "quality/qualityequipmentsub";
    }

}
