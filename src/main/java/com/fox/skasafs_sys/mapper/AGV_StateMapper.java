package com.fox.skasafs_sys.mapper;


import com.fox.skasafs_sys.entity.AGV_State;
import org.apache.ibatis.annotations.Param;

public interface AGV_StateMapper {


    AGV_State selectAGVState(@Param("StartDate")String StartDate, @Param("EndDate")String EndDate);

}