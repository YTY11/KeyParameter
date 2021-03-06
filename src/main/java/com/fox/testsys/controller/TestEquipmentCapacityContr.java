package com.fox.testsys.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author
 * @create 2020-04-23 13:47
 */
@Controller
public class TestEquipmentCapacityContr {


    @RequestMapping("equipmentcapacity")
    public String TestEquipmentCapacityContr(){


        return "testsys/testequipmentcapacity";
    }

}
