package com.fox.testsys.service;

import com.fox.qualitysys.entity.Warning_List_Slot;
import com.fox.qualitysys.mapper.Warning_List_SlotMapper;
import com.fox.testsys.entity.*;
import com.fox.testsys.mapper.*;
import com.fox.testsys.utility.TaskService;
import com.fox.testsys.utility.TimeUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author
 * @create 2020-05-08 10:17
 */
@Service
public class TestAProductService {
    //机种名称
    private static final String LOT_PRODUCT = "Lotus";
    private static final String MAC_PRODUCT = "Macan";
    private static final String PHA_PRODUCT = "Pha";
    //班别
    private static final String TwoTimeType = "two";
    private static final String DayTimeType = "Day";
    private static final String NightTimeType = "Night";
    private static final String YestrerDayTimeType = "Yesterday";


    @Autowired
    AutoFloor_RateMapper autoFloor_rateMapper;

    @Autowired
    AutoFloor_Wait_TimeMapper autoFloor_wait_timeMapper;

    DateFormat Yeardf = new SimpleDateFormat("yyyy/MM/dd");
    @Autowired
    TaskService taskService;
    @Autowired
    TimeUtility timeUtility;
    @Autowired
    AutoFloor_TargetMapper autoFloor_targetMapper;
    @Autowired
    AutoFloor_UPHMapper autoFloor_uphMapper;
    @Autowired
    AutoFloor_WIPMapper autoFloor_wipMapper;
    @Autowired
    AutoFloor_Fail_RETestMapper autoFloor_fail_reTestMapper;
    @Autowired
    AUTOFLOOR_Robot_Comm_TimesMapper autofloor_robot_comm_timesMapper;
    @Autowired
    AUTOFLOOR_Robot_EfficiencyMapper autofloor_robot_efficiencyMapper;
    @Autowired
    TestEquipmentHealthService testEquipmentHealthService;
    @Autowired
    AutoFloor_CCDDataMapper autoFloor_ccdDataMapper;
    @Autowired
    Warning_List_SlotMapper warning_list_slotMapper;
    @Autowired
    Base_StationMapper base_stationMapper;

    public Map<String,Set<String>> TargetSetData(){
        Map<String,Set<String>> TargetSetMap=new HashMap<>();
        List<AutoFloor_Target> autoFloor_targets = autoFloor_targetMapper.ALLFloorLine();
        Set<String> FloorSet=  new HashSet<>();
        Set<String> AreaSet=  new HashSet<>();
        for (AutoFloor_Target autoFloor_target : autoFloor_targets) {
            String Area = autoFloor_target.gettArea();
            String floor = autoFloor_target.gettFloor();

            FloorSet.add(floor);
            AreaSet.add(Area);
        }
        TargetSetMap.put("FloorSet",FloorSet);
        TargetSetMap.put("AreaSet",AreaSet);

        return TargetSetMap;
    }

    public List<String> TTLProductList(Date startDate, Date endDate, String floorName){
        List<AutoFloor_Target> autoFloor_targets = autoFloor_targetMapper.TTLLineList(startDate, endDate, floorName);

        List<String> TTLProductList=new ArrayList<>();

        autoFloor_targets.forEach(Target->{
            String Product = Target.gettProduct();
            TTLProductList.add(Product);
        });

        return TTLProductList;

    }


    public List<AutoFloor_Target> LineData(String FloorName, String LineName, Date schedule, String Product, String TimeType, String StartDate, String EndDate) {
        List<AutoFloor_Target> LineDataList = new ArrayList<>();
        List<java.sql.Date> sqlList = taskService.Schedule();
        /*当前日期*/
        if (TwoTimeType.equals(TimeType)) {
            if (timeUtility.Timequantum("08:30", "20:29")) {
                /* 白班 线体数据*/
                LineDataList = autoFloor_targetMapper.outPutTarGet(FloorName, DayTimeType, LineName, schedule, schedule, Product);
            } else {
                /* 夜班 线体数据*/
                LineDataList = autoFloor_targetMapper.outPutTarGet(FloorName, NightTimeType, LineName, schedule, schedule, Product);
            }
        } else if (DayTimeType.equals(TimeType)) {
            if (timeUtility.Timequantum("08:30", "20:29")) {
                /* 白班 线体数据*/
                LineDataList = autoFloor_targetMapper.outPutTarGet(FloorName, DayTimeType, LineName, schedule, schedule, Product);
            } else {
                /* 夜班 线体数据*/
                LineDataList = autoFloor_targetMapper.outPutTarGet(FloorName, NightTimeType, LineName, schedule, schedule, Product);
            }
        } else if (NightTimeType.equals(TimeType)) {
            LineDataList = autoFloor_targetMapper.outPutTarGet(FloorName, NightTimeType, LineName, schedule, schedule, Product);
        } else if (YestrerDayTimeType.equals(TimeType)) {
            Date enddate = sqlList.get(1);
            LineDataList = autoFloor_targetMapper.YesterDayLinenameTarGet(FloorName, LineName, enddate, enddate, Product);
        } else if ("SelectData".equals(TimeType)) {
            String SelectDate = Yeardf.format(schedule);
            if (!StartDate.equals(StartDate)&&!EndDate.equals(EndDate)){

            }
            LineDataList = autoFloor_targetMapper.SelectLinenameTarGet(FloorName, LineName, StartDate,EndDate, Product);
        }
        return LineDataList;
    }

    ///产能
    public Map<String, Object> getOutCondition(List<AutoFloor_Target> LineList, String StartDate, String EndDate, Integer actionMinuteD, Integer FloatNum, String TypeTime) {
        /*初始化实际输出值*/
        int ActuaPutSum = 0;
        int ActuaOutSum = 0;
        double TarGetSumdouble = 0.0;
        List<AutoFloorData> OutConditionList = new ArrayList<AutoFloorData>();
        for (AutoFloor_Target autoFloorGet : LineList) {
            int dTarget = autoFloorGet.getdTarget().intValue();
            //产能状态实体
            AutoFloorData LineOutCondition = new AutoFloorData();
            String Product = autoFloorGet.gettProduct();
            /*线体名称*/
            String lineName = autoFloorGet.getLineName();
            /*天目标*/
            int DayTargetint = 0;
            /*小时目标*/
            int HourTargetint = 0;
            /*投入数*/
            int ActualPut = 0;
            //线体实际输出数量
            int ActualOut = 0;
            //实际产出值
            double ActualData = 0;
            if (dTarget>0){
                //线体楼层
                String Floor = autoFloorGet.gettFloor();
                //线体机种

                //线体每天目标
                Integer DayTargetInteger = 0;
                BigDecimal DayTarget = autoFloorGet.getdTarget();
                DayTargetInteger = Integer.parseInt(DayTarget.toString());


                if (DayTarget != null) {
                    DayTargetint = Integer.parseInt(DayTarget.toString());
                }

                //线体运行时间
                Double workingHourD = 0.0;
                BigDecimal workingHours = autoFloorGet.getWorkingHours();
                if (workingHours != null) {
                    workingHourD = Double.parseDouble(workingHours.toString());
                }
                //线体每小时目标
                Double HourTargetD = 0.0;
                BigDecimal HourTarget = autoFloorGet.gethTarget();

                if (HourTarget != null) {
                    HourTargetint = Integer.parseInt(HourTarget.toString());
                    HourTargetD = Double.parseDouble(HourTarget.toString());
                }

                Integer lineActualPut = autoFloor_wipMapper.lineActualPut(Floor, lineName, StartDate, EndDate);
                if (lineActualPut != null) {
                    ActualPut = lineActualPut;
                }
                ActuaPutSum += ActualPut;

                Integer lineActualOut = autoFloor_uphMapper.lineActualOut(Floor, lineName, StartDate, EndDate);
                if (lineActualOut != null) {
                    ActualOut = lineActualOut;
                }
                //实际输出总量加总
                ActuaOutSum += ActualOut;
                //线体达成率
                if (DayTargetInteger != 0) {
                    Double achievingRate = ((double) ActualOut / Double.parseDouble(DayTarget.toString())) * 100;
                    BigDecimal achievingRateB = new BigDecimal(achievingRate);
                    /*线体达成率保留后两位*/
                    achievingRate = achievingRateB.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                    /*线体数据传值*/
                    /*实际数量*/
                    LineOutCondition.setAchievingRate(achievingRate);
                } else {
                    LineOutCondition.setActualData(0.0);
                }

                if (TwoTimeType.equals(TypeTime)) {
                    if (timeUtility.Timequantum("08:30", "20:30")) {
                        Map<String, Object> DayActionMap = DayActionTarget(workingHourD, HourTargetD, actionMinuteD, FloatNum);
                        Double DayActionTarget = (Double) DayActionMap.get("TarGetSumdouble");
                        FloatNum = (Integer) DayActionMap.get("FloatNum");
                        if (DayActionTarget != null) {
                            TarGetSumdouble += DayActionTarget;
                            ActualData = DayActionTarget;
                        }
                    } else {
                        Map<String, Object> NightActionMap = NightActionTarget(workingHourD, HourTargetD, actionMinuteD, FloatNum);
                        Double NightActionTarget = (Double) NightActionMap.get("TarGetSumdouble");
                        FloatNum = (Integer) NightActionMap.get("FloatNum");
                        if (NightActionTarget != null) {
                            TarGetSumdouble += NightActionTarget;
                            ActualData = NightActionTarget;
                        }
                    }
                } else if (DayTimeType.equals(TypeTime)) {
                    Map<String, Object> DayActionMap = DayActionTarget(workingHourD, HourTargetD, actionMinuteD, FloatNum);
                    Double DayActionTarget = (Double) DayActionMap.get("TarGetSumdouble");
                    FloatNum = (Integer) DayActionMap.get("FloatNum");
                    if (DayActionTarget != null) {
                        TarGetSumdouble += DayActionTarget;
                        ActualData = DayActionTarget;
                    }
                } else if (NightTimeType.equals(TypeTime)) {
                    Map<String, Object> NightActionMap = NightActionTarget(workingHourD, HourTargetD, actionMinuteD, FloatNum);
                    Double NightActionTarget = (Double) NightActionMap.get("TarGetSumdouble");
                    FloatNum = (Integer) NightActionMap.get("FloatNum");
                    if (NightActionTarget != null) {
                        TarGetSumdouble += NightActionTarget;
                        ActualData = NightActionTarget;
                    }
                } else if (YestrerDayTimeType.equals(TypeTime)) {
                    FloatNum = 0;
                    TarGetSumdouble += DayTargetInteger;
                    ActualData = DayTargetInteger;
                } else if ("SelectData".equals(TypeTime)) {
                    FloatNum = 0;
                    TarGetSumdouble += DayTargetInteger;
                    ActualData = DayTargetInteger;
                }
            }
            LineOutCondition.setTProduct(Product);
            /*线体名称*/
            LineOutCondition.setLineName(lineName);
            /*天目标*/
            LineOutCondition.setdTarget(DayTargetint);
            /*小时目标*/
            LineOutCondition.sethTarget(HourTargetint);
            //每分钟实际值对比目标值
            /*投入数*/
            LineOutCondition.setActualPut(ActualPut);
            /*产出数*/
            LineOutCondition.setActualOuts(ActualOut);
            //实际产出值
            LineOutCondition.setActualData(ActualData);
            OutConditionList.add(LineOutCondition);
        }
        Map<String, Object> OutConditionMap = new HashMap<String, Object>();
        //产能状况列表数据
        OutConditionMap.put("OutConditionList", OutConditionList);
        //产能状况每条线的投入
        OutConditionMap.put("ActuaPutSum", ActuaPutSum);
        //产能状况每条线的产出
        OutConditionMap.put("ActuaOutSum", ActuaOutSum);
        //产能状况每条线每分钟应该产出的数量 作比较
        OutConditionMap.put("TarGetSumdouble", TarGetSumdouble);
        OutConditionMap.put("FloatNum", FloatNum);
        return OutConditionMap;
    }

