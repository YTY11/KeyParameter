package com.fox.alarmsys.mapper;


import com.fox.alarmsys.entity.AUTOFLOOR_FLOOR_TARGET;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AUTOFLOOR_FLOOR_TARGETMapper {
    List<AUTOFLOOR_FLOOR_TARGET> selectByTypeData(@Param("typeName")String typeName);
}