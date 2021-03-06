package com.fox.testsys.controller;


import com.fox.testsys.entity.*;
import com.fox.testsys.service.TestAProductService;
import com.fox.testsys.service.TestEquipmentCapacityService;
import com.fox.testsys.service.TestKeyParameterService;
import com.fox.testsys.utility.AutoFloorDate;
import com.fox.testsys.utility.CooKieUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.*;

/**
 * @author
 * @create 2020-04-22 15:38
 */
@Controller
public class TestAProductsContr {

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
    private static  String LOT_PRODUCT = "Lotus";
    private static final String MAC_PRODUCT = "Raptor";
    private static final String PHA_PRODUCT = "Pha";
    //班别
    private static final String TwoTimeType = "two";
    private static final String DayTimeType = "Day";
    private static final String NightTimeType = "Night";
    private static final String YestrerDayTimeType = "Yesterday";


    @RequestMapping("testproducts")
    public String TestProductsContr(HttpServletRequest request, HttpServletResponse response, HttpSession Session,
                                    @RequestParam(value = "FloorName", required = true, defaultValue = "D061F") String FloorName,
                                    @RequestParam(value = "TimeType", required = true, defaultValue = "Day") String TypeTime,
                                    @RequestParam(value = "Product", required = true, defaultValue = "All") String Product,
                                    @RequestParam(value = "DateTime", required = true ,defaultValue = "") String DateTime, Map map) {



        List<String> testFloorList = new ArrayList<>();
        testFloorList.add("D061F");
        Session.setAttribute("FloorName", FloorName);
        String SessionFloorName = (String) Session.getAttribute("FloorName");
        Session.setAttribute("FloorName", SessionFloorName);
        FloorName = SessionFloorName;

        map.put("TypeTime", TypeTime);
        String TEFloorName ="";
        if (FloorName.length()>=5){
             TEFloorName =FloorName.substring(0, 3) + "-" + FloorName.substring(3);
        }

        map.put("TEFloorName", TEFloorName);
        map.put("FloorName", FloorName);
        Map<String, Integer> floor_proudct = autoFloorDate.getFloor_Proudct(FloorName);
        if ("K051F".equals(FloorName)){
            LOT_PRODUCT ="Apollo";
        }
        Integer Lot = floor_proudct.get("Lot");
        Integer Mac = floor_proudct.get("Mac");
        Integer Pha = floor_proudct.get("Pha");

        map.put("Lot", Lot);
        map.put("Mac", Mac);
        map.put("Pha", Pha);
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
        //查询时间

        String InquiryStartDate="";
        String InquiryEndDate="";
        if(!"".equals(DateTime)){
            String[] split = DateTime.split("-");
            InquiryStartDate=split[0];
            InquiryEndDate=split[1];
            twoDateStartDate=InquiryStartDate+"08:30:00";
            twoDateEndDate=InquiryEndDate+" 08:30:00";
            StartDateStr=InquiryStartDate+"08:30:00";
            EndDateStr=InquiryEndDate+" 08:30:00";
            DataStartTime = InquiryStartDate+"00:00:00";
            DataEndTime =InquiryEndDate+" 00:00:00";

            /*if (InquiryStartDate!=null&&InquiryStartDate.substring(0,10).equals(InquiryEndDate.substring(1,11))){
                twoDateStartDate=InquiryStartDate+"08:30:00";
                twoDateEndDate=InquiryEndDate+" 20:30:00";
                StartDateStr=InquiryStartDate+"08:30:00";
                EndDateStr=InquiryEndDate+" 20:30:00";
                DataStartTime = InquiryStartDate+"00:00:00";
                DataEndTime =InquiryEndDate+" 00:10:00";
            }*/


            TypeTime="SelectData";
            map.put("DateTime",DateTime);
        }

        List<String> ProductList = testAProductService.TTLProductList(schedule, schedule, FloorName);



        ////////////////////////////Lot
        List<AutoFloor_Target> LotautoLine = new ArrayList<>();
        List<AutoFloor_Target> TLLLotautoLine = new ArrayList<>();

        //System.out.println(FloorName+"--"+FloorName+"--"+schedule+"--"+LOT_PRODUCT+"--"+TypeTime+"--"+DataStartTime+"--"+DataEndTime);
        LotautoLine = testAProductService.LineData(FloorName, FloorName, schedule, LOT_PRODUCT, TypeTime, DataStartTime,DataEndTime);
        TLLLotautoLine = testAProductService.LineData(FloorName, "TTL", schedule, LOT_PRODUCT, TypeTime,DataStartTime,DataEndTime);
        //有产能排配的线体的数量
        int LotautoTargetsize = 0;
        for (AutoFloor_Target autoDayT0 : LotautoLine) {
            if (autoDayT0.getdTarget() != null) {
                if (autoDayT0.getdTarget().intValue() > 0) {
                    LotautoTargetsize++;
                }
            }
        }
        map.put("LotautoTargetsize", LotautoTargetsize);
        ////////////////////////////Mac
        List<AutoFloor_Target> MacautoLine = new ArrayList<>();
        List<AutoFloor_Target> TLLMacautoLine = new ArrayList<>();
        MacautoLine = testAProductService.LineData(FloorName, FloorName, schedule, MAC_PRODUCT, TypeTime,DataStartTime,DataEndTime);
        TLLMacautoLine = testAProductService.LineData(FloorName, "TTL", schedule, MAC_PRODUCT, TypeTime,DataStartTime,DataEndTime);
        //有产能排配的线体的数量
        int MacautoTargetsize = 0;
        for (AutoFloor_Target autoDayT0 : MacautoLine) {
            if (autoDayT0.getdTarget() != null) {
                if (autoDayT0.getdTarget().intValue() > 0) {
                    MacautoTargetsize++;
                }
            }
        }
        map.put("MacautoTargetsize", MacautoTargetsize);
        ////////////////////////////Pha
        List<AutoFloor_Target> PhaautoLine = new ArrayList<>();
        List<AutoFloor_Target> TLLPhaautoLine = new ArrayList<>();
        PhaautoLine = testAProductService.LineData(FloorName, FloorName, schedule, PHA_PRODUCT, TypeTime, DataStartTime,DataEndTime);
        TLLPhaautoLine = testAProductService.LineData(FloorName, "TTL", schedule, PHA_PRODUCT, TypeTime, DataStartTime,DataEndTime);
        //有产能排配的线体的数量
        int PhaautoTargetsize = 0;
        for (AutoFloor_Target autoDayT0 : PhaautoLine) {
            if (autoDayT0.getdTarget() != null) {
                if (autoDayT0.getdTarget().intValue() > 0) {
                    PhaautoTargetsize++;
                }
            }
        }
        map.put("PhaautoTargetsize", PhaautoTargetsize);

        /*--------------***----------LotOutCondition-----------**---------------------*/
        List<AutoFloorData> LotOutConditionList = new ArrayList<AutoFloorData>();
        /* TLL天目标*/
        /*初始化实际输出值*/
        int LotActuaPutSum = 0;
        int LotActuaOutSum = 0;
        double LotTarGetSumdouble = 0.0;
        Map<String, Object> LotoutCondition = testAProductService.getOutCondition(LotautoLine, StartDateStr, EndDateStr, actionMinuteD, FloatNum, TypeTime);
        LotOutConditionList = (List<AutoFloorData>) LotoutCondition.get("OutConditionList");
        LotActuaPutSum = (Integer) LotoutCondition.get("ActuaPutSum");
        LotActuaOutSum = (Integer) LotoutCondition.get("ActuaOutSum");
        LotTarGetSumdouble = (Double) LotoutCondition.get("TarGetSumdouble");

        /*TLL达成率*/
        double LotachievingRatesum = 0.00;
        //TLL目标
        int LotTargetsum = 0;
        for (AutoFloor_Target autofloor_target : TLLLotautoLine) {
            BigDecimal bigDecimal = autofloor_target.getdTarget();
            if (bigDecimal != null) {
                LotTargetsum = Integer.parseInt(bigDecimal.toString());
            }
        }
        if (LotTargetsum != 0) {
            LotachievingRatesum = ((double) LotActuaOutSum / LotTargetsum);
        }

        AutoFloorData LotTLLOutCondition = new AutoFloorData();
        LotTLLOutCondition.setTProduct(LOT_PRODUCT);
        /*TLL天目标*/
        LotTLLOutCondition.setdTargetsum(LotTargetsum);

        /*TLL实际投入*/
        LotTLLOutCondition.setActualPutSum(LotActuaPutSum);
        /*TLL实际产出*/
        LotTLLOutCondition.setActualOutsum(LotActuaOutSum);
        /*TLL达成率*/
        LotTLLOutCondition.setActualDatasum(LotachievingRatesum);
        LotTLLOutCondition.setAchievingRatesum(LotachievingRatesum);
        map.put("LotOutConditionList", LotOutConditionList);
        map.put("LotTLLOutCondition", LotTLLOutCondition);
        map.put("LotActuaOutSum", LotTarGetSumdouble);

        /*--------------------**------------------MacOutCondition------------**--------------------*/

        List<AutoFloorData> MacOutConditionList = new ArrayList<AutoFloorData>();
        /* TLL天目标*/
        /*初始化实际输出值*/
        int MacActuaPutSum = 0;
        int MacActuaOutSum = 0;
        double MacTarGetSumdouble = 0.0;
        Map<String, Object> MacoutCondition = testAProductService.getOutCondition(MacautoLine, StartDateStr, EndDateStr, actionMinuteD, FloatNum, TypeTime);
        MacOutConditionList = (List<AutoFloorData>) MacoutCondition.get("OutConditionList");
        MacActuaPutSum = (Integer) MacoutCondition.get("ActuaPutSum");
        MacActuaOutSum = (Integer) MacoutCondition.get("ActuaOutSum");
        MacTarGetSumdouble = (Double) MacoutCondition.get("TarGetSumdouble");
        /*TLL达成率*/
        double MacachievingRatesum = 0.00;
        //TLL目标
        int MacTargetsum = 0;
        for (AutoFloor_Target autofloor_target : TLLMacautoLine) {
            BigDecimal bigDecimal = autofloor_target.getdTarget();
            if (bigDecimal != null) {
                MacTargetsum = Integer.parseInt(bigDecimal.toString());
            }
        }
        if (MacTargetsum != 0) {
            MacachievingRatesum = ((double) MacActuaOutSum / MacTargetsum);
        }
        AutoFloorData MacTLLOutCondition = new AutoFloorData();
        MacTLLOutCondition.setTProduct(MAC_PRODUCT);
        /*TLL天目标*/
        MacTLLOutCondition.setdTargetsum(MacTargetsum);
        /*TLL实际投入*/
        MacTLLOutCondition.setActualPutSum(MacActuaPutSum);
        /*TLL实际产出*/
        MacTLLOutCondition.setActualOutsum(MacActuaOutSum);
        /*TLL达成率*/
        MacTLLOutCondition.setActualDatasum(MacachievingRatesum);
        MacTLLOutCondition.setAchievingRatesum(MacachievingRatesum);


        map.put("MacOutConditionList", MacOutConditionList);
        map.put("MacTLLOutCondition", MacTLLOutCondition);
        map.put("MacActuaOutSum", MacTarGetSumdouble);

        /*--------------------**------------------PhaOutCondition------------**--------------------*/

        List<AutoFloorData> PhaOutConditionList = new ArrayList<AutoFloorData>();
        /* TLL天目标*/
        /*初始化实际输出值*/
        int PhaActuaPutSum = 0;
        int PhaActuaOutSum = 0;
        double PhaTarGetSumdouble = 0.0;
        Map<String, Object> PhaoutCondition = testAProductService.getOutCondition(PhaautoLine, StartDateStr, EndDateStr, actionMinuteD, FloatNum, TypeTime);
        PhaOutConditionList = (List<AutoFloorData>) PhaoutCondition.get("OutConditionList");
        PhaActuaPutSum = (Integer) PhaoutCondition.get("ActuaPutSum");
        PhaActuaOutSum = (Integer) PhaoutCondition.get("ActuaOutSum");
        PhaTarGetSumdouble = (Double) PhaoutCondition.get("TarGetSumdouble");

        /*TLL达成率*/
        double PhaachievingRatesum = 0.00;
        //TLL目标
        int PhaTargetsum = 0;
        for (AutoFloor_Target autofloor_target : TLLPhaautoLine) {
            BigDecimal bigDecimal = autofloor_target.getdTarget();
            if (bigDecimal != null) {
                PhaTargetsum = Integer.parseInt(bigDecimal.toString());
            }
        }
        if (PhaTargetsum != 0) {
            PhaachievingRatesum = ((double) PhaActuaOutSum / PhaTargetsum);
        }

        AutoFloorData PhaTLLOutCondition = new AutoFloorData();
        PhaTLLOutCondition.setTProduct(PHA_PRODUCT);
        /*TLL天目标*/
        PhaTLLOutCondition.setdTargetsum(PhaTargetsum);
        /*TLL实际投入*/
        PhaTLLOutCondition.setActualPutSum(PhaActuaPutSum);
        /*TLL实际产出*/
        PhaTLLOutCondition.setActualOutsum(PhaActuaOutSum);
        /*TLL达成率*/
        PhaTLLOutCondition.setActualDatasum(PhaachievingRatesum);
        PhaTLLOutCondition.setAchievingRatesum(PhaachievingRatesum);
        map.put("PhaOutConditionList", PhaOutConditionList);
        map.put("PhaTLLOutCondition", PhaTLLOutCondition);
        map.put("PhaActuaOutSum", PhaTarGetSumdouble);
        FloatNum = (Integer) LotoutCondition.get("FloatNum");
        map.put("FloatNum", FloatNum);
        ////////////////////////////////////////設備運行監控\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
        /*Lot*/
        /*Lot測試冶具*/
        List<EquipmentCapacity> LotEquipmentDataList = new ArrayList<>();
        EquipmentCapacity LotEquipmentALLData = new EquipmentCapacity();
        Map<String, Object> LotEquipmentCapacityMap = testEquipmentCapacityService.EquipmentCapacityData(LotautoLine,TEFloorName);
        List<EquipmentCapacity> LotEquipmentHealthList = (List<EquipmentCapacity>) LotEquipmentCapacityMap.get("TestEquipmentHealthList");
        EquipmentCapacity LotEquipmentHealthAll = (EquipmentCapacity) LotEquipmentCapacityMap.get("TestEquipmentHealthAll");
        LotEquipmentALLData.setTestEquipmentAllNum(LotEquipmentHealthAll.getTestEquipmentAllNum());
        LotEquipmentALLData.setTestEquipmentHealthNum(LotEquipmentHealthAll.getTestEquipmentHealthNum());
        LotEquipmentALLData.setTestEquipmentHealthRate(LotEquipmentHealthAll.getTestEquipmentHealthRate());
        /*LotRobot*/
        Map<String, Object> LotEquipmenRobotMap = testEquipmentCapacityService.EquipmenRobotData(LotautoLine, TEFloorName);
        EquipmentCapacity LotRobotHealthAll = (EquipmentCapacity) LotEquipmenRobotMap.get("RobotHealthAll");
        List<EquipmentCapacity> LotrobotHealthList = (List<EquipmentCapacity>) LotEquipmenRobotMap.get("RobotHealthList");
        LotEquipmentALLData.setTestRobotAllNum(LotRobotHealthAll.getTestRobotAllNum());
        LotEquipmentALLData.setTestRobotHealthNum(LotRobotHealthAll.getTestRobotHealthNum());
        LotEquipmentALLData.setTestRobotHealthRate(LotRobotHealthAll.getTestRobotHealthRate());
        for (AutoFloor_Target autoFloor_target : LotautoLine) {
            String lineName = autoFloor_target.getLineName();
            BigDecimal dTarget = autoFloor_target.getdTarget();
            EquipmentCapacity equipment = new EquipmentCapacity();

            equipment.setDTarget(dTarget.intValue());
            for (EquipmentCapacity equipmentCapacity : LotEquipmentHealthList) {
                String equipmentlineName = equipmentCapacity.getLineName();
                Integer testEquipmentAllNum = equipmentCapacity.getTestEquipmentAllNum();
                Integer testEquipmentHealthNum = equipmentCapacity.getTestEquipmentHealthNum();
                Double testEquipmentHealthRate = equipmentCapacity.getTestEquipmentHealthRate();
                if (lineName.equals(equipmentlineName)) {
                    equipment.setLineName(lineName);
                    equipment.setTestEquipmentAllNum(testEquipmentAllNum);
                    equipment.setTestEquipmentHealthNum(testEquipmentHealthNum);
                    equipment.setTestEquipmentHealthRate(testEquipmentHealthRate);
                }
            }
            for (EquipmentCapacity equipmentCapacity : LotrobotHealthList) {
                String RobotlineName = equipmentCapacity.getLineName();
                Integer testRobotAllNum = equipmentCapacity.getTestRobotAllNum();
                Integer testRobotHealthNum = equipmentCapacity.getTestRobotHealthNum();
                Double testRobotHealthRate = equipmentCapacity.getTestRobotHealthRate();
                if (lineName.equals(RobotlineName)) {
                    equipment.setTestRobotAllNum(testRobotAllNum);
                    equipment.setTestRobotHealthNum(testRobotHealthNum);
                    equipment.setTestRobotHealthRate(testRobotHealthRate);
                }
            }
            LotEquipmentDataList.add(equipment);
        }
        map.put("LotEquipmentDataList", LotEquipmentDataList);
        map.put("LotEquipmentALLData", LotEquipmentALLData);

        /*Mac*/
        List<EquipmentCapacity> MacEquipmentDataList = new ArrayList<>();
        EquipmentCapacity MacEquipmentALLData = new EquipmentCapacity();
        Map<String, Object> MacEquipmentCapacityMap = testEquipmentCapacityService.EquipmentCapacityData(MacautoLine,TEFloorName);
        List<EquipmentCapacity> MacEquipmentHealthList = (List<EquipmentCapacity>) MacEquipmentCapacityMap.get("TestEquipmentHealthList");
        EquipmentCapacity MacEquipmentHealthAll = (EquipmentCapacity) MacEquipmentCapacityMap.get("TestEquipmentHealthAll");
        MacEquipmentALLData.setTestEquipmentAllNum(MacEquipmentHealthAll.getTestEquipmentAllNum());
        MacEquipmentALLData.setTestEquipmentHealthNum(MacEquipmentHealthAll.getTestEquipmentHealthNum());
        MacEquipmentALLData.setTestEquipmentHealthRate(MacEquipmentHealthAll.getTestEquipmentHealthRate());
        /*MacRobot*/
        Map<String, Object> MacEquipmenRobotMap = testEquipmentCapacityService.EquipmenRobotData(MacautoLine, TEFloorName);
        EquipmentCapacity MacRobotHealthAll = (EquipmentCapacity) MacEquipmenRobotMap.get("RobotHealthAll");
        List<EquipmentCapacity> MacrobotHealthList = (List<EquipmentCapacity>) MacEquipmenRobotMap.get("RobotHealthList");
        MacEquipmentALLData.setTestRobotAllNum(MacRobotHealthAll.getTestRobotAllNum());
        MacEquipmentALLData.setTestRobotHealthNum(MacRobotHealthAll.getTestRobotHealthNum());
        MacEquipmentALLData.setTestRobotHealthRate(MacRobotHealthAll.getTestRobotHealthRate());
        for (AutoFloor_Target autoFloor_target : MacautoLine) {
            String lineName = autoFloor_target.getLineName();
            BigDecimal dTarget = autoFloor_target.getdTarget();
            EquipmentCapacity equipment = new EquipmentCapacity();

            equipment.setDTarget(dTarget.intValue());
            for (EquipmentCapacity equipmentCapacity : MacEquipmentHealthList) {
                String equipmentlineName = equipmentCapacity.getLineName();
                Integer testEquipmentAllNum = equipmentCapacity.getTestEquipmentAllNum();
                Integer testEquipmentHealthNum = equipmentCapacity.getTestEquipmentHealthNum();
                Double testEquipmentHealthRate = equipmentCapacity.getTestEquipmentHealthRate();
                if (lineName.equals(equipmentlineName)) {
                    equipment.setLineName(lineName);
                    equipment.setTestEquipmentAllNum(testEquipmentAllNum);
                    equipment.setTestEquipmentHealthNum(testEquipmentHealthNum);
                    equipment.setTestEquipmentHealthRate(testEquipmentHealthRate);
                }
            }
            for (EquipmentCapacity equipmentCapacity : MacrobotHealthList) {
                String RobotlineName = equipmentCapacity.getLineName();
                Integer testRobotAllNum = equipmentCapacity.getTestRobotAllNum();
                Integer testRobotHealthNum = equipmentCapacity.getTestRobotHealthNum();
                Double testRobotHealthRate = equipmentCapacity.getTestRobotHealthRate();
                if (lineName.equals(RobotlineName)) {
                    equipment.setTestRobotAllNum(testRobotAllNum);
                    equipment.setTestRobotHealthNum(testRobotHealthNum);
                    equipment.setTestRobotHealthRate(testRobotHealthRate);
                }
            }
            MacEquipmentDataList.add(equipment);
        }
        map.put("MacEquipmentDataList", MacEquipmentDataList);
        map.put("MacEquipmentALLData", MacEquipmentALLData);
        /*Pha*/
        List<EquipmentCapacity> PhaEquipmentDataList = new ArrayList<>();
        EquipmentCapacity PhaEquipmentALLData = new EquipmentCapacity();
        Map<String, Object> PhaEquipmentCapacityMap = testEquipmentCapacityService.EquipmentCapacityData(PhaautoLine,TEFloorName);
        List<EquipmentCapacity> PhaEquipmentHealthList = (List<EquipmentCapacity>) PhaEquipmentCapacityMap.get("TestEquipmentHealthList");
        EquipmentCapacity PhaEquipmentHealthAll = (EquipmentCapacity) PhaEquipmentCapacityMap.get("TestEquipmentHealthAll");
        PhaEquipmentALLData.setTestEquipmentAllNum(PhaEquipmentHealthAll.getTestEquipmentAllNum());
        PhaEquipmentALLData.setTestEquipmentHealthNum(PhaEquipmentHealthAll.getTestEquipmentHealthNum());
        PhaEquipmentALLData.setTestEquipmentHealthRate(PhaEquipmentHealthAll.getTestEquipmentHealthRate());
        /*PhaRobot*/
        Map<String, Object> PhaEquipmenRobotMap = testEquipmentCapacityService.EquipmenRobotData(PhaautoLine,TEFloorName);
        EquipmentCapacity PhaRobotHealthAll = (EquipmentCapacity) PhaEquipmenRobotMap.get("RobotHealthAll");
        List<EquipmentCapacity> PharobotHealthList = (List<EquipmentCapacity>) PhaEquipmenRobotMap.get("RobotHealthList");
        PhaEquipmentALLData.setTestRobotAllNum(PhaRobotHealthAll.getTestRobotAllNum());
        PhaEquipmentALLData.setTestRobotHealthNum(PhaRobotHealthAll.getTestRobotHealthNum());
        PhaEquipmentALLData.setTestRobotHealthRate(PhaRobotHealthAll.getTestRobotHealthRate());
        for (AutoFloor_Target autoFloor_target : PhaautoLine) {
            String lineName = autoFloor_target.getLineName();
            BigDecimal dTarget = autoFloor_target.getdTarget();
            EquipmentCapacity equipment = new EquipmentCapacity();

            equipment.setDTarget(dTarget.intValue());
            for (EquipmentCapacity equipmentCapacity : PhaEquipmentHealthList) {
                String equipmentlineName = equipmentCapacity.getLineName();
                Integer testEquipmentAllNum = equipmentCapacity.getTestEquipmentAllNum();
                Integer testEquipmentHealthNum = equipmentCapacity.getTestEquipmentHealthNum();
                Double testEquipmentHealthRate = equipmentCapacity.getTestEquipmentHealthRate();

                if (lineName.equals(equipmentlineName)) {
                    equipment.setLineName(lineName);
                    equipment.setTestEquipmentAllNum(testEquipmentAllNum);
                    equipment.setTestEquipmentHealthNum(testEquipmentHealthNum);
                    equipment.setTestEquipmentHealthRate(testEquipmentHealthRate);
                }
            }
            for (EquipmentCapacity equipmentCapacity : PharobotHealthList) {
                String RobotlineName = equipmentCapacity.getLineName();
                Integer testRobotAllNum = equipmentCapacity.getTestRobotAllNum();
                Integer testRobotHealthNum = equipmentCapacity.getTestRobotHealthNum();
                Double testRobotHealthRate = equipmentCapacity.getTestRobotHealthRate();
                if (lineName.equals(RobotlineName)) {
                    equipment.setTestRobotAllNum(testRobotAllNum);
                    equipment.setTestRobotHealthNum(testRobotHealthNum);
                    equipment.setTestRobotHealthRate(testRobotHealthRate);
                }
            }
            PhaEquipmentDataList.add(equipment);
        }
        map.put("PhaEquipmentDataList", PhaEquipmentDataList);
        map.put("PhaEquipmentALLData", PhaEquipmentALLData);

        ////////////////////////////////////////設備健康監控\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
        List<TestEquipmentHealth> LotTestEquipmentHealths = new ArrayList<>();
        TestEquipmentHealth LotAllTestEquipmentHealths = new TestEquipmentHealth();
        //Lot机故率
        Map<String, Object> LotMachineRateMap = testAProductService.MachineRate(LotautoLine, TypeTime, twoDateStartDate, twoDateEndDate, FloorName, actionMinuteD);
        List<TestEquipmentHealth> LotmachineRateList = (List<TestEquipmentHealth>) LotMachineRateMap.get("MachineRateList");
        double LotmachineRateAll = (double) LotMachineRateMap.get("MachineRateAll");
        LotAllTestEquipmentHealths.setMachineRate(LotmachineRateAll);
        /*-----*-----Lot-誤測率-----*-----------*/
        Map<String, Object> LotTestMachine = new HashMap<>();
        LotTestMachine = testAProductService.getTestMachine(LotautoLine, FloorName, twoDateStartDate, twoDateEndDate, TypeTime, LOT_PRODUCT);
        List<AmountsUPH> autoFloor_LotreteList = (List<AmountsUPH>) LotTestMachine.get("autoFloor_reteList");
        AmountsUPH amountsLotUPHAll = (AmountsUPH) LotTestMachine.get("amountsUPHAll");
        //LotALL誤測率
        LotAllTestEquipmentHealths.setMisdetetRate(amountsLotUPHAll.getMISDETET());
        //Lot超时待确认
        Map<String, Object> LotTimeOutData = testAProductService.TimeOutData(LotautoLine, twoDateStartDate, twoDateEndDate, FloorName);
        List<TestEquipmentHealth> LottimeOutList = (List<TestEquipmentHealth>) LotTimeOutData.get("TimeOutList");
        double LotTimeOutAll = (double) LotTimeOutData.get("TimeOutRateALL");
        LotAllTestEquipmentHealths.setTimeOutRate(LotTimeOutAll);
        /*-----*-----Lot-偏位率-----*-----------*/
        Map<String, Object> LotRobotMap = new HashMap<>();
        for (String testfloor : testFloorList) {
            if (testfloor.equals(FloorName)) {
                LotRobotMap = testAProductService.getTestRobot(LotautoLine, FloorName, twoDateStartDate, twoDateEndDate, TypeTime, LOT_PRODUCT, Htime);
            } else {
                LotRobotMap = testAProductService.getRobot(LotautoLine, FloorName, twoDateStartDate, twoDateEndDate, TypeTime, LOT_PRODUCT, Htime);
            }
        }
        List<AutoFloor_Wait_Time> LotAutoFloor_Wait_TimeList = (List<AutoFloor_Wait_Time>) LotRobotMap.get("AutoFloor_Wait_TimeList");
        Double LotRobotNoNormalRate = (Double) LotRobotMap.get("RobotNoNormalRate");
        //LotALL偏位率
        LotAllTestEquipmentHealths.setRobotNoNormalRate(LotRobotNoNormalRate);
        for (AutoFloor_Target autoFloor_target : LotautoLine) {
            BigDecimal bigDecimal = autoFloor_target.getdTarget();
            TestEquipmentHealth testEquipmentHealth = new TestEquipmentHealth();
            //线体
            String lineName = autoFloor_target.getLineName();
            testEquipmentHealth.setLineName(lineName);
            //产线排配
            testEquipmentHealth.setdTarget(bigDecimal);
            for (AmountsUPH amountsUPH : autoFloor_LotreteList) {
                //误测线体
                String Misdetetline_name = amountsUPH.getLINE_NAME();
                //误测率
                Double misdetet = amountsUPH.getMISDETET();
                if (lineName.equals(Misdetetline_name)) {
                    testEquipmentHealth.setMisdetetRate(misdetet);
                }
            }
            for (AutoFloor_Wait_Time autoFloor_wait_time : LotAutoFloor_Wait_TimeList) {
                //偏位线体
                String NoNormalline_name = autoFloor_wait_time.getLine_name();
                //偏位率
                Double NoNormalRate = autoFloor_wait_time.getLineNoNormalRate();
                if (lineName.equals(NoNormalline_name)) {
                    testEquipmentHealth.setRobotNoNormalRate(NoNormalRate);
                }
            }
            for (TestEquipmentHealth equipmentHealth : LottimeOutList) {
                //超时线体
                String TimeOutlineName = equipmentHealth.getLineName();
                //超时待确认率
                Double timeOutRate = equipmentHealth.getTimeOutRate();
                if (lineName.equals(TimeOutlineName)) {
                    testEquipmentHealth.setTimeOutRate(timeOutRate);
                }

            }
            for (TestEquipmentHealth MachineRateData : LotmachineRateList) {
                //机故线体
                String MachineRatelineName = MachineRateData.getLineName();
                //机故率
                Double machineRate = MachineRateData.getMachineRate();
                if (lineName.equals(MachineRatelineName)) {
                    testEquipmentHealth.setMachineRate(machineRate);
                }
            }
            LotTestEquipmentHealths.add(testEquipmentHealth);
        }
        map.put("LotAllTestEquipmentHealths", LotAllTestEquipmentHealths);
        map.put("LotTestEquipmentHealths", LotTestEquipmentHealths);
        /*-----*-----Mac-----*-----------*/
        List<TestEquipmentHealth> MacTestEquipmentHealths = new ArrayList<>();
        TestEquipmentHealth MacAllTestEquipmentHealths = new TestEquipmentHealth();
        long FalutStartTime = System.currentTimeMillis();
        /*-----*-----Mac-机故率-----*-----------*/
        Map<String, Object> MacMachineRateMap = testAProductService.MachineRate(MacautoLine, TypeTime, twoDateStartDate, twoDateEndDate, FloorName, actionMinuteD);
        List<TestEquipmentHealth> MacmachineRateList = (List<TestEquipmentHealth>) MacMachineRateMap.get("MachineRateList");
        double MacmachineRateAll = (double) MacMachineRateMap.get("MachineRateAll");
        MacAllTestEquipmentHealths.setMachineRate(MacmachineRateAll);
        long FalutEndTime = System.currentTimeMillis();
        //System.out.println("机故率Time----"+" "+(FalutEndTime-FalutStartTime));

        /*-----*-----Mac-誤測率-----*-----------*/
        Map<String, Object> MacTestMachine = new HashMap<>();
        MacTestMachine = testAProductService.getTestMachine(MacautoLine, FloorName, twoDateStartDate, twoDateEndDate, TypeTime, MAC_PRODUCT);
        List<AmountsUPH> autoFloor_MacreteList = (List<AmountsUPH>) MacTestMachine.get("autoFloor_reteList");
        AmountsUPH amountsMacUPHAll = (AmountsUPH) MacTestMachine.get("amountsUPHAll");
        //MacALL誤測率
        MacAllTestEquipmentHealths.setMisdetetRate(amountsMacUPHAll.getMISDETET());
        /*-----*-----Mac-超时待确认-----*-----------*/
        //Mac超时待确认
        Map<String, Object> MacTimeOutData = testAProductService.TimeOutData(MacautoLine, twoDateStartDate, twoDateEndDate, FloorName);
        List<TestEquipmentHealth> MactimeOutList = (List<TestEquipmentHealth>) MacTimeOutData.get("TimeOutList");
        double MacTimeOutAll = (double) MacTimeOutData.get("TimeOutRateALL");
        MacAllTestEquipmentHealths.setTimeOutRate(MacTimeOutAll);
        /*-----*-----Mac-偏位率-----*-----------*/
        Map<String, Object> MacRobotMap = new HashMap<>();
        for (String testfloor : testFloorList) {
            if (testfloor.equals(FloorName)) {
                MacRobotMap = testAProductService.getTestRobot(MacautoLine, FloorName, twoDateStartDate, twoDateEndDate, TypeTime, MAC_PRODUCT, Htime);
            } else {
                MacRobotMap = testAProductService.getRobot(MacautoLine, FloorName, twoDateStartDate, twoDateEndDate, TypeTime, MAC_PRODUCT, Htime);
            }
        }
        List<AutoFloor_Wait_Time> MacAutoFloor_Wait_TimeList = (List<AutoFloor_Wait_Time>) MacRobotMap.get("AutoFloor_Wait_TimeList");
        Double MacRobotNoNormalRate = (Double) MacRobotMap.get("RobotNoNormalRate");
        //MacALL偏位率
        MacAllTestEquipmentHealths.setRobotNoNormalRate(MacRobotNoNormalRate);
        for (AutoFloor_Target autoFloor_target : MacautoLine) {
            BigDecimal bigDecimal = autoFloor_target.getdTarget();
            TestEquipmentHealth testEquipmentHealth = new TestEquipmentHealth();
            //线体
            String lineName = autoFloor_target.getLineName();
            testEquipmentHealth.setLineName(lineName);
            //产线排配
            testEquipmentHealth.setdTarget(bigDecimal);
            for (AmountsUPH amountsUPH : autoFloor_MacreteList) {
                //误测线体
                String Misdetetline_name = amountsUPH.getLINE_NAME();
                //误测率
                Double misdetet = amountsUPH.getMISDETET();
                if (lineName.equals(Misdetetline_name)) {
                    testEquipmentHealth.setMisdetetRate(misdetet);
                }
            }
            for (AutoFloor_Wait_Time autoFloor_wait_time : MacAutoFloor_Wait_TimeList) {
                //偏位线体
                String NoNormalline_name = autoFloor_wait_time.getLine_name();
                //偏位率
                Double NoNormalRate = autoFloor_wait_time.getLineNoNormalRate();
                if (lineName.equals(NoNormalline_name)) {
                    testEquipmentHealth.setRobotNoNormalRate(NoNormalRate);
                }
            }
            for (TestEquipmentHealth equipmentHealth : MactimeOutList) {
                //超时线体
                String TimeOutlineName = equipmentHealth.getLineName();
                //超时待确认率
                Double timeOutRate = equipmentHealth.getTimeOutRate();
                if (lineName.equals(TimeOutlineName)) {
                    testEquipmentHealth.setTimeOutRate(timeOutRate);
                }

            }
            for (TestEquipmentHealth MachineRateData : MacmachineRateList) {
                //机故线体
                String MachineRatelineName = MachineRateData.getLineName();
                //机故率
                Double machineRate = MachineRateData.getMachineRate();
                if (lineName.equals(MachineRatelineName)) {
                    testEquipmentHealth.setMachineRate(machineRate);
                }
            }
            MacTestEquipmentHealths.add(testEquipmentHealth);
        }
        map.put("MacAllTestEquipmentHealths", MacAllTestEquipmentHealths);
        map.put("MacTestEquipmentHealths", MacTestEquipmentHealths);
        /*-----*-----Pha-----*-----------*/
        List<TestEquipmentHealth> PhaTestEquipmentHealths = new ArrayList<>();
        TestEquipmentHealth PhaAllTestEquipmentHealths = new TestEquipmentHealth();
        /*-----*-----Pha-机故率-----*-----------*/
        Map<String, Object> PhaMachineRateMap = testAProductService.MachineRate(PhaautoLine, TypeTime, twoDateStartDate, twoDateEndDate, FloorName, actionMinuteD);
        List<TestEquipmentHealth> PhamachineRateList = (List<TestEquipmentHealth>) PhaMachineRateMap.get("MachineRateList");
        double PhamachineRateAll = (double) PhaMachineRateMap.get("MachineRateAll");
        PhaAllTestEquipmentHealths.setMachineRate(PhamachineRateAll);
        /*-----*-----Pha-誤測率-----*-----------*/
        Map<String, Object> PhaTestMachine = new HashMap<>();
        PhaTestMachine = testAProductService.getTestMachine(PhaautoLine, FloorName, twoDateStartDate, twoDateEndDate, TypeTime, PHA_PRODUCT);
        List<AmountsUPH> autoFloor_PhareteList = (List<AmountsUPH>) PhaTestMachine.get("autoFloor_reteList");
        AmountsUPH amountsPhaUPHAll = (AmountsUPH) PhaTestMachine.get("amountsUPHAll");
        //PhaALL誤測率
        PhaAllTestEquipmentHealths.setMisdetetRate(amountsPhaUPHAll.getMISDETET());
        /*-----*-----Pha-超时待确认-----*-----------*/
        //Mac超时待确认
        Map<String, Object> PhaTimeOutData = testAProductService.TimeOutData(PhaautoLine, twoDateStartDate, twoDateEndDate, FloorName);
        List<TestEquipmentHealth> PhatimeOutList = (List<TestEquipmentHealth>) PhaTimeOutData.get("TimeOutList");
        double PhaTimeOutAll = (double) PhaTimeOutData.get("TimeOutRateALL");
        PhaAllTestEquipmentHealths.setTimeOutRate(PhaTimeOutAll);
        /*-----*-----Pha-偏位率-----*-----------*/
        Map<String, Object> PhaRobotMap = new HashMap<>();
        for (String testfloor : testFloorList) {
            if (testfloor.equals(FloorName)) {
                PhaRobotMap = testAProductService.getTestRobot(PhaautoLine, FloorName, twoDateStartDate, twoDateEndDate, TypeTime, PHA_PRODUCT, Htime);
            } else {
                PhaRobotMap = testAProductService.getRobot(PhaautoLine, FloorName, twoDateStartDate, twoDateEndDate, TypeTime, PHA_PRODUCT, Htime);
            }
        }
        List<AutoFloor_Wait_Time> PhaAutoFloor_Wait_TimeList = (List<AutoFloor_Wait_Time>) PhaRobotMap.get("AutoFloor_Wait_TimeList");
        Double PhaRobotNoNormalRate = (Double) PhaRobotMap.get("RobotNoNormalRate");
        //PhaALL偏位率
        PhaAllTestEquipmentHealths.setRobotNoNormalRate(PhaRobotNoNormalRate);
        for (AutoFloor_Target autoFloor_target : PhaautoLine) {
            BigDecimal bigDecimal = autoFloor_target.getdTarget();
            TestEquipmentHealth testEquipmentHealth = new TestEquipmentHealth();
            //线体
            String lineName = autoFloor_target.getLineName();
            testEquipmentHealth.setLineName(lineName);
            //产线排配
            testEquipmentHealth.setdTarget(bigDecimal);
            for (AmountsUPH amountsUPH : autoFloor_PhareteList) {
                //误测线体
                String Misdetetline_name = amountsUPH.getLINE_NAME();
                //误测率
                Double misdetet = amountsUPH.getMISDETET();
                if (lineName.equals(Misdetetline_name)) {
                    testEquipmentHealth.setMisdetetRate(misdetet);
                }
            }
            for (AutoFloor_Wait_Time autoFloor_wait_time : PhaAutoFloor_Wait_TimeList) {
                //偏位线体
                String NoNormalline_name = autoFloor_wait_time.getLine_name();
                //偏位率
                Double NoNormalRate = autoFloor_wait_time.getLineNoNormalRate();
                if (lineName.equals(NoNormalline_name)) {
                    testEquipmentHealth.setRobotNoNormalRate(NoNormalRate);
                }
            }
            for (TestEquipmentHealth equipmentHealth : PhatimeOutList) {
                //超时线体
                String TimeOutlineName = equipmentHealth.getLineName();
                //超时待确认率
                Double timeOutRate = equipmentHealth.getTimeOutRate();
                if (lineName.equals(TimeOutlineName)) {
                    testEquipmentHealth.setTimeOutRate(timeOutRate);
                }

            }
            for (TestEquipmentHealth MachineRateData : PhamachineRateList) {
                //机故线体
                String MachineRatelineName = MachineRateData.getLineName();
                //机故率
                Double machineRate = MachineRateData.getMachineRate();
                if (lineName.equals(MachineRatelineName)) {
                    testEquipmentHealth.setMachineRate(machineRate);
                }
            }
            PhaTestEquipmentHealths.add(testEquipmentHealth);
        }
        map.put("PhaAllTestEquipmentHealths", PhaAllTestEquipmentHealths);
        map.put("PhaTestEquipmentHealths", PhaTestEquipmentHealths);


        ////////////////////////////////////////关键参数监控\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
        Map<String, Object> LotKeyParameterData = testKeyParameterService.KeyParameterData(LotautoLine, FloorName, twoDateStartDate, twoDateEndDate);
        TestKeyParameter LotKeyParameterAll = (TestKeyParameter) LotKeyParameterData.get("KeyParameterAll");
        List<TestKeyParameter> LotKeyParameterList = (List<TestKeyParameter>) LotKeyParameterData.get("KeyParameterList");
        map.put("LotKeyParameterAll", LotKeyParameterAll);
        map.put("LotKeyParameterList", LotKeyParameterList);
        Map<String, Object> MacKeyParameterData = testKeyParameterService.KeyParameterData(MacautoLine, FloorName, twoDateStartDate, twoDateEndDate);
        TestKeyParameter MacKeyParameterAll = (TestKeyParameter) MacKeyParameterData.get("KeyParameterAll");
        List<TestKeyParameter> MacKeyParameterList = (List<TestKeyParameter>) MacKeyParameterData.get("KeyParameterList");
        map.put("MacKeyParameterAll", MacKeyParameterAll);
        map.put("MacKeyParameterList", MacKeyParameterList);
        Map<String, Object> PhaKeyParameterData = testKeyParameterService.KeyParameterData(PhaautoLine, FloorName, twoDateStartDate, twoDateEndDate);
        TestKeyParameter PhaKeyParameterAll = (TestKeyParameter) PhaKeyParameterData.get("KeyParameterAll");
        List<TestKeyParameter> PhaKeyParameterList = (List<TestKeyParameter>) PhaKeyParameterData.get("KeyParameterList");
        map.put("PhaKeyParameterAll", PhaKeyParameterAll);
        map.put("PhaKeyParameterList", PhaKeyParameterList);

        Map<String, Set<String>> TargetSetDataMap = testAProductService.TargetSetData();
        Set<String> FloorSet = TargetSetDataMap.get("FloorSet");
        Set<String> AreaSet = TargetSetDataMap.get("AreaSet");

        map.put("FloorSet",FloorSet);
        map.put("AreaSet",AreaSet);
        map.put("DataStartTime", StartDateStr);
        map.put("DataEndTime", EndDateStr);
        return "testsys/testproducts";
    }


}