    public Map<String, Object> DayActionTarget(Double WorkingHours, Double HourTarget, Integer actionMinuteD, Integer FloatNum) {
        Double TarGetSumdouble = 0.0;
        double MinuteTarget = 0.0;
        if (HourTarget != null) {
            MinuteTarget = HourTarget / 60;
        }
        //11小时 运行分钟数
        int WorkingMinute11 = 630;
        //10小时 运行分钟数
        int WorkingMinute10 = 570;
        //9小时 运行分钟数
        int WorkingMinute9 = 520;
        //8小时 运行分钟数
        int WorkingMinute8 = 460;
        /*白班實時目標*/
        if (WorkingHours == 10.5) {
            if (timeUtility.Timequantum("08:30", "09:30")) {
                Double actdataDouble = (actionMinuteD * MinuteTarget) * 0.5;
                TarGetSumdouble += new BigDecimal(actdataDouble).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            } else if (timeUtility.Timequantum("09:30", "12:30")) {
                Integer actionTimeh = actionMinuteD - 30;
                Double actdataDouble = (actionTimeh * MinuteTarget);
                TarGetSumdouble += new BigDecimal(actdataDouble).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

            } else if (timeUtility.Timequantum("12:30", "20:30")) {
                Integer actionTimeh = actionMinuteD - 90;
                Double actdataDouble = (actionTimeh * MinuteTarget);
                TarGetSumdouble += new BigDecimal(actdataDouble).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            } else {
                FloatNum = 0;
                Double actdataDouble = (WorkingMinute11 * MinuteTarget);
                TarGetSumdouble += new BigDecimal(actdataDouble).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            }
        } else if (WorkingHours == 9.5) {
            if (timeUtility.Timequantum("08:30", "09:30")) {
                Double actdataDouble = (actionMinuteD * MinuteTarget) * 0.5;
                TarGetSumdouble += new BigDecimal(actdataDouble).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            } else if (timeUtility.Timequantum("09:30", "12:30")) {
                Integer actionTimeh = actionMinuteD - 30;
                Double actdataDouble = (actionTimeh * MinuteTarget);
                TarGetSumdouble += new BigDecimal(actdataDouble).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

            } else if (timeUtility.Timequantum("12:30", "19:30")) {
                Integer actionTimeh = actionMinuteD - 90;
                Double actdataDouble = (actionTimeh * MinuteTarget);
                TarGetSumdouble += new BigDecimal(actdataDouble).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

            } else {
                FloatNum = 0;
                Double actdataDouble = (WorkingMinute10 * MinuteTarget);
                TarGetSumdouble += new BigDecimal(actdataDouble).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            }
        } else if (WorkingHours == 8.67) {
            if (timeUtility.Timequantum("08:30", "09:30")) {
                Double actdataDouble = (actionMinuteD * MinuteTarget) * 0.5;
                TarGetSumdouble += new BigDecimal(actdataDouble).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            } else if (timeUtility.Timequantum("09:30", "12:30")) {
                Integer actionTimeh = actionMinuteD - 30;
                Double actdataDouble = (actionTimeh * MinuteTarget);
                TarGetSumdouble += new BigDecimal(actdataDouble).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

            } else if (timeUtility.Timequantum("12:30", "18:30")) {
                Integer actionTimeh = actionMinuteD - 90;
                Double actdataDouble = (actionTimeh * MinuteTarget);
                TarGetSumdouble += new BigDecimal(actdataDouble).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            } else {
                FloatNum = 0;
                Double actdataDouble = (WorkingMinute9 * MinuteTarget);
                TarGetSumdouble += new BigDecimal(actdataDouble).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            }
        } else if (WorkingHours == 7.67) {
            if (timeUtility.Timequantum("08:30", "09:30")) {
                Double actdataDouble = (actionMinuteD * MinuteTarget) * 0.5;
                TarGetSumdouble += new BigDecimal(actdataDouble).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            } else if (timeUtility.Timequantum("09:30", "12:30")) {
                Integer actionTimeh = actionMinuteD - 30;
                Double actdataDouble = (actionTimeh * MinuteTarget);
                TarGetSumdouble += new BigDecimal(actdataDouble).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            } else if (timeUtility.Timequantum("12:30", "17:30")) {
                Integer actionTimeh = actionMinuteD - 90;
                Double actdataDouble = (actionTimeh * MinuteTarget);
                TarGetSumdouble += new BigDecimal(actdataDouble).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            } else {
                FloatNum = 0;
                Double actdataDouble = (WorkingMinute8 * MinuteTarget);
                TarGetSumdouble += new BigDecimal(actdataDouble).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            }

        }

        Map<String, Object> DayActionTargetMap = new HashMap<>();
        DayActionTargetMap.put("FloatNum", FloatNum);
        DayActionTargetMap.put("TarGetSumdouble", TarGetSumdouble);
        return DayActionTargetMap;
    }

