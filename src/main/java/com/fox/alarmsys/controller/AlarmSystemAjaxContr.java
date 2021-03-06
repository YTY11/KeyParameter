package com.fox.alarmsys.controller;

import com.alibaba.fastjson.JSON;

import com.fox.alarmsys.entity.AutoFloor_Key_CheckUP_HIS;
import com.fox.alarmsys.entity.AutoFloor_Visrdo_Target;
import com.fox.alarmsys.entity.ZoneTarget;
import com.fox.alarmsys.mapper.AutoFloor_Key_CheckUP_HISMapper;
import com.fox.alarmsys.mapper.AutoFloor_Test_ExceptionMapper;
import com.fox.alarmsys.mapper.AutoFloor_Visrdo_TargetMapper;
import com.fox.alarmsys.service.AlarmSystemAjaxService;
import com.fox.alarmsys.service.AlarmSystemService;
import com.fox.avisys.mapper.AutoFloor_AVI_EmployeeMapper;
import com.fox.qualitysys.entity.AutoFloor_Key_CheCkUp;
import com.fox.qualitysys.mapper.AutoFloor_Key_CheCkUpMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author
 * @create 2020-08-10 15:08
 */
@RestController
public class AlarmSystemAjaxContr {

    @Autowired
    private AutoFloor_Test_ExceptionMapper autoFloor_test_exceptionMapper;

    @Autowired
    private AutoFloor_Key_CheCkUpMapper autoFloor_key_cheCkUpMapper;

    @Autowired
    private AlarmSystemService alarmSystemService;

    @Autowired
    private AlarmSystemAjaxService alarmSystemAjaxService;

    @Autowired
    private AutoFloor_Key_CheckUP_HISMapper autoFloor_key_checkUP_hisMapper;

    @Autowired
    private AutoFloor_Visrdo_TargetMapper autoFloor_visrdo_targetMapper;

    @Autowired
    private AutoFloor_AVI_EmployeeMapper autoFloor_avi_employeeMapper;
    DateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    @RequestMapping("/alarmsystemtypeup")
    public  void  AlarmSystemUp(@RequestParam(value = "Id")String Id, @RequestParam(value = "Flag")Integer Flag,
                                @Param("MaintainText")String MaintainText,
                                @Param("Product")String Product){

        int id =0;
        int Flagint=0;
        Boolean IntaBoolean = alarmSystemService.ValStrList(Id);
        if (IntaBoolean){
             id = Integer.parseInt(Id);
        }
        if (Flag!=null){
            Flagint=Flag;
        }
        Date DBdate = autoFloor_test_exceptionMapper.DBDate();
        String DateTime = this.format.format(DBdate);

        if ("請選擇".equals(MaintainText)){MaintainText="";}

        if ("KeyParameter".equals(Product)){
            Boolean aBoolean1 =  autoFloor_key_cheCkUpMapper.updateAlarmType(id,DateTime, Flagint,MaintainText);
            if (Flagint==1){
                AutoFloor_Key_CheCkUp autoFloor_key_cheCkUp = autoFloor_key_cheCkUpMapper.SelectId_CHECKUP(id);
                AutoFloor_Key_CheckUP_HIS autoFloor_key_checkUP_his= new AutoFloor_Key_CheckUP_HIS();
                autoFloor_key_checkUP_his.setProduct(autoFloor_key_cheCkUp.getProduct());
                autoFloor_key_checkUP_his.setFloor(autoFloor_key_cheCkUp.getFloor());
                autoFloor_key_checkUP_his.setLinename(autoFloor_key_cheCkUp.getLinename());
                autoFloor_key_checkUP_his.setWorkstation(autoFloor_key_cheCkUp.getWorkstation());
                autoFloor_key_checkUP_his.setMachinetype(autoFloor_key_cheCkUp.getMachinetype());
                autoFloor_key_checkUP_his.setKeyPmsEn(autoFloor_key_cheCkUp.getKeyPmsCh());
                autoFloor_key_checkUP_his.setKeyPmsCh(autoFloor_key_cheCkUp.getKeyPmsCh());
                autoFloor_key_checkUP_his.setSpec(autoFloor_key_cheCkUp.getSpec());
                autoFloor_key_checkUP_his.setSpecMin(autoFloor_key_cheCkUp.getSpecMin());
                autoFloor_key_checkUP_his.setSpecMax(autoFloor_key_cheCkUp.getSpecMax());
                autoFloor_key_checkUP_his.setKeyVal(autoFloor_key_cheCkUp.getKeyVal());
                autoFloor_key_checkUP_his.setCheckResult(autoFloor_key_cheCkUp.getCheckResult());
                autoFloor_key_checkUP_his.setTesttime(autoFloor_key_cheCkUp.getTesttime());
                autoFloor_key_checkUP_his.setUpdateTime(autoFloor_key_cheCkUp.getUpdateTime());
                autoFloor_key_checkUP_his.setComponenttype(autoFloor_key_cheCkUp.getComPonentType());
                autoFloor_key_checkUP_his.setFlag(new BigDecimal(autoFloor_key_cheCkUp.getFlag()));
                autoFloor_key_checkUP_his.setRepairContent(autoFloor_key_cheCkUp.getRepairContent());
                autoFloor_key_checkUP_his.setRepairEndTime(autoFloor_key_cheCkUp.getRepairEndTime());
                autoFloor_key_checkUP_hisMapper.insert(autoFloor_key_checkUP_his);
            }

        }else{
            Boolean aBoolean1 = autoFloor_test_exceptionMapper.updateAlarmType(id,DateTime, Flagint,MaintainText);
        }



    }
    //avi數據信息更改；
    @RequestMapping(value = "/aviAlarmsystemTypeUp")
    public String aviAlarmsystemUp(@Param(value = "id")String id,
                                  @RequestParam("messageArry")String[] messageArry){
        String upMessage="";

        Date DBdate = autoFloor_test_exceptionMapper.DBDate();
        String DateTime = this.format.format(DBdate);
        String machineFaultName =messageArry[0];
        String machineFaultType =messageArry[1];
        String machineFaultMessage =messageArry[2];
        String maintainval =messageArry[3];
        String employeeNumber= messageArry[4];

        String employeeName = autoFloor_avi_employeeMapper.selectBYEmployeeNum(employeeNumber);
        Boolean aBoolean = autoFloor_test_exceptionMapper.upDateAVIAlarmType(id, machineFaultName, machineFaultType, machineFaultMessage, maintainval, DateTime,employeeName );
        alarmSystemAjaxService.insertAVIMaintainRecord(id,machineFaultName,machineFaultType,machineFaultMessage,maintainval,employeeName );
        if (aBoolean){
            upMessage="修改成功！！！";
        }else {
            upMessage="修改失敗！！！";
        }
        return upMessage;
    }


