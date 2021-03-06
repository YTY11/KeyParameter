package com.fox.testsys.mapper;

import com.fox.testsys.entity.AUTOFLOOR_Robot_Comm_Times;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AUTOFLOOR_Robot_Comm_TimesMapper {
    List<AUTOFLOOR_Robot_Comm_Times> ROBOTRunTime(@Param("startdate") String startdate, @Param("enddate")String enddate, @Param("floorname")String floorname);

    List<AUTOFLOOR_Robot_Comm_Times> ROBOTCellRunTime(@Param("startdate") String startdate, @Param("enddate")String enddate, @Param("floorname")String floorname);

    List<AUTOFLOOR_Robot_Comm_Times> ROBOTRunNum(@Param("floorname")String floorname);

    Integer ROBOTNum(@Param("linename") String linename);


}