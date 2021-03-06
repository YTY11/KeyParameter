package com.fox.alarmsys.service;


import com.fox.alarmsys.entity.AutoFloor_Visrdo_Target;
import com.fox.alarmsys.mapper.AutoFloor_Visrdo_TargetMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author
 * @create 2020-10-17 14:52
 */

@Service
public class AlarmSettingService {

    @Autowired
    private AutoFloor_Visrdo_TargetMapper autoFloor_visrdo_targetMapper;


    public Map<String, List>  AlarmO2SettingContr(){
        Map<String,List> alarmSettingMap=new HashMap<>();

        List<AutoFloor_Visrdo_Target> autoFloor_visrdo_targets = autoFloor_visrdo_targetMapper.selectALL_Visrdo_FloorMessage();
        //获取所有楼层
        Set floorSet= autoFloor_visrdo_targets.stream().map(AutoFloor_Visrdo_Target::getFloor).collect(Collectors.toSet());
        List floorList = Arrays.asList(floorSet.toArray());
        //排序
        Collections.sort(floorList);



        //获取所有线体
        Set<String> linenameSet = autoFloor_visrdo_targets.stream().map(AutoFloor_Visrdo_Target::getLinename).collect(Collectors.toSet());
        List linenameList = Arrays.asList(linenameSet.toArray());
        Collections.sort(linenameList);


        //获取所有机器类型
        List machineTypeList = autoFloor_visrdo_targets.stream().map(AutoFloor_Visrdo_Target::getMachinetype).collect(Collectors.toList());
        List machineTypelineList = autoFloor_visrdo_targets.stream().map(AutoFloor_Visrdo_Target::getLinename).collect(Collectors.toList());


        alarmSettingMap.put("machineTypelineList",machineTypelineList);
        alarmSettingMap.put("floorList",floorList);
        alarmSettingMap.put("machineTypeList",machineTypeList);
        alarmSettingMap.put("linenameList",linenameList);

        return alarmSettingMap;

    }

    public List<AutoFloor_Visrdo_Target>   AlarmO2SettingContrQuiries(String floorName, String lineName, String machineTypeName){

        List<AutoFloor_Visrdo_Target> autoFloor_visrdo_targets = autoFloor_visrdo_targetMapper.selectALL_Visrdo_Target();

        List<AutoFloor_Visrdo_Target> quiriesTabData=new ArrayList<>();

        autoFloor_visrdo_targets.forEach(visrdo_targets->{
            String floor = visrdo_targets.getFloor();
            String line = visrdo_targets.getLinename();
            String machinetype = visrdo_targets.getMachinetype();
            if (floor!=null&&floor.equals(floorName)){if (line!=null&&line.equals(lineName)){
                if (machinetype!=null&&machinetype.equals(machineTypeName)){
                    quiriesTabData.add(visrdo_targets);

                }
            }}

        });

        return quiriesTabData;

    }



}
