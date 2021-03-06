package com.fox.testsys.mapper;

import com.fox.testsys.entity.AutoFloor_Slant_Count;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AutoFloor_Slant_CountMapper {

    List<AutoFloor_Slant_Count> getCellSlantCount(@Param("startdate")String startdate, @Param("enddate")String enddate, @Param("linename")String linename);

}