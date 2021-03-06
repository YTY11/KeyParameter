package com.fox.testsys.controller;

import com.fox.testsys.entity.*;
import com.fox.testsys.service.TestAProductService;
import com.fox.testsys.service.TestCapacityMessageService;
import com.fox.testsys.utility.AutoFloorDate;
import com.fox.testsys.utility.TimeUtility;
import com.google.gson.Gson;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 测试能力消息来讲
 *
 * @author wu
 * @create 2020-04-23 13:47
 * @date 2020/10/14
 */
@Controller
public class TestCapacityMessageContr {

    @Autowired
    AutoFloorDate autoFloorDate;
    @Autowired
    TestAProductService testAProductService;

    @Autowired
    TestCapacityMessageService testCapacityMessageService;

    @Autowired
    TimeUtility timeUtility;
    //班别
    private static final String TwoTimeType = "two";
    private static final String DayTimeType = "Day";
    private static final String NightTimeType = "Night";
    private static final String YestrerDayTimeType = "Yesterday";

    @RequestMapping("capacitymessage")
    public String TestCapacityMessageContr(@RequestParam(value = "Linename", required = false,defaultValue = "D061FARF01") String Linename,
                                           @RequestParam(value = "TypeTime", required = false, defaultValue = "two") String TypeTime, Map map) {
        map.put("Linename", Linename);
        /*数据开始时间*/
        String DataStartTime = null;
        /*数据结束时间*/
        String DataEndTime = null;
        Map<String, Object> AutoFloorDateMap = autoFloorDate.AutoFloorDate(TypeTime);
        /*当前日期*/
        Date schedule = (Date) AutoFloorDateMap.get("schedule");
        /*工作时间*/
        Integer actionMinuteD = (Integer) AutoFloorDateMap.get("actionMinuteD");
        /*过24时 时间变量*/
        String StartDateStr = (String) AutoFloorDateMap.get("StartDateStr");
        String EndDateStr = (String) AutoFloorDateMap.get("EndDateStr");
        DataStartTime = StartDateStr;
        DataEndTime = EndDateStr;
        //浮动数据
        Integer FloatNum = 200;
        AutoFloor_Target targetsum = testCapacityMessageService.AloneLineData(Linename, schedule, TypeTime);
        /*线体小时目标*/
        int hTarget = 0;
        /*线体楼层名称*/
        String Floorname = "";
        /*线体天目标*/
        BigDecimal DTarget = new BigDecimal(0);
        /*机种工站*/
        String Productname = "";
        /*排配时长*/
        double WorkingHours = 0;
        if (targetsum != null) {
            BigDecimal hTargets = targetsum.gethTarget();
            hTarget = hTargets.intValue();
            Floorname = targetsum.gettFloor();
            DTarget = targetsum.getdTarget();
            Productname = targetsum.gettProduct();
            WorkingHours = targetsum.getWorkingHours().doubleValue();
        }
        map.put("DTarget", DTarget);
        map.put("HTarget", hTarget);
        Map<String, Object> CapacityAutoMap = testCapacityMessageService.CapacityAutoData(Linename, TypeTime, StartDateStr, EndDateStr, Productname, hTarget, actionMinuteD);
        SumNumber ttlCapacityAutoData = (SumNumber) CapacityAutoMap.get("TTLCapacityAutoData");
        List<SumNumber> capacityAutoDataList = (List<SumNumber>) CapacityAutoMap.get("CapacityAutoDataList");
        Double ActionTarget = testCapacityMessageService.ActionTarget(DTarget.intValue(), TypeTime, WorkingHours, hTarget, actionMinuteD, FloatNum);
        ttlCapacityAutoData.setCell_A(ActionTarget.intValue());
        ttlCapacityAutoData.setDTarget(DTarget.intValue());
        capacityAutoDataList = testCapacityMessageService.capacityAutoDataTarget(capacityAutoDataList, TypeTime, hTarget, WorkingHours);

        map.put("ttlCapacityAutoData", ttlCapacityAutoData);
        map.put("capacityAutoDataList", capacityAutoDataList);
        map.put("DataStartTime", DataStartTime);
        map.put("DataEndTime", DataEndTime);

        List<AutoFloor_UPH> MachineDataList = testCapacityMessageService.MachineData(ttlCapacityAutoData, Floorname, Linename, Productname, StartDateStr, EndDateStr, hTarget);
        for (AutoFloor_UPH autoFloor_uph : MachineDataList) {
            Integer stationActionTarget = autoFloor_uph.getStationActionTarget();
            if (stationActionTarget != null) {
                stationActionTarget = testCapacityMessageService.ActionTarget(DTarget.intValue(), TypeTime, WorkingHours, stationActionTarget, actionMinuteD, 0).intValue();
            }
            autoFloor_uph.setStationActionTarget(stationActionTarget);
        }
        map.put("MachineDataList", MachineDataList);

        Map<String, List<AmountsUPH>> MachineDataSubMap = testCapacityMessageService.MachineDataSub(ttlCapacityAutoData, Productname, StartDateStr, EndDateStr, Linename);
        map.put("MachineDataSubMap", MachineDataSubMap);
        //////////////////////待板時間分析\\\\\\\\\\\\\\\\\\\\\\
        Map<String, List<WaitPlateTimeData>> waitPlateTimeDataMap = testCapacityMessageService.waitPlateTime(StartDateStr, EndDateStr, Floorname, Linename, hTarget);
        List<WaitPlateTimeData> daywaitPlateTimeList = waitPlateTimeDataMap.get("DaywaitPlateTimeList");
        Gson daywaitPlateTimeListgson = new Gson();
        String daywaitPlateTimeListJson = daywaitPlateTimeListgson.toJson(daywaitPlateTimeList);
        map.put("daywaitPlateTimeListJson", daywaitPlateTimeListJson);
        List<WaitPlateTimeData> NightwaitPlateTimeList = waitPlateTimeDataMap.get("NightwaitPlateTimeList");
        String NightwaitPlateTimeListJson = daywaitPlateTimeListgson.toJson(NightwaitPlateTimeList);
        map.put("NightwaitPlateTimeListJson", NightwaitPlateTimeListJson);
        int DayTypeTime = 0;
        int NightTypeTime = 0;
        if (TwoTimeType.equals(TypeTime)) {
            if (timeUtility.Timequantum("08:30", "20:30")) {
                DayTypeTime = 1;
            } else {
                NightTypeTime = 1;
            }
        } else if (DayTimeType.equals(TypeTime)) {
            DayTypeTime = 1;
        } else if (NightTimeType.equals(TypeTime)) {
            NightTypeTime = 1;
        } else if (YestrerDayTimeType.equals(TypeTime)) {
            DayTypeTime = 1;
            NightTypeTime = 1;
        }

        map.put("Productname",Productname);
        map.put("DayTypeTime", DayTypeTime);
        map.put("NightTypeTime", NightTypeTime);
        map.put("DataStartTime", DataStartTime);
        map.put("DataEndTime", DataEndTime);
        return "testsys/testcapacitymessage";
    }

}





