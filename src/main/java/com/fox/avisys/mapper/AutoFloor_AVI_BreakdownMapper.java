package com.fox.avisys.mapper;

import com.fox.avisys.entity.AutoFloor_AVI_Breakdown;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AutoFloor_AVI_BreakdownMapper {
    List<AutoFloor_AVI_Breakdown> selectUNMessage(@Param("floorname")String floorname, @Param("startdate")String startdate, @Param("enddate")String enddate);
}