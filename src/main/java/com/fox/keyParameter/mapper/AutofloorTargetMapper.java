package com.fox.keyParameter.mapper;

import com.fox.keyParameter.entity.AutofloorTarget;

import java.util.ArrayList;

public interface AutofloorTargetMapper {
    //获取所有楼层和腺体
    ArrayList<AutofloorTarget> getAllFloorAndLine();

    //获取系统时间
    String getSqlDate();
}