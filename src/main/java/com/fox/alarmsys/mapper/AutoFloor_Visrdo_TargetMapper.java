package com.fox.alarmsys.mapper;


import com.fox.alarmsys.entity.AutoFloor_Visrdo_Target;
import com.fox.alarmsys.entity.ZoneTarget;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AutoFloor_Visrdo_TargetMapper {

    List<AutoFloor_Visrdo_Target> selectALL_Visrdo_FloorMessage();

    List<AutoFloor_Visrdo_Target> selectALL_Visrdo_Target();

    int updateByPrimaryKeySelective(@Param("zoneTargets") List<ZoneTarget> zoneTargets);

    int updateByPrimaryKey(AutoFloor_Visrdo_Target record);
}