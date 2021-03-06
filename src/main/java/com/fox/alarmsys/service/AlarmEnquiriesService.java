package com.fox.alarmsys.service;


import com.fox.alarmsys.entity.*;
import com.fox.alarmsys.mapper.AUTOFLOOR_FLOOR_TARGETMapper;
import com.fox.alarmsys.mapper.AutoFloor_Key_CheckUP_HISMapper;
import com.fox.alarmsys.mapper.AutoFloor_Test_ExceptionMapper;
import com.fox.alarmsys.mapper.Floor_Hermes_TargetMapper;
import com.fox.qualitysys.entity.AutoFloor_Key_CheckNG;
import com.fox.qualitysys.mapper.AutoFloor_Key_CheckNGMapper;
import com.fox.testsys.entity.AutoFloor_Target;
import com.fox.testsys.mapper.AutoFloor_TargetMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author
 * @create 2020-06-30 18:52
 */
@Service
public class AlarmEnquiriesService {

    @Autowired
    private AutoFloor_TargetMapper autoFloor_targetMapper;
    @Autowired
    private AUTOFLOOR_FLOOR_TARGETMapper autofloor_floor_targetMapper;

    @Autowired
    private Floor_Hermes_TargetMapper floor_hermes_targetMapper;

    @Autowired
    private AutoFloor_Key_CheckNGMapper autoFloor_key_checkNGMapper;
    @Autowired
    private AutoFloor_Test_ExceptionMapper autoFloor_test_exceptionMapper;
    @Autowired
    private AutoFloor_Key_CheckUP_HISMapper autoFloor_key_checkUP_hisMapper;
    @Autowired
    private AlarmSystemService alarmSystemService;



    DateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");


    public Map<String,Object> alarmEnquiriesSearchData(String typeName){
        Map<String,Object> alarmEnquiriesSearchMap=new HashMap<>();
        Set<String> floorSet=  new HashSet<>();
        List<String> lineList=  new ArrayList<>();
        lineList.add("ALL");
        Set<String> productSet=  new HashSet<>();
        productSet.add("ALL");
        if ("HotAirGuns".equals(typeName)){
            List<AUTOFLOOR_FLOOR_TARGET> autofloor_floor_targets = autofloor_floor_targetMapper.selectByTypeData(typeName);
            autofloor_floor_targets.forEach(data-> {
                floorSet.add(data.getFloorname());
            });
        }else {
            List<AutoFloor_Target> autoFloor_targets = autoFloor_targetMapper.ALLFloorLine();
            for (AutoFloor_Target autoFloor_target : autoFloor_targets) {
                String floor = autoFloor_target.gettFloor();
                String line = autoFloor_target.getLineName();
                String product = autoFloor_target.gettProduct();
                floorSet.add(floor);
                lineList.add(line);
                productSet.add(product);
            }
        }



        Map<String, String> alarmProjectMap = alarmSystemService.AlarmProjectMap();

        List<AlarmProjectEntity> alarmTypeList= new ArrayList<>();

        List<AlarmProjectEntity> machineFaultList=new ArrayList<>();

        for (Map.Entry<String, String> alarmProjectEntry : alarmProjectMap.entrySet()) {
            AlarmProjectEntity alarmProjectEntity=new AlarmProjectEntity();
            String projectEntrykey = alarmProjectEntry.getKey();
            String projectEntryvalue = alarmProjectEntry.getValue();
            alarmProjectEntity.setProjectChName(projectEntrykey);
            alarmProjectEntity.setProjectEnName(projectEntryvalue);
            alarmTypeList.add(alarmProjectEntity);

        List<String> titleTypeList = alarmSystemService.titleTypeList(projectEntryvalue);
            for (String titleType : titleTypeList) {
                AlarmProjectEntity machineFaultEntity=new AlarmProjectEntity();
                machineFaultEntity.setProjectChName(titleType);
                machineFaultEntity.setProjectEnName(projectEntryvalue);
                machineFaultList.add(machineFaultEntity);
            }
        }

        alarmEnquiriesSearchMap.put("floorSet",floorSet);
        alarmEnquiriesSearchMap.put("lineList",lineList);
        alarmEnquiriesSearchMap.put("productSet",productSet);
        alarmEnquiriesSearchMap.put("alarmTypeList",alarmTypeList);
        alarmEnquiriesSearchMap.put("machineFaultList",machineFaultList);

        return alarmEnquiriesSearchMap;
    }



