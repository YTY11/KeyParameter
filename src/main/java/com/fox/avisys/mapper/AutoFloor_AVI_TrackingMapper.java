package com.fox.avisys.mapper;

import com.fox.avisys.entity.AutoFloor_AVI_Tracking;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AutoFloor_AVI_TrackingMapper {

    AutoFloor_AVI_Tracking selectProgramVersion(@Param("floorname") String floorname, @Param("linename")String linename, @Param("starttime")String starttime, @Param("endtime")String endtime);

    List<AutoFloor_AVI_Tracking> selectAVIUPH(@Param("floorname") String floorname, @Param("starttime")String starttime, @Param("endtime")String endtime);

    List<AutoFloor_AVI_Tracking> selectAVIRepetitionUPH(@Param("floorname") String floorname, @Param("starttime")String starttime, @Param("endtime")String endtime);

    List<AutoFloor_AVI_Tracking> selectAVIUKState(@Param("floorname") String floorname, @Param("starttime")String starttime, @Param("endtime")String endtime);

}