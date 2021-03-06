package com.fox.testsys.service;


import com.fox.testsys.entity.*;
import com.fox.testsys.mapper.AUTOFLOOR_Robot_EfficiencyMapper;
import com.fox.testsys.mapper.AutoFloor_TargetMapper;
import com.fox.testsys.mapper.AutoFloor_UPHMapper;
import com.fox.testsys.utility.OutTimeH;
import com.fox.testsys.utility.TaskService;
import com.fox.testsys.utility.TimeUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;

/**
 * @author
 * @create 2020-05-12 10:53
 */
@Service
public class TestCapacityMessageService {
    //机种名称
    private static final String LOT_PRODUCT = "Lotus";
    private static final String MAC_PRODUCT = "Macan";
    private static final String PHA_PRODUCT = "Pha";
    //班别
    private static final String TwoTimeType = "two";
    private static final String DayTimeType = "Day";
    private static final String NightTimeType = "Night";
    private static final String YestrerDayTimeType = "Yesterday";

    @Autowired
    AUTOFLOOR_Robot_EfficiencyMapper autofloor_robot_efficiencyMapper;
    @Autowired
    TimeUtility timeUtility;
    @Autowired
    TaskService taskService;
    @Autowired
    AutoFloor_TargetMapper autoFloor_targetMapper;
    @Autowired
    AutoFloor_UPHMapper autoFloor_uphMapper;
    @Autowired
    TestAProductService testAProductService;
    @Autowired
    OutTimeH outTimeH;

    public AutoFloor_Target AloneLineData(String LineName, Date schedule, String TimeType) {
        AutoFloor_Target LineData = new AutoFloor_Target();
        List<java.sql.Date> sqlList = taskService.Schedule();
        /*当前日期*/
        if (TwoTimeType.equals(TimeType)) {
            if (timeUtility.Timequantum("08:30", "20:29")) {
                /* 白班 线体数据*/
                LineData = autoFloor_targetMapper.AloneLine(DayTimeType, LineName, schedule, schedule);
            } else {
                /* 夜班 线体数据*/
                LineData = autoFloor_targetMapper.AloneLine(NightTimeType, LineName, schedule, schedule);
            }
        } else if (DayTimeType.equals(TimeType)) {
            if (timeUtility.Timequantum("08:30", "20:29")) {
                /* 白班 线体数据*/
                LineData = autoFloor_targetMapper.AloneLine(DayTimeType, LineName, schedule, schedule);
            } else {
                /* 夜班 线体数据*/
                LineData = autoFloor_targetMapper.AloneLine(NightTimeType, LineName, schedule, schedule);
            }
        } else if (NightTimeType.equals(TimeType)) {
            LineData = autoFloor_targetMapper.AloneLine(NightTimeType, LineName, schedule, schedule);
        } else if (YestrerDayTimeType.equals(TimeType)) {
            Date enddate = sqlList.get(1);
            LineData = autoFloor_targetMapper.AloneLine("ALL", LineName, schedule, enddate);
        }
        return LineData;
    }

