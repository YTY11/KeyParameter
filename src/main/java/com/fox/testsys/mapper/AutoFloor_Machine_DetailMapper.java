package com.fox.testsys.mapper;


import com.fox.testsys.entity.AutoFloor_Machine_Detail;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AutoFloor_Machine_DetailMapper {

    List<AutoFloor_Machine_Detail> selectFloor_Product(@Param("floor_name") String floor_name);

    List<AutoFloor_Machine_Detail> MachineCellList(@Param("linename")String linename);
}