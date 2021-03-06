package com.fox.alarmsys.entity;

import lombok.Data;

/**
 * @author
 * @create 2020-08-13 10:44
 */
@Data
public class AlarmSystemTitle {


    private String MachineExType;//機故類型

    private Integer MachineExALL;//機故ALL數量

    private Integer MachineExHNum;//機故H數量

    private Integer MachineExMNum;//機故M數量

    private Integer MachineExLNum;//機故L數量

    private Integer WaitDispatchNum;// 代派工數量

    private Integer UnderRepairNum;// 修理中數量

    private Integer RepairCompleteNum;// 修理完成數量

    private String  ExecptionType;

    private Integer ExecptionNum;

    private String  startDate;

    private String  endDate;

    private Integer  type;

    private Integer floorNumber;

    private Integer unFloorNumber;

}