    public Map<String, Object> NightActionTarget(Double WorkingHours, Double HourTarget, Integer actionMinuteD, Integer FloatNum) {
        Double TarGetSumdouble = 0.0;
        double MinuteTarget = 0.0;
        if (HourTarget != null) {
            MinuteTarget = HourTarget / 60;
        }
        //11小时 运行分钟数
        int WorkingMinute11 = 630;
        //10小时 运行分钟数
        int WorkingMinute10 = 570;
        //9小时 运行分钟数
        int WorkingMinute9 = 520;
        //8小时 运行分钟数
        int WorkingMinute8 = 460;
        /*夜班實時目標*/

        if (WorkingHours == 10.5) {
            if (timeUtility.Timequantum("20:30", "21:30")) {
                Double actdataDouble = (actionMinuteD * MinuteTarget) * 0.5;
                TarGetSumdouble += new BigDecimal(actdataDouble).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            } else if (timeUtility.Timequantum("23:30", "00:30")) {
                Integer actionTimeh = actionMinuteD - 30;
                Double actdataDouble = (actionTimeh * MinuteTarget);
                TarGetSumdouble += new BigDecimal(actdataDouble).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            } else if (timeUtility.Timequantum("00:30", "08:30")) {
                Integer actionTimeh = actionMinuteD - 90;
                Double actdataDouble = (actionTimeh * MinuteTarget);
                TarGetSumdouble += new BigDecimal(actdataDouble).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            } else {
                FloatNum = 0;
                Double actdataDouble = (WorkingMinute11 * MinuteTarget);
                TarGetSumdouble += new BigDecimal(actdataDouble).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            }
        } else if (WorkingHours == 9.5) {
            if (timeUtility.Timequantum("20:30", "21:30")) {
                Double actdataDouble = (actionMinuteD * MinuteTarget) * 0.5;
                TarGetSumdouble += new BigDecimal(actdataDouble).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            } else if (timeUtility.Timequantum("23:30", "00:30")) {
                Integer actionTimeh = actionMinuteD - 30;
                Double actdataDouble = (actionTimeh * MinuteTarget);
                TarGetSumdouble += new BigDecimal(actdataDouble).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            } else if (timeUtility.Timequantum("00:30", "07:30")) {
                Integer actionTimeh = actionMinuteD - 90;
                Double actdataDouble = (actionTimeh * MinuteTarget);
                TarGetSumdouble += new BigDecimal(actdataDouble).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            } else {
                FloatNum = 0;
                Double actdataDouble = (WorkingMinute10 * MinuteTarget);
                TarGetSumdouble += new BigDecimal(actdataDouble).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            }
        } else if (WorkingHours == 8.67) {
            if (timeUtility.Timequantum("20:30", "21:30")) {
                Double actdataDouble = (actionMinuteD * MinuteTarget) * 0.5;
                TarGetSumdouble += new BigDecimal(actdataDouble).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            } else if (timeUtility.Timequantum("23:30", "00:30")) {
                Integer actionTimeh = actionMinuteD - 30;
                Double actdataDouble = (actionTimeh * MinuteTarget);
                TarGetSumdouble += new BigDecimal(actdataDouble).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            } else if (timeUtility.Timequantum("00:30", "06:30")) {
                Integer actionTimeh = actionMinuteD - 90;
                Double actdataDouble = (actionTimeh * MinuteTarget);
                TarGetSumdouble += new BigDecimal(actdataDouble).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            } else {
                FloatNum = 0;
                Double actdataDouble = (WorkingMinute9 * MinuteTarget);
                TarGetSumdouble += new BigDecimal(actdataDouble).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            }
        } else if (WorkingHours == 7.67) {
            if (timeUtility.Timequantum("20:30", "21:30")) {
                Double actdataDouble = (actionMinuteD * MinuteTarget) * 0.5;
                TarGetSumdouble += new BigDecimal(actdataDouble).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            } else if (timeUtility.Timequantum("23:30", "00:30")) {
                Integer actionTimeh = actionMinuteD - 30;
                Double actdataDouble = (actionTimeh * MinuteTarget);
                TarGetSumdouble += new BigDecimal(actdataDouble).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            } else if (timeUtility.Timequantum("00:30", "05:30")) {
                Integer actionTimeh = actionMinuteD - 90;
                Double actdataDouble = (actionTimeh * MinuteTarget);
                TarGetSumdouble += new BigDecimal(actdataDouble).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            } else {
                FloatNum = 0;
                Double actdataDouble = (WorkingMinute8 * MinuteTarget);
                TarGetSumdouble += new BigDecimal(actdataDouble).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            }
        }
        Map<String, Object> NightActionTargetMap = new HashMap<>();
        NightActionTargetMap.put("FloatNum", FloatNum);
        NightActionTargetMap.put("TarGetSumdouble", TarGetSumdouble);
        return NightActionTargetMap;
    }
    //設備健康監控

    //误测率
    public Map<String, Object> getTestMachine(List<AutoFloor_Target> LineList, String FloorName, String StartDate, String EndDate, String TypeTime, String Product) {
        List<AmountsUPH> autoFloor_reteList = new ArrayList<>();
        Double rateMisdetetAll = 0.0;
        int LineNum=0;
        AmountsUPH amountsUPHAll = new AmountsUPH();
        for (AutoFloor_Target autoFloorGet : LineList) {
            AmountsUPH amountsUPH = new AmountsUPH();
            int dTarget = autoFloorGet.getdTarget().intValue();
            if (dTarget>0){
                LineNum++;
                Double MisdetetARF = 0.0;
                List<AutoFloor_Rate> autoFloor_ratesARFs = autoFloor_rateMapper.AutoFloorALLRate_Yield(StartDate, EndDate, FloorName, autoFloorGet.getLineName());
                for (AutoFloor_Rate autoFloor_ratesARF : autoFloor_ratesARFs) {
                    /*-----****---------- 误测率  ----------***------*/
                    Double misdetet = autoFloor_ratesARF.getMisdetet();
                    MisdetetARF += misdetet;
                    amountsUPH.setMISDETET(MisdetetARF);
                }
                if (MisdetetARF != 0) {
                    rateMisdetetAll += MisdetetARF;
                }
                amountsUPH.setLINE_NAME(autoFloorGet.getLineName());
                autoFloor_reteList.add(amountsUPH);
            }
            }
        /*-----------全部线体误测率-----------*/
        if (LineNum!=0){rateMisdetetAll/=LineNum;}
        BigDecimal rateMisdetetAllBig=new BigDecimal(rateMisdetetAll*100);
        rateMisdetetAll=rateMisdetetAllBig.setScale(2,BigDecimal.ROUND_FLOOR).doubleValue();
        amountsUPHAll.setMISDETET(rateMisdetetAll);
        Map<String, Object> TestMachineMap = new HashMap<>();
        TestMachineMap.put("autoFloor_reteList", autoFloor_reteList);
        TestMachineMap.put("amountsUPHAll", amountsUPHAll);
        return TestMachineMap;
    }