    public Map<String, List<String>> alarmSystemFACAreaAndFloor(){

        Map<String ,List<String>> alarmSystemFACAreaAndFloorMap=new HashMap<>();

        List<Floor_Hermes_Target> floor_Hermes_Targets = floor_hermes_targetMapper.selectHermesByFloor();

        Set<String> areaSet=new HashSet<>();
        List<String> hermesFloorList =new ArrayList<>();
        for (Floor_Hermes_Target floor_hermes_target : floor_Hermes_Targets) {
            String area = floor_hermes_target.getArea();
            String hermesFloor = floor_hermes_target.getHermesFloor();
            areaSet.add(area);
            hermesFloorList.add(hermesFloor);
        }
        List<String> areaList= Arrays.asList(areaSet.toArray(new String[0]));

        alarmSystemFACAreaAndFloorMap.put("FloorList",hermesFloorList);
        alarmSystemFACAreaAndFloorMap.put("areaList",areaList);

        return alarmSystemFACAreaAndFloorMap;
    }

    public List<AutoFloor_Key_CheckNG>  SearchKeyNGMessage(String Unit, String ProductName, String lineName, String startDate, String endDate){
        List<AutoFloor_Key_CheckNG> searchNGMessage=new ArrayList<>();
        List<AutoFloor_Key_CheckUP_HIS>  SearchNGMessage = autoFloor_key_checkUP_hisMapper.selectUpHisData(ProductName,lineName, startDate, endDate);
        for (AutoFloor_Key_CheckUP_HIS autoFloor_key_checkNG : SearchNGMessage) {
            AutoFloor_Key_CheckNG autoFloor_key_cheCkUp=new AutoFloor_Key_CheckNG();
            String floor = autoFloor_key_checkNG.getFloor();
            String linename = autoFloor_key_checkNG.getLinename();
            String machine = autoFloor_key_checkNG.getMachinetype();
            String ExceptionTime = autoFloor_key_checkNG.getTesttime();
            BigDecimal id = autoFloor_key_checkNG.getId();
            String keyPmsCh = autoFloor_key_checkNG.getKeyPmsCh();
            String keyVal = autoFloor_key_checkNG.getKeyVal();
            String spec = autoFloor_key_checkNG.getSpec();
            Boolean aBoolean = alarmSystemService.ValStrList(keyVal);
            Date updateTime = autoFloor_key_checkNG.getUpdateTime();
            String repairContent = autoFloor_key_checkNG.getRepairContent();
            BigDecimal flag = autoFloor_key_checkNG.getFlag();
            if (aBoolean){
                Double valDouble = Double.parseDouble(keyVal);
                BigDecimal valDoublebig=new BigDecimal(valDouble);
                valDouble =valDoublebig.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
                autoFloor_key_checkNG.setKeyVal(valDouble+"");
            }
            autoFloor_key_cheCkUp.setFloor(floor);
            autoFloor_key_cheCkUp.setLinename(linename);
            autoFloor_key_cheCkUp.setMachinetype(machine);
            autoFloor_key_cheCkUp.setKeyPmsCh(keyPmsCh);
            autoFloor_key_cheCkUp.setTesttime(ExceptionTime);
            autoFloor_key_cheCkUp.setKeyVal(keyVal);
            autoFloor_key_cheCkUp.setSpec(spec);
            autoFloor_key_cheCkUp.setId(id);
            autoFloor_key_cheCkUp.setUpDateTimeStr(format.format(updateTime));
            autoFloor_key_cheCkUp.setRepairContent(repairContent);
            if (flag!=null){autoFloor_key_cheCkUp.setFlag(flag.intValue());}
            searchNGMessage.add(autoFloor_key_cheCkUp);
        }

        return searchNGMessage;
    }

