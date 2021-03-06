package com.fox.qualitysys.mapper;

import com.fox.qualitysys.entity.AutoFloor_Key_KeyPMS;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AutoFloor_Key_KeyPMSMapper {

    AutoFloor_Key_KeyPMS selectWorkStation(@Param("WorkStation") String  WorkStation);

    List<AutoFloor_Key_KeyPMS> selectQuality(@Param("floor")String   floor);

    List<AutoFloor_Key_KeyPMS> selectPMSProcessing(@Param("WorkStation")String WorkStation,@Param("lineName")String lineName);
}