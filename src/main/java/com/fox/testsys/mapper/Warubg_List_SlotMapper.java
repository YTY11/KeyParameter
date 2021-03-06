package com.fox.testsys.mapper;

import com.fox.testsys.entity.Warubg_List_Slot;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface Warubg_List_SlotMapper {

    List<Warubg_List_Slot> WipasNum(@Param("product") String product, @Param("floorname")String floorname, @Param("StartDate")String StartDate, @Param("EndDate")String EndDate);

    List<Warubg_List_Slot> PathlossNum(@Param("product") String product,@Param("floorname")String floorname,@Param("StartDate")String StartDate,@Param("EndDate")String EndDate);

    List<Warubg_List_Slot> KeyParameterAll(@Param("Line")String Line);
}