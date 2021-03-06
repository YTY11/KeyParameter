package com.fox.testsys.controller;

import com.fox.testsys.entity.AmountsUPH;
import com.fox.testsys.entity.AutoFloor_Target;
import com.fox.testsys.entity.TestEquipmentHealth;
import com.fox.testsys.entity.TestJsonData;
import com.fox.testsys.service.TestAProductService;
import com.fox.testsys.service.TestEquipmentCapacityService;
import com.fox.testsys.service.TestKeyParameterService;
import com.fox.testsys.utility.AutoFloorDate;
import com.fox.testsys.utility.CooKieUtil;
import com.google.gson.Gson;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * @author
 * @create 2020-07-27 16:18
 */
@RestController
@CrossOrigin
public class TestData {

    @Autowired
    TestAProductService testAProductService;

    @Autowired
    AutoFloorDate autoFloorDate;

    @Autowired
    CooKieUtil cooKieUtil;

    @Autowired
    TestEquipmentCapacityService testEquipmentCapacityService;
    @Autowired
    TestKeyParameterService testKeyParameterService;

    //机种名称
    private static final String LOT_PRODUCT = "Lotus";
    private static final String MAC_PRODUCT = "Raptor";
    private static final String PHA_PRODUCT = "Pha";
    //班别
    private static final String TwoTimeType = "two";

    @RequestMapping(value = "testdata",method = RequestMethod.GET)
    public String  TestData(@RequestParam(value = "floorName", required = true, defaultValue = "D061F")String FloorName,
                          @RequestParam(value = "timeType", required = true, defaultValue = "Day") String TypeTime,
                          @RequestParam(value = "product", required = true, defaultValue = "All") String Product){

        Gson gson=new Gson();
        /*数据开始时间*/
        String DataStartTime = null;
        /*数据结束时间*/
        String DataEndTime = null;
        Map<String, Object> AutoFloorDateMap = autoFloorDate.AutoFloorDate(TypeTime);
        /*当前日期*/
        Date schedule = (Date) AutoFloorDateMap.get("schedule");
        /*工作时间*/
        Integer actionMinuteD = (Integer) AutoFloorDateMap.get("actionMinuteD");
        //運行時間
        Integer TimeGNumber = (Integer) AutoFloorDateMap.get("TimeGNumber");
        //产能浮动数据
        Integer FloatNum = (Integer) AutoFloorDateMap.get("FloatNum");
        /*过24时 时间变量*/
        String StartDateStr = (String) AutoFloorDateMap.get("StartDateStr");
        String EndDateStr = (String) AutoFloorDateMap.get("EndDateStr");
        //开始时间
        String twoDateStartDate = (String) AutoFloorDateMap.get("twoDateStartDate");
        //结束时间
        String twoDateEndDate = (String) AutoFloorDateMap.get("twoDateEndDate");
        /*时*/
        Integer Htime = (Integer) AutoFloorDateMap.get("Htime");
        if (TwoTimeType.equals(TypeTime)) {
            DataStartTime = twoDateStartDate;
            DataEndTime = twoDateEndDate;
        } else {
            DataStartTime = StartDateStr;
            DataEndTime = EndDateStr;
        }
        ////////////////////////////Lot
        List<AutoFloor_Target> LotautoLine = new ArrayList<>();
        LotautoLine = testAProductService.LineData(FloorName, FloorName, schedule, LOT_PRODUCT, TypeTime, "","");
        ////////////////////////////Mac
        List<AutoFloor_Target> MacautoLine = new ArrayList<>();
        MacautoLine = testAProductService.LineData(FloorName, FloorName, schedule, MAC_PRODUCT, TypeTime, "","");
        ////////////////////////////Pha
        List<AutoFloor_Target> PhaautoLine = new ArrayList<>();
        PhaautoLine = testAProductService.LineData(FloorName, FloorName, schedule, PHA_PRODUCT, TypeTime, "","");
        ////////////////////////////////////////設備健康監控\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\


        //-----*-----Mac-----*-----------
        List<TestEquipmentHealth> MacTestEquipmentHealths = new ArrayList<>();
        TestEquipmentHealth MacAllTestEquipmentHealths = new TestEquipmentHealth();
        //-----*-----Mac-机故率-----*-----------
        Map<String, Object> MacMachineRateMap = testAProductService.MachineRate(MacautoLine, TypeTime, twoDateStartDate, twoDateEndDate, FloorName, actionMinuteD);
        List<TestEquipmentHealth> MacmachineRateList = (List<TestEquipmentHealth>) MacMachineRateMap.get("MachineRateList");
        double MacmachineRateAll = (double) MacMachineRateMap.get("MachineRateAll");
        MacAllTestEquipmentHealths.setMachineRate(MacmachineRateAll);
        //----*-----Mac-誤測率-----*-----------
        Map<String, Object> MacTestMachine = new HashMap<>();
        MacTestMachine = testAProductService.getTestMachine(MacautoLine, FloorName, twoDateStartDate, twoDateEndDate, TypeTime, MAC_PRODUCT);
        List<AmountsUPH> autoFloor_MacreteList = (List<AmountsUPH>) MacTestMachine.get("autoFloor_reteList");
        AmountsUPH amountsMacUPHAll = (AmountsUPH) MacTestMachine.get("amountsUPHAll");
        //MacALL誤測率
        MacAllTestEquipmentHealths.setMisdetetRate(amountsMacUPHAll.getMISDETET());


        Double machineRate = MacAllTestEquipmentHealths.getMachineRate();
        Double misdetetRate = MacAllTestEquipmentHealths.getMisdetetRate();

        TestJsonData testjsonData=new TestJsonData();
        testjsonData.setFloorName(FloorName);
        testjsonData.setFaiareRate(machineRate);
        testjsonData.setMisdetectionRate(misdetetRate);

        String JsonData = gson.toJson(testjsonData);


        return JsonData;
    }


}
