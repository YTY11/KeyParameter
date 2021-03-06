package com.fox.qualitysys.service;


import com.fox.http.HttpClient;
import com.fox.qualitysys.entity.QualityequipmentUN;
import com.fox.qualitysys.mapper.AutoFloor_Key_CheCkUpMapper;
import com.fox.qualitysys.mapper.AutoFloor_Key_CheckNGMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author
 * @create 2020-04-24 8:35
 */
@Component
public class QualityMonitoringService {

    @Autowired
    QualityEquipmentService qualityEquipmentService;

    @Autowired
    HttpClient httpClient;

    @Autowired
    AutoFloor_Key_CheckNGMapper autoFloor_key_checkNGMapper;

    @Autowired
    AutoFloor_Key_CheCkUpMapper autoFloor_key_cheCkUpMapper;

    public List<String> D06_1FEquipmentList() {
        List<String> EquipmentList = new ArrayList<>();
        EquipmentList.add("PRINTER");
        EquipmentList.add("SPI");
        EquipmentList.add("PRE-AOI");
        EquipmentList.add("REFLOW");
        EquipmentList.add("POST-AOI");
        EquipmentList.add("49K");
        EquipmentList.add("50A");
        EquipmentList.add("51D");
        EquipmentList.add("RF_UF_OVEN");
        EquipmentList.add("AP_UF_OVEN");
        EquipmentList.add("38H");
        EquipmentList.add("導熱膠");
        EquipmentList.add("助焊劑");
        EquipmentList.add("ROUTER");
        EquipmentList.add("Wipas");
        EquipmentList.add("Pathloss");
        EquipmentList.add("AVI");
        return EquipmentList;
    }

    public List<String> D06_1FStationEquipmentList() {
        List<String> EquipmentList = new ArrayList<>();
        EquipmentList.add("Wipas");
        EquipmentList.add("Pathloss");
        return EquipmentList;
    }

    public Integer  QualityMonitoringData(String FloorName,String StartDate,String EndDate){
        Integer FloorQuality=0;
        List<QualityequipmentUN> FloorQualityequipmentData = new ArrayList<>();
        List<String> StationEquipment = D06_1FStationEquipmentList();
        for (String WorkStation : StationEquipment) {
            List<String> StationList = qualityEquipmentService.TestfloorLineALL(WorkStation);
            Map<String, List<QualityequipmentUN>> StationEquipmentMap = qualityEquipmentService.TestALLFloorLineListMap(FloorName, WorkStation);
            List<QualityequipmentUN> qualityequipmentUNS =new ArrayList<>();
            qualityequipmentUNS.addAll(StationEquipmentMap.get(WorkStation));
            for (String Station : StationList) {
                for (String Key : StationEquipmentMap.keySet()){
                    if (Station.equals(Key)){
                        qualityequipmentUNS.addAll(StationEquipmentMap.get(Key));
                    }
                }
            }
            if (qualityequipmentUNS!=null){
                FloorQualityequipmentData.addAll(qualityequipmentUNS);
            }
        }
        for (QualityequipmentUN floorQualityequipmentDatum : FloorQualityequipmentData) {
            Integer unNum = floorQualityequipmentDatum.getUNNum();
            if (unNum!=null) {
                if (unNum>0){
                    FloorQuality=1;
                }
            }
        }
        Integer ng = autoFloor_key_cheCkUpMapper.selectFloorNGNum(FloorName, "NG", StartDate, EndDate);

        if (ng>0){
            FloorQuality=1;
        }
        return FloorQuality;
    }

}
