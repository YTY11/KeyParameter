package com.fox.keyParameter.service;

import com.fox.keyParameter.entity.AutoFloorKeyCheckup;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;

/**
 * @author Yang TanYing
 * @Description:
 * @create 2021-03-01 10:23
 */
public interface AutoFloorKeyCheckupService {

    //获取key数据
    ArrayList<AutoFloorKeyCheckup> getKeyUpData(String startTime,String endTime
            ,String floor,String lineName,String flag);



    //修改状态
    boolean updateErrorMsg(String flag,String UpDateTime,String content,String id);


    AutoFloorKeyCheckup getUpdataId(String id);

}
