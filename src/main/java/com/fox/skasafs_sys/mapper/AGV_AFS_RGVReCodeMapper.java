package com.fox.skasafs_sys.mapper;

import com.fox.skasafs_sys.entity.AGV_AFS_RGVReCode;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AGV_AFS_RGVReCodeMapper {

    List<AGV_AFS_RGVReCode> selectStoreIssueQuantity(@Param("FloorName") String FloorName, @Param("Workstation")String Workstation, @Param("LineName") String LineName, @Param("StartDate") String StartDate, @Param("EndDate") String EndDate);

}