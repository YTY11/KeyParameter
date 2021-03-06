package com.fox.avisys.mapper;

import com.fox.avisys.entity.AutoFloor_AVI_TOP2;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AutoFloor_AVI_TOP2Mapper {

    List<AutoFloor_AVI_TOP2> selectByLineTop2Data(@Param("floorName")String floorName, @Param("startDate")String startDate, @Param("endDate")String endDate);

}