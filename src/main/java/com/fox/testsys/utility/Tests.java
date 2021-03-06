package com.fox.testsys.utility;


import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author
 * @create 2019-11-09 8:30
 */
@Component
public class Tests {



    public void TestClass() throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date date = df.parse("2019-11-1");
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int Month_WEEK = c.get(Calendar.WEEK_OF_MONTH);
        int Day = c.get(Calendar.DAY_OF_WEEK);
        int MonthDay = Day - 1;
        int Week = 0;
        if (MonthDay == 1) {
            Week = Month_WEEK - 1;
        } else {
            Week = Month_WEEK;
        }
    }


}
