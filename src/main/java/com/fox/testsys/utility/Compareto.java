package com.fox.testsys.utility;


import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author
 * @create 2019-09-03 17:29
 */
@Component
public class Compareto {


    public int WeekNum(String Date) throws Exception {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date date = df.parse(Date);

        Calendar c = Calendar.getInstance();
        c.setTime(date);

        Calendar j = Calendar.getInstance();
        j.setTime(new Date());

        int Month_WEEK = j.get(Calendar.WEEK_OF_MONTH);

        int Day = c.get(Calendar.DAY_OF_WEEK);

        int MonthDay = Day - 1;

        int Week = 0;

        if (MonthDay != 1) {
            Week = Month_WEEK - 1;
        } else {
            Week = Month_WEEK;
        }
        return Week;
    }


}