    //Robot
    public Map<String, Object> getRobot(List<AutoFloor_Target> LineList, String FloorName, String StartDate, String EndDate, String TypeTime, String Product, Integer
            Hours) {
        List<AutoFloor_Wait_Time> AutoFloor_Wait_TimeList = new ArrayList<>();
        int lineSum = 0;
        Double RobotNoNormalRate = 0.0;
        Double RobotPlateRate = 0.0;

        for (AutoFloor_Target autofloorTarget : LineList) {
            int dTarget = autofloorTarget.getdTarget().intValue();
            if (dTarget>0){
                int HtimeRate = 0;
                int one = 0;
                int two = 0;
                int three = 0;
                Integer LinenameNoNormalSum = 0;
                Double LineTTLNoNormalRateOne = 0.0;
                Double LineTTLNoNormalRateTwo = 0.0;
                Double LineTTLNoNormalRateThree = 0.0;
                Double LineTTLPlateRateOne = 0.0;
                Double LineTTLPlateRateTwo = 0.0;
                Double LineTTLPlateRateThree = 0.0;
                AutoFloor_Wait_Time autoFloor_wait_times = new AutoFloor_Wait_Time();
                autoFloor_wait_times.setLine_name(autofloorTarget.getLineName());
                BigDecimal workingHours = autofloorTarget.getWorkingHours();
                Double WorkingHour = workingHours.doubleValue();
                HtimeRate = RobotRunHtime(TypeTime, WorkingHour);
                AutoFloor_Wait_Time autoFloor_wait_timeA = null;
                autoFloor_wait_timeA = autoFloor_wait_timeMapper.RobotAnalyzes(StartDate, EndDate, FloorName, autofloorTarget.gettProduct(), autofloorTarget.getLineName() + "-A");
                if (autoFloor_wait_timeA != null) {
                    autoFloor_wait_timeA.setAwaitNoNormalRate(autoFloor_wait_timeA.getAwaitNoNormalRate() / ((HtimeRate) * 3600));
                    autoFloor_wait_timeA.setAwaitPlateRate(autoFloor_wait_timeA.getAwaitPlateRate() / ((HtimeRate) * 3600));
                }

                if (autoFloor_wait_timeA != null) {
                    one++;
                    Integer awaitNoNormalSum = autoFloor_wait_timeA.getAwaitNoNormalSum();
                    LinenameNoNormalSum += awaitNoNormalSum;
                    autoFloor_wait_times.setANoNormalSum(awaitNoNormalSum);

                    Double awaitNoNormalRate = autoFloor_wait_timeA.getAwaitNoNormalRate();
                    LineTTLNoNormalRateOne += awaitNoNormalRate;
                    autoFloor_wait_times.setANoNormalRate(awaitNoNormalRate);

                    Double awaitPlateRate = autoFloor_wait_timeA.getAwaitPlateRate();
                    LineTTLPlateRateOne += awaitPlateRate;
                    autoFloor_wait_times.setAPlateRate(awaitPlateRate);
                } else {
                    autoFloor_wait_times.setANoNormalSum(0);
                    autoFloor_wait_times.setANoNormalRate(0.0);
                    autoFloor_wait_times.setAPlateRate(0.0);
                }

                AutoFloor_Wait_Time autoFloor_wait_timeB = null;
                autoFloor_wait_timeB = autoFloor_wait_timeMapper.RobotAnalyzes(StartDate, EndDate, FloorName, autofloorTarget.gettProduct(), autofloorTarget.getLineName() + "-B");
                if (autoFloor_wait_timeB != null) {
                    autoFloor_wait_timeB.setAwaitNoNormalRate(autoFloor_wait_timeB.getAwaitNoNormalRate() / ((HtimeRate) * 3600));
                    autoFloor_wait_timeB.setAwaitPlateRate(autoFloor_wait_timeB.getAwaitPlateRate() / ((HtimeRate) * 3600));
                }
                if (autoFloor_wait_timeB != null) {
                    two++;
                    Integer awaitNoNormalSum = autoFloor_wait_timeB.getAwaitNoNormalSum();
                    LinenameNoNormalSum += awaitNoNormalSum;
                    autoFloor_wait_times.setBNoNormalSum(awaitNoNormalSum);

                    Double awaitNoNormalRate = autoFloor_wait_timeB.getAwaitNoNormalRate();
                    LineTTLNoNormalRateTwo += awaitNoNormalRate;
                    autoFloor_wait_times.setBNoNormalRate(awaitNoNormalRate);

                    Double awaitPlateRate = autoFloor_wait_timeB.getAwaitPlateRate();
                    LineTTLPlateRateTwo += awaitPlateRate;
                    autoFloor_wait_times.setBPlateRate(awaitPlateRate);
                } else {
                    autoFloor_wait_times.setBNoNormalSum(0);
                    autoFloor_wait_times.setBNoNormalRate(0.0);
                    autoFloor_wait_times.setBPlateRate(0.0);
                }

                AutoFloor_Wait_Time autoFloor_wait_timeC = null;
                autoFloor_wait_timeC = autoFloor_wait_timeMapper.RobotAnalyzes(StartDate, EndDate, FloorName, autofloorTarget.gettProduct(), autofloorTarget.getLineName() + "-C");
                if (autoFloor_wait_timeC != null) {
                    autoFloor_wait_timeC.setAwaitNoNormalRate(autoFloor_wait_timeC.getAwaitNoNormalRate() / ((HtimeRate) * 3600));
                    autoFloor_wait_timeC.setAwaitPlateRate(autoFloor_wait_timeC.getAwaitPlateRate() / ((HtimeRate) * 3600));
                }
                if (autoFloor_wait_timeC != null) {
                    two++;
                    Integer awaitNoNormalSum = autoFloor_wait_timeC.getAwaitNoNormalSum();
                    LinenameNoNormalSum += awaitNoNormalSum;
                    autoFloor_wait_times.setCNoNormalSum(awaitNoNormalSum);

                    Double awaitNoNormalRate = autoFloor_wait_timeC.getAwaitNoNormalRate();
                    LineTTLNoNormalRateTwo += awaitNoNormalRate;
                    autoFloor_wait_times.setCNoNormalRate(awaitNoNormalRate);

                    Double awaitPlateRate = autoFloor_wait_timeC.getAwaitPlateRate();
                    LineTTLPlateRateTwo += awaitPlateRate;
                    autoFloor_wait_times.setCPlateRate(awaitPlateRate);
                } else {
                    autoFloor_wait_times.setCNoNormalSum(0);
                    autoFloor_wait_times.setCNoNormalRate(0.0);
                    autoFloor_wait_times.setCPlateRate(0.0);
                }

                AutoFloor_Wait_Time autoFloor_wait_timeD = null;
                autoFloor_wait_timeD = autoFloor_wait_timeMapper.RobotAnalyzes(StartDate, EndDate, FloorName, autofloorTarget.gettProduct(), autofloorTarget.getLineName() + "-D");
                if (autoFloor_wait_timeD != null) {
                    autoFloor_wait_timeD.setAwaitNoNormalRate(autoFloor_wait_timeD.getAwaitNoNormalRate() / ((HtimeRate) * 3600));
                    autoFloor_wait_timeD.setAwaitPlateRate(autoFloor_wait_timeD.getAwaitPlateRate() / ((HtimeRate) * 3600));
                }
                if (autoFloor_wait_timeD != null) {
                    two++;
                    Integer awaitNoNormalSum = autoFloor_wait_timeD.getAwaitNoNormalSum();
                    LinenameNoNormalSum += awaitNoNormalSum;
                    autoFloor_wait_times.setDNoNormalSum(awaitNoNormalSum);

                    Double awaitNoNormalRate = autoFloor_wait_timeD.getAwaitNoNormalRate();
                    LineTTLNoNormalRateTwo += awaitNoNormalRate;
                    autoFloor_wait_times.setDNoNormalRate(awaitNoNormalRate);

                    Double awaitPlateRate = autoFloor_wait_timeD.getAwaitPlateRate();
                    LineTTLPlateRateTwo += awaitPlateRate;
                    autoFloor_wait_times.setDPlateRate(awaitPlateRate);
                } else {
                    autoFloor_wait_times.setDNoNormalSum(0);
                    autoFloor_wait_times.setDNoNormalRate(0.0);
                    autoFloor_wait_times.setDPlateRate(0.0);
                }


                AutoFloor_Wait_Time autoFloor_wait_timeE = null;
                autoFloor_wait_timeE = autoFloor_wait_timeMapper.RobotAnalyzes(StartDate, EndDate, FloorName, autofloorTarget.gettProduct(), autofloorTarget.getLineName() + "-E");
                if (autoFloor_wait_timeE != null) {
                    autoFloor_wait_timeE.setAwaitNoNormalRate(autoFloor_wait_timeE.getAwaitNoNormalRate() / ((HtimeRate) * 3600));
                    autoFloor_wait_timeE.setAwaitPlateRate(autoFloor_wait_timeE.getAwaitPlateRate() / ((HtimeRate) * 3600));
                }
                if (autoFloor_wait_timeE != null) {
                    three++;
                    Integer awaitNoNormalSum = autoFloor_wait_timeE.getAwaitNoNormalSum();
                    LinenameNoNormalSum += awaitNoNormalSum;
                    autoFloor_wait_times.setENoNormalSum(awaitNoNormalSum);

                    Double awaitNoNormalRate = autoFloor_wait_timeE.getAwaitNoNormalRate();
                    LineTTLNoNormalRateThree += awaitNoNormalRate;
                    autoFloor_wait_times.setENoNormalRate(awaitNoNormalRate);

                    Double awaitPlateRate = autoFloor_wait_timeE.getAwaitPlateRate();
                    LineTTLPlateRateThree += awaitPlateRate;
                    autoFloor_wait_times.setEPlateRate(awaitPlateRate);
                } else {
                    autoFloor_wait_times.setENoNormalSum(0);
                    autoFloor_wait_times.setENoNormalRate(0.0);
                    autoFloor_wait_times.setEPlateRate(0.0);
                }

                AutoFloor_Wait_Time autoFloor_wait_timeF = null;
                autoFloor_wait_timeF = autoFloor_wait_timeMapper.RobotAnalyzes(StartDate, EndDate, FloorName, autofloorTarget.gettProduct(), autofloorTarget.getLineName() + "-F");
                if (autoFloor_wait_timeF != null) {
                    autoFloor_wait_timeF.setAwaitNoNormalRate(autoFloor_wait_timeF.getAwaitNoNormalRate() / ((HtimeRate) * 3600));
                    autoFloor_wait_timeF.setAwaitPlateRate(autoFloor_wait_timeF.getAwaitPlateRate() / ((HtimeRate) * 3600));
                }
                if (autoFloor_wait_timeF != null) {
                    three++;
                    Integer awaitNoNormalSum = autoFloor_wait_timeF.getAwaitNoNormalSum();
                    LinenameNoNormalSum += awaitNoNormalSum;
                    autoFloor_wait_times.setFNoNormalSum(awaitNoNormalSum);

                    Double awaitNoNormalRate = autoFloor_wait_timeF.getAwaitNoNormalRate();
                    LineTTLNoNormalRateThree += awaitNoNormalRate;
                    autoFloor_wait_times.setFNoNormalRate(awaitNoNormalRate);

                    Double awaitPlateRate = autoFloor_wait_timeF.getAwaitPlateRate();
                    LineTTLPlateRateThree += awaitPlateRate;
                    autoFloor_wait_times.setFPlateRate(awaitPlateRate);
                } else {
                    autoFloor_wait_times.setFNoNormalSum(0);
                    autoFloor_wait_times.setFNoNormalRate(0.0);
                    autoFloor_wait_times.setFPlateRate(0.0);
                }

                AutoFloor_Wait_Time autoFloor_wait_timeG = null;
                autoFloor_wait_timeG = autoFloor_wait_timeMapper.RobotAnalyzes(StartDate, EndDate, FloorName, autofloorTarget.gettProduct(), autofloorTarget.getLineName() + "-G");
                if (autoFloor_wait_timeG != null) {
                    autoFloor_wait_timeG.setAwaitNoNormalRate(autoFloor_wait_timeG.getAwaitNoNormalRate() / ((HtimeRate) * 3600));
                    autoFloor_wait_timeG.setAwaitPlateRate(autoFloor_wait_timeG.getAwaitPlateRate() / ((HtimeRate) * 3600));
                }
                if (autoFloor_wait_timeG != null) {
                    three++;
                    Integer awaitNoNormalSum = autoFloor_wait_timeG.getAwaitNoNormalSum();
                    LinenameNoNormalSum += awaitNoNormalSum;
                    autoFloor_wait_times.setGNoNormalSum(awaitNoNormalSum);

                    Double awaitNoNormalRate = autoFloor_wait_timeG.getAwaitNoNormalRate();
                    LineTTLNoNormalRateThree += awaitNoNormalRate;
                    autoFloor_wait_times.setGNoNormalRate(awaitNoNormalRate);

                    Double awaitPlateRate = autoFloor_wait_timeG.getAwaitPlateRate();
                    LineTTLPlateRateThree += awaitPlateRate;
                    autoFloor_wait_times.setGPlateRate(awaitPlateRate);
                } else {
                    autoFloor_wait_times.setGNoNormalSum(0);
                    autoFloor_wait_times.setGNoNormalRate(0.0);
                    autoFloor_wait_times.setGPlateRate(0.0);
                }
                AutoFloor_Wait_Time autoFloor_wait_timeH = null;
                autoFloor_wait_timeH = autoFloor_wait_timeMapper.RobotAnalyzes(StartDate, EndDate, FloorName, autofloorTarget.gettProduct(), autofloorTarget.getLineName() + "-H");
                if (autoFloor_wait_timeH != null) {
                    autoFloor_wait_timeH.setAwaitNoNormalRate(autoFloor_wait_timeH.getAwaitNoNormalRate() / ((HtimeRate) * 3600));
                    autoFloor_wait_timeH.setAwaitPlateRate(autoFloor_wait_timeH.getAwaitPlateRate() / ((HtimeRate) * 3600));
                }
                if (autoFloor_wait_timeH != null) {
                    three++;
                    Integer awaitNoNormalSum = autoFloor_wait_timeH.getAwaitNoNormalSum();
                    LinenameNoNormalSum += awaitNoNormalSum;
                    autoFloor_wait_times.setHNoNormalSum(awaitNoNormalSum);

                    Double awaitNoNormalRate = autoFloor_wait_timeH.getAwaitNoNormalRate();
                    LineTTLNoNormalRateThree += awaitNoNormalRate;
                    autoFloor_wait_times.setHNoNormalRate(awaitNoNormalRate);

                    Double awaitPlateRate = autoFloor_wait_timeH.getAwaitPlateRate();
                    LineTTLPlateRateThree += awaitPlateRate;
                    autoFloor_wait_times.setHPlateRate(awaitPlateRate);
                } else {
                    autoFloor_wait_times.setHNoNormalSum(0);
                    autoFloor_wait_times.setHNoNormalRate(0.0);
                    autoFloor_wait_times.setHPlateRate(0.0);
                }
                Double LinenameNoNormalRate = 0.0;
                Double LinenamePlateRate = 0.0;

                LinenameNoNormalRate = (LineTTLNoNormalRateOne + (LineTTLNoNormalRateTwo / 3) + (LineTTLNoNormalRateThree / 4)) / 3;
                LinenamePlateRate = (LineTTLPlateRateOne + (LineTTLPlateRateTwo / 3) + (LineTTLPlateRateThree / 4)) / 3;
                RobotNoNormalRate += LinenameNoNormalRate;
                RobotPlateRate += LinenamePlateRate;
                autoFloor_wait_times.setLineNoNormalRate(LinenameNoNormalRate);
                autoFloor_wait_times.setLinePlateRate(LinenamePlateRate);
                autoFloor_wait_times.setLineNoNormalSum(LinenameNoNormalSum);
                //
                // 待板時間 By Line  By cell
                // 偏位時間 By Line  By cell
                // 待板率 By Line  By cell
                AutoFloor_Wait_TimeList.add(autoFloor_wait_times);
            }
        }
        if (lineSum != 0) {
            RobotNoNormalRate /= lineSum;
            RobotPlateRate /= lineSum;
        }
        Map<String, Object> RobotMap = new HashMap<>();
        BigDecimal RobotNoNormalRateBig=new BigDecimal(RobotNoNormalRate*100);
        RobotNoNormalRate=RobotNoNormalRateBig.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
        RobotMap.put("AutoFloor_Wait_TimeList", AutoFloor_Wait_TimeList);
        RobotMap.put("RobotNoNormalRate", RobotNoNormalRate);
        RobotMap.put("RobotPlateRate", RobotPlateRate);

        return RobotMap;
    }

