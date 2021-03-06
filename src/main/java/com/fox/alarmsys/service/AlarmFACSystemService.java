package com.fox.alarmsys.service;


import com.fox.alarmsys.entity.AlarmSystemTitle;
import com.fox.alarmsys.entity.AutoFloor_Test_Exception;
import com.fox.alarmsys.entity.Floor_Hermes_Target;
import com.fox.alarmsys.mapper.AutoFloor_Test_ExceptionMapper;
import com.fox.alarmsys.mapper.Floor_Hermes_TargetMapper;
import com.fox.qualitysys.entity.AutoFloor_Key_CheCkUp;
import com.fox.qualitysys.mapper.AutoFloor_Key_CheCkUpMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author wu
 * @create 2020-06-18 18:26
 * @date 2020/10/14
 */
@Service
public class AlarmFACSystemService {

    @Autowired
    private AlarmEnquiriesService alarmEnquiriesService;
    @Autowired
    private AutoFloor_Key_CheCkUpMapper autoFloor_key_cheCkUpMapper;
    @Autowired
    private AutoFloor_Test_ExceptionMapper autoFloor_test_exceptionMapper;
    @Autowired
    private Floor_Hermes_TargetMapper floor_hermes_targetMapper;
    public static final DateFormat Timeformat = new SimpleDateFormat("HH:mm:ss");

    private final  DateFormat df2=new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    Date testDate = null;