    public Map<String, Object> CapacityAutoData(String LineName, String TypeTime, String StartDate, String EndDate, String Productname, Integer HTarget, Integer actionMinuteD) {
        Map<String, Object> CapacityAutoMap = new HashMap<>();
        String ProductnameCCT = "CCT_COMBO";
        String ProductnameWIFIBT = "WIFI/BT_W1";
        String ProductnameWIFIBT2 = "WIFI/BT_W2";
        String ProductnameUWB="UWB";
        if (Productname.equals("Pha")) {
            ProductnameCCT = "CCT";
            ProductnameWIFIBT = "WIFI/BT";
            ProductnameWIFIBT2 = "WIFI/BT2";
        } else if (Productname.equals("Macan")) {
            ProductnameCCT = "CCT";
            ProductnameWIFIBT = "WIFI/BT";
            ProductnameWIFIBT2 = "WIFI/BT2";
        } else if (Productname.equals("Raptor")) {
            ProductnameCCT = "CCT";
            ProductnameWIFIBT = "WIFI/BT_W1";
            ProductnameWIFIBT2 = "WIFI/BT_W2";
            ProductnameUWB="ACOND";
        }   else if (Productname.equals("Apollo")) {
            ProductnameCCT = "CCT";
            ProductnameUWB="ACOND";
        }

        Integer Pre_DFUsum = 0;
        Integer DFUsum = 0;
        Integer FCTsum = 0;
        Integer S1sum = 0;
        Integer S2sum = 0;
        Integer S3sum = 0;
        Integer S4sum = 0;
        Integer CCTsum = 0;
        Integer W1sum = 0;
        Integer W2sum = 0;
        Integer UWBsum = 0;
        Integer SCONDsum = 0;
        Integer WIFIASSOsum = 0;
        Integer MARSCONDsum = 0;

        SumNumber TTLCapacityAutoData = new SumNumber();
        List<AutoFloor_UPH> autoFloor_uphs = autoFloor_uphMapper.StationOut(LineName, StartDate, EndDate);
        List<SumNumber> CapacityAutoDataList = new ArrayList<>();
        List<String> TIME_SLOTList = new ArrayList<>();

        if (TwoTimeType.equals(TypeTime)){
            if (timeUtility.Timequantum("08:30", "20:29")) {
                TIME_SLOTList = DayTIME_SLOT();
            } else {
                TIME_SLOTList = NightTIME_SLOT();
            }
        }else if ( DayTimeType.equals(TypeTime)) {
            TIME_SLOTList = DayTIME_SLOT();
        } else if (NightTimeType.equals(TypeTime)) {
            TIME_SLOTList = NightTIME_SLOT();
        } else if (YestrerDayTimeType.equals(TypeTime)) {
            TIME_SLOTList.addAll(DayTIME_SLOT());
            TIME_SLOTList.addAll(NightTIME_SLOT());
        }
        for (String TIME_SLOT : TIME_SLOTList) {
            SumNumber apacityAutoUPH = new SumNumber();
            apacityAutoUPH.setTime_Slot(TIME_SLOT);
            Integer PreDFU = 0;
            Integer DFU = 0;
            Integer FCT = 0;
            Integer S1 = 0;
            Integer S2 = 0;
            Integer S3 = 0;
            Integer S4 = 0;
            Integer CCT = 0;
            Integer W1 = 0;
            Integer W2 = 0;
            Integer UWB = 0;
            Integer SCOND = 0;
            Integer WIFIASSO = 0;
            Integer MARSCOND = 0;
            for (AutoFloor_UPH autoFloor_uph : autoFloor_uphs) {
                //时间段
                String uphtime_slot = autoFloor_uph.getTime_slot();
                //UPH
                int uph = 0;
                if (autoFloor_uph.getUph() != null) {
                    uph = autoFloor_uph.getUph().intValue();
                }
                //工站名
                String station_name = autoFloor_uph.getStation_name();
                if (TIME_SLOT.equals(uphtime_slot)) {
                    if (station_name.equals("PRE_DFU_SOC")) {
                        Pre_DFUsum += uph;
                        PreDFU = uph;
                    }else if (station_name.equals("DFU")) {
                        DFUsum += uph;
                        DFU = uph;
                    } else if (station_name.equals("FCT")) {
                        FCTsum += uph;
                        FCT = uph;
                    } else if (station_name.equals("CELL_S1")) {
                        S1sum += uph;
                        S1 = uph;
                    } else if (station_name.equals("CELL_S2")) {
                        S2sum += uph;
                        S2 = uph;
                    } else if (station_name.equals("CELL_S3")) {
                        S3sum += uph;
                        S3 = uph;
                    }else if  (station_name.equals("CELL_S4")) {
                        S4sum += uph;
                        S4 = uph;
                    } else if (station_name.equals(ProductnameCCT)) {
                        CCTsum += uph;
                        CCT = uph;
                    } else if (station_name.equals(ProductnameWIFIBT)) {
                        W1sum += uph;
                        W1 = uph;
                    } else if (station_name.equals(ProductnameWIFIBT2)) {
                        W2sum += uph;
                        W2 = uph;
                    } else if (station_name.equals("SCOND")) {
                        SCONDsum += uph;
                        SCOND = uph;
                    } else if (station_name.equals(ProductnameUWB)) {
                        UWBsum += uph;
                        UWB = uph;
                    } else if (station_name.equals("WIFI_ASSOC")) {
                        WIFIASSOsum += uph;
                        WIFIASSO = uph;
                    } else if (station_name.equals("MARS_COND")) {
                        MARSCONDsum += uph;
                        MARSCOND = uph;
                    }
                } else {
                    apacityAutoUPH.setDTarget(HTarget);
                }
            }
            apacityAutoUPH.setPre_DFUamount(PreDFU);
            apacityAutoUPH.setDFUamount(DFU);
            apacityAutoUPH.setFCTamount(FCT);
            apacityAutoUPH.setS1amount(S1);
            apacityAutoUPH.setS2amount(S2);
            apacityAutoUPH.setS3amount(S3);
            apacityAutoUPH.setS4amount(S4);
            apacityAutoUPH.setCCTamount(CCT);
            apacityAutoUPH.setW1amount(W1);
            apacityAutoUPH.setW2amount(W2);
            apacityAutoUPH.setSCONDamount(SCOND);
            apacityAutoUPH.setUWBamount(UWB);
            apacityAutoUPH.setWIFIASSOamount(WIFIASSO);
            apacityAutoUPH.setMARSCONDamount(MARSCOND);
            CapacityAutoDataList.add(apacityAutoUPH);
        }
        TTLCapacityAutoData.setPre_DFUsum(Pre_DFUsum);
        TTLCapacityAutoData.setDFUsum(DFUsum);
        TTLCapacityAutoData.setFCTsum(FCTsum);
        TTLCapacityAutoData.setS1sum(S1sum);
        TTLCapacityAutoData.setS2sum(S2sum);
        TTLCapacityAutoData.setS3sum(S3sum);
        TTLCapacityAutoData.setS4sum(S4sum);
        TTLCapacityAutoData.setCCTsum(CCTsum);
        TTLCapacityAutoData.setW1sum(W1sum);
        TTLCapacityAutoData.setW2sum(W2sum);
        TTLCapacityAutoData.setUWBsum(UWBsum);
        TTLCapacityAutoData.setSCONDsum(SCONDsum);
        TTLCapacityAutoData.setWIFIASSOsum(WIFIASSOsum);
        TTLCapacityAutoData.setMARSCONDsum(MARSCONDsum);
        CapacityAutoMap.put("TTLCapacityAutoData", TTLCapacityAutoData);
        CapacityAutoMap.put("CapacityAutoDataList", CapacityAutoDataList);
        return CapacityAutoMap;
    }

    private List<String> DayTIME_SLOT() {
        List<String> DayTime_Slot = new ArrayList<>();
        DayTime_Slot.add("8:30~9:30");
        DayTime_Slot.add("9:30~10:30");
        DayTime_Slot.add("10:30~11:30");
        DayTime_Slot.add("11:30~12:30");
        DayTime_Slot.add("12:30~13:30");
        DayTime_Slot.add("13:30~14:30");
        DayTime_Slot.add("14:30~15:30");
        DayTime_Slot.add("15:30~16:30");
        DayTime_Slot.add("16:30~17:30");
        DayTime_Slot.add("17:30~18:30");
        DayTime_Slot.add("18:30~19:30");
        DayTime_Slot.add("19:30~20:30");
        return DayTime_Slot;
    }

