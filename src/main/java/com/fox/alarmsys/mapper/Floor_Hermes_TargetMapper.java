package com.fox.alarmsys.mapper;

import com.fox.alarmsys.entity.Floor_Hermes_Target;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface Floor_Hermes_TargetMapper {

    List<Floor_Hermes_Target> selectByFloorHermesName(@Param("floor_Name") String floor_Name);

    List<Floor_Hermes_Target> selectHermesByFloor();
}