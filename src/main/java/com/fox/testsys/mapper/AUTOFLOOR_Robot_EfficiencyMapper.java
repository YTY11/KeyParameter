package com.fox.testsys.mapper;

import com.fox.testsys.entity.AUTOFLOOR_Robot_Efficiency;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AUTOFLOOR_Robot_EfficiencyMapper {
    List<AUTOFLOOR_Robot_Efficiency> ROBOTEfficiencyTime(@Param("startdate") String startdate, @Param("enddate")String enddate, @Param("floorname")String floorname);

    List<AUTOFLOOR_Robot_Efficiency> ROBOTCellEfficiencyTime(@Param("startdate") String startdate, @Param("enddate")String enddate, @Param("floorname")String floorname);

    List<AUTOFLOOR_Robot_Efficiency>LineWaitPlateTime(@Param("startdate") String startdate, @Param("enddate")String enddate, @Param("floorname")String floorname,@Param("linename")String linename);
}