    private List<String> NightTIME_SLOT() {
        List<String> NightTime_Slot = new ArrayList<>();
        NightTime_Slot.add("20:30~21:30");
        NightTime_Slot.add("21:30~22:30");
        NightTime_Slot.add("22:30~23:30");
        NightTime_Slot.add("23:30~00:30");
        NightTime_Slot.add("0:30~1:30");
        NightTime_Slot.add("1:30~2:30");
        NightTime_Slot.add("2:30~3:30");
        NightTime_Slot.add("3:30~4:30");
        NightTime_Slot.add("4:30~5:30");
        NightTime_Slot.add("5:30~6:30");
        NightTime_Slot.add("6:30~7:30");
        NightTime_Slot.add("7:30~8:30");
        return NightTime_Slot;
    }

    private List<String> AutoStationList(String Productname) {
        String ProductnameCCT = "CCT_COMBO";
        String ProductnameWIFIBT = "WIFI/BT_W1";
        String ProductnameWIFIBT2 = "WIFI/BT_W2";
        String ProductnameUWB= "UWB";
        if (Productname.equals("Pha")) {
            ProductnameCCT = "CCT";
            ProductnameWIFIBT = "WIFI/BT";
            ProductnameWIFIBT2 = "WIFI/BT2";
        } else if (Productname.equals("Macan")) {
            ProductnameCCT = "CCT";
            ProductnameWIFIBT = "WIFI/BT";
            ProductnameWIFIBT2 = "WIFI/BT2";
        }else if (Productname.equals("Apollo")) {
            ProductnameCCT = "CCT";
            ProductnameUWB="ACOND";
        }
        List<String> AutoStation = new ArrayList<>();
        AutoStation.add("PRE_DFU_SOC");
        AutoStation.add("DFU");
        AutoStation.add("FCT");
        AutoStation.add("CELL_S1");
        AutoStation.add("CELL_S2");
        AutoStation.add("CELL_S3");
        AutoStation.add(ProductnameCCT);
        AutoStation.add(ProductnameWIFIBT);
        AutoStation.add(ProductnameWIFIBT2);
        AutoStation.add("SCOND");
        AutoStation.add(ProductnameUWB);
        AutoStation.add("WIFI_ASSOC");
        AutoStation.add("MARS_COND");
        return AutoStation;
    }

    public List<SumNumber> capacityAutoDataTarget(List<SumNumber> capacityAutoData, String TypeTime, Integer HTarget, Double WorkingHours) {
        for (SumNumber capacityAutoDatum : capacityAutoData) {
            capacityAutoDatum.setDTarget(HTarget);
            String time_slot = capacityAutoDatum.getTime_Slot();
            Integer dTarget = capacityAutoDatum.getDTarget();
            if (TwoTimeType.equals(TypeTime) || DayTimeType.equals(TypeTime)) {
                if (time_slot.equals("8:30~9:30")) {
                    int hTarget08 = dTarget / 2;
                    capacityAutoDatum.setDTarget(hTarget08);
                }
                if (time_slot.equals("11:30~12:30")) {
                    capacityAutoDatum.setDTarget(0);
                }
                if (WorkingHours == 10.5) {
                } else if (WorkingHours == 9.5) {
                    if (time_slot.equals("19:30~20:30")) {
                        capacityAutoDatum.setDTarget(0);
                    }
                } else if (WorkingHours == 8.67) {
                    if (time_slot.equals("18:30~19:30")) {
                        capacityAutoDatum.setDTarget(0);
                    }
                    if (time_slot.equals("19:30~20:30")) {
                        capacityAutoDatum.setDTarget(0);
                    }

                } else if (WorkingHours == 7.67) {
                    if (time_slot.equals("17:30~18:30")) {
                        capacityAutoDatum.setDTarget(0);
                    }
                    if (time_slot.equals("18:30~19:30")) {
                        capacityAutoDatum.setDTarget(0);
                    }
                    if (time_slot.equals("19:30~20:30")) {
                        capacityAutoDatum.setDTarget(0);
                    }
                }
            } else if (NightTimeType.equals(TypeTime)) {
                if (time_slot.equals("20:30~21:30")) {
                    int hTarget08 = dTarget / 2;
                    capacityAutoDatum.setDTarget(hTarget08);
                }
                if (time_slot.equals("23:30~00:30")) {
                    capacityAutoDatum.setDTarget(0);
                }

                if (WorkingHours == 10.5) {
                } else if (WorkingHours == 9.5) {
                    if (time_slot.equals("07:30~08:30")) {
                        capacityAutoDatum.setDTarget(0);
                    }
                } else if (WorkingHours == 8.67) {
                    if (time_slot.equals("06:30~07:30")) {
                        capacityAutoDatum.setDTarget(0);
                    }
                    if (time_slot.equals("07:30~08:30")) {
                        capacityAutoDatum.setDTarget(0);
                    }

                } else if (WorkingHours == 7.67) {
                    if (time_slot.equals("05:30~06:30")) {
                        capacityAutoDatum.setDTarget(0);
                    }
                    if (time_slot.equals("06:30~07:30")) {
                        capacityAutoDatum.setDTarget(0);
                    }
                    if (time_slot.equals("07:30~08:30")) {
                        capacityAutoDatum.setDTarget(0);
                    }
                }
            } else if (YestrerDayTimeType.equals(TypeTime)) {
                dTarget /= 2;
                capacityAutoDatum.setDTarget(dTarget);
                if (time_slot.equals("8:30~9:30")) {
                    int hTarget08 = dTarget / 2;
                    capacityAutoDatum.setDTarget(hTarget08);
                }
                if (time_slot.equals("11:30~12:30")) {
                    capacityAutoDatum.setDTarget(0);
                }
                if (time_slot.equals("20:30~21:30")) {
                    int hTarget08 = dTarget / 2;
                    capacityAutoDatum.setDTarget(hTarget08);

                }
                if (time_slot.equals("23:30~00:30")) {
                    capacityAutoDatum.setDTarget(0);
                }

            }
        }
        return capacityAutoData;
    }