    public Map<String, Object> getTestRobot(List<AutoFloor_Target> LineList, String FloorName, String StartDate, String EndDate, String TypeTime, String Product, Integer Hours) {
        List<AutoFloor_Wait_Time> AutoFloor_Wait_TimeList = new ArrayList<>();
        int lineSum = 0;
        Double RobotNoNormalRate = 0.0;
        Double RobotPlateRate = 0.0;
        for (AutoFloor_Target autofloorTarget : LineList) {
            int dTarget = autofloorTarget.getdTarget().intValue();
            if (dTarget > 0) {
                lineSum++;
                int HtimeRate = 0;
                Integer LinenameNoNormalSum = 0;
                Double LineTTLNoNormalRate = 0.0;
                Double LineTTLPlateRate = 0.0;
                Integer LineTTLCellSum = 0;
                Double ANoNormalRate = 0.0;
                Double APlateRate = 0.0;
                AutoFloor_Wait_Time autoFloor_wait_times = new AutoFloor_Wait_Time();
                autoFloor_wait_times.setLine_name(autofloorTarget.getLineName());
                BigDecimal workingHours = autofloorTarget.getWorkingHours();
                Double WorkingHour = workingHours.doubleValue();
                //System.out.println(TypeTime+"------------"+WorkingHour);
                HtimeRate = RobotRunHtime(TypeTime, WorkingHour);
                AutoFloor_Wait_Time autoFloor_wait_timeA = null;
                autoFloor_wait_timeA = autoFloor_wait_timeMapper.RobotAnalyzes(StartDate, EndDate, FloorName, autofloorTarget.gettProduct(), autofloorTarget.getLineName() + "-A");
                if (autoFloor_wait_timeA != null) {
                    autoFloor_wait_timeA.setAwaitNoNormalRate(autoFloor_wait_timeA.getAwaitNoNormalRate() / ((HtimeRate) * 3600));
                    autoFloor_wait_timeA.setAwaitPlateRate(autoFloor_wait_timeA.getAwaitPlateRate() / ((HtimeRate) * 3600));
                }

                if (autoFloor_wait_timeA != null) {
                    Integer awaitNoNormalSum = autoFloor_wait_timeA.getAwaitNoNormalSum();
                    LinenameNoNormalSum += awaitNoNormalSum;
                    autoFloor_wait_times.setANoNormalSum(awaitNoNormalSum);

                    Double awaitNoNormalRate = autoFloor_wait_timeA.getAwaitNoNormalRate();
                    ANoNormalRate += awaitNoNormalRate;
                    autoFloor_wait_times.setANoNormalRate(awaitNoNormalRate);

                    Double awaitPlateRate = autoFloor_wait_timeA.getAwaitPlateRate();
                    APlateRate += awaitPlateRate;
                    autoFloor_wait_times.setAPlateRate(awaitPlateRate);
                } else {
                    autoFloor_wait_times.setANoNormalSum(0);
                    autoFloor_wait_times.setANoNormalRate(0.0);
                    autoFloor_wait_times.setAPlateRate(0.0);
                }

                AutoFloor_Wait_Time autoFloor_wait_timeB = null;
                autoFloor_wait_timeB = autoFloor_wait_timeMapper.RobotAnalyzes(StartDate, EndDate, FloorName, autofloorTarget.gettProduct(), autofloorTarget.getLineName() + "-B");
                if (autoFloor_wait_timeB != null) {
                    autoFloor_wait_timeB.setAwaitNoNormalRate(autoFloor_wait_timeB.getAwaitNoNormalRate() / ((HtimeRate) * 3600));
                    autoFloor_wait_timeB.setAwaitPlateRate(autoFloor_wait_timeB.getAwaitPlateRate() / ((HtimeRate) * 3600));
                }
                if (autoFloor_wait_timeB != null) {
                    LineTTLCellSum++;
                    Integer awaitNoNormalSum = autoFloor_wait_timeB.getAwaitNoNormalSum();
                    LinenameNoNormalSum += awaitNoNormalSum;
                    autoFloor_wait_times.setBNoNormalSum(awaitNoNormalSum);

                    Double awaitNoNormalRate = autoFloor_wait_timeB.getAwaitNoNormalRate();
                    LineTTLNoNormalRate += awaitNoNormalRate;
                    autoFloor_wait_times.setBNoNormalRate(awaitNoNormalRate);

                    Double awaitPlateRate = autoFloor_wait_timeB.getAwaitPlateRate();
                    LineTTLPlateRate += awaitPlateRate;
                    autoFloor_wait_times.setBPlateRate(awaitPlateRate);
                } else {
                    autoFloor_wait_times.setBNoNormalSum(0);
                    autoFloor_wait_times.setBNoNormalRate(0.0);
                    autoFloor_wait_times.setBPlateRate(0.0);
                }

                AutoFloor_Wait_Time autoFloor_wait_timeC = null;
                autoFloor_wait_timeC = autoFloor_wait_timeMapper.RobotAnalyzes(StartDate, EndDate, FloorName, autofloorTarget.gettProduct(), autofloorTarget.getLineName() + "-C");
                if (autoFloor_wait_timeC != null) {
                    autoFloor_wait_timeC.setAwaitNoNormalRate(autoFloor_wait_timeC.getAwaitNoNormalRate() / ((HtimeRate) * 3600));
                    autoFloor_wait_timeC.setAwaitPlateRate(autoFloor_wait_timeC.getAwaitPlateRate() / ((HtimeRate) * 3600));
                }
                if (autoFloor_wait_timeC != null) {
                    LineTTLCellSum++;
                    Integer awaitNoNormalSum = autoFloor_wait_timeC.getAwaitNoNormalSum();
                    LinenameNoNormalSum += awaitNoNormalSum;
                    autoFloor_wait_times.setCNoNormalSum(awaitNoNormalSum);

                    Double awaitNoNormalRate = autoFloor_wait_timeC.getAwaitNoNormalRate();
                    LineTTLNoNormalRate += awaitNoNormalRate;
                    autoFloor_wait_times.setCNoNormalRate(awaitNoNormalRate);

                    Double awaitPlateRate = autoFloor_wait_timeC.getAwaitPlateRate();
                    LineTTLPlateRate += awaitPlateRate;
                    autoFloor_wait_times.setCPlateRate(awaitPlateRate);
                } else {
                    autoFloor_wait_times.setCNoNormalSum(0);
                    autoFloor_wait_times.setCNoNormalRate(0.0);
                    autoFloor_wait_times.setCPlateRate(0.0);
                }

                AutoFloor_Wait_Time autoFloor_wait_timeD = null;
                autoFloor_wait_timeD = autoFloor_wait_timeMapper.RobotAnalyzes(StartDate, EndDate, FloorName, autofloorTarget.gettProduct(), autofloorTarget.getLineName() + "-D");
                if (autoFloor_wait_timeD != null) {
                    autoFloor_wait_timeD.setAwaitNoNormalRate(autoFloor_wait_timeD.getAwaitNoNormalRate() / ((HtimeRate) * 3600));
                    autoFloor_wait_timeD.setAwaitPlateRate(autoFloor_wait_timeD.getAwaitPlateRate() / ((HtimeRate) * 3600));
                }
                if (autoFloor_wait_timeD != null) {
                    LineTTLCellSum++;
                    Integer awaitNoNormalSum = autoFloor_wait_timeD.getAwaitNoNormalSum();
                    LinenameNoNormalSum += awaitNoNormalSum;
                    autoFloor_wait_times.setDNoNormalSum(awaitNoNormalSum);

                    Double awaitNoNormalRate = autoFloor_wait_timeD.getAwaitNoNormalRate();
                    LineTTLNoNormalRate += awaitNoNormalRate;
                    autoFloor_wait_times.setDNoNormalRate(awaitNoNormalRate);

                    Double awaitPlateRate = autoFloor_wait_timeD.getAwaitPlateRate();
                    LineTTLPlateRate += awaitPlateRate;
                    autoFloor_wait_times.setDPlateRate(awaitPlateRate);
                } else {
                    autoFloor_wait_times.setDNoNormalSum(0);
                    autoFloor_wait_times.setDNoNormalRate(0.0);
                    autoFloor_wait_times.setDPlateRate(0.0);
                }


                AutoFloor_Wait_Time autoFloor_wait_timeE = null;
                autoFloor_wait_timeE = autoFloor_wait_timeMapper.RobotAnalyzes(StartDate, EndDate, FloorName, autofloorTarget.gettProduct(), autofloorTarget.getLineName() + "-E");
                if (autoFloor_wait_timeE != null) {
                    autoFloor_wait_timeE.setAwaitNoNormalRate(autoFloor_wait_timeE.getAwaitNoNormalRate() / ((HtimeRate) * 3600));
                    autoFloor_wait_timeE.setAwaitPlateRate(autoFloor_wait_timeE.getAwaitPlateRate() / ((HtimeRate) * 3600));
                }
                if (autoFloor_wait_timeE != null) {
                    LineTTLCellSum++;
                    Integer awaitNoNormalSum = autoFloor_wait_timeE.getAwaitNoNormalSum();
                    LinenameNoNormalSum += awaitNoNormalSum;
                    autoFloor_wait_times.setENoNormalSum(awaitNoNormalSum);

                    Double awaitNoNormalRate = autoFloor_wait_timeE.getAwaitNoNormalRate();
                    LineTTLNoNormalRate += awaitNoNormalRate;
                    autoFloor_wait_times.setENoNormalRate(awaitNoNormalRate);

                    Double awaitPlateRate = autoFloor_wait_timeE.getAwaitPlateRate();
                    LineTTLPlateRate += awaitPlateRate;
                    autoFloor_wait_times.setEPlateRate(awaitPlateRate);
                } else {
                    autoFloor_wait_times.setENoNormalSum(0);
                    autoFloor_wait_times.setENoNormalRate(0.0);
                    autoFloor_wait_times.setEPlateRate(0.0);
                }

                AutoFloor_Wait_Time autoFloor_wait_timeF = null;
                autoFloor_wait_timeF = autoFloor_wait_timeMapper.RobotAnalyzes(StartDate, EndDate, FloorName, autofloorTarget.gettProduct(), autofloorTarget.getLineName() + "-F");
                if (autoFloor_wait_timeF != null) {
                    autoFloor_wait_timeF.setAwaitNoNormalRate(autoFloor_wait_timeF.getAwaitNoNormalRate() / ((HtimeRate) * 3600));
                    autoFloor_wait_timeF.setAwaitPlateRate(autoFloor_wait_timeF.getAwaitPlateRate() / ((HtimeRate) * 3600));
                }
                if (autoFloor_wait_timeF != null) {
                    LineTTLCellSum++;
                    Integer awaitNoNormalSum = autoFloor_wait_timeF.getAwaitNoNormalSum();
                    LinenameNoNormalSum += awaitNoNormalSum;
                    autoFloor_wait_times.setFNoNormalSum(awaitNoNormalSum);

                    Double awaitNoNormalRate = autoFloor_wait_timeF.getAwaitNoNormalRate();
                    LineTTLNoNormalRate += awaitNoNormalRate;
                    autoFloor_wait_times.setFNoNormalRate(awaitNoNormalRate);

                    Double awaitPlateRate = autoFloor_wait_timeF.getAwaitPlateRate();
                    LineTTLPlateRate += awaitPlateRate;
                    autoFloor_wait_times.setFPlateRate(awaitPlateRate);
                } else {
                    autoFloor_wait_times.setFNoNormalSum(0);
                    autoFloor_wait_times.setFNoNormalRate(0.0);
                    autoFloor_wait_times.setFPlateRate(0.0);
                }

                AutoFloor_Wait_Time autoFloor_wait_timeG = null;
                autoFloor_wait_timeG = autoFloor_wait_timeMapper.RobotAnalyzes(StartDate, EndDate, FloorName, autofloorTarget.gettProduct(), autofloorTarget.getLineName() + "-G");
                if (autoFloor_wait_timeG != null) {
                    autoFloor_wait_timeG.setAwaitNoNormalRate(autoFloor_wait_timeG.getAwaitNoNormalRate() / ((HtimeRate) * 3600));
                    autoFloor_wait_timeG.setAwaitPlateRate(autoFloor_wait_timeG.getAwaitPlateRate() / ((HtimeRate) * 3600));
                }
                if (autoFloor_wait_timeG != null) {
                    LineTTLCellSum++;
                    Integer awaitNoNormalSum = autoFloor_wait_timeG.getAwaitNoNormalSum();
                    LinenameNoNormalSum += awaitNoNormalSum;
                    autoFloor_wait_times.setGNoNormalSum(awaitNoNormalSum);

                    Double awaitNoNormalRate = autoFloor_wait_timeG.getAwaitNoNormalRate();
                    LineTTLNoNormalRate += awaitNoNormalRate;
                    autoFloor_wait_times.setGNoNormalRate(awaitNoNormalRate);

                    Double awaitPlateRate = autoFloor_wait_timeG.getAwaitPlateRate();
                    LineTTLPlateRate += awaitPlateRate;
                    autoFloor_wait_times.setGPlateRate(awaitPlateRate);
                } else {
                    autoFloor_wait_times.setGNoNormalSum(0);
                    autoFloor_wait_times.setGNoNormalRate(0.0);
                    autoFloor_wait_times.setGPlateRate(0.0);
                }
                AutoFloor_Wait_Time autoFloor_wait_timeH = null;
                autoFloor_wait_timeH = autoFloor_wait_timeMapper.RobotAnalyzes(StartDate, EndDate, FloorName, autofloorTarget.gettProduct(), autofloorTarget.getLineName() + "-H");
                if (autoFloor_wait_timeH != null) {
                    autoFloor_wait_timeH.setAwaitNoNormalRate(autoFloor_wait_timeH.getAwaitNoNormalRate() / ((HtimeRate) * 3600));
                    autoFloor_wait_timeH.setAwaitPlateRate(autoFloor_wait_timeH.getAwaitPlateRate() / ((HtimeRate) * 3600));
                }
                if (autoFloor_wait_timeH != null) {
                    LineTTLCellSum++;
                    Integer awaitNoNormalSum = autoFloor_wait_timeH.getAwaitNoNormalSum();
                    LinenameNoNormalSum += awaitNoNormalSum;
                    autoFloor_wait_times.setHNoNormalSum(awaitNoNormalSum);

                    Double awaitNoNormalRate = autoFloor_wait_timeH.getAwaitNoNormalRate();
                    LineTTLNoNormalRate += awaitNoNormalRate;
                    autoFloor_wait_times.setHNoNormalRate(awaitNoNormalRate);

                    Double awaitPlateRate = autoFloor_wait_timeH.getAwaitPlateRate();
                    LineTTLPlateRate += awaitPlateRate;
                    autoFloor_wait_times.setHPlateRate(awaitPlateRate);
                } else {
                    autoFloor_wait_times.setHNoNormalSum(0);
                    autoFloor_wait_times.setHNoNormalRate(0.0);
                    autoFloor_wait_times.setHPlateRate(0.0);
                }
                Double LinenameNoNormalRate = 0.0;
                Double LinenamePlateRate = 0.0;
                if (LineTTLCellSum != 0) {
                    LinenamePlateRate = APlateRate + (LineTTLPlateRate / LineTTLCellSum);
                    LinenameNoNormalRate = ANoNormalRate + (LineTTLNoNormalRate / LineTTLCellSum);
                }
                RobotNoNormalRate += LinenameNoNormalRate;
                RobotPlateRate += LinenamePlateRate;
                autoFloor_wait_times.setLineNoNormalRate(LinenameNoNormalRate);
                autoFloor_wait_times.setLinePlateRate(LinenamePlateRate);
                autoFloor_wait_times.setLineNoNormalSum(LinenameNoNormalSum);
                AutoFloor_Wait_TimeList.add(autoFloor_wait_times);
            }

        }
        if (lineSum != 0) {
            RobotNoNormalRate /= lineSum;
            RobotPlateRate /= lineSum;
        }
        Map<String, Object> RobotMap = new HashMap<>();
        BigDecimal RobotNoNormalRateBig=new BigDecimal(RobotNoNormalRate*100);
        RobotNoNormalRate=RobotNoNormalRateBig.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();

        RobotMap.put("AutoFloor_Wait_TimeList", AutoFloor_Wait_TimeList);
        RobotMap.put("RobotNoNormalRate", RobotNoNormalRate);
        RobotMap.put("RobotPlateRate", RobotPlateRate);

        return RobotMap;
    }

