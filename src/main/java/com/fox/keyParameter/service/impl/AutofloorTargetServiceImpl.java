package com.fox.keyParameter.service.impl;

import com.fox.keyParameter.entity.AutofloorTarget;
import com.fox.keyParameter.mapper.AutofloorTargetMapper;
import com.fox.keyParameter.service.AutofloorTargetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Yang TanYing
 * @Description:
 * @create 2021-02-26 17:12
 */
@Primary
@Service
public class AutofloorTargetServiceImpl implements AutofloorTargetService {
    @Autowired
    private AutofloorTargetMapper autofloorTargetMapper;

    @Override
    public List<AutofloorTarget> getAllFloorAndLine() {
        return autofloorTargetMapper.getAllFloorAndLine();
    }
}