    //Test
    public List<AutoFloor_Key_CheCkUp>  AlarmSystemTestTabData(String FloorName, String StartDate, String EndDate, String Type , String Product, List<String> DisableException_TypeList, String MachineType, String PriorityOrFlg, String LineName){
        //所有樓層
        Map<String, List<String>> FACAreaAndFloorMap = alarmEnquiriesService.alarmSystemFACAreaAndFloor();
        List<String> floorList = FACAreaAndFloorMap.get("FloorList");
        List<AutoFloor_Key_CheCkUp> autoFloor_key_checkNGS =new ArrayList<>();
        List<AutoFloor_Test_Exception> autoFloor_test_exceptions = new ArrayList<>();
        List<AutoFloor_Test_Exception> autoFloor_test_FACexceptions = autoFloor_test_exceptionMapper.selectFACTestExceptionList(FloorName, StartDate, EndDate, Type, DisableException_TypeList);
        if ("ALL".equals(FloorName)){
            for (String floor : floorList) {
                for (AutoFloor_Test_Exception autoFloor_test_exception : autoFloor_test_FACexceptions) {
                    String dataFloor = autoFloor_test_exception.getFloor();
                    String dataFloorReplace ="";
                    if (dataFloor!=null&&!"".equals(dataFloor)){ dataFloorReplace = dataFloor.replace("-", ""); }
                    if (floor!=null&&floor.equals(dataFloorReplace)){
                        autoFloor_test_exceptions.add(autoFloor_test_exception);
                    }
                }
            }
        }else {
            autoFloor_test_exceptions.addAll(autoFloor_test_FACexceptions);
        }
        List<Floor_Hermes_Target> hermesNameList = floor_hermes_targetMapper.selectByFloorHermesName(FloorName);
        List<AutoFloor_Key_CheCkUp> LowautoFloor_key_checkNGS =new ArrayList<>();
        List<AutoFloor_Key_CheCkUp> AccomplishFloor_key_check =new ArrayList<>();
        List<AutoFloor_Key_CheCkUp> LowAccomplishFloor_key_check =new ArrayList<>();
        Date date = autoFloor_test_exceptionMapper.DBDate();//異常時長
        for (int i = 0; i < autoFloor_test_exceptions.size(); i++) {
            AutoFloor_Test_Exception autoFloor_test_exception = autoFloor_test_exceptions.get(i);
            AutoFloor_Key_CheCkUp autoFloor_key_cheCkUp=new AutoFloor_Key_CheCkUp();
            String exceptionType = autoFloor_test_exception.getExceptionType();
            String floor = autoFloor_test_exception.getFloor();
            String linename = autoFloor_test_exception.getLinename();
            String machine = autoFloor_test_exception.getMachine();
            String exceptionDescribe = autoFloor_test_exception.getExceptionDescribe();
            Date ExceptionTime = autoFloor_test_exception.getExceptionTime();
            String priority = autoFloor_test_exception.getPriority();
            Integer flag = autoFloor_test_exception.getFlag();
            BigDecimal id = autoFloor_test_exception.getId();
            Date updateTime = autoFloor_test_exception.getUpdateTime();
            String repairPropose = autoFloor_test_exception.getRepairPropose();
            String repairContent = autoFloor_test_exception.getRepairContent();
            String Responsibilities="TE";
            //設備名稱修改
            for (Floor_Hermes_Target floor_hermes_target : hermesNameList) {
                String hermesStatioName = floor_hermes_target.getHermesStatioName();
                String hermesStatioSn = floor_hermes_target.getHermesStatioSn();
                if (hermesStatioSn!=null&&hermesStatioSn.equals(machine)){ machine=hermesStatioName; }
            }
            autoFloor_key_cheCkUp.setResponsibilities(Responsibilities);
            autoFloor_key_cheCkUp.setFlag(flag);
            autoFloor_key_cheCkUp.setFloor(floor);
            autoFloor_key_cheCkUp.setLinename(linename);
            autoFloor_key_cheCkUp.setMachinetype(machine);
            autoFloor_key_cheCkUp.setKeyPmsCh(exceptionDescribe);
            String endTime="";
            String testTime = df2.format(ExceptionTime);
            autoFloor_key_cheCkUp.setTesttime(testTime);
            autoFloor_key_cheCkUp.setProduct(priority);
            autoFloor_key_cheCkUp.setExceptionType(exceptionType);
            autoFloor_key_cheCkUp.setId(id);
            autoFloor_key_cheCkUp.setRepairPropose(repairPropose);
            autoFloor_key_cheCkUp.setRepairContent(repairContent);
            long Actiontime = ExceptionTime.getTime();//異常開始時間
            long EndTime = date.getTime();//異常時長
            if (flag==1){
                //flag為1時異常結束 異常時長時間為update時間
                if (updateTime!=null){
                    endTime=df2.format(updateTime);
                    autoFloor_key_cheCkUp.setStrupdateTime(endTime);
                }
            }
            double Dispose =(double)(EndTime-Actiontime)/1000;
            BigDecimal DisposeBig=new BigDecimal(Dispose/60); //异常时间取分钟数
            int DisposeTime =DisposeBig.setScale(0,BigDecimal.ROUND_HALF_UP).intValue(); //四舍五入方法取值
            if (DisposeTime<0){DisposeTime=0;}
            autoFloor_key_cheCkUp.setDisposeTime(DisposeTime);  //Flag  1=维修完成 0=发生异常  2=维修中

            if (MachineType != null) {
                switch (MachineType){
                    case "能源":
                        if ("ENERGYMONITORING".equals(linename)){
                            //排序方式
                            SortPriortyOrFlg(PriorityOrFlg, autoFloor_key_checkNGS, LowautoFloor_key_checkNGS, AccomplishFloor_key_check, LowAccomplishFloor_key_check, autoFloor_key_cheCkUp, priority, flag,exceptionType);
                        }
                    break;
                    case "環境":
                        if ("ENVIRONMENTAL".equals(linename)){
                            //排序方式
                            SortPriortyOrFlg(PriorityOrFlg, autoFloor_key_checkNGS, LowautoFloor_key_checkNGS, AccomplishFloor_key_check, LowAccomplishFloor_key_check, autoFloor_key_cheCkUp, priority, flag,exceptionType);
                        }
                    break;
                    case "能耗":
                        if ("ENERGYCONSUMPTION".equals(linename)){
                            //排序方式
                            SortPriortyOrFlg(PriorityOrFlg, autoFloor_key_checkNGS, LowautoFloor_key_checkNGS, AccomplishFloor_key_check, LowAccomplishFloor_key_check, autoFloor_key_cheCkUp, priority, flag,exceptionType);
                        }
                    break;
                    case "ALL":
                        SortPriortyOrFlg(PriorityOrFlg, autoFloor_key_checkNGS, LowautoFloor_key_checkNGS, AccomplishFloor_key_check, LowAccomplishFloor_key_check, autoFloor_key_cheCkUp, priority, flag,exceptionType);
                        break;
                }
            }


        }
        //按照 H/M/L 排序  维修完成的排在最后
        autoFloor_key_checkNGS.addAll(LowautoFloor_key_checkNGS);
        autoFloor_key_checkNGS.addAll(AccomplishFloor_key_check);
        autoFloor_key_checkNGS.addAll(LowAccomplishFloor_key_check);
        return autoFloor_key_checkNGS;

    }

