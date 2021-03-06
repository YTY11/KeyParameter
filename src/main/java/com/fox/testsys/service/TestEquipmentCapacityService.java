package com.fox.testsys.service;



import com.fox.testsys.entity.AutoFloor_Target;
import com.fox.testsys.entity.Base_Station;
import com.fox.testsys.entity.EquipmentCapacity;
import com.fox.testsys.mapper.AUTOFLOOR_Robot_Comm_TimesMapper;
import com.fox.testsys.mapper.Base_StationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TestEquipmentCapacityService {

    @Autowired
    Base_StationMapper base_stationMapper;
    @Autowired
    AUTOFLOOR_Robot_Comm_TimesMapper autofloor_robot_comm_timesMapper;

    public Map<String, Object> EquipmentCapacityData(List<AutoFloor_Target> LineList, String TEFloorName) {
        Map<String, Object> TestEquipmentHealthMap = new HashMap<>();
        List<EquipmentCapacity> TestEquipmentHealthList = new ArrayList<>();
        EquipmentCapacity TestEquipmentHealthAll = new EquipmentCapacity();
        String Product = "D42";
        int EquipmentAllNum = 0;
        int EquipmentHealthNum = 0;
        double EquipmentHealthRate = 0.0;
        List<Base_Station> base_stationList = base_stationMapper.TestEquipmentNums(Product, TEFloorName);
        for (AutoFloor_Target autoFloor_target : LineList) {
            int dTarget = autoFloor_target.getdTarget().intValue();
            EquipmentCapacity EquipmentHealth = new EquipmentCapacity();
            String lineName = autoFloor_target.getLineName();
            int AllNum = 0;
            int HealthNum = 0;
            double HealthRate = 0;
            if (dTarget>0){
                for (Base_Station base_station : base_stationList) {
                    String line = base_station.getLine();
                    String linesubstr = lineName.substring(8, 10);
                    if (line!=null&&line.equals(linesubstr)){
                        Integer ttl = base_station.getTtl();
                        Integer onlines = base_station.getOnlines();
                        Double normals = base_station.getNormals();
                        HealthRate=normals;
                        AllNum=ttl;
                        EquipmentAllNum+=ttl;
                        HealthNum=onlines;
                        EquipmentHealthNum+=onlines;
                    }
                }
                if (AllNum > 0) {
                    HealthRate = HealthNum / (double) AllNum;
                }
                BigDecimal HealthRateBig = new BigDecimal(HealthRate * 100);
                HealthRate = HealthRateBig.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            }

            EquipmentHealth.setLineName(lineName);
            EquipmentHealth.setTestEquipmentAllNum(AllNum);
            EquipmentHealth.setTestEquipmentHealthNum(HealthNum);
            EquipmentHealth.setTestEquipmentHealthRate(HealthRate);
            TestEquipmentHealthList.add(EquipmentHealth);
        }

        if (EquipmentAllNum > 0) {
            EquipmentHealthRate = EquipmentHealthNum / (double) EquipmentAllNum;
        }
        BigDecimal EquipmentHealthRateBig = new BigDecimal(EquipmentHealthRate * 100);
        EquipmentHealthRate = EquipmentHealthRateBig.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        TestEquipmentHealthAll.setTestEquipmentAllNum(EquipmentAllNum);
        TestEquipmentHealthAll.setTestEquipmentHealthNum(EquipmentHealthNum);
        TestEquipmentHealthAll.setTestEquipmentHealthRate(EquipmentHealthRate);
        TestEquipmentHealthMap.put("TestEquipmentHealthList", TestEquipmentHealthList);
        TestEquipmentHealthMap.put("TestEquipmentHealthAll", TestEquipmentHealthAll);
        return TestEquipmentHealthMap;
    }

    public Map<String, Object> EquipmenRobotData(List<AutoFloor_Target> LineList, String FloorName) {
        List<Base_Station> autofloor_robot_comm_times = base_stationMapper.TestEquipmentRoBotNums(FloorName);
        List<EquipmentCapacity> RobotHealthList = new ArrayList<>();
        EquipmentCapacity RobotHealthAll = new EquipmentCapacity();
        Map<String, Object> EquipmenRobotDataMap = new HashMap<>();
        int RobotAllNumALL = 0;
        int RobotHealthNumALL = 0;
        double RobotHealthRateALL = 0.0;

        for (AutoFloor_Target autoFloor_target : LineList) {
            EquipmentCapacity RobotHealth = new EquipmentCapacity();
            String lineName = autoFloor_target.getLineName();
            int dTarget = autoFloor_target.getdTarget().intValue();
            int RobotRunNum = 0;
            int RobotAllNum = 0;
            double RobotHealthRate = 0.0;
            if (dTarget>0){
                for (Base_Station autofloor_robot : autofloor_robot_comm_times) {
                    String line = autofloor_robot.getLine();
                    String linesubstr= lineName.substring(8, 10);
                    if (line.equals(linesubstr)) {
                        RobotAllNum= autofloor_robot.getTtl();
                        RobotAllNumALL+=RobotAllNum;
                        RobotRunNum= autofloor_robot.getOnlines();
                        RobotHealthNumALL+=RobotRunNum;
                    }
                }
                if (RobotAllNum != 0) {
                    RobotHealthRate = RobotRunNum / (double) RobotAllNum;
                }
                BigDecimal RobotHealthRateBig = new BigDecimal(RobotHealthRate * 100);
                RobotHealthRate = RobotHealthRateBig.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            }

            RobotHealth.setLineName(lineName);
            RobotHealth.setTestRobotAllNum(RobotAllNum);
            RobotHealth.setTestRobotHealthNum(RobotRunNum);
            RobotHealth.setTestRobotHealthRate(RobotHealthRate);
            RobotHealthList.add(RobotHealth);
        }
        if (RobotAllNumALL != 0) {
            RobotHealthRateALL = RobotHealthNumALL / (double) RobotAllNumALL;
        }
        BigDecimal RobotHealthRateALLBig = new BigDecimal(RobotHealthRateALL * 100);
        RobotHealthRateALL = RobotHealthRateALLBig.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        RobotHealthAll.setTestRobotHealthRate(RobotHealthRateALL);
        RobotHealthAll.setTestRobotHealthNum(RobotHealthNumALL);
        RobotHealthAll.setTestRobotAllNum(RobotAllNumALL);
        EquipmenRobotDataMap.put("RobotHealthAll", RobotHealthAll);
        EquipmenRobotDataMap.put("RobotHealthList", RobotHealthList);

        return EquipmenRobotDataMap;
    }


}
