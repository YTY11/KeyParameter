package com.fox.alarmsys.entity;

import lombok.Data;

import java.util.List;

/**
 * @author
 * @create 2020-09-18 17:31
 */

@Data
public class AlarmModelingtoolData {

    private String  StationName;

    private Integer TTLSum;

    private Integer HealthSum;

    private Double  HealthRate;

    private Integer UnusualSum;

    private List<AlarmModelEntity> StationNoList;


}
