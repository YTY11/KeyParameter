package com.fox.skasafs_sys.controller;

import com.alibaba.fastjson.JSON;

import com.fox.skasafs_sys.entity.AFSLineData;
import com.fox.skasafs_sys.entity.AGV_AFS_State;
import com.fox.skasafs_sys.entity.AGV_Exception;
import com.fox.skasafs_sys.entity.SKAS_AFSData;
import com.fox.skasafs_sys.service.SKASAFSSystemContService;
import com.fox.skasafs_sys.service.SKASAFSSystemMessageAjaxService;
import com.fox.testsys.utility.AutoFloorDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author
 * @create 2020-06-08 10:190
 */
@Controller
public class SKASAFSSystemCont {

        @Autowired
        private SKASAFSSystemContService skasafsSystemContService;
        @Autowired
        private AutoFloorDate autoFloorDate;
        @Autowired
        private  SKASAFSSystemMessageAjaxCont skasafsSystemMessageAjaxCont;

        @Autowired
        private SKASAFSSystemMessageAjaxService skasafsSystemMessageAjaxService;

        private static final String TwoTimeType = "two";

        @RequestMapping("saksafssyscont")
//        @ResponseBody
        public  String  SAKSAFSSystemCont(@RequestParam(value = "FloorName", required = true, defaultValue = "D061F") String FloorName, Map map){
          Map<String, Object> AutoFloorDateMap = autoFloorDate.AutoFloorDate(TwoTimeType);
          /*过24时 时间变量*/
          String StartDateStr = (String) AutoFloorDateMap.get("StartDateStr");
          String EndDateStr = (String) AutoFloorDateMap.get("EndDateStr");
          /*工作时间*/
          Integer actionMinuteD = (Integer) AutoFloorDateMap.get("actionMinuteD");
          //当前日期
          Date schedule = (Date) AutoFloorDateMap.get("schedule");
          Date TomorrowDate = (Date) AutoFloorDateMap.get("TomorrowDate");
          //異常信息
          List<AGV_Exception> ExceptionDescribe = skasafsSystemContService.SKASAFSexceptionDescribe(FloorName, StartDateStr, EndDateStr);
          map.put("ExceptionDescribe",ExceptionDescribe);
          //下方圖片設備狀態
          Map<String, Integer> SysStateMapData = skasafsSystemContService.SAKSAFSSysStateMapData(FloorName, StartDateStr, EndDateStr,actionMinuteD);
          map.put("SysStateMapData",SysStateMapData);
          //設備狀態信息
          AGV_AFS_State AGV_AFSState = skasafsSystemContService.SKASAFSMachineStateData(SysStateMapData);
          map.put("AGV_AFSState",AGV_AFSState);
          //機故率
          List<AFSLineData> afsLineData = skasafsSystemContService.SKASAFSMachineRate(FloorName,schedule ,TomorrowDate);
          String afsLineDataJson = JSON.toJSONString(afsLineData);
          map.put("afsLineDataJson",afsLineDataJson);
          //設備狀態信息圓型
          Map<String, Integer> SaksAfsSysPieMapData = skasafsSystemContService.SAKSAFSSysStateMapData(FloorName, StartDateStr, EndDateStr,actionMinuteD);
          AGV_AFS_State SKASAFSMachineState = skasafsSystemContService.SKASAFSMachineStateData(SaksAfsSysPieMapData);
          String AGV_AFSPieStateJson = JSON.toJSONString(SKASAFSMachineState);
          map.put("aGV_AFSPieStateJson",AGV_AFSPieStateJson);
          //設備關鍵指標
          List<SKAS_AFSData> skas_afsData = skasafsSystemMessageAjaxService.SKAS_AFSBarData(FloorName, StartDateStr, EndDateStr);
          String skas_afsDataJson = JSON.toJSONString(skas_afsData);
          map.put("skas_afsDataJson",skas_afsDataJson);

          return "SkasAfs";
        }


////        刷新数据
//       @RequestMapping("getSkasData")
//       @ResponseBody
//       public  Map<String,Object>  SAKSAFSSystemContException(){
//           String FloorName = "D061F";
//           HashMap<String, Object> map = new HashMap<>();
//
//           Map<String, Object> AutoFloorDateMap = autoFloorDate.AutoFloorDate(TwoTimeType);
//           /*过24时 时间变量*/
//           String StartDateStr = (String) AutoFloorDateMap.get("StartDateStr");
//           String EndDateStr = (String) AutoFloorDateMap.get("EndDateStr");
//           /*工作时间*/
//           Integer actionMinuteD = (Integer) AutoFloorDateMap.get("actionMinuteD");
//           //当前日期
//           Date schedule = (Date) AutoFloorDateMap.get("schedule");
//           Date TomorrowDate = (Date) AutoFloorDateMap.get("TomorrowDate");
//           List<AGV_Exception> ExceptionDescribe = skasafsSystemContService.SKASAFSexceptionDescribe(FloorName, StartDateStr, EndDateStr);
//           map.put("ExceptionDescribe",ExceptionDescribe);
//
//           Map<String, Integer> SysStateMapData = skasafsSystemContService.SAKSAFSSysStateMapData(FloorName, StartDateStr, EndDateStr,actionMinuteD);
//           map.put("SysStateMapData",SysStateMapData);
//
//           AGV_AFS_State AGV_AFSState = skasafsSystemContService.SKASAFSMachineStateData(SysStateMapData);
//           map.put("AGV_AFSState",AGV_AFSState);
//
//           List<AFSLineData> afsLineData = skasafsSystemContService.SKASAFSMachineRate(FloorName,schedule ,TomorrowDate);
//           String afsLineDataJson = JSON.toJSONString(afsLineData);
//           map.put("afsLineDataJson",afsLineDataJson);
//
//           Map<String, Integer> SaksAfsSysPieMapData = skasafsSystemContService.SAKSAFSSysStateMapData(FloorName, StartDateStr, EndDateStr,actionMinuteD);
//           AGV_AFS_State SKASAFSMachineState = skasafsSystemContService.SKASAFSMachineStateData(SaksAfsSysPieMapData);
//           String AGV_AFSPieStateJson = JSON.toJSONString(SKASAFSMachineState);
//           map.put("aGV_AFSPieStateJson",AGV_AFSPieStateJson);
//
//           List<SKAS_AFSData> skas_afsData = skasafsSystemMessageAjaxService.SKAS_AFSBarData(FloorName, StartDateStr, EndDateStr);
//           String skas_afsDataJson = JSON.toJSONString(skas_afsData);
//           map.put("skas_afsDataJson",skas_afsDataJson);
//           return map;
//       }