    public Double ActionTarget(Integer DayTargetInteger, String TypeTime, Double workingHourD, Integer HourTarget, Integer actionMinuteD, Integer FloatNum) {
        double TarGetSumdouble = 0;
        double ActualData = 0;
        double HourTargetD = HourTarget.doubleValue();
        if (TwoTimeType.equals(TypeTime) || DayTimeType.equals(TypeTime)) {
            Map<String, Object> DayActionMap = testAProductService.DayActionTarget(workingHourD, HourTargetD, actionMinuteD, FloatNum);
            Double DayActionTarget = (Double) DayActionMap.get("TarGetSumdouble");
            FloatNum = (Integer) DayActionMap.get("FloatNum");
            if (DayActionTarget != null) {
                TarGetSumdouble += DayActionTarget;
                ActualData = DayActionTarget;
            }
        } else if (NightTimeType.equals(TypeTime)) {
            Map<String, Object> NightActionMap = testAProductService.NightActionTarget(workingHourD, HourTargetD, actionMinuteD, FloatNum);
            Double NightActionTarget = (Double) NightActionMap.get("TarGetSumdouble");
            FloatNum = (Integer) NightActionMap.get("FloatNum");
            if (NightActionTarget != null) {
                TarGetSumdouble += NightActionTarget;
                ActualData = NightActionTarget;
            }
        } else if (YestrerDayTimeType.equals(TypeTime)) {
            FloatNum = 0;
            TarGetSumdouble += DayTargetInteger;
            ActualData = DayTargetInteger;
        }
        return ActualData;
    }

    public List<String> machineList(String Productname) {
        String ProductnameCCT = "CCT_COMBO";
        String ProductnameWIFIBT = "WIFI/BT_W1";
        String ProductnameWIFIBT2 = "WIFI/BT_W2";
        String ProductnameUWB ="UWB";
        if (Productname.equals("Pha")) {
            ProductnameCCT = "CCT";
            ProductnameWIFIBT = "WIFI/BT";
            ProductnameWIFIBT2 = "WIFI/BT2";
        } else if (Productname.equals("Macan")) {
            ProductnameCCT = "CCT";
            ProductnameWIFIBT = "WIFI/BT";
            ProductnameWIFIBT2 = "WIFI/BT2";
        }else if (Productname.equals("Apollo")) {
            ProductnameCCT = "CCT";
            ProductnameUWB="ACOND";
        }
        List<String> machineList = new ArrayList<>();

        machineList.add("PRE_DFU_SOC");
        machineList.add("DFU");
        machineList.add("FCT");
        machineList.add("CELL_S1");
        machineList.add("CELL_S2");
        machineList.add("CELL_S3");
        machineList.add("CELL_S4");
        machineList.add(ProductnameCCT);
        machineList.add(ProductnameWIFIBT);
        machineList.add(ProductnameWIFIBT2);
        machineList.add(ProductnameUWB);
        machineList.add("SCOND");
        machineList.add("WIFI_ASSOC");
        machineList.add("MARS_COND");
        return machineList;
    }

    public List<AutoFloor_UPH> MachineData(SumNumber ttlCapacityAutoData, String FloorName, String LineName, String Product, String StartDate, String EndDate, Integer HTarget) {
        List<AutoFloor_UPH> workstationamount = autoFloor_uphMapper.Workstationamount(FloorName, LineName);
        List<AutoFloor_UPH> MachinaeActionSum = autoFloor_uphMapper.MachinaeActionSum(FloorName, LineName, StartDate, EndDate);
        List<AmountsUPH> amountsUPHS = MachineNoList(ttlCapacityAutoData, Product);
        List<AutoFloor_UPH> MachineDataList = new ArrayList<>();
        List<String> machineList = machineList(Product);
        for (String machine : machineList) {
            AutoFloor_UPH MachineData = new AutoFloor_UPH();
            MachineData.setStation_name(machine);
            if (machine.equals("CELL_S1")) {
                MachineData.setStation_name("S1");
            } else if (machine.equals("CELL_S2")) {
                MachineData.setStation_name("S2");
            } else if (machine.equals("CELL_S3")) {
                MachineData.setStation_name("S3");
            } else if (machine.equals("CELL_S4")) {
                MachineData.setStation_name("S4");
            } else if (machine.equals("CCT_COMBO")) {
                MachineData.setStation_name("CCT");
            } else if (machine.equals("WIFI/BT")) {
                MachineData.setStation_name("W1");
            } else if (machine.equals("WIFI/BT2")) {
                MachineData.setStation_name("W2");
            } else if (machine.equals("WIFI/BT_W1")) {
                MachineData.setStation_name("W1");
            } else if (machine.equals("WIFI/BT_W2")) {
                MachineData.setStation_name("W2");
            }else if (machine.equals("PRE_DFU_SOC")) {
                MachineData.setStation_name("Pre_DFU");
            }else if (machine.equals("ACOND")) {
                MachineData.setStation_name("UWB");
            }
            for (AutoFloor_UPH autoFloor_uph : workstationamount) {
                String station_name = autoFloor_uph.getStation_name();
                String workstation = machine;
                if (station_name!=null){
                    if (station_name.equals(workstation)) {
                        Integer stationSum = autoFloor_uph.getStationSum();
                        int HStationTarget = 0;
                        if (stationSum != null && stationSum != 0) {
                            HStationTarget = HTarget / stationSum;
                            MachineData.setStationActionTarget(HStationTarget);
                        }
                        MachineData.setStationSum(stationSum);
                    }
                }
            }
            for (AutoFloor_UPH MachinaeAction : MachinaeActionSum) {
                String station_name = MachinaeAction.getStation_name();
                if (station_name.equals(machine)) {
                    Integer stationSum = MachinaeAction.getStationSum();
                    MachineData.setStationActionSum(stationSum);
                }
            }
            for (AmountsUPH amountsUPH : amountsUPHS) {
                String station_name = amountsUPH.getStation_Name();
                if (machine.equals(station_name)) {
                    MachineDataList.add(MachineData);
                }
            }
        }

        return MachineDataList;
    }

