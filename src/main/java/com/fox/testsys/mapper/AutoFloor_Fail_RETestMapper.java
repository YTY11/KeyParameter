package com.fox.testsys.mapper;


import com.fox.testsys.entity.AutoFloor_Fail_RETest;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AutoFloor_Fail_RETestMapper {

    List<AutoFloor_Fail_RETest>FAILRETEST(@Param("startdate") String startdate, @Param("enddate")String enddate, @Param("floorname")String floorname);



}