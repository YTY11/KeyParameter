package com.fox.testsys.mapper;

import com.fox.testsys.entity.Base_Station;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface Base_StationMapper {

    List<Base_Station> TestEquipmentNums(@Param("product")String product, @Param("linename")String linename);

    List<Base_Station> TestEquipmentRoBotNums(@Param("linename")String linename);

    List<Base_Station> selectEquipmentMachineSum(@Param("product")String product,@Param("linename")String linename);

    List<Base_Station> TestEquipmentWipasStation(@Param("LineName")String lineName);

    Base_Station selectDBDate();

    List<String>   selectBySLineEquipmentLine(@Param("sLineName") String sLineName);


}