    public List<AmountsUPH> MachineNoList(SumNumber ttlCapacityAutoData, String Product) {
        List<AmountsUPH> MachineList = new ArrayList<>();

        String ProductnameCCT = "CCT_COMBO";
        String ProductnameWIFIBT = "WIFI/BT_W1";
        String ProductnameWIFIBT2 = "WIFI/BT_W2";
        String ProductnameUWB="UWB";
        if (Product.equals("Pha")) {
            ProductnameCCT = "CCT";
            ProductnameWIFIBT = "WIFI/BT";
            ProductnameWIFIBT2 = "WIFI/BT2";
        } else if (Product.equals("Macan")) {
            ProductnameCCT = "CCT";
            ProductnameWIFIBT = "WIFI/BT";
            ProductnameWIFIBT2 = "WIFI/BT2";
        }else if (Product.equals("Apollo")) {
            ProductnameCCT = "CCT";
            ProductnameUWB="ACOND";
        }
        /*工站总数*/
        Integer Pre_DFUSum = 0;
        Integer DFUSum = 0;
        Integer FCTSum = 0;
        Integer S1Sum = 0;
        Integer S2Sum = 0;
        Integer S3Sum = 0;
        Integer S4Sum = 0;
        Integer CCTSum = 0;
        Integer W1Sum = 0;
        Integer W2Sum = 0;
        Integer UWBSum = 0;
        Integer SCONDSum = 0;
        Integer WIFIASSOSum = 0;
        Integer MARSCONDSum = 0;
        Pre_DFUSum=ttlCapacityAutoData.getPre_DFUsum();
        DFUSum = ttlCapacityAutoData.getDFUsum();
        FCTSum = ttlCapacityAutoData.getFCTsum();
        S1Sum = ttlCapacityAutoData.getS1sum();
        S2Sum = ttlCapacityAutoData.getS2sum();
        S3Sum = ttlCapacityAutoData.getS3sum();
        S4Sum = ttlCapacityAutoData.getS4sum();
        CCTSum = ttlCapacityAutoData.getCCTsum();
        W1Sum = ttlCapacityAutoData.getW1sum();
        W2Sum = ttlCapacityAutoData.getW2sum();
        UWBSum = ttlCapacityAutoData.getUWBsum();
        SCONDSum = ttlCapacityAutoData.getSCONDsum();
        WIFIASSOSum = ttlCapacityAutoData.getWIFIASSOsum();
        MARSCONDSum = ttlCapacityAutoData.getMARSCONDsum();

        Integer ActionDTarget = ttlCapacityAutoData.getCell_A();
        if (ActionDTarget > Pre_DFUSum) {
            AmountsUPH Machine = new AmountsUPH();
            Machine.setStation_Name("PRE_DFU_SOC");
            Machine.setStation_Name_No("Pre_DFU");
            MachineList.add(Machine);
        }
        if (ActionDTarget > DFUSum) {
            AmountsUPH Machine = new AmountsUPH();
            Machine.setStation_Name("DFU");
            Machine.setStation_Name_No("DFU");
            MachineList.add(Machine);
        }
        if (ActionDTarget > FCTSum) {
            AmountsUPH Machine = new AmountsUPH();
            Machine.setStation_Name("FCT");
            Machine.setStation_Name_No("FCT");
            MachineList.add(Machine);
        }
        if (ActionDTarget > S1Sum) {
            AmountsUPH Machine = new AmountsUPH();
            Machine.setStation_Name("CELL_S1");
            Machine.setStation_Name_No("S1");
            MachineList.add(Machine);
        }
        if (ActionDTarget > S2Sum) {
            AmountsUPH Machine = new AmountsUPH();
            Machine.setStation_Name("CELL_S2");
            Machine.setStation_Name_No("S2");
            MachineList.add(Machine);
        }
        if (ActionDTarget > S3Sum) {
            AmountsUPH Machine = new AmountsUPH();
            Machine.setStation_Name("CELL_S3");
            Machine.setStation_Name_No("S3");
            MachineList.add(Machine);
        }
        if (ActionDTarget > S4Sum) {
            AmountsUPH Machine = new AmountsUPH();
            Machine.setStation_Name("CELL_S4");
            Machine.setStation_Name_No("S4");
            MachineList.add(Machine);
        }
        if (ActionDTarget > CCTSum) {
            AmountsUPH Machine = new AmountsUPH();
            Machine.setStation_Name(ProductnameCCT);
            Machine.setStation_Name_No("CCT");
            MachineList.add(Machine);
        }
        if (ActionDTarget > W1Sum) {
            AmountsUPH Machine = new AmountsUPH();
            Machine.setStation_Name(ProductnameWIFIBT);
            Machine.setStation_Name_No("W1");
            MachineList.add(Machine);
        }
        if (ActionDTarget > W2Sum) {
            AmountsUPH Machine = new AmountsUPH();
            Machine.setStation_Name(ProductnameWIFIBT2);
            Machine.setStation_Name_No("W2");
            MachineList.add(Machine);
        }
        if (ActionDTarget > UWBSum) {
            AmountsUPH Machine = new AmountsUPH();
            Machine.setStation_Name(ProductnameUWB);
            Machine.setStation_Name_No("UWB");
            MachineList.add(Machine);
        }
        if (ActionDTarget > SCONDSum) {
            AmountsUPH Machine = new AmountsUPH();
            Machine.setStation_Name("SCOND");
            Machine.setStation_Name_No("SCOND");
            MachineList.add(Machine);
        }
        if (ActionDTarget > WIFIASSOSum) {
            AmountsUPH Machine = new AmountsUPH();
            Machine.setStation_Name("WIFI_ASSOC");
            Machine.setStation_Name_No("WIFIASSO");
            MachineList.add(Machine);
        }
        if (ActionDTarget > MARSCONDSum) {
            AmountsUPH Machine = new AmountsUPH();
            Machine.setStation_Name("MARS_COND");
            Machine.setStation_Name_No("MARSCOND");
            MachineList.add(Machine);
        }
        return MachineList;
    }

