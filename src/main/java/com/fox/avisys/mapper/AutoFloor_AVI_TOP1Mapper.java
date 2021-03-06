package com.fox.avisys.mapper;

import com.fox.avisys.entity.AutoFloor_AVI_TOP1;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AutoFloor_AVI_TOP1Mapper {

   List<AutoFloor_AVI_TOP1> selectByLineTop1Data(@Param("floorName")String floorName, @Param("startDate")String startDate, @Param("endDate")String endDate);

}