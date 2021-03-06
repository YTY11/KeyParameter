package com.fox.qualitysys.mapper;

import com.fox.qualitysys.entity.Warning_List_Slot;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface Warning_List_SlotMapper {

    List<Warning_List_Slot> TestUNStation(@Param("Warning_Case")String WARNING_CASE, @Param("LineName")String LineName);

    List<Warning_List_Slot> TestALLUNStation(@Param("floorName")String floorName);

    List<Warning_List_Slot> TestUNTime(@Param("StartDate")String StartDate,@Param("EndDate")String EndDate,@Param("LineName")String LineName);

    Integer TestUNStationSum(@Param("StartDate")String StartDate,@Param("EndDate")String EndDate,@Param("LineName")String LineName);

    List<Warning_List_Slot> TestCellUNStationSum(@Param("StartDate")String StartDate,@Param("EndDate")String EndDate,@Param("LineName")String LineName);


    List<Warning_List_Slot>  TestUNMessage(@Param("StartDate")String StartDate,@Param("EndDate")String EndDate,@Param("LineName")String LineName);

    Warning_List_Slot  TestUNASCMessage(@Param("StartDate")String StartDate,@Param("EndDate")String EndDate,@Param("LineName")String LineName);
}