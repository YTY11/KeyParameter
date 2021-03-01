package com.fox.keyParameter.service.impl;

import com.fox.keyParameter.entity.AutofloorKeyCheckupHis;
import com.fox.keyParameter.service.AutofloorKeyCheckupHisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

/**
 * @author Yang TanYing
 * @Description:
 * @create 2021-03-01 17:57
 */

@Primary
@Service
public class AutofloorKeyCheckupHisServiceImpl implements AutofloorKeyCheckupHisService {

    @Autowired
    private AutofloorKeyCheckupHisService autofloorKeyCheckupHisService;

    @Override
    public int insertKeyData(AutofloorKeyCheckupHis record) {
        return autofloorKeyCheckupHisService.insertKeyData(record);
    }
}