    public Boolean RobotNormTime(String exceptionType, Integer flag, Date updateTime, long actiontime) {
        Boolean NormTime=false;
            if (flag==1){
                long time = updateTime.getTime()- actiontime;
                time/=1000;
                if (time>5){
                    NormTime=true;
                }
            }else {
                NormTime=true;
            }
        return NormTime;
    }


    public void SortPriortyOrFlg(String PriorityOrFlg, List<AutoFloor_Key_CheCkUp> autoFloor_key_checkNGS, List<AutoFloor_Key_CheCkUp> lowautoFloor_key_checkNGS, List<AutoFloor_Key_CheCkUp> accomplishFloor_key_check, List<AutoFloor_Key_CheCkUp> lowAccomplishFloor_key_check, AutoFloor_Key_CheCkUp autoFloor_key_cheCkUp, String priority, Integer flag,String exceptionType) {

        switch (PriorityOrFlg){
            case  "H":
                if ("H".equals(priority)){
                    SortOrder(autoFloor_key_checkNGS, lowautoFloor_key_checkNGS, accomplishFloor_key_check, lowAccomplishFloor_key_check, autoFloor_key_cheCkUp, priority, flag);
                }
                break;
            case "M":
                if ("M".equals(priority)){
                    //排序方式
                    SortOrder(autoFloor_key_checkNGS, lowautoFloor_key_checkNGS, accomplishFloor_key_check, lowAccomplishFloor_key_check, autoFloor_key_cheCkUp, priority, flag);
                }
                break;
            case "L":
                if ("L".equals(priority)){
                    //排序方式
                    SortOrder(autoFloor_key_checkNGS, lowautoFloor_key_checkNGS, accomplishFloor_key_check, lowAccomplishFloor_key_check, autoFloor_key_cheCkUp, priority, flag);
                }
                break;
            case "0":
            case "1":
            case "2":
                int Flag= Integer.parseInt(PriorityOrFlg);
                if (Flag==flag){
                    //排序方式
                    SortOrder(autoFloor_key_checkNGS, lowautoFloor_key_checkNGS, accomplishFloor_key_check, lowAccomplishFloor_key_check, autoFloor_key_cheCkUp, priority, flag);
                }
                break;
            case "beyond":
                Flag=0;
                if (Flag==flag){
                    if (!"OFFLINE".equals(exceptionType)){
                        //排序方式
                        SortOrder(autoFloor_key_checkNGS, lowautoFloor_key_checkNGS, accomplishFloor_key_check, lowAccomplishFloor_key_check, autoFloor_key_cheCkUp, priority, flag);
                    }

                }
                break;
            case "offLine":
                  Flag=0;
                if (Flag==flag){
                    if ("OFFLINE".equals(exceptionType)){
                        //排序方式
                        SortOrder(autoFloor_key_checkNGS, lowautoFloor_key_checkNGS, accomplishFloor_key_check, lowAccomplishFloor_key_check, autoFloor_key_cheCkUp, priority, flag);
                    }

                }
                break;
            case "ALL":
                    //排序方式
                    SortOrder(autoFloor_key_checkNGS, lowautoFloor_key_checkNGS, accomplishFloor_key_check, lowAccomplishFloor_key_check, autoFloor_key_cheCkUp, priority, flag);
                    break;
        }
    }


