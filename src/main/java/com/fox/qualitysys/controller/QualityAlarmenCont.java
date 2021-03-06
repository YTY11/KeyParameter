package com.fox.qualitysys.controller;


import com.fox.alarmsys.mapper.AutoFloor_Test_ExceptionMapper;
import com.fox.alarmsys.service.AlarmSystemService;
import com.fox.qualitysys.entity.AutoFloor_Key_CheCkUp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author
 * @create 2020-06-13 9:47
 */
@Controller
public class QualityAlarmenCont {

    @Autowired
    AlarmSystemService alarmSystemService;

    @Autowired
    AutoFloor_Test_ExceptionMapper autoFloor_test_exceptionMapper;

    DateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    @RequestMapping("qualityalarmen")
    public String QualityAlarmenCont(@RequestParam(value = "FloorName",required = true,defaultValue = "D061F") String FloorName, Map map){
        long EndTime = autoFloor_test_exceptionMapper.DBDate().getTime();
        long StartTime = EndTime - (3 * 60 * 1000);
        String StartDate=format.format(new Date(StartTime));
        String EndDate=format.format(new Date(EndTime));
        List<AutoFloor_Key_CheCkUp> autoFloor_key_checkNGS = alarmSystemService.AlarmSystemData(FloorName, StartDate, EndDate,"ALL");
        map.put("autoFloor_key_checkNGS",autoFloor_key_checkNGS);
        map.put("StartDate",StartDate);
        map.put("EndDate",EndDate);

        return "quality/qualityalarmen";
    }
}
