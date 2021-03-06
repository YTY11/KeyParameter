package com.fox.skasafs_sys.mapper;


import com.fox.skasafs_sys.entity.AGV_Exception;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface AGV_ExceptionMapper {

    List<AGV_Exception> selectMachineFaultTime(@Param("floorname") String floorname, @Param("startdate") String startdate, @Param("enddate") String enddate);

    List<AGV_Exception>  selectMachineMessage(@Param("floorname") String floorname,@Param("startdate") String startdate,@Param("enddate") String enddate);

    List<AGV_Exception> selectMachineTimeSlotCost(@Param("floorname") String floorname,@Param("startdate") String startdate,@Param("enddate") String enddate);

    List<AGV_Exception> selectMachineUnMessage(@Param("CarNum") String CarNum,@Param("floorname") String floorname,@Param("time_slot")String time_slot,@Param("startdate") String startdate,@Param("enddate") String enddate);



}