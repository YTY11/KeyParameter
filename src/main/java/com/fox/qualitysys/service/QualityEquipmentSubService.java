package com.fox.qualitysys.service;


import com.fox.http.HttpClient;
import com.fox.qualitysys.entity.*;
import com.fox.qualitysys.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author
 * @create 2020-05-18 14:26
 */

@Service
public class QualityEquipmentSubService {

    @Autowired
    private AutoFloor_Fail_RetestMapper autoFloor_fail_retestMapper;

    @Autowired
    private QualityEquipmentService qualityEquipmentService;

    @Autowired
    private TE_Software_versionMapper te_software_versionMapper;

    @Autowired
    private HttpClient httpClient;

    @Autowired
    private Warning_List_SlotMapper warning_list_slotMapper;

    @Autowired
    private TE_Base_StationMapper te_base_stationMapper;


    @Autowired
    private AutoFloor_Key_CheCkUpMapper autoFloor_key_cheCkUpMapper;

    @Autowired
    private AutoFloor_Key_KeyPMSMapper autoFloor_key_keyPMSMapper;

    public List<AutoFloor_Key_CheCkUp> QualityEquipmentSubData(String WorkStation, String LineName, String StartDate, String EndDate) {
        List<AutoFloor_Key_CheCkUp> autoFloor_key_cheCkUps = autoFloor_key_cheCkUpMapper.selectWorkStationMessage(WorkStation, LineName,StartDate,EndDate);
        List<AutoFloor_Key_KeyPMS> processingPMSList = autoFloor_key_keyPMSMapper.selectPMSProcessing(WorkStation, LineName);
        for (AutoFloor_Key_CheCkUp autoFloor_key_cheCkUp : autoFloor_key_cheCkUps) {
            String keyUPMachinetype = autoFloor_key_cheCkUp.getMachinetype();
            for (AutoFloor_Key_KeyPMS autoFloor_key_keyPMS : processingPMSList) {
                String PMSmachinetype = autoFloor_key_keyPMS.getMachinetype();
                if (keyUPMachinetype!=null&&keyUPMachinetype.equals(PMSmachinetype)){
                    autoFloor_key_cheCkUp.setProcessing(autoFloor_key_keyPMS.getProcessing());
                    autoFloor_key_cheCkUp.setLinename2(autoFloor_key_keyPMS.getLinename2());
                    switch (keyUPMachinetype){
                        case "AVI":
                        case "PD":
                            autoFloor_key_cheCkUp.setProcessing("TEST");
                            break;

                    }
                }
            }

        }

        return autoFloor_key_cheCkUps;
    }

    public Boolean ValStrList(String val) {
        Boolean ValStr = null;
        try {
            Double.parseDouble(val);
            ValStr = true;
        } catch (Exception e) {
            ValStr = false;
        }

        return ValStr;
    }

    public List<EquipmentSub> TestALLUnStationData (List<QualityequipmentUN> qualityequipmentUNList, String FloorName) {
        List<EquipmentSub> equipmentSubList = new ArrayList<>();
        List<Warning_List_Slot> warning_list_slots = warning_list_slotMapper.TestALLUNStation( FloorName);
        List<TE_Software_version> Software_version = te_software_versionMapper.selectSoftwareVersion("D54");


        for (Warning_List_Slot warning_list_slot : warning_list_slots) {
            EquipmentSub equipmentSub = new EquipmentSub();
            String station = warning_list_slot.getStation();
            String sline = warning_list_slot.getSLine();
            String line = warning_list_slot.getLine();
            String stationNo = warning_list_slot.getStationNo();
            String warningDetail = warning_list_slot.getWarningDetail();
            String warningCase = warning_list_slot.getWarningCase();

            String Spec = "";
            for (TE_Software_version te_software_version : Software_version) {
                String versionstation = te_software_version.getStation();
                String stationOverlay = te_software_version.getStationOverlay();
                for (QualityequipmentUN qualityequipmentUN : qualityequipmentUNList) {
                     String quality = qualityequipmentUN.getQuality();
                    if (quality.equals(versionstation)) {
                        Spec += stationOverlay+ ";\n";
                    }
                }
            }
            for (QualityequipmentUN qualityequipmentUN : qualityequipmentUNList) {
                String quality = qualityequipmentUN.getQuality();
                if (quality.equals(station)) {
                    equipmentSub.setMachineType(line + " " + stationNo + "號機台");
                    equipmentSub.setUnJudge(0);
                    equipmentSub.setKey_Pms_CH("軟體版本");
                    equipmentSub.setSpec(Spec);
                    equipmentSub.setWarningCase(warningCase);
                    equipmentSub.setSLine(sline);
                    equipmentSub.setVal(warningDetail+";");
                    equipmentSub.setStation(station);
                    equipmentSubList.add(equipmentSub);

                }
            }
        }
        return equipmentSubList;
    }

