package com.fox.keyParameter.service.impl;

import com.fox.keyParameter.entity.AutoFloorKeyCheckup;
import com.fox.keyParameter.mapper.AutoFloorKeyCheckupMapper;
import com.fox.keyParameter.service.AutoFloorKeyCheckupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

/**
 * @author Yang TanYing
 * @Description:
 * @create 2021-03-01 10:23
 */
@Primary
@Service
public class AutoFloorKeyCheckupServiceImpl implements AutoFloorKeyCheckupService {

    @Autowired
    private AutoFloorKeyCheckupMapper autoFloorKeyCheckupMapper;

    @Override
    public ArrayList<AutoFloorKeyCheckup> getKeyUpData(String startTime, String endTime, String floor, String lineName,String flag) {
        return autoFloorKeyCheckupMapper.getKeyUpData(startTime, endTime, floor, lineName,flag);
    }

    @Override
    public boolean updateErrorMsg(String flag, String UpDateTime, String content, String id) {
        return autoFloorKeyCheckupMapper.updateErrorMsg(flag, UpDateTime, content, id);
    }

    @Override
    public AutoFloorKeyCheckup getUpdataId(String id) {
        return autoFloorKeyCheckupMapper.getUpdataId(id);
    }
}
