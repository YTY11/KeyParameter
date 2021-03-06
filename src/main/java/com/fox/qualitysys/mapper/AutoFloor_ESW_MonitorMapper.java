package com.fox.qualitysys.mapper;

import com.fox.qualitysys.entity.AutoFloor_ESW_Monitor;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AutoFloor_ESW_MonitorMapper {

    List<AutoFloor_ESW_Monitor> selectESWMonitor(@Param("floorname")String floorname, @Param("product")String product, @Param("linename")String linename);

    List<AutoFloor_ESW_Monitor> selectUNESWMonitor(@Param("floorname")String floorname,@Param("product")String product,@Param("linename")String linename);

}