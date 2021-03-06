package com.fox.skasafs_sys.mapper;


import com.fox.skasafs_sys.entity.AutoFloor_SKAS_AFS_ALARMCODE;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AutoFloor_SKAS_AFS_ALARMCODEMapper {

    List<AutoFloor_SKAS_AFS_ALARMCODE> selectAlarmCodeList(@Param("Device") String Device);

}