package com.fox.qualitysys.mapper;

import com.fox.qualitysys.entity.AutoFloor_Key_CheCkUp;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AutoFloor_Key_CheCkUpMapper {

        List<AutoFloor_Key_CheCkUp> selectUNKeyMessage(@Param("floor")String floor, @Param("startdate")String startdate, @Param("enddate")String enddate);

        List<AutoFloor_Key_CheCkUp> selectUNQuality(@Param("floor")String floor,@Param("startdate")String startdate,@Param("enddate")String enddate);

        List<AutoFloor_Key_CheCkUp>  selectWorkStationMessage(@Param("workstation")String workstation,@Param("linename")String linename,@Param("startdate")String startdate,@Param("enddate")String enddate);

        Integer  selectFloorNGNum(@Param("floorname")String floorname,@Param("checkresult")String checkresult,@Param("startdate")String startdate,@Param("enddate")String enddate);

        List<AutoFloor_Key_CheCkUp>  selectNGMessage(@Param("floor")String floor,@Param("startdate")String startdate,@Param("enddate")String enddate);

        Boolean updateAlarmType(@Param("Id")Integer Id,@Param("UpDateTime")String UpDateTime,@Param("Flag")Integer Flag,@Param("MaintainText")String MaintainText);

        AutoFloor_Key_CheCkUp SelectId_CHECKUP(@Param("Id")Integer Id);
}