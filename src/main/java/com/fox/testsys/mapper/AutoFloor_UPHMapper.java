package com.fox.testsys.mapper;


import com.fox.testsys.entity.AmountsUPH;
import com.fox.testsys.entity.AutoFloor_Machine_Detail;
import com.fox.testsys.entity.AutoFloor_UPH;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AutoFloor_UPHMapper {

    Integer  lineActualOut(@Param("floorname")String floorname, @Param("linename") String linename , @Param("startdate") String startdate, @Param("enddate") String enddate);

    List<AutoFloor_UPH> StationOut(@Param("line_name")String LineName, @Param("startdate") String startdate, @Param("enddate") String enddate);

    List<AutoFloor_UPH> Workstationamount(@Param("FloorName")String FloorName,@Param("line_name")String LineName);

    Integer selectCellWorkstationSum(@Param("linename")String LineName);

    List<AutoFloor_Machine_Detail> selectTestCellWorkstationSum(@Param("linename")String LineName);

    List<AutoFloor_UPH> MachinaeActionSum(@Param("FloorName")String FloorName,@Param("line_name")String LineName,@Param("startdate") String startdate, @Param("enddate") String enddate);

    //某個時間段內，某個工站 所有幾台的產能  (變量：時間， 樓層，機種，線體，時間段，工站）
    List<AmountsUPH> MachinaeUPH(@Param("startdate") String startdate , @Param("enddate") String enddate , @Param("line_name") String line_name);
    //統計沒有产出的幾台的詳細 白班内，某個工站  (變量：時間， 樓層，機種，線體，時間段，工站）
    List<AmountsUPH> MachinaeDetail( @Param("startdate") String startdate ,@Param("enddate") String enddate ,@Param("line_name") String line_name);
    //机台直通率产能状况
    List<AmountsUPH>MachinaeUPHFPY(@Param("startdate") String startdate ,@Param("enddate") String enddate ,@Param("line_name") String line_name);
}