    public Map<String, List<AmountsUPH>> MachineDataSub(SumNumber ttlCapacityAutoData, String Product, String StartDate, String EndDate, String Linename) {
        List<AmountsUPH> MachineNoList = MachineNoList(ttlCapacityAutoData, Product);
        //工站UPH
        List<AmountsUPH> MachinaeUPHList = autoFloor_uphMapper.MachinaeUPH(StartDate, EndDate, Linename);
        //没有生产的机台
        List<AmountsUPH> MachinaeDetailList = autoFloor_uphMapper.MachinaeDetail(StartDate, EndDate, Linename);
        //机台的良率详细信息
        List<AmountsUPH> MachinaeUPHFPYList = autoFloor_uphMapper.MachinaeUPHFPY(StartDate, EndDate, Linename);
        Map<String, List<AmountsUPH>> MachineDetailMap = new HashMap<>();
        for (AmountsUPH Machine : MachineNoList) {
            //工站名称
            String station_name = Machine.getStation_Name();
            //工站昵称
            String station_name_no = Machine.getStation_Name_No();
            List<AmountsUPH> MachineSub = new ArrayList<>();

            if (station_name_no.equals("Pre_DFU")) {
                MachineSub = MachineSubList(MachinaeUPHList, MachinaeDetailList, MachinaeUPHFPYList, station_name);
            }else if (station_name_no.equals("DFU")) {
                MachineSub = MachineSubList(MachinaeUPHList, MachinaeDetailList, MachinaeUPHFPYList, station_name);
            } else if (station_name_no.equals("FCT")) {
                MachineSub = MachineSubList(MachinaeUPHList, MachinaeDetailList, MachinaeUPHFPYList, station_name);
            } else if (station_name_no.equals("S1")) {
                MachineSub = MachineSubList(MachinaeUPHList, MachinaeDetailList, MachinaeUPHFPYList, station_name);
            } else if (station_name_no.equals("S2")) {
                MachineSub = MachineSubList(MachinaeUPHList, MachinaeDetailList, MachinaeUPHFPYList, station_name);
            } else if (station_name_no.equals("S3")) {
                MachineSub = MachineSubList(MachinaeUPHList, MachinaeDetailList, MachinaeUPHFPYList, station_name);
            } else if (station_name_no.equals("S4")) {
                MachineSub = MachineSubList(MachinaeUPHList, MachinaeDetailList, MachinaeUPHFPYList, station_name);
            }  else if (station_name_no.equals("CCT")) {
                MachineSub = MachineSubList(MachinaeUPHList, MachinaeDetailList, MachinaeUPHFPYList, station_name);
            } else if (station_name_no.equals("W1")) {
                MachineSub = MachineSubList(MachinaeUPHList, MachinaeDetailList, MachinaeUPHFPYList, station_name);
            } else if (station_name_no.equals("W2")) {
                MachineSub = MachineSubList(MachinaeUPHList, MachinaeDetailList, MachinaeUPHFPYList, station_name);
            } else if (station_name_no.equals("UWB")) {
                MachineSub = MachineSubList(MachinaeUPHList, MachinaeDetailList, MachinaeUPHFPYList, station_name);
            } else if (station_name_no.equals("SCOND")) {
                MachineSub = MachineSubList(MachinaeUPHList, MachinaeDetailList, MachinaeUPHFPYList, station_name);
            } else if (station_name_no.equals("WIFIASSO")) {
                MachineSub = MachineSubList(MachinaeUPHList, MachinaeDetailList, MachinaeUPHFPYList, station_name);
            } else if (station_name_no.equals("MARSCOND")) {
                MachineSub = MachineSubList(MachinaeUPHList, MachinaeDetailList, MachinaeUPHFPYList, station_name);
            }
            MachineDetailMap.put(station_name_no, MachineSub);

        }

        return MachineDetailMap;
    }

