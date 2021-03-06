package com.fox.alarmsys.entity;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author
 * @create 2020-10-19 18:37
 */
@Data
public class ZoneTarget {

    @NotNull
    private Integer id;
    @NotNull
    private Integer warningMax;
    @NotNull
    private Integer warningMin;
    @NotNull
    private Integer preWarningMax;
    @NotNull
    private Integer preWarningMin;
    @NotNull
    private Integer flag;

    public ZoneTarget(Integer id, Integer warningMax, Integer warningMin, Integer preWarningMax, Integer preWarningMin, Integer flag) {
        this.id = id;
        this.warningMax = warningMax;
        this.warningMin = warningMin;
        this.preWarningMax = preWarningMax;
        this.preWarningMin = preWarningMin;
        this.flag = flag;
    }
}
