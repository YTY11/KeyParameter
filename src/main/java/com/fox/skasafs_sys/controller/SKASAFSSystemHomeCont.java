package com.fox.skasafs_sys.controller;

import com.alibaba.fastjson.JSON;

import com.fox.skasafs_sys.entity.SKAS_AFSData;
import com.fox.skasafs_sys.service.SKASAFSSystemHomeService;
import com.fox.testsys.service.TestAProductService;
import com.fox.testsys.utility.AutoFloorDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author
 * @create 2020-06-08 10:19
 */
@Controller
public class SKASAFSSystemHomeCont {

    @Autowired
    AutoFloorDate autoFloorDate;

    @Autowired
    TestAProductService testAProductService;

    @Autowired
    SKASAFSSystemHomeService skasafsSystemHomeService;

    //机种名称
    private static final String LOT_PRODUCT = "Lotus";
    private static final String MAC_PRODUCT = "Macan";
    private static final String PHA_PRODUCT = "Pha";
    private static final String TwoTimeType = "two";

    DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
    private  static  final  String Proudct="D54" ;

    @RequestMapping("saksafssyshomecont")
//    @ResponseBody
    public  String  SAKSAFSSystemHomeCont(HttpServletRequest request, HttpServletResponse response, HttpSession Session,
                                          @RequestParam(value = "FloorName", required = true, defaultValue = "D061F") String FloorName,
                                          @RequestParam(value = "Product", required = true, defaultValue = "All") String Product,
                                          @RequestParam(value = "DateTime", required = true, defaultValue = "") String DateTime, Map map){

        Map<String, Object> AutoFloorDateMap = autoFloorDate.AutoFloorDate(TwoTimeType);
        Map<String, Object> SKASAutoDateMap = autoFloorDate.SKASAutoDate(TwoTimeType);
        /*当前日期*/
        Date schedule = (Date) AutoFloorDateMap.get("schedule");
        /*工作时间*/
        Integer actionMinuteD = (Integer) AutoFloorDateMap.get("actionMinuteD");
        //運行時間
        Integer TimeGNumber = (Integer) AutoFloorDateMap.get("TimeGNumber");
        /*过24时 时间变量*/
        String StartDateStr = (String) SKASAutoDateMap.get("StartDateStr");
        String EndDateStr = (String) SKASAutoDateMap.get("EndDateStr");
        /*过24时 时间变量*/
        String TimeStartDateStr = (String) AutoFloorDateMap.get("StartDateStr");
        String TimeEndDateStr = (String) AutoFloorDateMap.get("EndDateStr");

        double WorkingHours=0;
        if(!"".equals(DateTime)) {
            String[] split = DateTime.split("-");
            String InquiryStartDate = split[0].substring(0,19);
            String InquiryEndDate = split[1].substring(1);
            StartDateStr =InquiryStartDate;
            EndDateStr=InquiryEndDate;
            TimeStartDateStr=InquiryStartDate;
            TimeEndDateStr=InquiryEndDate;
            ZoneId zoneId=ZoneId.systemDefault();
            Date StartDate = Date.from(LocalDateTime.parse(InquiryStartDate,df).atZone(zoneId).toInstant());
            Date EndDate = Date.from(LocalDateTime.parse(InquiryEndDate,df).atZone(zoneId).toInstant());
            long DifferSeconds = (EndDate.getTime() - StartDate.getTime())/1000;
            WorkingHours =(double)DifferSeconds;
            Session.setAttribute("InquiryStartDate", InquiryStartDate);
            Session.setAttribute("InquiryEndDate", InquiryEndDate);
        }else {
            Session.invalidate();
        }


        SKAS_AFSData RGVTabData = skasafsSystemHomeService.RGV_AGVSystemData("RGV", FloorName, StartDateStr, EndDateStr, WorkingHours, actionMinuteD);

        SKAS_AFSData AGVTabData = skasafsSystemHomeService.RGV_AGVSystemData("AGV", FloorName, StartDateStr, EndDateStr, WorkingHours, actionMinuteD);

        List<SKAS_AFSData> BUFFERTabData = skasafsSystemHomeService.BUFFER_AFCSystemData("BUFFER", FloorName, StartDateStr, EndDateStr, WorkingHours, actionMinuteD);
        String BUFFERJsonData = JSON.toJSONString(BUFFERTabData);

        List<SKAS_AFSData> AFSTabData = skasafsSystemHomeService.BUFFER_AFCSystemData("AFS", FloorName, StartDateStr, EndDateStr, WorkingHours, actionMinuteD);
        String AFSJsonData = JSON.toJSONString(AFSTabData);

        map.put("Proudct",Proudct);
        map.put("DateTime",DateTime);
        map.put("FloorName",FloorName);
        map.put("StartDate",TimeStartDateStr);
        map.put("EndDate",TimeEndDateStr);
        map.put("AGVTabData",AGVTabData);
        map.put("AFSTabData",AFSTabData);
        map.put("RGVTabData",RGVTabData);
        map.put("BUFFERTabData",BUFFERTabData);
        map.put("AFSJsonData",AFSJsonData);
        map.put("BUFFERJsonData",BUFFERJsonData);

        return "SkasAfs2";
    }


}
