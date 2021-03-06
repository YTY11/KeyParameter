package com.fox.testsys.utility;


import com.fox.alarmsys.mapper.AutoFloor_Test_ExceptionMapper;
import com.fox.testsys.entity.AutoFloor_Machine_Detail;
import com.fox.testsys.mapper.AutoFloor_Machine_DetailMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author
 * @create 2020-04-09 18:05
 */
@Component
@Configuration
public class AutoFloorDate {
    @Autowired
    TimeUtility timeUtility;

    @Autowired
    TaskService taskService;

    @Autowired
    AutoFloor_Machine_DetailMapper autoFloor_machine_detailMapper;

    @Autowired
    AutoFloor_Test_ExceptionMapper autoFloor_test_exceptionMapper;

    //机种名称
    private static  String LOT_PRODUCT = "Lotus";
    private static final String MAC_PRODUCT = "Macan";
    private static final String PHA_PRODUCT = "Pha";
    //班别
    private static final String TwoTimeType = "two";
    private static final String DayTimeType = "Day";
    private static final String NightTimeType = "Night";
    private static final String YestrerDayTimeType = "Yesterday";

    DateFormat Yeardf = new SimpleDateFormat("yyyy/MM/dd");
    DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    DateFormat avitime = new SimpleDateFormat("yyyyMMdd");
    public Map<String, Object> AutoFloorDate(String TypeTime) {
        Map<String, Object> AutoFloorDateMap = new HashMap<>();
        Integer TimeGNumber = 1;
        if (TwoTimeType.equals(TypeTime)) {
            if (timeUtility.Timequantum("08:30", "20:29")) {
            } else {
                TimeGNumber = 0;
            }
        }
        /*获取当前时间*/
        List<java.sql.Date> sqlList = taskService.Schedule();
        /*当前日期*/
        Date schedule = sqlList.get(0);

        Date actiondate = sqlList.get(0);
        /*当前日期减1*/
        Date YesterdayDate = sqlList.get(1);
        /*当前日期加1*/
        Date TomorrowDate = sqlList.get(2);
        /*当前日期减2*/
        Date BeforeDay = sqlList.get(5);

        List<Integer> timeList = taskService.getTime(new Date());
        /*时*/
        Integer Htime = timeList.get(2);
        /*分*/
        Integer Minute = timeList.get(3);
        /*工作时间*/
        Integer actionMinuteD = 0;
        Integer actionMinute;
        if (timeUtility.Timequantum("08:30", "20:30")){
            actionMinute = ((Htime - 8) * 60 + Minute) - 30;
            actionMinuteD = actionMinute;
        }if (timeUtility.Timequantum("20:30", "23:59")){
            actionMinute = ((Htime - 20) * 60 + Minute) - 30;
            actionMinuteD = actionMinute;
        }else if (timeUtility.Timequantum("00:00", "08:29")){
            actionMinute = ((4+Htime) * 60 + Minute) - 30;
            actionMinuteD = actionMinute;
        }

        //产能浮动数据
        Integer FloatNum = 200;
        /*过24时 时间变量*/
        String StartDateStr = null;
        String EndDateStr = null;

        /*过24时 时间变量*/
        String AviStartDateStr = null;
        String AviEndDateStr = null;
        //符合两小时一查的数据时间
        String twoDateStartDate = null;
        String twoDateEndDate = null;
        //查询线体时间
        if (TwoTimeType.equals(TypeTime)) {
            schedule = actiondate;
            if (timeUtility.Timequantum("00:00", "08:30")) {
                StartDateStr = Yeardf.format(YesterdayDate);
                StartDateStr += " 20:30:00";
                EndDateStr = Yeardf.format(actiondate);
                EndDateStr += " 08:30:00";

                AviStartDateStr=avitime.format(YesterdayDate);
                AviStartDateStr += "203000";
                AviEndDateStr = avitime.format(actiondate);
                AviEndDateStr += "083000";

                schedule = YesterdayDate;
            } else if (timeUtility.Timequantum("08:30", "20:30")) {
                StartDateStr = Yeardf.format(actiondate);
                StartDateStr += " 08:30:00";
                EndDateStr = Yeardf.format(actiondate);
                EndDateStr += " 20:30:00";

                AviStartDateStr = avitime.format(actiondate);
                AviStartDateStr += "083000";
                AviEndDateStr = avitime.format(actiondate);
                AviEndDateStr += "203000";
            } else if (timeUtility.Timequantum("20:30", "23:59")) {
                StartDateStr = Yeardf.format(actiondate);
                StartDateStr += " 20:30:00";
                EndDateStr = Yeardf.format(TomorrowDate);
                EndDateStr += " 08:30:00";

                AviStartDateStr = avitime.format(actiondate);
                AviStartDateStr += "203000";
                AviEndDateStr = avitime.format(TomorrowDate);
                AviEndDateStr += "083000";
            }
            //符合两小时一查的数据时间
            if (timeUtility.Timequantum(" 08:30", "10:30")) {
                twoDateStartDate = Yeardf.format(actiondate);
                twoDateStartDate += " 08:30:00";
                twoDateEndDate = Yeardf.format(actiondate);
                twoDateEndDate += " 10:30:00";

            } else if (timeUtility.Timequantum(" 20:30", "22:30")) {
                twoDateStartDate = Yeardf.format(actiondate);
                twoDateStartDate += " 20:30:00";
                twoDateEndDate = Yeardf.format(actiondate);
                twoDateEndDate += " 22:30:00";

            } else {
                long EndLong = autoFloor_test_exceptionMapper.DBDate().getTime();
                long StartLong = EndLong - 2 * (60 * 60 * 1000);
                twoDateStartDate = df.format(new Date(StartLong));
                twoDateEndDate = df.format(new Date(EndLong));
            }
        } else if (DayTimeType.equals(TypeTime)) {
            schedule = actiondate;
            if (timeUtility.Timequantum("00:00", "08:30")) {
                StartDateStr = Yeardf.format(YesterdayDate);
                StartDateStr += " 08:30:00";
                EndDateStr = Yeardf.format(YesterdayDate);
                EndDateStr += " 20:30:00";
            } else if (timeUtility.Timequantum("08:30", "20:30")) {
                StartDateStr = Yeardf.format(actiondate);
                StartDateStr += " 08:30:00";
                EndDateStr = Yeardf.format(actiondate);
                EndDateStr += " 20:30:00";
            } else if (timeUtility.Timequantum("20:30", "23:59")) {
                StartDateStr = Yeardf.format(actiondate);
                StartDateStr += " 20:30:00";
                EndDateStr = Yeardf.format(TomorrowDate);
                EndDateStr += " 08:30:00";
            }
        } else if (NightTimeType.equals(TypeTime)) {
            TimeGNumber = 0;
            if (timeUtility.Timequantum("20:29", "23:59")) {
                schedule = actiondate;
                StartDateStr = Yeardf.format(actiondate);
                StartDateStr += " 20:30:00";
                EndDateStr = Yeardf.format(TomorrowDate);
                EndDateStr += " 08:30:00";
            } else if (timeUtility.Timequantum("00:00", "20:29")) {
                schedule = YesterdayDate;
                StartDateStr = Yeardf.format(YesterdayDate);
                StartDateStr += " 20:30:00";
                EndDateStr = Yeardf.format(actiondate);
                EndDateStr += " 08:30:00";
            }
        } else if (YestrerDayTimeType.equals(TypeTime)) {
            TimeGNumber = 2;
            schedule = YesterdayDate;
            StartDateStr = Yeardf.format(YesterdayDate);
            StartDateStr += " 08:30:00";
            EndDateStr = Yeardf.format(actiondate);
            EndDateStr += " 08:30:00";
            if (timeUtility.Timequantum("00:00", "08:30")) {
                StartDateStr = Yeardf.format(BeforeDay);
                StartDateStr += " 08:30:00";
                EndDateStr = Yeardf.format(YesterdayDate);
                EndDateStr += " 08:30:00";
            }
        }
        if (!TwoTimeType.equals(TypeTime)) {
            twoDateStartDate = StartDateStr;
            twoDateEndDate = EndDateStr;
        }

        AutoFloorDateMap.put("TomorrowDate", TomorrowDate);
        AutoFloorDateMap.put("schedule", schedule);
        AutoFloorDateMap.put("actionMinuteD", actionMinuteD);
        AutoFloorDateMap.put("TimeGNumber", TimeGNumber);
        AutoFloorDateMap.put("StartDateStr", StartDateStr);
        AutoFloorDateMap.put("EndDateStr", EndDateStr);
        AutoFloorDateMap.put("AviStartDateStr", AviStartDateStr);
        AutoFloorDateMap.put("AviEndDateStr", AviEndDateStr);
        AutoFloorDateMap.put("FloatNum", FloatNum);
        AutoFloorDateMap.put("actiondate", actiondate);
        AutoFloorDateMap.put("Htime", Htime);
        AutoFloorDateMap.put("Minute", Minute);
        AutoFloorDateMap.put("twoDateStartDate", twoDateStartDate);
        AutoFloorDateMap.put("twoDateEndDate", twoDateEndDate);
        AutoFloorDateMap.put("YesterdayDate", YesterdayDate);

        return AutoFloorDateMap;
    }

