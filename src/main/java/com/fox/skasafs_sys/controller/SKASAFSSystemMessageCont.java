package com.fox.skasafs_sys.controller;

import com.fox.skasafs_sys.entity.AGVLineData;
import com.fox.skasafs_sys.entity.AGVTabData;
import com.fox.skasafs_sys.entity.BUFFERLineData;
import com.fox.skasafs_sys.entity.BUFFERTabData;
import com.fox.skasafs_sys.service.SKASAFSSystemMessageService;
import com.fox.testsys.entity.AutoFloor_Target;
import com.fox.testsys.service.TestAProductService;
import com.fox.testsys.utility.AutoFloorDate;
import com.google.gson.Gson;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * @author
 * @create 2020-06-10 11:24
 */
@Controller
public class SKASAFSSystemMessageCont {

    @Autowired
    AutoFloorDate autoFloorDate;
    @Autowired
    SKASAFSSystemMessageService skasafsSystemMessageService;
    @Autowired
    TestAProductService testAProductService;

    private static final String TwoTimeType = "two";
    private static final String MAC_PRODUCT = "Macan";
    private  static  final  String Proudct="D54" ;


    @RequestMapping("skasafssystemmessage")
    public  String  SKASAFSSystemMessageCont(HttpSession Session,
                                             @RequestParam(value = "FloorName", required = true, defaultValue = "D061F") String FloorName,
                                             @RequestParam(value = "Product", required = true, defaultValue = "All") String Product,
                                             @RequestParam(value = "Machine", required = true, defaultValue = "AGV") String Machine,
                                             @RequestParam(value = "CarNum", required = true, defaultValue = "C001") String CarNum, Map map){
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
        String InquiryStartDate = (String) Session.getAttribute("InquiryStartDate");
        String InquiryEndDate = (String) Session.getAttribute("InquiryEndDate");
        if (InquiryStartDate!=null&&InquiryEndDate!=null){
            StartDateStr=InquiryStartDate;
            EndDateStr =InquiryEndDate;
            TimeStartDateStr=InquiryStartDate;
            TimeEndDateStr=InquiryEndDate;
        }


        double WorkingHours=0;
        List<AutoFloor_Target> MacautoLine = testAProductService.LineData(FloorName, FloorName, schedule, MAC_PRODUCT,TwoTimeType,"","");
        if (MacautoLine.size()>0){
            WorkingHours = MacautoLine.get(0).getWorkingHours().doubleValue();
        }

        Gson gson=new Gson();
        List<AGVTabData> agvTabData = new ArrayList<>();
        List<AGVLineData> agvLineData = new ArrayList<>();
        String agvLineDataJson ="";

        List<AGVTabData> rgvTabData = new ArrayList<>();
        List<AGVLineData> rgvLineData = new ArrayList<>();
        String rgvLineDataJson="";

        List<BUFFERTabData> bufferTabData = new ArrayList<>();
        Map<String,List<BUFFERLineData>> bufferLineData = new HashMap<>();
        String bufferLineDataJson="";

        List<BUFFERTabData> afsTabData = new ArrayList<>();
        Map<String,List<BUFFERLineData>> afsLineData = new HashMap<>();
        String afsLineDataJson="";


        if (Machine.equals("RGV")){
             rgvTabData  = skasafsSystemMessageService.SKASAFSSystemMessageData("RGV",WorkingHours,actionMinuteD,FloorName, StartDateStr, EndDateStr);
             rgvLineData = skasafsSystemMessageService.SKASAFSSystemMessageLineData("RGV",schedule, FloorName);
             rgvLineDataJson = gson.toJson(rgvLineData);
        }else if (Machine.equals("AGV")){
             agvTabData  = skasafsSystemMessageService.SKASAFSSystemMessageData("AGV",WorkingHours,actionMinuteD,FloorName, StartDateStr, EndDateStr);
             agvLineData = skasafsSystemMessageService.SKASAFSSystemMessageLineData("AGV",schedule, FloorName);
             agvLineDataJson = gson.toJson(agvLineData);
        }else if (Machine.equals("BUFFER")){
             bufferTabData  = skasafsSystemMessageService.BUFFERAFSSystemMessageData("BUFFER",CarNum,WorkingHours,actionMinuteD,FloorName, StartDateStr, EndDateStr);
             bufferLineData = skasafsSystemMessageService.BUFFERAFSSystemMessageLineData("BUFFER",CarNum,schedule, FloorName);
             bufferLineDataJson = gson.toJson(bufferLineData);
        }else if (Machine.equals("AFS")){
             afsTabData  = skasafsSystemMessageService.BUFFERAFSSystemMessageData("AFS",CarNum,WorkingHours,actionMinuteD,FloorName, StartDateStr, EndDateStr);
             afsLineData = skasafsSystemMessageService.BUFFERAFSSystemMessageLineData("AFS",CarNum,schedule, FloorName);
             afsLineDataJson = gson.toJson(afsLineData);
        }

        map.put("CarNum",CarNum);
        map.put("FloorName",FloorName);
        map.put("agvLineData",agvLineDataJson);
        map.put("agvTabData",agvTabData);
        map.put("rgvLineData",rgvLineDataJson);
        map.put("rgvTabData",rgvTabData);
        map.put("bufferLineData",bufferLineDataJson);
        map.put("bufferTabData",bufferTabData);
        map.put("afsLineData",afsLineDataJson);
        map.put("afsTabData",afsTabData);
        map.put("Machine",Machine);
        map.put("StartDate",TimeStartDateStr);
        map.put("EndDate",TimeEndDateStr);
        map.put("Proudct",Proudct);
        return "saksafssys/skasafssystemmessage";
    }
}
