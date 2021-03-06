package com.fox.skasafs_sys.utility;

import org.springframework.web.bind.annotation.RestController;

/**
 * @author
 * @create 2020-07-18 16:12
 */
@RestController
public class SKASAFSUtility {

    public Boolean ConvertDouble(String Num){
        try {


            double v = Double.parseDouble(Num);
            return true;


        }catch (Exception e){


            return  false;
        }
    }


}