    public AutoFloor_Target RobotLineData(Date startdate, Date enddate, String LineName, String TimeType) {
        AutoFloor_Target RobotLineData = new AutoFloor_Target();

        if (TwoTimeType.equals(TimeType)) {
            if (timeUtility.Timequantum("08:30", "20:29")) {
                /* 白班 线体数据*/
                RobotLineData = autoFloor_targetMapper.RobotLineWorkingHours(startdate, enddate, LineName, DayTimeType);
            } else {
                /* 夜班 线体数据*/
                RobotLineData = autoFloor_targetMapper.RobotLineWorkingHours(startdate, enddate, LineName, NightTimeType);
            }
        } else if (DayTimeType.equals(TimeType)) {
            if (timeUtility.Timequantum("08:30", "20:29")) {
                /* 白班 线体数据*/
                RobotLineData = autoFloor_targetMapper.RobotLineWorkingHours(startdate, enddate, LineName, DayTimeType);
            } else {
                /* 夜班 线体数据*/
                RobotLineData = autoFloor_targetMapper.RobotLineWorkingHours(startdate, enddate, LineName, NightTimeType);
            }
        } else if (NightTimeType.equals(TimeType)) {
            RobotLineData = autoFloor_targetMapper.RobotLineWorkingHours(startdate, enddate, LineName, NightTimeType);
        } else if (YestrerDayTimeType.equals(TimeType)) {
            if (timeUtility.Timequantum("08:30", "20:29")) {
                RobotLineData = autoFloor_targetMapper.RobotLineWorkingHours(startdate, enddate, LineName, NightTimeType);
            } else {
                RobotLineData = autoFloor_targetMapper.RobotLineWorkingHours(startdate, enddate, LineName, NightTimeType);
            }
        }
        return RobotLineData;
    }