    public void SortOrder(List<AutoFloor_Key_CheCkUp> autoFloor_key_checkNGS, List<AutoFloor_Key_CheCkUp> lowautoFloor_key_checkNGS, List<AutoFloor_Key_CheCkUp> accomplishFloor_key_check, List<AutoFloor_Key_CheCkUp> lowAccomplishFloor_key_check, AutoFloor_Key_CheCkUp autoFloor_key_cheCkUp, String priority, Integer flag) {
        if ("L".equals(priority) && flag != 1) {   //异常等级为L 且没有维修完的List
            lowautoFloor_key_checkNGS.add(autoFloor_key_cheCkUp);
        } else if (flag == 1) {
            if ("L".equals(priority)) {
                lowAccomplishFloor_key_check.add(autoFloor_key_cheCkUp);
            } else {
                accomplishFloor_key_check.add(autoFloor_key_cheCkUp);
            }
        } else if (flag != 1) {
            autoFloor_key_checkNGS.add(autoFloor_key_cheCkUp);
        }
    }

   // Test
    public List<AlarmSystemTitle> AlarmSystemTitleData(List<AutoFloor_Test_Exception> autoFloor_test_FACExceptions, String Product, String FloorName, String startDate, String endDate){
        Map<String, List<String>> FACAreaAndFloorMap = alarmEnquiriesService.alarmSystemFACAreaAndFloor();
        List<String> floorList = FACAreaAndFloorMap.get("FloorList");
        List<AutoFloor_Test_Exception>  autoFloor_test_exceptions=new ArrayList<>();
        if ("ALL".equals(FloorName)){
            for (String floor : floorList) {
                for (AutoFloor_Test_Exception autoFloor_test_exception : autoFloor_test_FACExceptions) {
                    String dataFloor = autoFloor_test_exception.getFloor();
                    String dataFloorReplace ="";
                    if (dataFloor!=null&&!"".equals(dataFloor)){ dataFloorReplace = dataFloor.replace("-", ""); }
                    if (floor!=null&&floor.equals(dataFloorReplace)){
                        autoFloor_test_exceptions.add(autoFloor_test_exception);
                    }
                }
            }
        }else {
            autoFloor_test_exceptions.addAll(autoFloor_test_FACExceptions);
        }

        List<Floor_Hermes_Target> floor_Hermes_Targets = floor_hermes_targetMapper.selectHermesByFloor();
        List<AutoFloor_Test_Exception> FACTitleUnFloorList = autoFloor_test_exceptionMapper.selectFACTitleUnFloorList(startDate, endDate);
        List<String> titleTypeList = titleTypeList(Product);//根據模塊種類顯示title內容

        List<AlarmSystemTitle> AlarmSystemTitleList=new ArrayList<>();
        int ALLWaitDispatchNum=0;//代派工數量
        int ALLUnderRepairNum=0;// 修理中數量
        int ALLRepairCompleteNum=0;// 修理完成數量
        int ALLMachineExALL=0;//機故ALL數量
        int ALLMachineExHNum=0;//機故H數量
        int ALLMachineExMNum=0;//機故M數量
        int ALLMachineExLNum=0;//機故L數量
        int allFloorNumber=0;
        int allUnFloorNumber=0;
        for (String title : titleTypeList) {
            AlarmSystemTitle alarmSystemTitle=new AlarmSystemTitle();
            //賦值機故類型
            alarmSystemTitle.setMachineExType(title);
            int WaitDispatchNum=0;//代派工數量
            int UnderRepairNum=0;// 修理中數量
            int RepairCompleteNum=0;// 修理完成數量
            int MachineExALL=0;//機故ALL數量
            int MachineExHNum=0;//機故H數量
            int MachineExMNum=0;//機故M數量
            int MachineExLNum=0;//機故L數量
            Integer floorNumber=0;
            Integer unFloorNumber=0;
            List<String> TitleException_TypeList = TestException_Type(Product,title);//Titil所對應的異常
            for (AutoFloor_Test_Exception autoFloor_test_exception : autoFloor_test_exceptions) {
                String linename = autoFloor_test_exception.getLinename();
                String exceptionType = autoFloor_test_exception.getExceptionType();
                for (String Exception : TitleException_TypeList) {//判讀異常相同
                    if (Exception!=null&&Exception.equals(linename)){
                        // FAC判断线体与异常明细相同
                        Integer flag = autoFloor_test_exception.getFlag();
                        String priority = autoFloor_test_exception.getPriority();
                        switch (flag){//維修狀態
                            case 0:
                                WaitDispatchNum++;
                                break;
                            case 1:
                                RepairCompleteNum++;
                                break;
                            case 2:
                                UnderRepairNum++;
                                break;
                        }

                        switch (priority){//異常等級
                            case "H":
                                MachineExHNum++;
                                break;
                            case "M":
                                MachineExMNum++;
                                break;
                            case "L":
                                MachineExLNum++;
                                break;
                        }


                    }

                }
            }
            MachineExALL=MachineExHNum+MachineExMNum+MachineExLNum;
            //ALL的加總
            ALLWaitDispatchNum+=WaitDispatchNum;
            ALLUnderRepairNum+=UnderRepairNum;
            ALLRepairCompleteNum+=RepairCompleteNum;
            ALLMachineExALL+=MachineExALL;
            ALLMachineExHNum+=MachineExHNum;
            ALLMachineExMNum+=MachineExMNum;
            ALLMachineExLNum+=MachineExLNum;

            if ("ALL".equals(FloorName)){
                for (Floor_Hermes_Target floor_hermes_target : floor_Hermes_Targets) {
                    String hermesFloor = floor_hermes_target.getHermesFloor();
                    for (AutoFloor_Test_Exception autoFloor_test_exception : FACTitleUnFloorList) {
                        String floor = autoFloor_test_exception.getFloor();
                        String linename = autoFloor_test_exception.getLinename();
                        if (hermesFloor.equals(floor)){
                            switch (title){
                                case "能源":
                                    if ("ENERGYMONITORING".equals(linename)){
                                        unFloorNumber++;
                                    }
                                    break;
                                case "環境":
                                    if ("ENVIRONMENTAL".equals(linename)){
                                        unFloorNumber++;
                                    }
                                    break;
                                case "能耗":
                                    if ("ENERGYCONSUMPTION".equals(linename)){
                                        unFloorNumber++;
                                    }
                                    break;
                            }
                        }
                    }
                }

            }else {
                if (WaitDispatchNum>0){unFloorNumber=1;}
            }
            switch (title){
                case "能源":
                    floorNumber = ENERGYMONITORINGFloor(FloorName);
                    break;
                case "環境":
                    floorNumber = ENVIRONMENTALFloor(FloorName);
                    break;
                case "能耗":
                    floorNumber = ENERGYCONSUMPTIONFloor(FloorName);
                    break;
            }
            alarmSystemTitle.setFloorNumber(floorNumber);
            alarmSystemTitle.setUnFloorNumber(unFloorNumber);
            alarmSystemTitle.setWaitDispatchNum(WaitDispatchNum);
            alarmSystemTitle.setUnderRepairNum(UnderRepairNum);
            alarmSystemTitle.setRepairCompleteNum(RepairCompleteNum);
            alarmSystemTitle.setMachineExALL(MachineExALL);
            alarmSystemTitle.setMachineExHNum(MachineExHNum);
            alarmSystemTitle.setMachineExMNum(MachineExMNum);
            alarmSystemTitle.setMachineExLNum(MachineExLNum);
            AlarmSystemTitleList.add(alarmSystemTitle);
        }
        AlarmSystemTitle ALLalarmSystemTitle=new AlarmSystemTitle();
        //        ALL的數據
        OptionalInt unFloorMax = AlarmSystemTitleList.stream().mapToInt(AlarmSystemTitle::getUnFloorNumber).max();
        allUnFloorNumber = unFloorMax.getAsInt();
        OptionalInt floorMax = AlarmSystemTitleList.stream().mapToInt(AlarmSystemTitle::getFloorNumber).max();
        allFloorNumber   = floorMax.getAsInt();
        ALLalarmSystemTitle.setMachineExType("ALL");
        ALLalarmSystemTitle.setFloorNumber(allFloorNumber);
        ALLalarmSystemTitle.setUnFloorNumber(allUnFloorNumber);
        ALLalarmSystemTitle.setWaitDispatchNum(ALLWaitDispatchNum);
        ALLalarmSystemTitle.setUnderRepairNum(ALLUnderRepairNum);
        ALLalarmSystemTitle.setRepairCompleteNum(ALLRepairCompleteNum);
        ALLalarmSystemTitle.setMachineExALL(ALLMachineExALL);
        ALLalarmSystemTitle.setMachineExHNum(ALLMachineExHNum);
        ALLalarmSystemTitle.setMachineExMNum(ALLMachineExMNum);
        ALLalarmSystemTitle.setMachineExLNum(ALLMachineExLNum);
        AlarmSystemTitleList.add(ALLalarmSystemTitle);


        return AlarmSystemTitleList;
    }


