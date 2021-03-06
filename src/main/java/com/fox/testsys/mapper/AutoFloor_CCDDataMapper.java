package com.fox.testsys.mapper;


import com.fox.testsys.entity.AutoFloor_CCDData;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AutoFloor_CCDDataMapper {

    List<AutoFloor_CCDData> selectCCDDATATime(@Param("StartDate")String StartDate, @Param("EndDate")String EndDate, @Param("LineName")String LineName);

    AutoFloor_CCDData selectCCDDATAUnMessage(@Param("StartDate")String StartDate,@Param("EndDate")String EndDate,@Param("LineName")String LineName);
}