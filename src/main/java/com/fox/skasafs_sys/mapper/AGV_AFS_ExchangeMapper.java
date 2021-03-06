package com.fox.skasafs_sys.mapper;


import com.fox.skasafs_sys.entity.AGV_AFS_Exchange;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AGV_AFS_ExchangeMapper {

    List<AGV_AFS_Exchange>selectUPHSum();

    List<AGV_AFS_Exchange> selectStoreIssue(@Param("FloorName")String FloorName,@Param("StartDate") String StartDate,@Param("EndDate") String EndDate);

    List<AGV_AFS_Exchange> selectCarStoreIssue(@Param("FloorName")String FloorName,@Param("CarName")String CarName,@Param("StartDate") String StartDate,@Param("EndDate") String EndDate);


}