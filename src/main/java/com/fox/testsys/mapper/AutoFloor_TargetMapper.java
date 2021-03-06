package com.fox.testsys.mapper;

import com.fox.testsys.entity.AutoFloor_Target;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface AutoFloor_TargetMapper {
    //线体目标
    List<AutoFloor_Target> outPutTarGet(@Param("floorname")String floorname, @Param("tClass") String tClass, @Param("linename") String linename, @Param("startdate") Date startdate, @Param("enddate") Date enddate, @Param("product") String product);
    //多条记录线体目标
    List<AutoFloor_Target> YesterDayLinenameTarGet(@Param("floorname")String floorname,@Param("linename") String linename,@Param("startdate") Date startdate,@Param("enddate") Date enddate, @Param("Productname")String Productname);
    //查询條線的名称获取所有目标  D073F  Lotus  （變量：時間）
    List<AutoFloor_Target> SelectLinenameTarGet(@Param("floorname")String floorname,@Param("linename") String linename,@Param("startdate") String startdate, @Param("enddate")String enddate,@Param("Productname")String Productname);
    //每條線的目标 名称  D073F  Lotus  （變量：時間）
    AutoFloor_Target RobotLineWorkingHours(@Param("startdate") Date startdate,@Param("enddate") Date enddate,@Param("linename") String linename,@Param("tClass") String tClass);

    AutoFloor_Target AloneLine(@Param("tClass") String tClass,@Param("linename") String linename,@Param("startdate") Date startdate,@Param("enddate") Date enddate);

    List<AutoFloor_Target> ALLFloorLine();

    List<AutoFloor_Target> TTLLineList(@Param("startDate")Date startDate,@Param("endDate")Date endDate,@Param("floorName")String floorName);

}