    public Integer RobotRunHtime(String TypeTime, Double WorkingHours) {
        int HtimeRate = 0;
        Base_Station DBDate = base_stationMapper.selectDBDate();
        Date ActionDate = DBDate.getAddTime();
        List<Integer> timeList = taskService.getTime(ActionDate);
        /*时*/
        Integer Htime = timeList.get(2);
        if (TwoTimeType.equals(TypeTime)) {
            if (timeUtility.Timequantum("08:30", "10:30")) {
                HtimeRate = Htime - 8;
            } else if (timeUtility.Timequantum("20:30", "22:30")) {
                HtimeRate = Htime - 20;
            } else {
                HtimeRate = 2;
            }
            if (HtimeRate == 0) {
                HtimeRate = 1;
            }
        } else if (DayTimeType.equals(TypeTime)) {

            if (timeUtility.Timequantum("08:30", "12:30")) {
                HtimeRate = Htime - 8;
                if (HtimeRate == 0) {
                    HtimeRate = 1;
                }
            } else if (timeUtility.Timequantum("12:30", "20:30")) {
                if (WorkingHours == 10.5) {
                    if (timeUtility.Timequantum("12:30", "20:30")) {
                        HtimeRate = Htime - 9;
                    } else {
                        HtimeRate = (11);
                    }
                } else if (WorkingHours == 9.5) {
                    if (timeUtility.Timequantum("12:30", "19:30")) {
                        HtimeRate = Htime - 9;
                    } else {
                        HtimeRate = (10);
                    }
                } else if (WorkingHours == 8.67) {
                    if (timeUtility.Timequantum("12:30", "18:30")) {
                        HtimeRate = Htime - 9;
                    } else {
                        HtimeRate = 9;
                    }
                } else if (WorkingHours == 7.67) {
                    if (timeUtility.Timequantum("12:30", "17:30")) {
                        HtimeRate = Htime - 9;
                    } else {
                        HtimeRate = (8);
                    }
                }
            } else {
                if (WorkingHours == 10.5) {
                    HtimeRate = (11);
                } else if (WorkingHours == 9.5) {
                    HtimeRate = (10);
                } else if (WorkingHours == 8.67) {
                    HtimeRate = (9);
                } else if (WorkingHours == 7.67) {
                    HtimeRate = (8);
                }
            }
        } else if (NightTimeType.equals(TypeTime)) {
            HtimeRate = Htime - 20;
            if (timeUtility.Timequantum("20:29", "00:00")) {
                if (HtimeRate == 0) {
                    HtimeRate = 1;
                }
            } else if (timeUtility.Timequantum("00:00", "20:29")) {
                if (WorkingHours == 10.5) {
                    if (timeUtility.Timequantum("00:00", "08:30")) {
                        HtimeRate = (Htime + 3);
                    } else {
                        HtimeRate = (11);
                    }
                } else if (WorkingHours == 9.5) {
                    if (timeUtility.Timequantum("00:00", "07:30")) {
                        HtimeRate = (Htime + 3);
                    } else {
                        HtimeRate = (10);
                    }
                } else if (WorkingHours == 8.67) {
                    if (timeUtility.Timequantum("00:00", "06:30")) {
                        HtimeRate = (Htime + 3);
                    } else {
                        HtimeRate = (9);
                    }
                } else if (WorkingHours == 7.67) {
                    if (timeUtility.Timequantum("00:00", "05:30")) {
                        HtimeRate = (Htime + 3);
                    } else {
                        HtimeRate = (8);
                    }
                } else {
                    HtimeRate = (9);
                }

            }
        } else if (YestrerDayTimeType.equals(TypeTime)) {
            HtimeRate = WorkingHours.intValue();
        } else if ("SelectData".equals(TypeTime)) {
            HtimeRate = WorkingHours.intValue();
        }


        return HtimeRate;
    }

    //超时待确认
    public Map<String, Object> TimeOutData(List<AutoFloor_Target> LineList, String StartDate, String EndDate, String FloorName) {
        List<AutoFloor_Fail_RETest> failretest = autoFloor_fail_reTestMapper.FAILRETEST(StartDate, EndDate, FloorName);
        String retestTimeOutYES = "YES";
        String retestTimeOutNO = "NO";
        Map<String, Object> TimeOutMap = new HashMap<>();
        List<TestEquipmentHealth> TimeOutList = new ArrayList<>();
        double TimeOutRateALL = 0;
        int LineAllRow = 0;
        for (AutoFloor_Target Line : LineList) {
            int dTarget = Line.getdTarget().intValue();
            if (dTarget > 0) {
                LineAllRow++;
                int TimeOutNoNum = 0;
                int TimeOutAllNum = 0;
                TestEquipmentHealth equipmentHealth = new TestEquipmentHealth();
                String lineName = Line.getLineName();

                for (AutoFloor_Fail_RETest autoFloor_fail_reTest : failretest) {
                    String ReTestLineName = autoFloor_fail_reTest.getLineName();
                    if (lineName.equals(ReTestLineName)) {
                        String retestTimeOut = autoFloor_fail_reTest.getRetestTimeOut();
                        String retestFinish = autoFloor_fail_reTest.getRetestFinish();
                        if (retestTimeOutYES.equals(retestTimeOut)) {
                            TimeOutAllNum++;
                        }
                        if (retestTimeOutYES.equals(retestTimeOut) && retestTimeOutNO.equals(retestFinish)) {
                            TimeOutNoNum++;
                            TimeOutAllNum++;
                        }
                    }
                }
                double TimeOutRate = 0.0;
                if (TimeOutAllNum != 0) {
                    TimeOutRate = TimeOutNoNum / (double) TimeOutAllNum;
                }
                BigDecimal TimeOutRateBig = new BigDecimal(TimeOutRate * 100);
                TimeOutRate = TimeOutRateBig.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                TimeOutRateALL += TimeOutRate;
                equipmentHealth.setLineName(lineName);
                equipmentHealth.setTimeOutRate(TimeOutRate);

                TimeOutList.add(equipmentHealth);
            }
        }
        if (LineAllRow != 0) {
            TimeOutRateALL /= (double) LineAllRow;
        }
        BigDecimal TimeOutRateALLBig = new BigDecimal(TimeOutRateALL);
        TimeOutRateALL = TimeOutRateALLBig.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        TimeOutMap.put("TimeOutList", TimeOutList);
        TimeOutMap.put("TimeOutRateALL", TimeOutRateALL);
        return TimeOutMap;
    }

