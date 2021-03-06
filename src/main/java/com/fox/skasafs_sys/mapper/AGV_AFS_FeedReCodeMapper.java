package com.fox.skasafs_sys.mapper;


import com.fox.skasafs_sys.entity.AGV_AFS_FeedReCode;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AGV_AFS_FeedReCodeMapper {

    List<AGV_AFS_FeedReCode> selectAFSStoreIssue(@Param("FloorName") String FloorName, @Param("StartDate") String StartDate, @Param("EndDate")String EndDate);

    List<AGV_AFS_FeedReCode> selectCarAFSStoreIssue(@Param("FloorName") String FloorName,@Param("CarName") String CarName,@Param("StartDate") String StartDate,@Param("EndDate")String EndDate);
}