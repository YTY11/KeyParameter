package com.fox.avisys.mapper;

import com.fox.avisys.entity.AutoFloor_AVI_Warning;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AutoFloor_AVI_WarningMapper {


    AutoFloor_AVI_Warning selectMachinieType(@Param("linename")String linename, @Param("station")String station, @Param("startdate")String startdate, @Param("enddate")String enddate);

    List<AutoFloor_AVI_Warning> selectMachinieWarning(@Param("FloorName")String FloorName, @Param("startdate")String startdate, @Param("enddate")String enddate);

}