    public Map<String, List<AlarmSystemTitle>> FACAlarmSystemTitleData(List<AutoFloor_Test_Exception> autoFloor_test_FACExceptions,String FloorName, String Product,String startDate,String endDate){
        Map<String, List<String>> FACAreaAndFloorMap = alarmEnquiriesService.alarmSystemFACAreaAndFloor();
        List<String> floorList = FACAreaAndFloorMap.get("FloorList");
        List<AutoFloor_Test_Exception>  autoFloor_test_exceptions=new ArrayList<>();
        if ("ALL".equals(FloorName)){
            for (String floor : floorList) {
                for (AutoFloor_Test_Exception autoFloor_test_exception : autoFloor_test_FACExceptions) {
                    String dataFloor = autoFloor_test_exception.getFloor();
                    String dataFloorReplace ="";
                    if (dataFloor!=null&&!"".equals(dataFloor)){ dataFloorReplace = dataFloor.replace("-", ""); }
                    if (floor!=null&&floor.equals(dataFloorReplace)){
                        autoFloor_test_exceptions.add(autoFloor_test_exception);
                    }
                }
            }
        }else {
            autoFloor_test_exceptions.addAll(autoFloor_test_FACExceptions);
        }
        List<Floor_Hermes_Target> floor_Hermes_Targets = floor_hermes_targetMapper.selectHermesByFloor();
        List<AutoFloor_Test_Exception> FACTitleUnFloorList = autoFloor_test_exceptionMapper.selectFACTitleUnFloorList(startDate, endDate);
        Map<String,List<AlarmSystemTitle>>  AlarmSystemTitleDataMap=new HashMap<>();
        List<String> titleTypeList = titleTypeList(Product);//根據模塊種類顯示title內容

        List<String> ExceptionTypeList=new ArrayList<>();
        ExceptionTypeList.add("參數超標");
        ExceptionTypeList.add("設備離綫");

        for (String title : titleTypeList) {
            List<AlarmSystemTitle> AlarmSystemTitleList=new ArrayList<>();
            List<String> TitleException_TypeList = TestException_Type(Product,title);//Titil所對應的異常
            Integer floorNumber=0;
            Integer unFloorNumber=0;

            for (String ExceptionType : ExceptionTypeList) {
                int WaitDispatchNum=0;//代派工數量
                int UnderRepairNum=0;// 修理中數量
                int RepairCompleteNum=0;// 修理完成數量
                int MachineExALL=0;//機故ALL數量
                AlarmSystemTitle alarmSystemTitle=new AlarmSystemTitle();
                //賦值機故類型
                alarmSystemTitle.setMachineExType(title);
                if ("參數超標".equals(ExceptionType)){
                    for (AutoFloor_Test_Exception autoFloor_test_exception : autoFloor_test_exceptions) {
                        String linename = autoFloor_test_exception.getLinename();
                        String exceptionType = autoFloor_test_exception.getExceptionType();
                        for (String Exception : TitleException_TypeList) {//判讀異常相同
                            if (Exception!=null&&Exception.equals(linename)){
                                // FAC判断线体与异常明细相同
                                if (!"OFFLINE".equals(exceptionType)){
                                    Integer flag = autoFloor_test_exception.getFlag();
                                    String priority = autoFloor_test_exception.getPriority();
                                    switch (flag){//維修狀態
                                        case 0:
                                            WaitDispatchNum++;
                                            break;
                                        case 1:
                                            RepairCompleteNum++;
                                            break;
                                        case 2:
                                            UnderRepairNum++;
                                            break;
                                    }
                                    switch (priority){//異常等級
                                        case "H":
                                        case "M":
                                        case "L":
                                            MachineExALL++;
                                            break;
                                    }
                                }


                            }
                        }
                    }
                }else if ("設備離綫".equals(ExceptionType)){
                    for (AutoFloor_Test_Exception autoFloor_test_exception : autoFloor_test_exceptions) {
                        String linename = autoFloor_test_exception.getLinename();
                        String exceptionType = autoFloor_test_exception.getExceptionType();
                        for (String Exception : TitleException_TypeList) {//判讀異常相同
                            if (Exception!=null&&Exception.equals(linename)){
                                // FAC判断线体与异常明细相同
                                if ("OFFLINE".equals(exceptionType)){
                                    Integer flag = autoFloor_test_exception.getFlag();
                                    String priority = autoFloor_test_exception.getPriority();
                                    switch (flag){//維修狀態
                                        case 0:
                                            WaitDispatchNum++;
                                            break;
                                        case 1:
                                            RepairCompleteNum++;
                                            break;
                                        case 2:
                                            UnderRepairNum++;
                                            break;
                                    }
                                    switch (priority){//異常等級
                                        case "H":
                                        case "M":
                                        case "L":
                                            MachineExALL++;
                                            break;
                                    }
                                }


                            }
                        }
                    }
                }

                if ("ALL".equals(FloorName)){
                    for (Floor_Hermes_Target floor_hermes_target : floor_Hermes_Targets) {
                        String hermesFloor = floor_hermes_target.getHermesFloor();
                        for (AutoFloor_Test_Exception autoFloor_test_exception : FACTitleUnFloorList) {
                            String floor = autoFloor_test_exception.getFloor();
                            String linename = autoFloor_test_exception.getLinename();
                            if (hermesFloor.equals(floor)){
                                switch (title){
                                    case "能源":
                                        if ("ENERGYMONITORING".equals(linename)){
                                            unFloorNumber++;
                                        }
                                        break;
                                    case "環境":
                                        if ("ENVIRONMENTAL".equals(linename)){
                                            unFloorNumber++;
                                        }
                                        break;
                                    case "能耗":
                                        if ("ENERGYCONSUMPTION".equals(linename)){
                                            unFloorNumber++;
                                        }
                                        break;
                                }
                            }
                        }
                    }

                }else {
                    if (WaitDispatchNum>0){unFloorNumber=1;}

                }
                switch (title){
                    case "能源":
                        floorNumber = ENERGYMONITORINGFloor(FloorName);
                        break;
                    case "環境":
                        floorNumber = ENVIRONMENTALFloor(FloorName);
                        break;
                    case "能耗":
                        floorNumber = ENERGYCONSUMPTIONFloor(FloorName);
                        break;
                }

                alarmSystemTitle.setFloorNumber(floorNumber);
                alarmSystemTitle.setUnFloorNumber(unFloorNumber);
                alarmSystemTitle.setWaitDispatchNum(WaitDispatchNum);
                alarmSystemTitle.setUnderRepairNum(UnderRepairNum);
                alarmSystemTitle.setRepairCompleteNum(RepairCompleteNum);
                alarmSystemTitle.setMachineExALL(MachineExALL);
                alarmSystemTitle.setExecptionType(ExceptionType);
                alarmSystemTitle.setType(0);
                AlarmSystemTitleList.add(alarmSystemTitle);
            }



            AlarmSystemTitleDataMap.put(title,AlarmSystemTitleList);

        }

        return AlarmSystemTitleDataMap;
    }


