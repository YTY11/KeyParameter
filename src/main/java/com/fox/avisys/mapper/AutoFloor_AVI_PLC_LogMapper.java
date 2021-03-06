package com.fox.avisys.mapper;

import com.fox.avisys.entity.AutoFloor_AVI_PLC_Log;
import org.apache.ibatis.annotations.Param;

public interface AutoFloor_AVI_PLC_LogMapper {

    AutoFloor_AVI_PLC_Log selectUnusualStatus(@Param("LineName")String LineName, @Param("TOP")String TOP);

    Integer selectLineSum();
}