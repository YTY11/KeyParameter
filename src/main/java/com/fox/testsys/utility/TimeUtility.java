package com.fox.testsys.utility;

import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Component
public class TimeUtility {


    public Boolean Timequantum(String StartDate, String EndDate) {
        SimpleDateFormat df = new SimpleDateFormat("HH:mm");//设置日期格式
        //初始化
        Date nowTime = null;
        Date beginTime = null;
        Date endTime = null;
        try {
            //格式化当前时间格式
            nowTime = df.parse(df.format(new Date()));
            //定义开始时间
            beginTime = df.parse(StartDate);
            //定义结束时间
            endTime = df.parse(EndDate);

        } catch (Exception e) {
            e.printStackTrace();

        }
        //调用判断方法
        boolean flag = belongCalendar(nowTime, beginTime, endTime);
        //输出为结果
        if (flag) {
            //处于规定的时间段内，执行对应的逻辑代码
            return true;
        } else {
            //不处于规定的时间段内，执行对应的逻辑代码
            return false;
        }

    }


    public Boolean presentTimequantum(String StartDate, String EndDate) {
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");//设置日期格式
        //初始化
        Date nowTime = null;
        Date beginTime = null;
        Date endTime = null;
        try {
            //格式化当前时间格式
            nowTime = df.parse(df.format(new Date()));
            //定义开始时间
            beginTime = df.parse(StartDate);
            //定义结束时间
            endTime = df.parse(EndDate);

        } catch (Exception e) {
            e.printStackTrace();

        }
        //调用判断方法
        boolean flag = belongCalendar(nowTime, beginTime, endTime);
        //输出为结果
        if (flag) {
            //处于规定的时间段内，执行对应的逻辑代码
            return true;
        } else {
            //不处于规定的时间段内，执行对应的逻辑代码
            return false;
        }

    }
    /**
     *      * 判断时间是否在时间段内
     *      * @param nowTime
     *      * @param beginTime
     *      * @param endTime
     *      * @return
     *      
     */
    public static boolean belongCalendar(Date nowTime, Date beginTime, Date endTime) {
        //设置当前时间
        Calendar date = Calendar.getInstance();
        date.setTime(nowTime);
        //设置开始时间
        Calendar begin = Calendar.getInstance();
        begin.setTime(beginTime);
        //设置结束时间
        Calendar end = Calendar.getInstance();
        end.setTime(endTime);
        //处于开始时间之后，和结束时间之前的判断
        if (date.after(begin) && date.before(end)) {
            return true;
        } else {

            return false;
        }
    }
}
