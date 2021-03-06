package com.fox.skasafs_sys.mapper;


import com.fox.skasafs_sys.entity.AGV_AFS_State;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AGV_AFS_StateMapper {
    List<AGV_AFS_State>selectCarState(@Param("FloorName")String FloorName, @Param("StartDate")String StartDate, @Param("EndDate")String EndDate);
}