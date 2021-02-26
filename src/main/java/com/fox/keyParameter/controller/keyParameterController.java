package com.fox.keyParameter.controller;

import com.fox.keyParameter.entity.AutofloorTarget;
import com.fox.keyParameter.service.AutofloorTargetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.*;

/**
 * @author Yang TanYing
 * @Description:
 * @create 2021-02-26 17:14
 */

@Controller
public class keyParameterController {
    @Autowired
    private AutofloorTargetService autofloorTargetService;


    @RequestMapping("getFloorAndLine")

    public String getFloorAndLine(Map map){
        List<AutofloorTarget> autofloor_targets = autofloorTargetService.getAllFloorAndLine();

        HashSet<String> floorSet = new HashSet<>();
        ArrayList<String> lineList = new ArrayList<>();

        //存入楼层和线体
        for (AutofloorTarget target : autofloor_targets) {
            floorSet.add(target.gettFloor());
            lineList.add(target.getLineName());
        }

        map.put("floorSet", floorSet);
        map.put("lineList", lineList);

        return "keyParameter";
    }
}