    @RequestMapping("saksafsSystabData")
    public  String  SAKSAFSSystemContException(@RequestParam(value = "FloorName", required = true, defaultValue = "D061F") String FloorName, Map map){
        Map<String, Object> AutoFloorDateMap = autoFloorDate.AutoFloorDate(TwoTimeType);
        /*过24时 时间变量*/
        String StartDateStr = (String) AutoFloorDateMap.get("StartDateStr");
        String EndDateStr = (String) AutoFloorDateMap.get("EndDateStr");
        /*工作时间*/
        Integer actionMinuteD = (Integer) AutoFloorDateMap.get("actionMinuteD");
        //当前日期
        Date schedule = (Date) AutoFloorDateMap.get("schedule");
        Date TomorrowDate = (Date) AutoFloorDateMap.get("TomorrowDate");
        List<AGV_Exception> ExceptionDescribe = skasafsSystemContService.SKASAFSexceptionDescribe(FloorName, StartDateStr, EndDateStr);
        map.put("ExceptionDescribe",ExceptionDescribe);

        Map<String, Integer> SysStateMapData = skasafsSystemContService.SAKSAFSSysStateMapData(FloorName, StartDateStr, EndDateStr,actionMinuteD);
        map.put("SysStateMapData",SysStateMapData);

        AGV_AFS_State AGV_AFSState = skasafsSystemContService.SKASAFSMachineStateData(SysStateMapData);
        map.put("AGV_AFSState",AGV_AFSState);

        List<AFSLineData> afsLineData = skasafsSystemContService.SKASAFSMachineRate(FloorName,schedule ,TomorrowDate);
        String afsLineDataJson = JSON.toJSONString(afsLineData);
        map.put("afsLineDataJson",afsLineDataJson);

        Map<String, Integer> SaksAfsSysPieMapData = skasafsSystemContService.SAKSAFSSysStateMapData(FloorName, StartDateStr, EndDateStr,actionMinuteD);
        AGV_AFS_State SKASAFSMachineState = skasafsSystemContService.SKASAFSMachineStateData(SaksAfsSysPieMapData);
        String AGV_AFSPieStateJson = JSON.toJSONString(SKASAFSMachineState);
        map.put("aGV_AFSPieStateJson",AGV_AFSPieStateJson);

        List<SKAS_AFSData> skas_afsData = skasafsSystemMessageAjaxService.SKAS_AFSBarData(FloorName, StartDateStr, EndDateStr);
        String skas_afsDataJson = JSON.toJSONString(skas_afsData);
        map.put("skas_afsDataJson",skas_afsDataJson);
        return "SkasAfs::tabData";
    }


}
