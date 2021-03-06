package com.fox.alarmsys.service;

import com.alibaba.fastjson.JSONObject;

import com.fox.alarmsys.entity.AutoFloor_Current_Count;
import com.fox.alarmsys.entity.SoftwareMonitoring;
import com.fox.alarmsys.mapper.AutoFloor_Current_CountMapper;
import com.fox.testsys.entity.AutoFloor_Target;
import com.fox.testsys.mapper.AutoFloor_TargetMapper;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author
 * @create 2020-08-17 18:52
 */
@Log4j
@Service
public class AlarmSoftwareMonioringService {

    @Autowired
    private AutoFloor_Current_CountMapper autoFloor_current_countMapper;
    @Autowired
    private AutoFloor_TargetMapper autoFloor_targetMapper;

    DateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public  List<SoftwareMonitoring>  AlarmSoftwareMonitoring(String FloorName){
        System.out.println(FloorName);
        List<AutoFloor_Current_Count> autoFloor_current_counts = autoFloor_current_countMapper.softwareMonitoringData(FloorName);
        System.out.println(autoFloor_current_counts.toString());
        Date date = autoFloor_current_countMapper.DBDate();
        List<SoftwareMonitoring>  AlarmSoftwareMonitoringData =new ArrayList<>();
        List<String> FLoorLineSum = FLoorLineSum(FloorName);
        String MachineType="";
        List<AutoFloor_Target> autoFloor_targets = autoFloor_targetMapper.ALLFloorLine();

        for (AutoFloor_Target target : autoFloor_targets) {
            String tFloor = target.gettFloor();
            if (FloorName.equals(tFloor)){
                MachineType= target.gettProduct();
            }
        }

        if ("K051F".equals(FloorName)){
            MachineType="Apollo";
        }
        for (String Line : FLoorLineSum) {
            SoftwareMonitoring softwareMonitoring=new SoftwareMonitoring();
            int Pre_AP =0 ;
            int Post_AP=0;
            int Roater=0;
            int RF_Cell01=0;
            int RF_Cell02=0;
            int RF_Cell03=0;
            int RF_Cell04=0;
            int RF_Cell05=0;
            int RF_Cell06=0;
            int RF_Cell07=0;
            int RF_Cell08=0;
            int RF_Module=0;
            int CCD_ASSORT=0;
            int CCD_Module1=0;
            int CCD_Module2=0;
            int CCD_Module3=0;
            int PD_Cell=0;
            for (AutoFloor_Current_Count autoFloor_current_count : autoFloor_current_counts) {
                String lineName = autoFloor_current_count.getLineName();
                if (lineName!=null){
                    Date updateTime = autoFloor_current_count.getUpdateTime();
                    if (updateTime.getTime()>date.getTime()-(30*1000)){
                        if (lineName.equals(Line+"-P")) {
                            Pre_AP=1;
                        }else if (lineName.equals(Line+"-A")) {
                            Post_AP=1;
                        }else if (lineName.equals(Line+"-R")) {
                            Roater=1;
                        }else if (lineName.equals(Line+"-B")) {
                            RF_Cell01=1;
                        }else if (lineName.equals(Line+"-C")) {
                            RF_Cell02=1;
                        }else if (lineName.equals(Line+"-D")) {
                            RF_Cell03=1;
                        }else if (lineName.equals(Line+"-E")) {
                            RF_Cell04=1;
                        }else if (lineName.equals(Line+"-F")) {
                            RF_Cell05=1;
                        }else if (lineName.equals(Line+"-G")) {
                            RF_Cell06=1;
                        }else if (lineName.equals(Line+"-H")) {
                            RF_Cell07=1;
                        }else if (lineName.equals(Line+"-J")) {
                            RF_Cell08=1;
                        }else if (lineName.equals(Line+"-RF_Module")) {
                            RF_Module=1;
                        }else if (lineName.equals("D061FARF01-CCD3")){
                            CCD_ASSORT=1;
                        }else if (lineName.equals("K051FARF07-CCD1")){
                            CCD_Module1=1;
                        }else if (lineName.equals("K051FARF07-CCD2")){
                            CCD_Module2=1;
                        }else if (lineName.equals("K051FARF07-CCD3")){
                            CCD_Module3=1;
                        }else if (lineName.equals(Line.substring(0,5 )+"-PD")) {
                            PD_Cell=1;
                        }
                    }
                }
            }
            softwareMonitoring.setLineName(Line);
            softwareMonitoring.setMachineType(MachineType);
            softwareMonitoring.setPre_AP(Pre_AP);
            softwareMonitoring.setPost_AP(Post_AP);
            softwareMonitoring.setRoater(Roater);
            softwareMonitoring.setRF_Cell01(RF_Cell01);
            softwareMonitoring.setRF_Cell02(RF_Cell02);
            softwareMonitoring.setRF_Cell03(RF_Cell03);
            softwareMonitoring.setRF_Cell04(RF_Cell04);
            softwareMonitoring.setRF_Cell05(RF_Cell05);
            softwareMonitoring.setRF_Cell06(RF_Cell06);
            softwareMonitoring.setRF_Cell07(RF_Cell07);
            softwareMonitoring.setRF_Cell08(RF_Cell08);
            softwareMonitoring.setRF_Module(RF_Module);
            softwareMonitoring.setCCD_ASSORT(CCD_ASSORT);
            softwareMonitoring.setCCD_Module1(CCD_Module1);
            softwareMonitoring.setCCD_Module2(CCD_Module2);
            softwareMonitoring.setCCD_Module3(CCD_Module3);
            softwareMonitoring.setPD_Cell(PD_Cell);
            AlarmSoftwareMonitoringData.add(softwareMonitoring);


            log.info(JSONObject.toJSONString(AlarmSoftwareMonitoringData, true));
        }
        return AlarmSoftwareMonitoringData;
    }

    private List<String> FLoorLineSum(String FloorName){
        List<String > FLoorLineSum=new ArrayList<>();
        switch (FloorName){
            case "D061F":
                FLoorLineSum.add("D061FARF01");
                FLoorLineSum.add("D061FARF02");
                break;
            case "K051F":
                FLoorLineSum.add("K051FARF08");
                break;
        }

        return FLoorLineSum;

    }


}