    public List<EquipmentSub> TestUnStationData(String StationName, String WARNING_CASE, String FloorName, String LineName) {
        List<EquipmentSub> equipmentSubList = new ArrayList<>();
        List<Warning_List_Slot> warning_list_slots = warning_list_slotMapper.TestUNStation(WARNING_CASE, FloorName);
        List<TE_Software_version> Software_version = te_software_versionMapper.selectSoftwareVersion("D54");


        for (Warning_List_Slot warning_list_slot : warning_list_slots) {
            EquipmentSub equipmentSub = new EquipmentSub();
            String station = warning_list_slot.getStation();
            String line = warning_list_slot.getSLine();
            String stationNo = warning_list_slot.getStationNo();
            String warningDetail = warning_list_slot.getWarningDetail();
            String StationLine = line;
            String Spec = "";
            for (TE_Software_version te_software_version : Software_version) {
                String versionstation = te_software_version.getStation();
                String stationOverlay = te_software_version.getStationOverlay();
                if (StationName.equals(versionstation)) {
                    Spec += stationOverlay+ ";\n";
                }
            }
            if (line.length() >= 12) {
                StationLine = line.substring(0, 12);
            }

            if (StationName.equals(station)) {
                if (LineName.equals(StationLine)) {
                    equipmentSub.setMachineType(line + " " + stationNo + "號機台");
                    equipmentSub.setUnJudge(0);
                    equipmentSub.setKey_Pms_CH("軟體版本");
                    equipmentSub.setSpec(Spec);
                    equipmentSub.setVal(warningDetail+";");
                    equipmentSubList.add(equipmentSub);
                }
            }
        }
        return equipmentSubList;
    }

    public List<EquipmentSub> TestUndfStationData(String StationName, String FloorName, String LineName) {
        List<EquipmentSub> equipmentSubList = new ArrayList<>();
        List<TE_Base_Station> allBaseStation = te_base_stationMapper.getALLBaseStation(StationName, LineName);
        List<TE_Software_version> Software_version = te_software_versionMapper.selectSoftwareVersion("D54");

        for (TE_Base_Station te_base_station : allBaseStation) {
            EquipmentSub equipmentSub = new EquipmentSub();
            String line = te_base_station.getLine();
            String stationNo = te_base_station.getStationNo();
            String station = te_base_station.getStation();
            String Spec = "";
            String Val = "";
            for (TE_Software_version te_software_version : Software_version) {
                String versionstation = te_software_version.getStation();
                String stationOverlay = te_software_version.getStationOverlay();
                if (StationName.equals(versionstation)) {
                    Spec += stationOverlay + ";\n";
                    Val = stationOverlay;

                }
            }
            equipmentSub.setMachineType(line + " " + stationNo + "號機台");
            equipmentSub.setKey_Pms_CH("軟體版本");
            equipmentSub.setVal(Val);
            equipmentSub.setSpec(Spec);
            equipmentSub.setUnJudge(1);
            equipmentSubList.add(equipmentSub);
        }


        return equipmentSubList;

    }

    public String SPIValDispose(String val, String key_pms_en) {
        //SPI
        //高度
        String Height = "HEIGHT";
        //体积
        String Volume = "VOLUME";
        //X偏移量
        String Xoffset = "XOFFSET";
        //Y偏移量
        String Yoffset = "YOFFSET";
        Double ValDouble = 0.0;
        String ValStr = "";
        if (Height.equals(key_pms_en)) {
            if (val != null && !"".equals(val)) {
                ValDouble = Double.parseDouble(val);
            }
            BigDecimal ValBie = new BigDecimal(ValDouble);
            ValDouble = ValBie.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            ValStr = ValDouble.toString();
        } else if (Volume.equals(key_pms_en)) {
            if (val != null && !"".equals(val)) {
                ValDouble = Double.parseDouble(val);
            }
            BigDecimal ValBie = new BigDecimal(ValDouble);
            ValDouble = ValBie.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            ValStr = ValDouble.toString();
        } else if (Xoffset.equals(key_pms_en)) {
            if (val != null && !"".equals(val)) {
                ValDouble = Double.parseDouble(val);
            }
            BigDecimal ValBie = new BigDecimal(ValDouble);
            ValDouble = ValBie.setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue();
            ValStr = ValDouble.toString();
        } else if (Yoffset.equals(key_pms_en)) {
            if (val != null && !"".equals(val)) {
                ValDouble = Double.parseDouble(val);
            }
            BigDecimal ValBie = new BigDecimal(ValDouble);
            ValDouble = ValBie.setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue();
            ValStr = ValDouble.toString();
        } else {
            ValStr = val;
        }

        return ValStr;
    }



}
