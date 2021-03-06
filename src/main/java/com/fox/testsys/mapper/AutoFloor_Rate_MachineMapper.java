package com.fox.testsys.mapper;


import com.fox.testsys.entity.AutoFloor_Rate_Machine;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AutoFloor_Rate_MachineMapper {

    List<AutoFloor_Rate_Machine> UNFPYMidetectData(@Param("start_Time")String start_Time, @Param("end_Time")String end_Time, @Param("product_name")String product_name, @Param("line_name")String line_name);
}