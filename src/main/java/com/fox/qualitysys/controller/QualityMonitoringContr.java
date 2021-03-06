package com.fox.qualitysys.controller;


import com.fox.alarmsys.mapper.AutoFloor_Test_ExceptionMapper;
import com.fox.qualitysys.service.QualityMonitoringService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * @author
 * @create 2020-04-24 8:35
 */
@Controller
public class QualityMonitoringContr {
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @Autowired
    QualityMonitoringService qualityMonitoringService;
    @Autowired
    AutoFloor_Test_ExceptionMapper autoFloor_test_exceptionMapper;

    @RequestMapping("qualitymonitoring")
    public String QualityMonitoringContr(Map map) {
        long EndTimeLong = autoFloor_test_exceptionMapper.DBDate().getTime();
        String EndDate = df.format(new Date(EndTimeLong+10*60*1000));
        String StartDate = df.format(new Date(EndTimeLong-60*60*1000));
        Integer D061F = qualityMonitoringService.QualityMonitoringData("D061F",StartDate,EndDate);
        map.put("D061F",D061F);
        return "quality/qualitymonitoring";
    }
}