    //SKAS戰術
    // 20:10: 08:00
    //"08:10", "20:00"
    public Map<String, Object> SKASAutoDate(String TypeTime) {
        Map<String, Object> AutoFloorDateMap = new HashMap<>();
        Integer TimeGNumber = 1;
        if (TwoTimeType.equals(TypeTime)) {
            if (timeUtility.Timequantum("08:30", "20:29")) {

            } else {
                TimeGNumber = 0;
            }
        }
        /*获取当前时间*/
        List<java.sql.Date> sqlList = taskService.Schedule();
        /*当前日期*/
        Date schedule = sqlList.get(0);
        Date actiondate = sqlList.get(0);
        /*当前日期减1*/
        Date YesterdayDate = sqlList.get(1);
        /*当前日期加1*/
        Date TomorrowDate = sqlList.get(2);
        /*当前日期减2*/
        Date BeforeDay = sqlList.get(5);

        List<Integer> timeList = taskService.getTime(new Date());
        /*时*/
        Integer Htime = timeList.get(2);
        /*分*/
        Integer Minute = timeList.get(3);
        /*工作时间*/
        Integer actionMinuteD = 0;
        Integer actionMinute;
        if (Htime != null && Htime >= 8) {
            actionMinute = ((Htime - 8) * 60 + Minute) - 30;
            actionMinuteD = actionMinute;
        }

        //产能浮动数据
        Integer FloatNum = 200;
        /*过24时 时间变量*/
        String StartDateStr = null;
        String EndDateStr = null;

        //查询线体时间
        if (TwoTimeType.equals(TypeTime)) {
            schedule = actiondate;
            if (timeUtility.Timequantum("00:00", "08:30")) {
                StartDateStr = Yeardf.format(YesterdayDate);
                StartDateStr += " 20:30:00";
                EndDateStr = Yeardf.format(actiondate);
                EndDateStr += " 08:30:00";

                schedule = YesterdayDate;
            } else if (timeUtility.Timequantum("08:30", "20:30")) {
                StartDateStr = Yeardf.format(actiondate);
                StartDateStr += " 08:30:00";
                EndDateStr = Yeardf.format(actiondate);
                EndDateStr += " 20:30:00";

            } else if (timeUtility.Timequantum("20:30", "23:59")) {
                StartDateStr = Yeardf.format(actiondate);
                StartDateStr += " 20:30:00";
                EndDateStr = Yeardf.format(TomorrowDate);
                EndDateStr += " 08:30:00";

            }
        } else if (DayTimeType.equals(TypeTime)) {
            schedule = actiondate;
            if (timeUtility.Timequantum("00:00", "08:30")) {
                StartDateStr = Yeardf.format(YesterdayDate);
                StartDateStr += " 08:10:00";
                EndDateStr = Yeardf.format(YesterdayDate);
                EndDateStr += " 20:00:00";
            } else if (timeUtility.Timequantum("08:30", "20:30")) {
                StartDateStr = Yeardf.format(actiondate);
                StartDateStr += " 08:10:00";
                EndDateStr = Yeardf.format(actiondate);
                EndDateStr += " 20:00:00";
            } else if (timeUtility.Timequantum("20:30", "23:59")) {
                StartDateStr = Yeardf.format(actiondate);
                StartDateStr += " 20:10:00";
                EndDateStr = Yeardf.format(TomorrowDate);
                EndDateStr += " 08:00:00";
            }
        } else if (NightTimeType.equals(TypeTime)) {
            TimeGNumber = 0;
            if (timeUtility.Timequantum("20:29", "23:59")) {
                schedule = actiondate;
                StartDateStr = Yeardf.format(actiondate);
                StartDateStr += " 20:10:00";
                EndDateStr = Yeardf.format(TomorrowDate);
                EndDateStr += " 08:00:00";
            } else if (timeUtility.Timequantum("00:00", "20:29")) {
                schedule = YesterdayDate;
                StartDateStr = Yeardf.format(YesterdayDate);
                StartDateStr += " 20:10:00";
                EndDateStr = Yeardf.format(actiondate);
                EndDateStr += " 08:00:00";
            }
        } else if (YestrerDayTimeType.equals(TypeTime)) {
            TimeGNumber = 2;
            schedule = YesterdayDate;
            StartDateStr = Yeardf.format(YesterdayDate);
            StartDateStr += " 08:10:00";
            EndDateStr = Yeardf.format(actiondate);
            EndDateStr += " 08:10:00";
            if (timeUtility.Timequantum("00:00", "08:30")) {
                StartDateStr = Yeardf.format(BeforeDay);
                StartDateStr += " 08:10:00";
                EndDateStr = Yeardf.format(YesterdayDate);
                EndDateStr += " 08:10:00";
            }
        }

        AutoFloorDateMap.put("TomorrowDate", TomorrowDate);
        AutoFloorDateMap.put("schedule", schedule);
        AutoFloorDateMap.put("actionMinuteD", actionMinuteD);
        AutoFloorDateMap.put("TimeGNumber", TimeGNumber);
        AutoFloorDateMap.put("StartDateStr", StartDateStr);
        AutoFloorDateMap.put("EndDateStr", EndDateStr);
        AutoFloorDateMap.put("FloatNum", FloatNum);
        AutoFloorDateMap.put("actiondate", actiondate);
        AutoFloorDateMap.put("Htime", Htime);
        return AutoFloorDateMap;
    }





    public Map<String, Integer> getFloor_Proudct(String floor_name) {
        int Lot = 0;
        int Mac = 0;
        int Pha = 0;
        if ("K051F".equals(floor_name)){
            LOT_PRODUCT ="Apollo";
        }
        List<AutoFloor_Machine_Detail> autoFloor_machine_details = autoFloor_machine_detailMapper.selectFloor_Product(floor_name);
        for (AutoFloor_Machine_Detail autoFloor_machine_detail : autoFloor_machine_details) {
            if (LOT_PRODUCT.equals(autoFloor_machine_detail.gettProduct())) {
                Lot = 1;
            }
            if (MAC_PRODUCT.equals(autoFloor_machine_detail.gettProduct())) {
                Mac = 1;
            }
            if (PHA_PRODUCT.equals(autoFloor_machine_detail.gettProduct())) {
                Pha = 1;
            }
        }
        Map<String, Integer> ProudctMap = new HashMap<>();
        ProudctMap.put("Lot", Lot);
        ProudctMap.put("Mac", Mac);
        ProudctMap.put("Pha", Pha);


        return ProudctMap;
    }
}