    //机故率
    public Map<String, Object> MachineRate(List<AutoFloor_Target> LineList, String TypeTime, String StartDate, String EndDate, String FloorName, Integer actionMinuteD) {
        Map<String, Object> MachineRateMap = new HashMap<>();
        Base_Station DBDate = base_stationMapper.selectDBDate();
        Date ActionDate = DBDate.getAddTime();
        List<Integer> timeList = taskService.getTime(ActionDate);
        /*时*/
        Integer Htime = timeList.get(2);
        Integer Minute = timeList.get(3);
        double MachineRateAll = 0;
        int LineNum=0;
        String Product = "D42";
        String TestUNFloor=FloorName.substring(0,3)+"-"+FloorName.substring(3);
        List<TestEquipmentHealth> MachineRateList = new ArrayList<>();
        List<AutoFloor_Wait_Time> autoFloor_wait_times=new ArrayList<>();
        List<AutoFloor_CCDData> autoFloor_ccdData=new ArrayList<>();
        List<Warning_List_Slot> warning_list_slots=new ArrayList<>();
        List<Base_Station> base_stations=new ArrayList<>();
        List<AutoFloor_Machine_Detail> TestCellWorkstationSum=new ArrayList<>();
        List<Warning_List_Slot> TestCellUNStationSum=new ArrayList<>();
        if (LineList.size()>0){
                 autoFloor_wait_times = autoFloor_wait_timeMapper.RobotCellAnalyzes(StartDate, EndDate, FloorName);
                 /*autoFloor_ccdData = autoFloor_ccdDataMapper.selectCCDDATATime(StartDate, EndDate,FloorName);*/
                 warning_list_slots = warning_list_slotMapper.TestUNTime(StartDate, EndDate, TestUNFloor);
                 base_stations = base_stationMapper.selectEquipmentMachineSum(Product, TestUNFloor);
                 TestCellWorkstationSum = autoFloor_uphMapper.selectTestCellWorkstationSum(FloorName);
                 TestCellUNStationSum = warning_list_slotMapper.TestCellUNStationSum(StartDate, EndDate, TestUNFloor);
        }
            for (AutoFloor_Target Line : LineList) {
                int DTarget = Line.getdTarget().intValue();
                TestEquipmentHealth testEquipmentHealth = new TestEquipmentHealth();
                if (DTarget>0){
                    LineNum++;
                    String lineName = Line.getLineName();
                    double WorkingHours = Line.getWorkingHours().doubleValue();
                    int ProductionTime = 0;
                    if (TwoTimeType.equals(TypeTime)) {
                        ProductionTime = RobotRunHtime(TypeTime, WorkingHours);
                        ProductionTime *= 60;
                        if (timeUtility.Timequantum("08:30", "10:30")) {
                            ProductionTime = ((Htime - 8) * 60 + Minute) - 30;
                        } else if (timeUtility.Timequantum("20:30", "22:30")) {
                            ProductionTime = ((Htime - 20) * 60 + Minute) - 30;
                        }
                    } else if (DayTimeType.equals(TypeTime)) {
                        ProductionTime = testEquipmentHealthService.DayActionRunTime(WorkingHours, actionMinuteD);
                    } else if (NightTimeType.equals(TypeTime)) {
                        ProductionTime = testEquipmentHealthService.NightActionRunTime(WorkingHours, actionMinuteD);
                    } else if (YestrerDayTimeType.equals(TypeTime)) {
                        ProductionTime = testEquipmentHealthService.YestrerActionRunTime(WorkingHours, actionMinuteD);
                    } else if ("SelectData".equals(TypeTime)) {
                        ProductionTime = testEquipmentHealthService.YestrerActionRunTime(WorkingHours, actionMinuteD);
                    }
                    ProductionTime *= 60;
                    List<String> autoFloor_machine_details = testEquipmentHealthService.EquipmentHealthLineCell(lineName);

                    double machineRate = 0;
                    int AutoHtimeALL = 0;
                    int MachineTimeALL = 0;
                    for (String lineNameCell : autoFloor_machine_details) {
                        AutoHtimeALL+=ProductionTime;
                        //偏位机故時間
                        double NoNormalUnTime=0;
                        for (AutoFloor_Wait_Time autoFloor_wait_time : autoFloor_wait_times) {
                            String WaitTimeLine_name = autoFloor_wait_time.getLine_name();
                            Double aNoNormalRate = autoFloor_wait_time.getAwaitNoNormalRate();
                            if (lineNameCell.equals(WaitTimeLine_name)) {
                                if (aNoNormalRate != null) {
                                    NoNormalUnTime = aNoNormalRate.intValue();
                                }
                            }
                        }
                        //模組机故時間
                        double ccdUntime=0;
                        Integer CellWorkstationSum = 0;
                        for (AutoFloor_Machine_Detail autoFloor_machine_detail : TestCellWorkstationSum) {
                            String TestCellCelllineName = autoFloor_machine_detail.getLineName();
                            if (TestCellCelllineName!=null){
                                if (TestCellCelllineName.equals(lineNameCell)){
                                    CellWorkstationSum= autoFloor_machine_detail.getMachineSum();
                                }
                            }
                        }

                        for (AutoFloor_CCDData autoFloor_ccdDatum : autoFloor_ccdData) {
                            String equipmentid = autoFloor_ccdDatum.getEquipmentid();
                            String CCDlinename = autoFloor_ccdDatum.getLinename();
                            if (lineName.equals(CCDlinename)){
                                String CCDEquipmentid = testEquipmentHealthService.CCDEquipmentid(lineNameCell, lineName);
                                if (equipmentid!=null){
                                    if (CCDEquipmentid.equals(equipmentid)){
                                        Date errorstarttime = autoFloor_ccdDatum.getErrorstarttime();
                                        Date errorendtime = autoFloor_ccdDatum.getErrorendtime();
                                        ccdUntime+=errorendtime.getTime()-errorstarttime.getTime();
                                    }
                                }
                            }
                        }
                        //治具
                        String TestUNTimeline=lineNameCell.substring(0,3)+"-"+lineNameCell.substring(3,8)+"-"+lineNameCell.substring(8,10)+lineNameCell.substring(11);
                        Integer unStationSum = 0;
                        for (Warning_List_Slot warning_list_slot : TestCellUNStationSum) {
                            String line = warning_list_slot.getLine();
                            if (line!=null){
                                if (TestUNTimeline.equals(line)){
                                    unStationSum = warning_list_slot.getMachineSum();
                                }
                            }
                        }
                        double Untime=0;
                        for (Warning_List_Slot warning_list_slot : warning_list_slots) {
                            String line = warning_list_slot.getLine();
                            if (TestUNTimeline.equals(line)){
                                Date addTime = warning_list_slot.getAddTime();
                                Date closeTime = warning_list_slot.getCloseTime();
                                Untime += (closeTime.getTime()-addTime.getTime());
                            }
                        }
                        String Floor = lineNameCell.substring(0, 3);
                        String FloorNum = lineNameCell.substring(3, 5);
                        String Lines = lineNameCell.substring(8,10);
                        String Cell=lineNameCell.substring(11);
                        String EquipmentLine = Floor + "-" + FloorNum + "ARF-" + Lines+Cell;
                        int EquipmentMachineSum=0;
                        for (Base_Station base_station : base_stations) {
                            String line = base_station.getLine();
                            Integer lineStatusNum = base_station.getLineStatusNum();
                            if (EquipmentLine.equals(line)){
                                EquipmentMachineSum=lineStatusNum;
                            }
                        }
                        Untime=(Untime/1000/4);
                        if(unStationSum!=null&&unStationSum!=0){
                            Untime=(Untime/unStationSum);
                            if (EquipmentMachineSum!=0){
                                Untime= (Untime/EquipmentMachineSum);
                            }
                        }
                        //机故时间  =偏位机故时间+治具机故时间+模組機故机故时间
                        //System.out.println(NoNormalUnTime+"--NoNormalUnTime--"+ccdUntime+"--ccdUntime--"+Untime+"---Untime");
                        //+ccdUntime
                        if (CellWorkstationSum!=0){
                            ccdUntime/=CellWorkstationSum;
                        }

                        double MachineTime = NoNormalUnTime+Untime+ccdUntime;
                        if (MachineTime<0){
                            MachineTime=0;
                        }
                        MachineTimeALL+=MachineTime;
                    }
                    if (AutoHtimeALL!=0){machineRate= MachineTimeALL/ (double)AutoHtimeALL;}
                    BigDecimal machineRateBig = new BigDecimal(machineRate * 100);
                    machineRate = machineRateBig.setScale(2, BigDecimal.ROUND_FLOOR).doubleValue();
                    testEquipmentHealth.setLineName(lineName);
                    if (machineRate < 0) {
                        machineRate = 0.0;
                    }
                    MachineRateAll += machineRate;

                    testEquipmentHealth.setMachineRate(machineRate);
                }
                MachineRateList.add(testEquipmentHealth);
            }
                if(LineNum!=0){MachineRateAll/=LineNum;}
                BigDecimal MachineRateAllBig = new BigDecimal(MachineRateAll);
                MachineRateAll = MachineRateAllBig.setScale(2, BigDecimal.ROUND_FLOOR).doubleValue();
                MachineRateMap.put("MachineRateList", MachineRateList);
                MachineRateMap.put("MachineRateAll", MachineRateAll);

        return MachineRateMap;
    }


}