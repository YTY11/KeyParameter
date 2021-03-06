package com.fox.skasafs_sys.controller;

import com.alibaba.fastjson.JSON;

import com.fox.skasafs_sys.entity.AGV_Exception;
import com.fox.skasafs_sys.service.SKASAFSSystemContService;
import com.fox.skasafs_sys.service.SKASAFSSystemMessageAjaxService;
import com.fox.testsys.utility.AutoFloorDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

/**
 * @author
 * @create 2020-07-06 14:06
 */

@RestController
public class   SKASAFSSystemMessageAjaxCont {
    @Autowired
    SKASAFSSystemMessageAjaxService skasafsSystemMessageAjaxService;

    @Autowired
    SKASAFSSystemContService skasafsSystemContService;

    @Autowired
    AutoFloorDate autoFloorDate;

    private static final String TwoTimeType = "two";




    @RequestMapping("/skasafsSysMessageAjax")
    public String SKASAFSystemMessageAjaxCont(HttpSession Session, @RequestParam(value = "FloorName", required = true, defaultValue = "D061F") String FloorName,
                                              @RequestParam(value = "Time", required = true, defaultValue = "8:30~9:30") String Time,
                                              @RequestParam(value = "Machine", required = true, defaultValue = "AGV") String Machine,
                                              @RequestParam(value = "CarNum", required = true, defaultValue = "C001") String CarNum){
        Map<String, Object> SKASAutoDateMap = autoFloorDate.SKASAutoDate(TwoTimeType);
        /*过24时 时间变量*/
        String StartDateStr = (String) SKASAutoDateMap.get("StartDateStr");
        String EndDateStr = (String) SKASAutoDateMap.get("EndDateStr");
        String InquiryStartDate = (String) Session.getAttribute("InquiryStartDate");
        String InquiryEndDate = (String) Session.getAttribute("InquiryEndDate");
        if (InquiryStartDate!=null&&InquiryEndDate!=null){
            StartDateStr=InquiryStartDate;
            EndDateStr =InquiryEndDate;
        }
        List<AGV_Exception> agv_exceptions = skasafsSystemMessageAjaxService.SKASAFSystemMessageAjaxData(Machine,CarNum,FloorName, Time, StartDateStr, EndDateStr);
        String agv_exceptionsString = JSON.toJSONString(agv_exceptions);

        return agv_exceptionsString;
    }









}
