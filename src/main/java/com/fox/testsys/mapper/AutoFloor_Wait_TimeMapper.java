package com.fox.testsys.mapper;
import com.fox.testsys.entity.AutoFloor_Wait_Time;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface AutoFloor_Wait_TimeMapper {


    // 偏位次數 偏位率 待板率 最近倆小時
    AutoFloor_Wait_Time RobotAnalyzes(@Param("start_date") String start_date, @Param("end_date") String end_date, @Param("floor_name") String floor_name, @Param("product_name") String product_name, @Param("line_name") String line_name);

    List<AutoFloor_Wait_Time> RobotCellAnalyzes(@Param("start_date") String start_date, @Param("end_date") String end_date, @Param("line_name") String line_name);


    // 偏位次數 偏位率 待板率 20
    AutoFloor_Wait_Time SelectRobotAnalyze(@Param("start_Time") String start_Time, @Param("end_Time") String end_Time, @Param("floor_name") String floor_name, @Param("product_name") String product_name, @Param("line_name") String line_name);

    List<AutoFloor_Wait_Time> AlarmCenterRobot(@Param("start_Time") String start_Time, @Param("end_Time") String end_Time, @Param("floor_name") String floor_name, @Param("line_name") String line_name);
    // 偏位機故率高TOP  5 夜班 分析
    List<AutoFloor_Wait_Time> RobotAnalyzeTOP5(@Param("start_Time") String start_Time, @Param("end_Time") String end_Time, @Param("floor_name") String floor_name, @Param("line_name") String line_name);

    // 偏位機故率高TOP  5 夜班位置說明
    List<AutoFloor_Wait_Time> RobotAnalyzeTOP5Site(@Param("start_Time") String start_Time, @Param("end_Time") String end_Time, @Param("floor_name") String floor_name);

}