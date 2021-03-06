package com.fox.alarmsys.mapper;

import com.fox.alarmsys.entity.AutoFloor_Key_CheckUP_HIS;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AutoFloor_Key_CheckUP_HISMapper {
    int insert(AutoFloor_Key_CheckUP_HIS record);

    int insertSelective(AutoFloor_Key_CheckUP_HIS record);

    List<AutoFloor_Key_CheckUP_HIS> selectUpHisData(@Param("Product")String Product,@Param("LineName")String LineName,@Param("StartTime")String StartTime,@Param("EndTime")String EndTime);
}