package com.fox.keyParameter.service;

import com.fox.keyParameter.entity.AutofloorTarget;

import java.util.List;

/**
 * @author Yang TanYing
 * @Description:
 * @create 2021-02-26 17:12
 */
public interface AutofloorTargetService {
    //获取楼层和线体
    List<AutofloorTarget> getAllFloorAndLine();
}
