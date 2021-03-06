package com.fox.testsys.mapper;

import com.fox.testsys.entity.AutoFloor_Rate;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AutoFloor_WIPMapper {

    Integer  lineActualPut(@Param("floorname")String floorname, @Param("linename") String linename , @Param("startdate") String startdate, @Param("enddate") String enddate);

    List<AutoFloor_Rate> MidetectFail(@Param("linename")String linename, @Param("startdate") String startdate, @Param("enddate") String enddate);
}