    public List<AmountsUPH> MachineSubList(List<AmountsUPH> MachinaeUPHList, List<AmountsUPH> MachinaeDetailList, List<AmountsUPH> MachinaeUPHFPYList, String station_name) {
        List<AmountsUPH> amountsUPHList = new ArrayList<>();
        for (AmountsUPH MachinaeDetail : MachinaeDetailList) {
            AmountsUPH MachineSub = new AmountsUPH();
            String Machinaestation_name = MachinaeDetail.getStation_Name();
            String line_name = MachinaeDetail.getLINE_NAME();
            if (station_name.equals(Machinaestation_name)) {
                String machine_no = MachinaeDetail.getMachine_no();
                MachineSub.setLINE_NAME(line_name);
                MachineSub.setMachine_no(machine_no);
                MachineSub.setAmountuph(0);
                amountsUPHList.add(MachineSub);
            }
        }
        for (AmountsUPH amountsUPH : MachinaeUPHList) {
            AmountsUPH MachineSub = new AmountsUPH();
            //工站名称
            String station_name1 = amountsUPH.getStation_Name();
            //线体名称
            String line_name = amountsUPH.getLINE_NAME();
            //机台号
            String machine_no1 = amountsUPH.getMachine_no();
            //FPYTarget
            Double FPYTarget = FPYTarget(station_name);


            if (station_name.equals(station_name1)) {
                Integer amountuph = amountsUPH.getAmountuph();
                MachineSub.setLINE_NAME(line_name);
                MachineSub.setMachine_no(machine_no1);
                MachineSub.setAmountuph(amountuph);
                for (AmountsUPH uph : MachinaeUPHFPYList) {
                    String machine_no = uph.getMachine_no();
                    if (machine_no1.equals(machine_no)) {
                        Integer fail_q = 0;
                        Double fpy = 0.0;
                        Double YIELD = 0.0;
                        Double MISDETET = 0.0;
                        Double Unknown = 0.0;
                        if (uph.getFAIL_Q() != null) {
                            fail_q = uph.getFAIL_Q();
                        }
                        if (uph.getFPY() != null) {
                            Double UPHfpy = uph.getFPY();
                            BigDecimal fpybig = new BigDecimal(UPHfpy * 100);
                            fpy = fpybig.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                        }
                        if (uph.getFPY() != null) {
                            Double UPHYIELD = uph.getYIELD();
                            BigDecimal YIELDbig = new BigDecimal(UPHYIELD * 100);
                            YIELD = YIELDbig.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                        }
                        if (uph.getFPY() != null) {
                            Double UPHMISDETET = uph.getMISDETET();
                            BigDecimal MISDETETbig = new BigDecimal(UPHMISDETET * 100);
                            MISDETET = MISDETETbig.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                        }
                        if (uph.getUnknown() != null) {
                            Double UPHUnknown = uph.getUnknown();
                            BigDecimal UPHUnknownbig = new BigDecimal(UPHUnknown * 100);
                            Unknown = UPHUnknownbig.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                        }
                        MachineSub.setFAIL_Q(fail_q);
                        MachineSub.setFPY(fpy);
                        MachineSub.setYIELD(YIELD);
                        MachineSub.setMISDETET(MISDETET);
                        MachineSub.setUnknown(Unknown);
                        MachineSub.setFPYTarget(FPYTarget);
                    }
                }
                amountsUPHList.add(MachineSub);
            }

        }


        if (amountsUPHList.size() >= 3) {
            amountsUPHList = amountsUPHList.subList(0, 3);
        } else if (amountsUPHList.size() == 2) {
            amountsUPHList = amountsUPHList.subList(0, 2);
        } else if (amountsUPHList.size() == 1) {
            amountsUPHList = amountsUPHList.subList(0, 1);
        }
        return amountsUPHList;
    }

    public Double FPYTarget(String StationName) {
        Double FPYTarget = 0.0;
        Double Pre_DFUFPYTarget = 98.90;
        Double DFUFPYTarget = 98.90;
        Double FCTFPYTarget = 99.20;
        Double S1FPYTarget = 97.35;
        Double S2FPYTarget = 96.40;
        Double S3FPYTarget = 98.45;
        Double S4FPYTarget = 98.45;
        Double CCTFPYTarget = 99.49;
        Double W1FPYTarget = 97.45;
        Double W2FPYTarget = 98.99;
        Double UWBFPYTarget = 98.95;
        Double SCONDFPYTarget = 98.47;
        Double WIFIASSOTarget = 98.47;
        Double MARSCONDTarget = 98.47;
        if (StationName.equals("Pre_DFU")) {
            FPYTarget = Pre_DFUFPYTarget;
        }else if (StationName.equals("DFU")) {
            FPYTarget = DFUFPYTarget;
        } else if (StationName.equals("FCT")) {
            FPYTarget = FCTFPYTarget;
        } else if (StationName.equals("S1")) {
            FPYTarget = S1FPYTarget;
        } else if (StationName.equals("S2")) {
            FPYTarget = S2FPYTarget;
        } else if (StationName.equals("S3")) {
            FPYTarget = S3FPYTarget;
        } else if (StationName.equals("CCT")) {
            FPYTarget = CCTFPYTarget;
        } else if (StationName.equals("W1")) {
            FPYTarget = W1FPYTarget;
        } else if (StationName.equals("W2")) {
            FPYTarget = W2FPYTarget;
        } else if (StationName.equals("UWB")) {
            FPYTarget = UWBFPYTarget;
        } else if (StationName.equals("SCOND")) {
            FPYTarget = SCONDFPYTarget;
        }

        return FPYTarget;
    }

    List<String> DaydateList = new ArrayList<>();
    List<String> NightdateList = new ArrayList<>();

