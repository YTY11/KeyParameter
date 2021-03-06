package com.fox.avisys.entity;

/**
 * @author
 * @create 2020-09-05 6:41
 */
public class AVIDATETIME {
    private String aviTime;
    private String aviTomorrowDate;
    private String aviYesterdayDate;
    private String time;
    private String aviEndDate;
    private String aviStartDate;
    private String avistarttime;
    private String aviendtime;

    public AVIDATETIME(String aviTime, String aviTomorrowDate, String aviYesterdayDate, String time, String aviEndDate, String aviStartDate, String AVISTARTTIME, String AVIENDTIME) {
        this.aviTime = aviTime;
        this.aviTomorrowDate = aviTomorrowDate;
        this.aviYesterdayDate = aviYesterdayDate;
        this.time = time;
        this.aviEndDate = aviEndDate;
        this.aviStartDate = aviStartDate;
        avistarttime = AVISTARTTIME;
        aviendtime = AVIENDTIME;
    }
}
