package com.fox.qualitysys.mapper;

import com.fox.qualitysys.entity.AutoFloor_Key_CheckNG;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AutoFloor_Key_CheckNGMapper {
    List<AutoFloor_Key_CheckNG> selectNGFloor();

    List<AutoFloor_Key_CheckNG> selectNGMessage(@Param("floorname")String floorname,@Param("startdate") String startdate,@Param("enddate") String enddate);

    List<AutoFloor_Key_CheckNG> SearchNGMessage(@Param("floorName") String floorName,@Param("lineName") String lineName,@Param("productName") String productName,@Param("startDate")String startDate,@Param("endDate")String endDate);
}