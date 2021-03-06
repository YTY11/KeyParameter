package com.fox.testsys.mapper;

import com.fox.testsys.entity.AutoFloor_Rate;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AutoFloor_RateMapper {

    //首页所有良率(相乘) 08-10 使用此sqll-->
    List<AutoFloor_Rate> AutoFloorALLRate_Yield(@Param("start_Time")String start_Time, @Param("end_Time")String end_Time, @Param("floor_name") String floor_name, @Param("line_name") String line_name);

    List<AutoFloor_Rate> AutoFloorALLStationRate(@Param("start_Time")String start_Time, @Param("end_Time")String end_Time, @Param("floor_name") String floor_name,@Param("line_name") String line_name);

}