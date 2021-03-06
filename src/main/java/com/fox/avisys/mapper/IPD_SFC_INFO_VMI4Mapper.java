package com.fox.avisys.mapper;

import com.fox.avisys.entity.IPD_SFC_INFO_VMI4;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface IPD_SFC_INFO_VMI4Mapper {

    List<IPD_SFC_INFO_VMI4> selectINFO_VMI4Data(@Param("FloorName")String FloorName, @Param("StartDate")String StartDate, @Param("EndDate")String EndDate);

}