    public Boolean ValStrList(String val) {
        Boolean ValStr = null;
        try {
            Double.parseDouble(val);
            ValStr = true;
        } catch (Exception e) {
            ValStr = false;
        }
        return ValStr;
    }

    //禁止出現的異常類型
    public List<String> DisableException_Type(String Product){
        List<String> DisableException_TypeList=new ArrayList<>();
        switch (Product){
            case "Equipment":
                DisableException_TypeList.add("SLANT");
                DisableException_TypeList.add("MACH_YIELD");
                DisableException_TypeList.add("WAIT");
                DisableException_TypeList.add("CELL_UPH_SUBSTANDARD");
                DisableException_TypeList.add("RETEST_TIMEOUT");
                break;
        }

        return DisableException_TypeList;
    }


    //summary类型种类
    public List<String> titleTypeList(String Product){
        List<String> titleTypeList=new ArrayList();
        switch (Product){
            case "KeyParameter":
                break;
            case "Equipment":
                titleTypeList.add("Robot");
                titleTypeList.add("治具");
                titleTypeList.add("工站連接");
                break;
            case "FacilityManager":
                titleTypeList.add("能源");
                titleTypeList.add("環境");
                titleTypeList.add("能耗");
                break;
            case "Equipment_SMT":
                titleTypeList.add("SMT投首");
                titleTypeList.add("印刷機");
                titleTypeList.add("SPI");
                titleTypeList.add("貼片機");
                titleTypeList.add("爐前/后 AOI");
                titleTypeList.add("回焊爐");
                titleTypeList.add("自動化烤箱");
                titleTypeList.add("鐳射Laber");
                titleTypeList.add("鐳射B/C");
                titleTypeList.add("點膠機anda");
                titleTypeList.add("自動化設備");
                break;
            case "SKASandAFS":
                titleTypeList.add("RGV");
                titleTypeList.add("AGV");
                titleTypeList.add("暫存倉");
                titleTypeList.add("AFS小車");
                break;
        }


        return titleTypeList;
    }
    //summary类型种类对应异常
    public List<String> TestException_Type(String Product,String Type){
        List<String> Exception_TypeList =new ArrayList<>();
        switch (Type) {
            case "能源":
                Exception_TypeList.add("ENERGYMONITORING");
                break;
            case "環境":
                Exception_TypeList.add("ENVIRONMENTAL");
                break;
            case "能耗":
                Exception_TypeList.add("ENERGYCONSUMPTION");
                break;
            case "ALL":
                Exception_TypeList.add("ENERGYMONITORING");
                Exception_TypeList.add("ENVIRONMENTAL");
                Exception_TypeList.add("ENERGYCONSUMPTION");
                break;
        }


        return Exception_TypeList;
    }

