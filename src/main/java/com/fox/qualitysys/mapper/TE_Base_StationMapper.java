package com.fox.qualitysys.mapper;

import com.fox.qualitysys.entity.TE_Base_Station;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TE_Base_StationMapper {

    List<TE_Base_Station> getALLBaseStation(@Param("stationname")String stationname, @Param("linename")String linename);

    List<TE_Base_Station> selectByFloorALLBaseStation(@Param("floorNameTE")String floorNameTE);
}