    public List<AutoFloor_Key_CheckNG>  alarmEnquiriesTeData(String alarmTypeName,String machineFaultName,String flags,String LineName, String FloorName, String StartDate , String EndDate, String Priority,String Type,String Unit){

        List<AutoFloor_Test_Exception> autoFloor_test_exceptions = autoFloor_test_exceptionMapper.selectQuiriesTestExceptionList(LineName,FloorName, StartDate, EndDate,Unit,flags);

        List<AutoFloor_Key_CheckNG>  LowautoFloor_key_checkNGS = new ArrayList<>();

        List<AutoFloor_Key_CheckNG>  searchNGMessage = new ArrayList<>();

        for (int i = 0; i < autoFloor_test_exceptions.size(); i++) {
            AutoFloor_Test_Exception autoFloor_test_exception = autoFloor_test_exceptions.get(i);
            AutoFloor_Key_CheckNG autoFloor_key_cheCkUp=new AutoFloor_Key_CheckNG();
            String floor = autoFloor_test_exception.getFloor();
            String linename = autoFloor_test_exception.getLinename();
            String machine = autoFloor_test_exception.getMachine();
            String exceptionType = autoFloor_test_exception.getExceptionType();
            String exceptionDescribe = autoFloor_test_exception.getExceptionDescribe();
            Date ExceptionTime = autoFloor_test_exception.getExceptionTime();
            String priority = autoFloor_test_exception.getPriority();
            BigDecimal id = autoFloor_test_exception.getId();
            Integer flag = autoFloor_test_exception.getFlag();
            Date updateTime = autoFloor_test_exception.getUpdateTime();
            String repairContent = autoFloor_test_exception.getRepairContent();
            autoFloor_key_cheCkUp.setFloor(floor);
            autoFloor_key_cheCkUp.setLinename(linename);
            autoFloor_key_cheCkUp.setMachinetype(machine);
            autoFloor_key_cheCkUp.setKeyPmsCh(exceptionDescribe);
            autoFloor_key_cheCkUp.setTesttime(format.format(ExceptionTime));
            if (updateTime!=null){autoFloor_key_cheCkUp.setUpDateTimeStr(format.format(updateTime));}
            autoFloor_key_cheCkUp.setProduct(priority);
            autoFloor_key_cheCkUp.setId(id);
            autoFloor_key_cheCkUp.setFlag(flag);
            autoFloor_key_cheCkUp.setRepairContent(repairContent);

            if (!"ALL".equals(alarmTypeName)){
                    List<String> TestExceptionList = alarmSystemService.TestException_Type(alarmTypeName, machineFaultName);
                    for (String TestException : TestExceptionList) {
                        if ("FAC".equals(Type)){ exceptionType=linename; }
                        if (TestException.equals(exceptionType)){
                            if (Priority.equals("")||Priority==null||Priority.equals("请选择")||Priority.equals("ALL")){
                                if (!priority.equals("L")){
                                    searchNGMessage.add(autoFloor_key_cheCkUp);
                                }else {
                                    LowautoFloor_key_checkNGS.add(autoFloor_key_cheCkUp);
                                }
                            }else if (Priority.equals("H")){
                                if (priority.equals("H")){
                                    searchNGMessage.add(autoFloor_key_cheCkUp);
                                }
                            }else if (Priority.equals("M")){
                                if (priority.equals("M")){
                                    searchNGMessage.add(autoFloor_key_cheCkUp);
                                }
                            }else if (Priority.equals("L")){
                                if (priority.equals("L")){
                                    searchNGMessage.add(autoFloor_key_cheCkUp);
                                }
                            }
                        }
                }
            }

        }
        searchNGMessage.addAll(LowautoFloor_key_checkNGS);

        return searchNGMessage;
    }
}
