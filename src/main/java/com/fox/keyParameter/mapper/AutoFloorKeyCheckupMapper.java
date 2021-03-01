package com.fox.keyParameter.mapper;

import com.fox.keyParameter.entity.AutoFloorKeyCheckup;
import org.apache.ibatis.annotations.Param;
import org.springframework.context.annotation.Primary;

import java.util.ArrayList;

public interface AutoFloorKeyCheckupMapper {
    //获取key数据
    ArrayList<AutoFloorKeyCheckup> getKeyUpData(@Param("startTime") String startTime, @Param("endTime") String endTime
                        , @Param("floor") String floor, @Param("lineName") String lineName, @Param("flag") String flag);



    //修改状态
    boolean updateErrorMsg(@Param("flag") String flag,@Param("UpDateTime") String UpDateTime,@Param("content") String content,@Param("id") String id);


    AutoFloorKeyCheckup getUpdataId(@Param("id") String id);
}