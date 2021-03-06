package com.fox.testsys.service;


import com.fox.testsys.entity.AutoFloor_Target;
import com.fox.testsys.entity.TestKeyParameter;
import com.fox.testsys.entity.Warubg_List_Slot;
import com.fox.testsys.mapper.Warubg_List_SlotMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TestKeyParameterService {

    @Autowired
    Warubg_List_SlotMapper warubg_list_slotMapper;
    @Autowired
    TestEquipmentCapacityService testEquipmentCapacityService;

    public Map<String, Object> KeyParameterData(List<AutoFloor_Target> LineList, String FloorName, String StartDate, String EndDate) {
        Map<String, Object> KeyParameterMap = new HashMap<>();
        String Floor = FloorName.substring(0, 3);
        String FloorNum = FloorName.substring(3, 5);
        FloorName = Floor + "-" + FloorNum;
        String Product = "D42";
        List<TestKeyParameter> KeyParameterList = new ArrayList<>();
        TestKeyParameter KeyParameterAll = new TestKeyParameter();

        List<Warubg_List_Slot> warubg_list_slots = warubg_list_slotMapper.KeyParameterAll(FloorName);
        double PathlossHealthRateAll = 0.0;
        double WipasHealthRateAll = 0.0;
        int  MachineAllNum = 0;
        int fori=2;
        for (int i = 0; i < LineList.size(); i++) {
            AutoFloor_Target autoFloor_target = LineList.get(i);
            TestKeyParameter KeyParameter = new TestKeyParameter();
            String lineName = autoFloor_target.getLineName();
            BigDecimal dTarget = autoFloor_target.getdTarget();
            String lineNum = lineName.substring(lineName.length() - 2);
            KeyParameter.setDTarget(dTarget.intValue());
            KeyParameter.setLineName(lineName);
            int EquipmentAllNum = 0;
            int PathlossHealthNum = 0;
            int WipasHealthNum = 0;
            double PathlossHealthRate = 0.0;
            double WipasHealthRate = 0.0;
            if (i<fori){
                for (Warubg_List_Slot warubg_list_slot : warubg_list_slots) {
                    String line = warubg_list_slot.getLine();
                    if (lineNum.equals(line)){
                        EquipmentAllNum= warubg_list_slot.getTTL();
                        WipasHealthNum = warubg_list_slot.getSW_OK();
                        PathlossHealthNum= warubg_list_slot.getLOSS_OK();
                        MachineAllNum+=EquipmentAllNum;
                        PathlossHealthRateAll+=PathlossHealthNum;
                        WipasHealthRateAll+=WipasHealthNum;
                    }
                }
            }
            if (EquipmentAllNum != 0) {
                PathlossHealthRate = PathlossHealthNum / (double) EquipmentAllNum;
                WipasHealthRate = WipasHealthNum / (double) EquipmentAllNum;
            }


            BigDecimal PathlossHealthRateBig = new BigDecimal(PathlossHealthRate * 100);
            PathlossHealthRate = PathlossHealthRateBig.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            BigDecimal WipasHealthRateBig = new BigDecimal(WipasHealthRate * 100);
            WipasHealthRate = WipasHealthRateBig.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            KeyParameter.setPathlossRate(PathlossHealthRate);
            KeyParameter.setWipasRate(WipasHealthRate);
            KeyParameterList.add(KeyParameter);
        }

        if (MachineAllNum != 0) {
            PathlossHealthRateAll = PathlossHealthRateAll / (double) MachineAllNum;
            WipasHealthRateAll = WipasHealthRateAll / (double) MachineAllNum;
        }
        BigDecimal PathlossHealthRateAllBig = new BigDecimal(PathlossHealthRateAll*100);
        PathlossHealthRateAll = PathlossHealthRateAllBig.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

        BigDecimal WipasHealthRateAllBig = new BigDecimal(WipasHealthRateAll*100);
        WipasHealthRateAll = WipasHealthRateAllBig.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        KeyParameterAll.setWipasRate(WipasHealthRateAll);
        KeyParameterAll.setPathlossRate(PathlossHealthRateAll);




        KeyParameterMap.put("KeyParameterAll", KeyParameterAll);
        KeyParameterMap.put("KeyParameterList", KeyParameterList);

        return KeyParameterMap;
    }


}
