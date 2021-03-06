package com.fox.testsys.controller;

import com.fox.testsys.entity.*;
import com.fox.testsys.service.TestCapacityMessageService;
import com.fox.testsys.service.TestEquipmentHealthService;
import com.fox.testsys.utility.AutoFloorDate;
import com.google.gson.Gson;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author
 * @create 2020-04-23 13:47
 */
@Controller
public class TestEquipmentHealthContr {

    @Autowired
    TestEquipmentHealthService testEquipmentHealthService;
    @Autowired
    AutoFloorDate autoFloorDate;
    @Autowired
    TestCapacityMessageService testCapacityMessageService;

    @RequestMapping("equipmenthealth")
    public String TestEquipmentHealthContr(@RequestParam(value = "Linename", required = true) String Linename,
                                           @RequestParam(value = "TypeTime", required = false, defaultValue = "two") String TypeTime, Map map) {
        /*数据开始时间*/
        String DataStartTime = null;
        /*数据结束时间*/
        String DataEndTime = null;
        map.put("Linename", Linename);
        map.put("TypeTime", TypeTime);
        Map<String, Object> AutoFloorDateMap = autoFloorDate.AutoFloorDate(TypeTime);
        /*当前日期*/
        Date schedule = (Date) AutoFloorDateMap.get("schedule");
        /*工作时间*/
        Integer actionMinuteD = (Integer) AutoFloorDateMap.get("actionMinuteD");
        /*过24时 时间变量*/
        String StartDateStr = (String) AutoFloorDateMap.get("twoDateStartDate");
        String EndDateStr = (String) AutoFloorDateMap.get("twoDateEndDate");
        DataStartTime = StartDateStr;
        DataEndTime = EndDateStr;
        AutoFloor_Target targetsum = testCapacityMessageService.AloneLineData(Linename, schedule, TypeTime);
        /*线体楼层名称*/
        String Floorname = "";
        /*机种工站*/
        String Productname = "";
        Double WorkingHours = 0.0;
        if (targetsum != null) {
            Floorname = targetsum.gettFloor();
            Productname = targetsum.gettProduct();
            WorkingHours = targetsum.getWorkingHours().doubleValue();
        }
        List<AutoFloor_Rate> EquipmentHealthRate = testEquipmentHealthService.TestEquipmentHealthRateSubData(Productname, StartDateStr, EndDateStr, Floorname, Linename);
        Map<String, Double> HealthRateYieldTarget = testEquipmentHealthService.HealthRateYieldTarget();
        Map<String, Double> HealthRateMisdetetTarget = testEquipmentHealthService.HealthRateMisdetetTarget();
        Map<String, Double> HealthRateUnknownTarget = testEquipmentHealthService.HealthRateUnknownTarget();
        Map<String, Double> HealthRateFPYTarget = testEquipmentHealthService.HealthRateFPYTarget();
        List<AutoFloor_Rate_Machine> UNFPYRateData = testEquipmentHealthService.EquipmentHealthUNFPYRateData(Productname, StartDateStr, EndDateStr, Floorname, Linename);
        //设备机故率
        Map<String, List<MachineRateData>> machineRateDataMap = testEquipmentHealthService.MachineRateDataList(Floorname,Linename, TypeTime, WorkingHours, StartDateStr, EndDateStr, actionMinuteD);
        List<MachineRateData> machineRateDataList = machineRateDataMap.get("machineRateDataList");
        Gson machineRateDatagson = new Gson();
        String machineRateDatajson = machineRateDatagson.toJson(machineRateDataList);
        //機故分析
        List<MachineRateData> OverstepMachineRateList = machineRateDataMap.get("OverstepMachineRateList");
        List<MachineRateAnalyze> machineRateAnalyzes = testEquipmentHealthService.MachineRateAnalyzeDataList(OverstepMachineRateList, TypeTime, WorkingHours, StartDateStr, EndDateStr, Floorname, actionMinuteD);
        map.put("EquipmentHealthRate", EquipmentHealthRate);
        //超時主板確認信息
        List<OutTimePathData> outTimePathData = testEquipmentHealthService.OutTimePathData(EquipmentHealthRate, StartDateStr, EndDateStr, Floorname);
        map.put("outTimePathData", outTimePathData);
        map.put("machineRateAnalyzes", machineRateAnalyzes);
        map.put("machineRateDatajson", machineRateDatajson);
        map.put("UNFPYRateData", UNFPYRateData);
        map.put("HealthRateUnknownTarget", HealthRateUnknownTarget);
        map.put("HealthRateFPYTarget", HealthRateFPYTarget);
        map.put("HealthRateMisdetetTarget", HealthRateMisdetetTarget);
        map.put("HealthRateYieldTarget", HealthRateYieldTarget);
        map.put("DataStartTime", DataStartTime);
        map.put("DataEndTime", DataEndTime);
        return "testsys/testequipmenthealth";
    }

}
