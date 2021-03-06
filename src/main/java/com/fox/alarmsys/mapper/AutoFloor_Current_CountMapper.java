package com.fox.alarmsys.mapper;


import com.fox.alarmsys.entity.AutoFloor_Current_Count;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface AutoFloor_Current_CountMapper {


    List<AutoFloor_Current_Count> softwareMonitoringData(@Param("FloorName") String FloorName);

    Date DBDate();
}