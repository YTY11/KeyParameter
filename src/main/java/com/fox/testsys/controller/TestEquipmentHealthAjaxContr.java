package com.fox.testsys.controller;

import com.alibaba.fastjson.JSONArray;
import com.fox.testsys.entity.AutoFloor_Target;
import com.fox.testsys.entity.OutTimePathData;
import com.fox.testsys.service.TestCapacityMessageService;
import com.fox.testsys.service.TestEquipmentHealthService;
import com.fox.testsys.utility.AutoFloorDate;
import com.google.gson.Gson;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author
 * @create 2020-04-23 13:47
 */
@RestController
public class TestEquipmentHealthAjaxContr {

    @Autowired
    TestEquipmentHealthService testEquipmentHealthService;
    @Autowired
    AutoFloorDate autoFloorDate;
    @Autowired
    TestCapacityMessageService testCapacityMessageService;

    @RequestMapping("equipmenthealthAjax")
    public String TestEquipmentHealthContr(@RequestParam(value = "LineName") String LineName,
                                           @RequestParam(value = "TypeTime") String TypeTime,
                                           @RequestParam(value = "StationName") String StationName) {
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
        String StartDateStr = (String) AutoFloorDateMap.get("twoDateStartDate");
        String EndDateStr = (String) AutoFloorDateMap.get("twoDateEndDate");
        DataStartTime = StartDateStr;
        DataEndTime = EndDateStr;
        AutoFloor_Target targetsum = testCapacityMessageService.AloneLineData(LineName, schedule, TypeTime);
        /*线体楼层名称*/
        String Floorname = "";
        /*机种工站*/
        String Productname = "";
        Double WorkingHours = 0.0;
        if (targetsum != null) {
            Floorname = targetsum.gettFloor();
            Productname = targetsum.gettProduct();
        }
        //超時主板確認信息
        List<OutTimePathData> outTimePathData = testEquipmentHealthService.OutTimePathAjaxData(StationName, StartDateStr, EndDateStr, LineName, Floorname, Productname);
        Gson gson = new Gson();
        String outTimePath = JSONArray.toJSONString(outTimePathData);
        return outTimePath;
    }

}
