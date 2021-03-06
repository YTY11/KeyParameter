package com.fox.testsys.utility;

import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author
 * @create 2019-11-06 11:26
 */
@Component
@Configuration
public class OutTimeH {


    public List<String> findDates(String dBegin, String dEnd) throws ParseException {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Calendar calBegin = Calendar.getInstance();
        //開始時間
        Calendar calStart = Calendar.getInstance();
        Date parse = format.parse(dBegin);
        calStart.setTime(parse);
        calStart.add(Calendar.HOUR, -1);
        Date StartdBegin = calStart.getTime();
        calBegin.setTime(StartdBegin);

        Calendar calEnd = Calendar.getInstance();
        //結束時間
        Calendar caldEnd = Calendar.getInstance();
        Date dEndparse = format.parse(dEnd);
        calStart.setTime(dEndparse);
        calStart.add(Calendar.HOUR, 1);
        Date dEndDate = caldEnd.getTime();
        calEnd.setTime(dEndDate);

        List<String> DateList = new ArrayList<>();

        while (format.parse(dEnd).after(calBegin.getTime())) {
            calBegin.add(Calendar.HOUR, 1);
            DateList.add(format.format(calBegin.getTime()));
        }
        List<String> TimeDateList = new ArrayList<>();

        for (String s : DateList) {
            String substring = s.substring(11, 13);
            TimeDateList.add(substring);
        }
        return TimeDateList;
    }


    public List<String> TimeHList(String dBegin, String dEnd) throws ParseException {
        DateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Calendar calBegin = Calendar.getInstance();
        //開始時間
        Calendar calStart = Calendar.getInstance();
        Date parse = format.parse(dBegin);
        calStart.setTime(parse);
        calStart.add(Calendar.HOUR, -1);
        Date StartdBegin = calStart.getTime();
        calBegin.setTime(StartdBegin);

        Calendar calEnd = Calendar.getInstance();
        //結束時間
        Calendar caldEnd = Calendar.getInstance();
        Date dEndparse = format.parse(dEnd);
        calStart.setTime(dEndparse);
        calStart.add(Calendar.HOUR, -1);
        Date dEndDate = caldEnd.getTime();
        calEnd.setTime(dEndDate);

        List<String> DateList = new ArrayList<>();

        while (format.parse(dEnd).after(calBegin.getTime())) {
            calBegin.add(Calendar.HOUR, 1);
            DateList.add(format.format(calBegin.getTime()));
        }
        List<String> TimeDateList = new ArrayList<>();

        for (String s : DateList) {
            String substring = s.substring(11, 13);
            TimeDateList.add(substring);
        }
        return TimeDateList;
    }

    public List<String> DayList(String dBegin, String dEnd) throws ParseException {
        DateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Calendar calBegin = Calendar.getInstance();
        //開始時間
        Calendar calStart = Calendar.getInstance();
        Date parse = format.parse(dBegin);
        calStart.setTime(parse);
        calStart.add(Calendar.HOUR, -1);
        Date StartdBegin = calStart.getTime();
        calBegin.setTime(StartdBegin);

        Calendar calEnd = Calendar.getInstance();
        //結束時間
        Calendar caldEnd = Calendar.getInstance();
        Date dEndparse = format.parse(dEnd);
        calStart.setTime(dEndparse);
        calStart.add(Calendar.HOUR, -1);
        Date dEndDate = caldEnd.getTime();
        calEnd.setTime(dEndDate);

        List<String> DateList = new ArrayList<>();

        while (format.parse(dEnd).after(calBegin.getTime())) {
            calBegin.add(Calendar.HOUR, 1);
            DateList.add(format.format(calBegin.getTime()));
        }
        List<String> TimeDateList = new ArrayList<>();

        for (String s : DateList) {
            String substring = s.substring(11, 13);
            TimeDateList.add(substring);
        }
        return TimeDateList;
    }


    public String MinusDay(Date date, Integer DaySum) {
        Calendar calBegin = Calendar.getInstance();
        //開始時間
        Calendar calStart = Calendar.getInstance();
        calStart.setTime(date);
        calStart.add(Calendar.DATE, DaySum);
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String Dateformat = format.format(calStart.getTime());


        return Dateformat;
    }


    public String AddDates(Date Date, Integer AddNum, Integer AddMinuteNum) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Calendar calDate = Calendar.getInstance();
        calDate.setTime(Date);
        calDate.add(Calendar.HOUR, AddNum);
        calDate.add(Calendar.MINUTE, AddMinuteNum);
        Date StartdBegin = calDate.getTime();
        calDate.setTime(StartdBegin);
        String format1 = format.format(calDate.getTime());
        System.out.println(format1);


        return format1;
    }


}
