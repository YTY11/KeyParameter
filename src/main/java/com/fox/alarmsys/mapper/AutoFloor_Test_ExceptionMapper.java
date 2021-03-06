package com.fox.alarmsys.mapper;

import com.fox.alarmsys.entity.AutoFloor_Test_Exception;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface AutoFloor_Test_ExceptionMapper {

    AutoFloor_Test_Exception selectByIdAutoFloorTestException(@Param("id") String id);

    List<AutoFloor_Test_Exception> selectTestExceptionList(@Param("FloorName") String FloorName,@Param("LineName") String LineName,@Param("StartDate")String StartDate,@Param("EndDate")String EndDate,@Param("Type")String Type, @Param("DisableException_Type") List<String> DisableException_Type);

    List<AutoFloor_Test_Exception> selectSkasExceptionList(@Param("FloorName") String FloorName,@Param("LineName") String LineName,@Param("StartDate")String StartDate,@Param("EndDate")String EndDate,@Param("Type")String Type, @Param("DisableException_Type") List<String> DisableException_Type);

    List<AutoFloor_Test_Exception> selectRobotTestExceptionList(@Param("FloorName") String FloorName,@Param("LineName") String LineName,@Param("StartDate")String StartDate,@Param("EndDate")String EndDate,@Param("Type")String Type, @Param("DisableException_Type") List<String> DisableException_Type);

    List<AutoFloor_Test_Exception> selectFACTestExceptionList (@Param("FloorName") String FloorName,@Param("StartDate")String StartDate,@Param("EndDate")String EndDate,@Param("Type")String Type, @Param("DisableException_Type") List<String> DisableException_Type);

    List<AutoFloor_Test_Exception> selectFACTitleUnFloorList   (@Param("StartDate")String StartDate,@Param("EndDate")String EndDate);

    List<AutoFloor_Test_Exception> selectNOTTestExceptionList(@Param("FloorName") String FloorName,@Param("StartDate")String StartDate,@Param("EndDate")String EndDate,@Param("Type")String Type, @Param("DisableException_Type") List<String> DisableException_Type);

    List<AutoFloor_Test_Exception> selectQuiriesTestExceptionList(@Param("LineName") String LineName,@Param("FloorName")String FloorName,@Param("StartDate")String StartDate,@Param("EndDate")String EndDate,@Param("Unit")String Unit,@Param("FLAG") String FLAG);

    List<AutoFloor_Test_Exception> selectModelingtoolData(@Param("FloorName") String FloorName,@Param("StartDate")String StartDate,@Param("EndDate")String EndDate,@Param("Type")String Type);

    Boolean updateAlarmType(@Param("Id")Integer Id,@Param("UpDateTime")String UpDateTime,@Param("Flag")Integer Flag,@Param("MaintainText")String MaintainText);

    List<AutoFloor_Test_Exception> selectTeZTLTOP(@Param("LineName") String LineName,@Param("StartDate")String StartDate,@Param("EndDate")String EndDate);

    List<AutoFloor_Test_Exception> selectK05CCD_Data(@Param("LineName") String LineName,@Param("StartDate")String StartDate,@Param("EndDate")String EndDate);

    Boolean upDateAVIAlarmType(@Param("id")String id,@Param("machineFaultName")String machineFaultName,@Param("machineFaultType")String machineFaultType,@Param("machineFaultMessage")String machineFaultMessage,@Param("maintainval")String maintainval,@Param("upDateTime")String upDateTime,@Param("empName")String empName);

    Date  DBDate();
}