    //场务拥有能源监控的楼层
    public int  ENERGYMONITORINGFloor(String floorName){
        int ENERGYMONITORING=0;
        List<String> ENERGYMONITORINGFloorList=new ArrayList<>();
        ENERGYMONITORINGFloorList.add("D061F");
        for (String ENERGYMONITORINGFloor : ENERGYMONITORINGFloorList) {
            if (ENERGYMONITORINGFloor.equals(floorName)){
                ENERGYMONITORING++;
            }
        }
        if ("ALL".equals(floorName)){
            ENERGYMONITORING=ENERGYMONITORINGFloorList.size();
        }

        return ENERGYMONITORING;
    }

    //场务拥有環境监控的楼层
    public int  ENVIRONMENTALFloor(String floorName){
        List<Floor_Hermes_Target> floor_Hermes_Targets = floor_hermes_targetMapper.selectHermesByFloor();
        int ENVIRONMENTA=0;
        for (Floor_Hermes_Target  hermesList : floor_Hermes_Targets) {
            String hermesFloor = hermesList.getHermesFloor();
            if (hermesFloor.equals(floorName)){
                ENVIRONMENTA++;
            }
        }

        if ("ALL".equals(floorName)){
            ENVIRONMENTA=floor_Hermes_Targets.size();
        }

        return ENVIRONMENTA;
    }

    //场务拥有能耗监控的楼层
    public int  ENERGYCONSUMPTIONFloor(String floorName){
        int ENERGYCONSUMPTION=0;
        List<String> ENERGYCONSUMPTIONFloorList=new ArrayList<>();
        ENERGYCONSUMPTIONFloorList.add("D061F");
        for (String ENERGYCONSUMPTIONFloor : ENERGYCONSUMPTIONFloorList) {
            if (ENERGYCONSUMPTIONFloor.equals(floorName)){
                ENERGYCONSUMPTION++;
            }
        }
        if ("ALL".equals(floorName)){
            ENERGYCONSUMPTION=ENERGYCONSUMPTIONFloorList.size();
        }

        return ENERGYCONSUMPTION;
    }

}