    @RequestMapping("/maintenancecpentent")
    public  Map<String,String>  AlarmMaintenanceCpntent(@RequestParam("Machinetype")String Machinetype){
        List<String> MaintenanceCpntentList=new ArrayList<>();
        Map<String,String> maintenanceCpententMap=new HashMap<>();
        alarmSystemAjaxService.getMaintenanceCepntent(Machinetype, MaintenanceCpntentList);
        String maintenanceCpnten = JSON.toJSONString(MaintenanceCpntentList);
        List<String> aviMachineFaultMessageList=new ArrayList<>();
        alarmSystemAjaxService.getAlarmAVIMachineFaultMessage(Machinetype,aviMachineFaultMessageList);
        String aviMachineFaultMessage = JSON.toJSONString(aviMachineFaultMessageList);
        List<String> aviEmployeeList = alarmSystemAjaxService.getAVIEmployeeList();
        String aviEmployeeNum = JSON.toJSONString(aviEmployeeList);
        maintenanceCpententMap.put("aviEmployeeNum",aviEmployeeNum);
        maintenanceCpententMap.put("maintenanceCpnten",maintenanceCpnten);
        maintenanceCpententMap.put("aviMachineFaultMessage",aviMachineFaultMessage);
        return maintenanceCpententMap;

    }
    //avi異常信息明細
    @RequestMapping("/getAVIMachineFaultMessage")
    public String alarmAVIMachineFaultMessage(@Param("machineType")String machineType){
        List<String> aviMachineFaultMessage=new ArrayList<>();
        alarmSystemAjaxService.getAlarmAVIMachineFaultMessage(machineType,aviMachineFaultMessage);
        String aviMachineFaultMessageJson = JSON.toJSONString(aviMachineFaultMessage);
        return aviMachineFaultMessageJson;
    }

    /**
     * 警報o2设置修改
     */
    @PostMapping("/alarmo2settingalter")
    public String AlarmO2SettingAlter(@RequestBody  List<AutoFloor_Visrdo_Target> visrdoTargets){

        Map<String, String> zoneTargetMax = new HashMap<>();
        Map<String, String> zoneTargetMin = new HashMap<>();
        Map<String, String> tableId = new HashMap<>();
        Map<String, String> flag = new HashMap<>();
        Map<String, String> zoneWarningTargetMax = new HashMap<>();
        Map<String, String> zoneWarningTargetMin = new HashMap<>();
        for (int i = 0; i < visrdoTargets.size(); i++) {
            AutoFloor_Visrdo_Target autoFloor_visrdo_target = visrdoTargets.get(i);
            String name = autoFloor_visrdo_target.getName();
            String value = autoFloor_visrdo_target.getValue();
            if (name.contains("zoneTargetMax")) {
                zoneTargetMax.put(name, value);
            } else if (name.contains("zoneTargetMin")) {
                zoneTargetMin.put(name, value);
            } else if (name.contains("tabid")) {
                tableId.put(name, value);
            } else if (name.contains("flag")) {
                flag.put(name, value);
            } else if (name.contains("zoneWarningTargetMax")) {
                zoneWarningTargetMax.put(name, value);
            } else if (name.contains("zoneWarningTargetMin")) {
                zoneWarningTargetMin.put(name, value);
            }
        }

        List<ZoneTarget> zoneTargetList = new ArrayList<>();


        for (int i = 1; i <= 8; i++) {
            Integer id = Integer.parseInt(tableId.get("tabid" + i));
            Integer targetMin = Integer.parseInt (zoneTargetMin.get("zoneTargetMin" + i)) ;
            Integer targetMax = Integer.parseInt (zoneTargetMax.get("zoneTargetMax" + i)) ;
            Integer flag1;
            if (flag.get("flag" + i)!=null){
                flag1=1;
            }else {
                flag1=0;
            }
            Integer warningTargetMax = Integer.parseInt(zoneWarningTargetMax.get("zoneWarningTargetMax" + i));
            Integer warningTargetMin = Integer.parseInt(zoneWarningTargetMin.get("zoneWarningTargetMin" + i));
            ZoneTarget zoneTarget = new ZoneTarget(id, targetMax, targetMin, warningTargetMax, warningTargetMin, flag1);
            zoneTargetList.add(zoneTarget);
        }
        int i = autoFloor_visrdo_targetMapper.updateByPrimaryKeySelective(zoneTargetList);
        String Messagestr="";
        if (i>0){
             Messagestr="修改失敗";
        }else {
             Messagestr="修改成功";
        }

        return Messagestr;
    }
}
