package com.fox.testsys.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 测试關鍵参数来讲
 *
 * @author wu
 * @create 2020-04-23 13:47
 * @date 2020/10/14
 */
@Controller
public class TestKeyParameterContr {


    @RequestMapping("keyparameter")
    public String TestKeyParameterContr() {


        return "testsys/testkeyparameter";
    }

}