    public Map<String, List<WaitPlateTimeData>> waitPlateTime(String StartDate, String EndDate, String FloorName, String LineName, Integer HTarget) {

        Map<String, List<WaitPlateTimeData>> LineWaitPlateTimeMap = new HashMap<>();
        List<AUTOFLOOR_Robot_Efficiency> LineWaitPlateTime = autofloor_robot_efficiencyMapper.LineWaitPlateTime(StartDate, EndDate, FloorName, LineName);
        String DayStartTime = "2020/5/23 08:30:00";
        String DayEndTime = "2020/5/23 20:30:00";
        String NightStartTime = "2020/5/22 20:30:00";
        String NightEndTime = "2020/5/23 08:30:00";
        try {
            DaydateList = outTimeH.TimeHList(DayStartTime, DayEndTime);
            NightdateList = outTimeH.TimeHList(NightStartTime, NightEndTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        WaitPlateTimeData waitPlateTimeAll = new WaitPlateTimeData();
        int ALLHTimeData = 0;
        int ALLwaitPlateAuto = 0;
        List<WaitPlateTimeData> DaywaitPlateTimeList = new ArrayList<>();
        for (int i = 0; i < DaydateList.size(); i++) {
            String date = DaydateList.get(i);
            WaitPlateTimeData waitPlateTimeData = new WaitPlateTimeData();
            if (i == DaydateList.size() - 1) {
                int intdate = Integer.parseInt(date);
                waitPlateTimeData.setHTime((intdate - 1) + ":30" + "-" + date + ":30");
            } else {
                int intdate = Integer.parseInt(date);
                waitPlateTimeData.setHTime(date + ":30" + "-" + (intdate + 1) + ":30");
                if (intdate + 1 == 9) {
                    waitPlateTimeData.setHTime(date + ":30" + "-0" + (intdate + 1) + ":30");
                }
            }
            int HTimeData = 0;
            for (AUTOFLOOR_Robot_Efficiency autofloor_robot_efficiency : LineWaitPlateTime) {
                String time = autofloor_robot_efficiency.getTime();
                if (date.equals(time)) {
                    if (autofloor_robot_efficiency.getWaitTime() != null) {
                        HTimeData = autofloor_robot_efficiency.getWaitTime().intValue();
                    }
                }
            }
            int HTimeDataMin = HTimeData / 60;
            ALLHTimeData += HTimeDataMin;
            int waitPlateAuto = 0;
            double waitPlateAutoD = HTimeDataMin * (HTarget.doubleValue() / 60);
            BigDecimal waitPlateAutobig = new BigDecimal(waitPlateAutoD);
            waitPlateAuto = waitPlateAutobig.setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
            ALLwaitPlateAuto += waitPlateAuto;
            waitPlateTimeData.setWaitPlateAuto(waitPlateAuto);
            waitPlateTimeData.setHTimeData(HTimeDataMin);
            DaywaitPlateTimeList.add(waitPlateTimeData);
        }
        waitPlateTimeAll.setWaitPlateAuto(ALLwaitPlateAuto);
        waitPlateTimeAll.setHTimeData(ALLHTimeData);
        waitPlateTimeAll.setHTime("ALL");
        DaywaitPlateTimeList.add(waitPlateTimeAll);
        LineWaitPlateTimeMap.put("DaywaitPlateTimeList", DaywaitPlateTimeList);
        WaitPlateTimeData NightwaitPlateTimeAll = new WaitPlateTimeData();
        int NightALLHTimeData = 0;
        int NightALLwaitPlateAuto = 0;
        List<WaitPlateTimeData> NightwaitPlateTimeList = new ArrayList<>();
        for (int i = 0; i < NightdateList.size(); i++) {
            String date = NightdateList.get(i);
            WaitPlateTimeData waitPlateTimeData = new WaitPlateTimeData();
            if (i == NightdateList.size() - 1) {
                int intdate = Integer.parseInt(date);
                waitPlateTimeData.setHTime((intdate - 1) + ":30" + "-" + date + ":30");
            } else {
                int intdate = Integer.parseInt(date);
                waitPlateTimeData.setHTime(date + ":30" + "-" + (intdate + 1) + ":30");
                if (intdate + 1 == 9) {
                    waitPlateTimeData.setHTime(date + ":30" + "-0" + (intdate + 1) + ":30");
                }
            }
            int HTimeData = 0;
            for (AUTOFLOOR_Robot_Efficiency autofloor_robot_efficiency : LineWaitPlateTime) {
                String time = autofloor_robot_efficiency.getTime();
                if (date.equals(time)) {
                    if (autofloor_robot_efficiency.getWaitTime() != null) {
                        HTimeData = autofloor_robot_efficiency.getWaitTime().intValue();
                    }
                }
            }
            int HTimeDataMin = HTimeData / 60;
            NightALLHTimeData += HTimeDataMin;
            int waitPlateAuto = 0;
            double waitPlateAutoD = HTimeDataMin * (HTarget.doubleValue() / 60);
            BigDecimal waitPlateAutobig = new BigDecimal(waitPlateAutoD);
            waitPlateAuto = waitPlateAutobig.setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
            NightALLwaitPlateAuto += waitPlateAuto;
            waitPlateTimeData.setWaitPlateAuto(waitPlateAuto);
            waitPlateTimeData.setHTimeData(HTimeDataMin);
            NightwaitPlateTimeList.add(waitPlateTimeData);
        }
        NightwaitPlateTimeAll.setWaitPlateAuto(NightALLwaitPlateAuto);
        NightwaitPlateTimeAll.setHTimeData(NightALLHTimeData);
        NightwaitPlateTimeAll.setHTime("ALL");
        NightwaitPlateTimeList.add(NightwaitPlateTimeAll);
        LineWaitPlateTimeMap.put("NightwaitPlateTimeList", NightwaitPlateTimeList);
        return LineWaitPlateTimeMap;
    }
}
