package com.fox.testsys.service;


import com.fox.alarmsys.entity.AutoFloor_Test_Exception;
import com.fox.alarmsys.mapper.AutoFloor_Test_ExceptionMapper;
import com.fox.qualitysys.entity.Warning_List_Slot;
import com.fox.qualitysys.mapper.Warning_List_SlotMapper;
import com.fox.testsys.entity.*;
import com.fox.testsys.mapper.*;
import com.fox.testsys.utility.TaskService;
import com.fox.testsys.utility.TimeUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author
 * @create 2020-05-14 16:50
 */
@Service
public class TestEquipmentHealthService {

    @Autowired
    TestCapacityMessageService testCapacityMessageService;

    @Autowired
    AutoFloor_RateMapper autoFloor_rateMapper;

    @Autowired
    AutoFloor_Rate_MachineMapper autoFloor_rate_machineMapper;

    @Autowired
    AutoFloor_Machine_DetailMapper autoFloor_machine_detailMapper;

    @Autowired
    TestAProductService testAProductService;

    @Autowired
    AUTOFLOOR_Robot_Comm_TimesMapper autofloor_robot_comm_timesMapper;

    @Autowired
    AUTOFLOOR_Robot_EfficiencyMapper autofloor_robot_efficiencyMapper;

    @Autowired
    AutoFloor_Wait_TimeMapper autoFloor_wait_timeMapper;

    @Autowired
    AutoFloor_WIPMapper autoFloor_wipMapper;
    @Autowired
    AutoFloor_UPHMapper autoFloor_uphMapper;

    @Autowired
    AutoFloor_Fail_RETestMapper autoFloor_fail_reTestMapper;

    @Autowired
    TimeUtility timeUtility;

    @Autowired
    TaskService taskService;

    @Autowired
    AutoFloor_Slant_CountMapper autoFloor_slant_countMapper;
    @Autowired
    Warning_List_SlotMapper warning_list_slotMapper;

    @Autowired
    AutoFloor_CCDDataMapper autoFloor_ccdDataMapper;

    @Autowired
    Base_StationMapper base_stationMapper;
    @Autowired
    AutoFloor_Test_ExceptionMapper autoFloor_test_exceptionMapper;

    private static final double MachineRateTarget = 5.0;

    //班别
    private static final String TwoTimeType = "two";
    private static final String DayTimeType = "Day";
    private static final String NightTimeType = "Night";
    private static final String YestrerDayTimeType = "Yesterday";

    DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public List<AutoFloor_Rate> TestEquipmentHealthRateSubData(String ProductName, String StartDate, String EndDate, String FloorName, String LineName) {
//        System.out.println(ProductName+"--------------ProductName");
        List<AmountsUPH> StationNameList = MachineNoList(ProductName);
        List<AutoFloor_Rate> AutoFloor_AllRateList = autoFloor_rateMapper.AutoFloorALLStationRate(StartDate, EndDate, FloorName, LineName);
        List<AutoFloor_Fail_RETest> FailretestList = autoFloor_fail_reTestMapper.FAILRETEST(StartDate, EndDate, FloorName);
        List<AutoFloor_UPH> workstationamount = autoFloor_uphMapper.Workstationamount(FloorName, LineName);
        List<AutoFloor_Rate> EquipmentHealthRateList = new ArrayList<>();
        List<String> UnStationList = new ArrayList<>();
        AutoFloor_Rate AllAutoFloor_Rate = new AutoFloor_Rate();
        EquipmentHealthRateList.add(0, AllAutoFloor_Rate);
        double ALLFPY = 1.0;
        double ALLYield = 1.0;
        double ALLMisdetet = 0.0;
        double ALLUnknown = 0.0;
        int ALLStationSum = 0;
        int ALLconfirmed = 0;
        int ALLUnconfirmed = 0;
        double ALLUnconfirmedRate = 0;


        for (AmountsUPH StationAmounts : StationNameList) {
            AutoFloor_Rate EquipmentHealthRate = new AutoFloor_Rate();
            String station_name = StationAmounts.getStation_Name();
            String station_name_no = StationAmounts.getStation_Name_No();
            String productName = StationAmounts.getProductName();
            EquipmentHealthRate.setProduct(productName);
            EquipmentHealthRate.setLINE_NAME(LineName);
            EquipmentHealthRate.setStationName(station_name_no);
            BigDecimal Input = new BigDecimal(0);
            BigDecimal Pass = new BigDecimal(0);
            double Yield = 0.0;
            double Misdetet = 0.0;
            double Unknown = 0.0;
            double FPY = 0.0;
            for (AutoFloor_Rate autoFloor_rate : AutoFloor_AllRateList) {
                String stationName = autoFloor_rate.getStationName();
                if (station_name.equals(stationName)) {
                    Input = autoFloor_rate.getInput();
                    Pass = autoFloor_rate.getPass();
                    if (autoFloor_rate.getFPY() != null) {
                        Double UPHfpy = autoFloor_rate.getFPY();
                        if (UPHfpy > 0) {
                            ALLFPY *= UPHfpy;
                        }
                        BigDecimal fpybig = new BigDecimal(UPHfpy * 100);
                        FPY = fpybig.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                        Map<String, Double> HealthFPYTarget = HealthRateFPYTarget();
                        Double FPYTarget = HealthFPYTarget.get(station_name_no);
                        if (FPY < FPYTarget) {
                            UnStationList.add(station_name);
                        }
                    }
                    if (autoFloor_rate.getFPY() != null) {
                        Double UPHYIELD = autoFloor_rate.getYield();
                        if (UPHYIELD > 0) {
                            ALLYield *= UPHYIELD;
                        }
                        BigDecimal YIELDbig = new BigDecimal(UPHYIELD * 100);
                        Yield = YIELDbig.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

                    }
                    if (autoFloor_rate.getFPY() != null) {
                        Double UPHMISDETET = autoFloor_rate.getMisdetet();
                        ALLMisdetet += UPHMISDETET;
                        BigDecimal MISDETETbig = new BigDecimal(UPHMISDETET * 100);
                        Misdetet = MISDETETbig.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

                    }
                    if (autoFloor_rate.getUnknown() != null) {
                        Double UPHUnknown = autoFloor_rate.getUnknownRate();
                        ALLUnknown += UPHUnknown;
                        BigDecimal UPHUnknownbig = new BigDecimal(UPHUnknown * 100);
                        Unknown = UPHUnknownbig.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                    }
                }
            }
            //待確認
            int confirmed = 0;
            //已確認
            int Unconfirmed = 0;
            for (AutoFloor_Fail_RETest autoFloor_fail_reTest : FailretestList) {
                String Fail_ReTeststationName = autoFloor_fail_reTest.getStationName();
                String retestTimeOut = autoFloor_fail_reTest.getRetestTimeOut();
                String retestFinish = autoFloor_fail_reTest.getRetestFinish();
                if (station_name.equals(Fail_ReTeststationName)) {
                    if ("YES".equals(retestTimeOut)&& "YES".equals(retestFinish)) {
                        confirmed++;
                    }
                    if ("YES".equals(retestTimeOut) && "NO".equals(retestFinish)) {
                        Unconfirmed++;
                    }
                }

            }
            ALLconfirmed += confirmed;
            ALLUnconfirmed += Unconfirmed;
            EquipmentHealthRate.setUnconfirmed(Unconfirmed);
            EquipmentHealthRate.setConfirmed(confirmed);
            //待確認率
            int AllConfigrmedSum = confirmed + Unconfirmed;
            double UnconfirmedRate = 0;
            if (AllConfigrmedSum != 0) {
                UnconfirmedRate = Unconfirmed / (double) AllConfigrmedSum;
            }
            BigDecimal UnconfirmedRateBig = new BigDecimal(UnconfirmedRate * 100);
            UnconfirmedRate = UnconfirmedRateBig.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            EquipmentHealthRate.setUnconfirmedRate(UnconfirmedRate);
            int StationSum = 0;
            for (AutoFloor_UPH autoFloor_uph : workstationamount) {
                String workstation_name = autoFloor_uph.getStation_name();
                String workstation = station_name;
                if (station_name.equals("WIFI/BT_W1")) {
                    workstation = "WIFI/BT";
                } else if (station_name.equals("WIFI/BT_W2")) {
                    workstation = "WIFI/BT2";
                } else if (station_name.equals("ACOND")) {
                    workstation = "UWB";
                }

                if (workstation.equals(workstation_name)) {
                    StationSum = autoFloor_uph.getStationSum();
                }
            }
            ALLStationSum += StationSum;
            EquipmentHealthRate.setStationSum(StationSum);
            EquipmentHealthRate.setInput(Input);
            EquipmentHealthRate.setPass(Pass);
            EquipmentHealthRate.setYield(Yield);
            EquipmentHealthRate.setMisdetet(Misdetet);
            EquipmentHealthRate.setUnknownRate(Unknown);
            EquipmentHealthRate.setFPY(FPY);
            EquipmentHealthRateList.add(EquipmentHealthRate);
        }

        AllAutoFloor_Rate.setStationName("ALL");
        AllAutoFloor_Rate.setStationSum(ALLStationSum);
        BigDecimal ALLFPYbig = new BigDecimal(ALLFPY * 100);
        ALLFPY = ALLFPYbig.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        BigDecimal ALLYieldbig = new BigDecimal(ALLYield * 100);
        ALLYield = ALLYieldbig.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        BigDecimal ALLMisdetetbig = new BigDecimal(ALLMisdetet * 100);
        ALLMisdetet = ALLMisdetetbig.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        BigDecimal ALLUnknownbig = new BigDecimal(ALLUnknown * 100);
        ALLUnknown = ALLUnknownbig.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        int yieldSize = 0;
        int FPYSize = 0;
        for (AutoFloor_Rate autoFloor_rate : EquipmentHealthRateList) {
            Double yield = autoFloor_rate.getYield();
            if (yield != null && yield > 0) {
                yieldSize++;
            }
            Double FPY = autoFloor_rate.getFPY();
            if (FPY != null && FPY > 0) {
                FPYSize++;
            }
        }
        AllAutoFloor_Rate.setYield(ALLYield);
        AllAutoFloor_Rate.setFPY(ALLFPY);
        if (yieldSize == 0) {
            AllAutoFloor_Rate.setYield(0.00);
        }
        if (FPYSize == 0) {
            AllAutoFloor_Rate.setFPY(0.00);
        }
        AllAutoFloor_Rate.setMisdetet(ALLMisdetet);
        AllAutoFloor_Rate.setUnknownRate(ALLUnknown);

        AllAutoFloor_Rate.setUnconfirmed(ALLUnconfirmed);
        AllAutoFloor_Rate.setConfirmed(ALLconfirmed);
        int ALLconfirmedSum = ALLconfirmed + ALLUnconfirmed;
        if (ALLconfirmedSum != 0) {
            ALLUnconfirmedRate = ALLUnconfirmed / (double) ALLconfirmedSum;
        }
        BigDecimal ALLUnconfirmedRateBig = new BigDecimal(ALLUnconfirmedRate * 100);
        ALLUnconfirmedRate = ALLUnconfirmedRateBig.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        AllAutoFloor_Rate.setUnconfirmedRate(ALLUnconfirmedRate);
        EquipmentHealthRateList.set(0, AllAutoFloor_Rate);
        return EquipmentHealthRateList;
    }

    //误测不达标工站
    public List<AutoFloor_Rate_Machine> EquipmentHealthUNFPYRateData(String ProductName, String StartDate, String EndDate, String FloorName, String LineName) {
        List<AutoFloor_Rate> AutoFloor_RateData = TestEquipmentHealthRateSubData(ProductName, StartDate, EndDate, FloorName, LineName);
        List<String> UNFPYStationList = FPYRateCompare(AutoFloor_RateData);
        List<AutoFloor_Rate_Machine> UNFPYRateDataList = new ArrayList<>();
        List<AutoFloor_Test_Exception> autoFloor_test_exceptions = autoFloor_test_exceptionMapper.selectTeZTLTOP(LineName, StartDate, EndDate);
        for (String UNFPYStation : UNFPYStationList) {
            List<AutoFloor_Rate_Machine> autoFloor_rate_machines = autoFloor_rate_machineMapper.UNFPYMidetectData(StartDate, EndDate, UNFPYStation, LineName);
            for (AutoFloor_Rate_Machine autoFloor_rate_machine : autoFloor_rate_machines) {
                String lineName = autoFloor_rate_machine.getLineName();
                String machineNo = autoFloor_rate_machine.getMachineNo();
                for (AutoFloor_Test_Exception autoFloor_test_exception : autoFloor_test_exceptions) {
                    String linename = autoFloor_test_exception.getLinename();
                    String machine = autoFloor_test_exception.getMachine();
                    String ExceptionDescribe = autoFloor_test_exception.getExceptionDescribe();
                    String[] split = ExceptionDescribe.split("#");
                    String Machine_NO = split[0];
                    if (Machine_NO.length()<2){
                        Machine_NO="0"+Machine_NO;
                    }
                    if (lineName!=null&&lineName.equals(linename)){
                        if (machine!=null&&machineNo.equals(machine+Machine_NO)){
                            String exceptionDescribe = autoFloor_test_exception.getExceptionDescribe();
                            String repairPropose = autoFloor_test_exception.getRepairPropose();
                            String[] exceptionSplit = exceptionDescribe.split(":");
                            for (String Ratesplit : exceptionSplit) {
                                if (Ratesplit.indexOf("%")!=-1){
                                    if (Ratesplit.length()>=4){
                                        String FailRateStr = Ratesplit.substring(0, 4);
                                        String doubleRate = FailRateStr.replace("%", "");
                                        double FailRate = Double.parseDouble(doubleRate);
                                    }

                                }
                            }
                            autoFloor_rate_machine.setUNMessage(exceptionDescribe+"/\n"+repairPropose);
                        }
                    }
                }
            }
            UNFPYRateDataList.addAll(autoFloor_rate_machines);
        }

        /*for (String UNFPYStation : UNFPYStationList) {
            for (AutoFloor_Test_Exception autoFloor_test_exception : autoFloor_test_exceptions) {
                AutoFloor_Rate_Machine autoFloor_rate_Machine = new AutoFloor_Rate_Machine();
                String linename = autoFloor_test_exception.getLinename();
                String machine = autoFloor_test_exception.getMachine();
                String ExceptionDescribe = autoFloor_test_exception.getExceptionDescribe();
                String[] split = ExceptionDescribe.split("#");
                String Machine_NO = split[0];
                if (Machine_NO.length() < 2) {
                    Machine_NO = "0" + Machine_NO;
                }
                autoFloor_rate_Machine.setLineName(linename);
                autoFloor_rate_Machine.setMachineNo(Machine_NO);
                String exceptionDescribe = autoFloor_test_exception.getExceptionDescribe();
                String repairPropose = autoFloor_test_exception.getRepairPropose();
                String[] exceptionSplit = exceptionDescribe.split(":");
                for (String Ratesplit : exceptionSplit) {
                    if (Ratesplit.indexOf("%") != -1) {
                        if (Ratesplit.length() >= 4) {
                            String FailRateStr = Ratesplit.substring(0, 4);
                            String doubleRate = FailRateStr.replace("%", "");
                            double FailRate = Double.parseDouble(doubleRate);
                        }

                    }
                }
                autoFloor_rate_Machine.setUNMessage(exceptionDescribe + "/\n" + repairPropose);
                UNFPYRateDataList.add(autoFloor_rate_Machine);
            }
        }*/

        return UNFPYRateDataList;
    }

    //超時主板確認
    public Integer OutTimePathConfirmed(AutoFloor_Fail_RETest autoFloor_fail_reTest) {
        int confirmed = 0;
        //第一次fail時間
        Date firstFailTime = autoFloor_fail_reTest.getFirstFailTime();
        //第二次fail時間
        Date secondTestTime = autoFloor_fail_reTest.getSecondTestTime();
        //第二次fail結果 1不良 0不通過
        BigDecimal secondTestResult = autoFloor_fail_reTest.getSecondTestResult();
        //第三次fail時間
        Date thirdTestTime = autoFloor_fail_reTest.getThirdTestTime();
        //第三次fail結果 1不良 0不通過
        BigDecimal thirdTestResult = autoFloor_fail_reTest.getThirdTestResult();
        if (firstFailTime != null) {
            confirmed++;
            if (secondTestTime != null) {
                confirmed++;
                if (secondTestResult.intValue() != 0) {
                    confirmed++;
                    if (secondTestResult.intValue() == 1) {
                        confirmed++;
                    }
                    if (thirdTestTime != null) {
                        confirmed++;
                        if (thirdTestResult.intValue() != 0) {
                            confirmed++;
                            if (thirdTestResult.intValue() == 1) {
                                confirmed++;
                            }
                        } else {
                            confirmed = 0;
                        }
                    } else {
                        confirmed = 0;
                    }
                } else {
                    confirmed = 0;
                }
            } else {
                confirmed = 0;
            }
        } else {
            confirmed = 0;
        }
        return confirmed;

    }

    private List<String> FPYRateCompare(List<AutoFloor_Rate> AutoFloor_RateData) {
        String ProductnameCCT = "CCT_COMBO";
        String ProductnameWIFIBT = "WIFI/BT_W1";
        String ProductnameWIFIBT2 = "WIFI/BT_W2";
        List<String> UNFPYRateList = new ArrayList<>();
        Map<String, Double> MisdetetTargetMap = HealthRateMisdetetTarget();
        for (AutoFloor_Rate autoFloor_rateData : AutoFloor_RateData) {
            Double MisdetetRate = autoFloor_rateData.getMisdetet();
            String stationName = autoFloor_rateData.getStationName();
            String product = autoFloor_rateData.getProduct();
            if (product != null && !product.equals("ALL")) {
                if (product.equals("Pha")) {
                    ProductnameCCT = "CCT";
                    ProductnameWIFIBT = "WIFI/BT";
                    ProductnameWIFIBT2 = "WIFI/BT2";
                } else if (product.equals("Macan")) {
                    ProductnameCCT = "CCT";
                    ProductnameWIFIBT = "WIFI/BT";
                    ProductnameWIFIBT2 = "WIFI/BT2";
                }
            }
            if (stationName.equals("DFU")) {
                Double MisdetetTarget = MisdetetTargetMap.get("DFU");
                if (MisdetetRate > MisdetetTarget) {
                    UNFPYRateList.add(stationName);
                }
            } else if (stationName.equals("FCT")) {
                Double MisdetetTarget = MisdetetTargetMap.get("FCT");
                if (MisdetetRate > MisdetetTarget) {
                    UNFPYRateList.add(stationName);
                }
            } else if (stationName.equals("S1")) {
                Double MisdetetTarget = MisdetetTargetMap.get("S1");
                if (MisdetetRate > MisdetetTarget) {
                    UNFPYRateList.add("CELL_S1");
                }
            } else if (stationName.equals("S2")) {
                Double MisdetetTarget = MisdetetTargetMap.get("S2");
                if (MisdetetRate > MisdetetTarget) {
                    UNFPYRateList.add("CELL_S2");
                }
            } else if (stationName.equals("S3")) {
                Double MisdetetTarget = MisdetetTargetMap.get("S3");
                if (MisdetetRate > MisdetetTarget) {
                    UNFPYRateList.add("CELL_S3");
                }
            } else if (stationName.equals("CCT")) {
                Double MisdetetTarget = MisdetetTargetMap.get("CCT");
                if (MisdetetRate > MisdetetTarget) {
                    UNFPYRateList.add(ProductnameCCT);
                }
            } else if (stationName.equals("W1")) {
                Double MisdetetTarget = MisdetetTargetMap.get("W1");
                if (MisdetetRate > MisdetetTarget) {
                    UNFPYRateList.add(ProductnameWIFIBT);
                }
            } else if (stationName.equals("W2")) {
                Double MisdetetTarget = MisdetetTargetMap.get("W2");
                if (MisdetetRate > MisdetetTarget) {
                    UNFPYRateList.add(ProductnameWIFIBT2);
                }
            } else if (stationName.equals("UWB")) {
                Double MisdetetTarget = MisdetetTargetMap.get("UWB");
                if (MisdetetRate > MisdetetTarget) {
                    UNFPYRateList.add(stationName);
                }
            } else if (stationName.equals("SCOND")) {
                Double MisdetetTarget = MisdetetTargetMap.get("SCOND");
                if (MisdetetRate > MisdetetTarget) {
                    UNFPYRateList.add(stationName);
                }
            }
        }
        return UNFPYRateList;
    }

    public List<AmountsUPH> MachineNoList(String Product) {
        List<AmountsUPH> MachineList = new ArrayList<>();
        String ProductnameCCT = "CCT_COMBO";
        String ProductnameWIFIBT = "WIFI/BT_W1";
        String ProductnameWIFIBT2 = "WIFI/BT_W2";
        String ProductnameUWB ="UWB";
        if (Product.equals("Pha")) {
            ProductnameCCT = "CCT";
            ProductnameWIFIBT = "WIFI/BT";
            ProductnameWIFIBT2 = "WIFI/BT2";
        } else if (Product.equals("Macan")) {
            ProductnameCCT = "CCT";
            ProductnameWIFIBT = "WIFI/BT";
            ProductnameWIFIBT2 = "WIFI/BT2";
        } else if (Product.equals("Apollo")) {
            ProductnameCCT = "CCT";
            ProductnameUWB ="ACOND";

        } else if (Product.equals("Raptor")) {
            ProductnameCCT = "CCT";
            ProductnameWIFIBT = "WIFI/BT_W1";
            ProductnameWIFIBT2 = "WIFI/BT_W2";
            ProductnameUWB="ACOND";
        }
        if (Product.equals("Raptor")) {
            AmountsUPH Pre_DFUMachine = new AmountsUPH();
            Pre_DFUMachine.setStation_Name("PRE_DFU_SOC");
            Pre_DFUMachine.setStation_Name_No("Pre_DFU");
            MachineList.add(Pre_DFUMachine);
        }
        AmountsUPH DFUMachine = new AmountsUPH();
        DFUMachine.setStation_Name("DFU");
        DFUMachine.setStation_Name_No("DFU");
        DFUMachine.setProductName(Product);
        MachineList.add(DFUMachine);

        AmountsUPH FCTMachine = new AmountsUPH();
        FCTMachine.setStation_Name("FCT");
        FCTMachine.setStation_Name_No("FCT");
        FCTMachine.setProductName(Product);
        MachineList.add(FCTMachine);

        AmountsUPH S1Machine = new AmountsUPH();
        S1Machine.setStation_Name("CELL_S1");
        S1Machine.setStation_Name_No("S1");
        S1Machine.setProductName(Product);
        MachineList.add(S1Machine);

        AmountsUPH S2Machine = new AmountsUPH();
        S2Machine.setStation_Name("CELL_S2");
        S2Machine.setStation_Name_No("S2");
        S2Machine.setProductName(Product);
        MachineList.add(S2Machine);

        AmountsUPH S3Machine = new AmountsUPH();
        S3Machine.setStation_Name("CELL_S3");
        S3Machine.setStation_Name_No("S3");
        S3Machine.setProductName(Product);
        MachineList.add(S3Machine);

        AmountsUPH S4Machine = new AmountsUPH();
        S4Machine.setStation_Name("CELL_S4");
        S4Machine.setStation_Name_No("S4");
        S4Machine.setProductName(Product);
        MachineList.add(S4Machine);

        AmountsUPH CCTMachine = new AmountsUPH();
        CCTMachine.setStation_Name(ProductnameCCT);
        CCTMachine.setStation_Name_No("CCT");
        CCTMachine.setProductName(Product);
        MachineList.add(CCTMachine);

        AmountsUPH W1Machine = new AmountsUPH();
        W1Machine.setStation_Name(ProductnameWIFIBT);
        W1Machine.setStation_Name_No("W1");
        W1Machine.setProductName(Product);
        MachineList.add(W1Machine);

        AmountsUPH W2Machine = new AmountsUPH();
        W2Machine.setStation_Name(ProductnameWIFIBT2);
        W2Machine.setStation_Name_No("W2");
        W2Machine.setProductName(Product);
        MachineList.add(W2Machine);

        AmountsUPH UWBMachine = new AmountsUPH();
        UWBMachine.setStation_Name(ProductnameUWB);
        UWBMachine.setStation_Name_No("UWB");
        UWBMachine.setProductName(Product);
        MachineList.add(UWBMachine);

        AmountsUPH SCONDMachine = new AmountsUPH();
        SCONDMachine.setStation_Name("SCOND");
        SCONDMachine.setStation_Name_No("SCOND");
        SCONDMachine.setProductName(Product);
        MachineList.add(SCONDMachine);

        return MachineList;
    }

    public Map<String, Double> HealthRateYieldTarget() {
        Map<String, Double> HealthRateYieldMap = new HashMap<>();
        HealthRateYieldMap.put("Pre_DFU", 99.85);
        HealthRateYieldMap.put("DFU", 99.83);
        HealthRateYieldMap.put("FCT", 99.90);
        HealthRateYieldMap.put("S1", 99.85);
        HealthRateYieldMap.put("S2", 99.92);
        HealthRateYieldMap.put("S3", 99.95);
        HealthRateYieldMap.put("S4", 99.88);
        HealthRateYieldMap.put("CCT", 99.99);
        HealthRateYieldMap.put("W1", 99.95);
        HealthRateYieldMap.put("W2", 99.97);
        HealthRateYieldMap.put("UWB", 99.95);
        HealthRateYieldMap.put("SCOND", 99.97);
        HealthRateYieldMap.put("ALL", 99.00);
        return HealthRateYieldMap;
    }

    public Map<String, Double> HealthRateMisdetetTarget() {
        Map<String, Double> HealthRateMisdetetMap = new HashMap<>();
        HealthRateMisdetetMap.put("Pre_DFU", 0.5);
        HealthRateMisdetetMap.put("DFU", 0.5);
        HealthRateMisdetetMap.put("FCT", 1.0);
        HealthRateMisdetetMap.put("S1", 3.0);
        HealthRateMisdetetMap.put("S2", 3.8);
        HealthRateMisdetetMap.put("S3", 1.5);
        HealthRateMisdetetMap.put("S4", 1.5);
        HealthRateMisdetetMap.put("CCT", 0.3);
        HealthRateMisdetetMap.put("W1", 3.5);
        HealthRateMisdetetMap.put("W2", 0.6);
        HealthRateMisdetetMap.put("UWB", 0.8);
        HealthRateMisdetetMap.put("SCOND", 1.0);
        HealthRateMisdetetMap.put("ALL", 18.0);
        return HealthRateMisdetetMap;
    }

    public Map<String, Double> HealthRateUnknownTarget() {
        Map<String, Double> HealthRateUnknownMap = new HashMap<>();
        HealthRateUnknownMap.put("Pre_DFU", 2.00);
        HealthRateUnknownMap.put("DFU", 2.00);
        HealthRateUnknownMap.put("FCT", 0.50);
        HealthRateUnknownMap.put("S1", 0.50);
        HealthRateUnknownMap.put("S2", 0.50);
        HealthRateUnknownMap.put("S3", 0.50);
        HealthRateUnknownMap.put("S4", 0.50);
        HealthRateUnknownMap.put("CCT", 0.50);
        HealthRateUnknownMap.put("W1", 0.50);
        HealthRateUnknownMap.put("W2", 0.50);
        HealthRateUnknownMap.put("UWB", 0.50);
        HealthRateUnknownMap.put("SCOND", 0.50);
        HealthRateUnknownMap.put("ALL", 6.50);
        return HealthRateUnknownMap;
    }

    public Map<String, Double> HealthRateFPYTarget() {
        Map<String, Double> HealthRateFPYMap = new HashMap<>();
        HealthRateFPYMap.put("Pre_DFU", 98.90);
        HealthRateFPYMap.put("DFU", 98.90);
        HealthRateFPYMap.put("DFU", 98.90);
        HealthRateFPYMap.put("FCT", 99.20);
        HealthRateFPYMap.put("S1", 97.33);
        HealthRateFPYMap.put("S2", 96.44);
        HealthRateFPYMap.put("S3", 99.20);
        HealthRateFPYMap.put("S4", 99.20);
        HealthRateFPYMap.put("CCT", 99.49);
        HealthRateFPYMap.put("W1", 97.47);
        HealthRateFPYMap.put("W2", 98.99);
        HealthRateFPYMap.put("UWB", 98.95);
        HealthRateFPYMap.put("SCOND", 98.48);
        HealthRateFPYMap.put("ALL", 83.65);
        return HealthRateFPYMap;
    }

    //设备机故率
    public Map<String, List<MachineRateData>> MachineRateDataList(String FloorName,String LineName, String TypeTime, Double WorkingHours, String StartDate, String EndDate, Integer actionMinuteD) {
        Map<String, List<MachineRateData>> MachineRateDataMap = new HashMap<>();
        Base_Station DBDate = base_stationMapper.selectDBDate();
        Date ActionDate = DBDate.getAddTime();
        List<Integer> timeList = taskService.getTime(ActionDate);
        /*时*/
        Integer Htime = timeList.get(2);
        Integer Minute = timeList.get(3);
        Integer RunHtime = 0;
        if (TwoTimeType.equals(TypeTime)) {
            RunHtime = testAProductService.RobotRunHtime(TypeTime, WorkingHours);
            RunHtime *= 60;
            if (timeUtility.Timequantum("08:30", "10:30")) {
                RunHtime = ((Htime - 8) * 60 + Minute) - 30;
            } else if (timeUtility.Timequantum("20:30", "22:30")) {
                RunHtime = ((Htime - 20) * 60 + Minute) - 30;
            }
        } else if (DayTimeType.equals(TypeTime)) {
            RunHtime = DayActionRunTime(WorkingHours, actionMinuteD);
        } else if (NightTimeType.equals(TypeTime)) {
            RunHtime = NightActionRunTime(WorkingHours, actionMinuteD);
        } else if (YestrerDayTimeType.equals(TypeTime)) {
            RunHtime = YestrerActionRunTime(WorkingHours, actionMinuteD);
        }
        List<String> autoFloor_machine_details = EquipmentHealthLineCell(LineName);
        List<MachineRateData> machineRateDataList = new ArrayList<>();
        MachineRateData machineRateDataListALL = new MachineRateData();
        int AutoHtimeALL = 0;
        int MachineTimeALL = 0;
        double MachineRateALL = 0;
        double NoNormalRateALL = 0.0;
        int NoNormalTimeALL = 0;

        machineRateDataList.add(machineRateDataListALL);
        int CellSize = autoFloor_machine_details.size();
        List<MachineRateData> OverstepMachineRateList = new ArrayList<>();
        String Product = "D42";
        String TestUNFloor=FloorName.substring(0,3)+"-"+FloorName.substring(3);
        List<TestEquipmentHealth> MachineRateList = new ArrayList<>();
        List<AutoFloor_Wait_Time> autoFloor_wait_times = autoFloor_wait_timeMapper.RobotCellAnalyzes(StartDate, EndDate, LineName);
        List<AutoFloor_CCDData> autoFloor_ccdData = autoFloor_ccdDataMapper.selectCCDDATATime(StartDate, EndDate,LineName);
        List<Warning_List_Slot> warning_list_slots = warning_list_slotMapper.TestUNTime(StartDate, EndDate, TestUNFloor);
        List<Base_Station> base_stations = base_stationMapper.selectEquipmentMachineSum(Product, TestUNFloor);

        for (int i = 0; i < autoFloor_machine_details.size(); i++) {
            String lineNameCell = autoFloor_machine_details.get(i);
            MachineRateData OverstepMachineRate = new MachineRateData();
            MachineRateData machineRateData = new MachineRateData();
            List<String> StationCellName = EquipmentHealthStationCellName(LineName);
            machineRateData.setStationNameStr(StationCellName.get(i));
            machineRateData.setCellName(lineNameCell);
            machineRateData.setAutoTime(RunHtime);
            //生产时间
            int ProductionTime = RunHtime * 60;
            AutoHtimeALL += RunHtime;
            //偏位率
            double NoNormalRate = 0.0;
            //偏位机故時間
            double NoNormalUnTime=0;
            int NoNormalTime = 0;
            for (AutoFloor_Wait_Time autoFloor_wait_time : autoFloor_wait_times) {
                String WaitTimeLine_name = autoFloor_wait_time.getLine_name();
                Double aNoNormalRate = autoFloor_wait_time.getAwaitNoNormalRate();
                if (lineNameCell.equals(WaitTimeLine_name)) {
                    if (aNoNormalRate != null) {
                        NoNormalUnTime+=aNoNormalRate.intValue();
                        NoNormalTime = aNoNormalRate.intValue() / 60;
                        NoNormalRate = aNoNormalRate / (RunHtime * 60);
                    }
                }
            }
            machineRateData.setNormalNum(NoNormalTime);
            NoNormalTimeALL += NoNormalTime;
            BigDecimal NoNormalRatebig = new BigDecimal(NoNormalRate * 100);
            NoNormalRate = NoNormalRatebig.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            machineRateData.setNormalRate(NoNormalRate);
            NoNormalRateALL += NoNormalRate;

            //模組机故時間
            double ccdUntime=0;
            Integer CellWorkstationSum = autoFloor_uphMapper.selectCellWorkstationSum(lineNameCell);


            for (AutoFloor_CCDData autoFloor_ccdDatum : autoFloor_ccdData) {
                String equipmentid = autoFloor_ccdDatum.getEquipmentid();
                String CCDEquipmentid = CCDEquipmentid(lineNameCell, LineName);
                String CCDlinename = autoFloor_ccdDatum.getLinename();
                if (LineName.equals(CCDlinename)){
                    if (CCDEquipmentid.equals(equipmentid)){
                        Date errorstarttime = autoFloor_ccdDatum.getErrorstarttime();
                        Date errorendtime = autoFloor_ccdDatum.getErrorendtime();
                        ccdUntime+=errorendtime.getTime()-errorstarttime.getTime();
                    }
                }

            }
            //治具
            String TestUNTimeline=lineNameCell.substring(0,3)+"-"+lineNameCell.substring(3,8)+"-"+lineNameCell.substring(8,10)+lineNameCell.substring(11);
            Integer unStationSum = warning_list_slotMapper.TestUNStationSum(StartDate, EndDate, TestUNTimeline);
            double Untime=0;
            for (Warning_List_Slot warning_list_slot : warning_list_slots) {
                String line = warning_list_slot.getLine();
                if (TestUNTimeline.equals(line)){
                    Date addTime = warning_list_slot.getAddTime();
                    Date closeTime = warning_list_slot.getCloseTime();
                    Untime += (closeTime.getTime()-addTime.getTime());
                }

            }
            String Floor = lineNameCell.substring(0, 3);
            String FloorNum = lineNameCell.substring(3, 5);
            String Line = lineNameCell.substring(8,10);
            String Cell=lineNameCell.substring(11);
            String EquipmentLine = Floor + "-" + FloorNum + "ARF-" + Line+Cell;
            int EquipmentMachineSum=0;
            for (Base_Station base_station : base_stations) {
                String line = base_station.getLine();
                Integer lineStatusNum = base_station.getLineStatusNum();
                if (EquipmentLine.equals(line)){
                    EquipmentMachineSum=lineStatusNum;
                }
            }
            Untime=(Untime/1000/4);
            if(unStationSum!=null&&unStationSum!=0){
                Untime=(Untime/unStationSum);
                if (EquipmentMachineSum!=0){
                    Untime= (Untime/EquipmentMachineSum);
                }
            }
            ccdUntime/=1000;
            if (CellWorkstationSum!=0){
                ccdUntime/=CellWorkstationSum;
            }

            //机故时间  =偏位机故时间+治具机故时间+模組機故机故时间
            double MachineTime = NoNormalUnTime+Untime+ccdUntime;//+ccdUntime
            MachineTimeALL+=MachineTime;
            //机故率
            double machineRate = 0;
            if (ProductionTime != 0) {
                machineRate = MachineTime / (double) ProductionTime;
            }
            double MachineMinu = MachineTime / 60;
            if (MachineMinu < 0) {
                MachineMinu = 0;
            }

            machineRateData.setMachineTime((int)MachineMinu);
            BigDecimal machineRateBig = new BigDecimal(machineRate * 100);
            machineRate = machineRateBig.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            if (machineRate > MachineRateTarget) {


                OverstepMachineRate.setCellName(lineNameCell);
                OverstepMachineRate.setMachineTime((int)MachineTime);
                OverstepMachineRate.setMachineRate(machineRate);
                OverstepMachineRateList.add(OverstepMachineRate);
            }
            if (machineRate < 0) {
                machineRate = 0.0;
            }
            machineRateData.setMachineRate(machineRate);
            machineRateDataList.add(machineRateData);
        }
        machineRateDataListALL.setStationNameStr("TTL");
        machineRateDataListALL.setCellName("TTL");
        machineRateDataListALL.setAutoTime(AutoHtimeALL);
        machineRateDataListALL.setMachineTime(MachineTimeALL / 60);
        if (AutoHtimeALL != 0) {
            BigDecimal MachineRateALLBig = new BigDecimal(((MachineTimeALL / 60.0) / (double) AutoHtimeALL) * 100);
            MachineRateALL = MachineRateALLBig.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        }
        machineRateDataListALL.setMachineRate(MachineRateALL);
        machineRateDataListALL.setNormalNum(NoNormalTimeALL);
        BigDecimal NoNormalRateALLBig = new BigDecimal(NoNormalRateALL / CellSize);
        NoNormalRateALL = NoNormalRateALLBig.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        machineRateDataListALL.setNormalRate(NoNormalRateALL);
        machineRateDataList.set(0, machineRateDataListALL);
        MachineRateDataMap.put("machineRateDataList", machineRateDataList);
        MachineRateDataMap.put("OverstepMachineRateList", OverstepMachineRateList);
        return MachineRateDataMap;
    }

    public  List<String>  EquipmentHealthLineCell(String LineName){
        List<String> HealthLineCell=new ArrayList<>();
        if ("K051FARF08".equals(LineName)){
            HealthLineCell.add(LineName+"-A");
            HealthLineCell.add(LineName+"-B");
            HealthLineCell.add(LineName+"-C");
            HealthLineCell.add(LineName+"-D");
            HealthLineCell.add(LineName+"-E");
            HealthLineCell.add(LineName+"-F");
            HealthLineCell.add(LineName+"-G");
            HealthLineCell.add(LineName+"-H");
            HealthLineCell.add(LineName+"-J");
        }else {
            HealthLineCell.add(LineName+"-P");
            HealthLineCell.add(LineName+"-A");
            HealthLineCell.add(LineName+"-B");
            HealthLineCell.add(LineName+"-C");
            HealthLineCell.add(LineName+"-D");
            HealthLineCell.add(LineName+"-E");
            HealthLineCell.add(LineName+"-F");
            HealthLineCell.add(LineName+"-G");
            HealthLineCell.add(LineName+"-H");
            HealthLineCell.add(LineName+"-PD");
        }


         return HealthLineCell;
    }

    public  List<String>  EquipmentHealthStationCellName(String LineName){
        List<String> HealthLineCell=new ArrayList<>();
        if ("K051FARF08".equals(LineName)){
            HealthLineCell.add("Post AP Cell");
            HealthLineCell.add("RF Cell01");
            HealthLineCell.add("RF Cell02");
            HealthLineCell.add("RF Cell03");
            HealthLineCell.add("RF Cell04");
            HealthLineCell.add("RF Cell05");
            HealthLineCell.add("RF Cell06");
            HealthLineCell.add("RF Cell07");
            HealthLineCell.add("RF Cell08");
        }else {

            HealthLineCell.add("Pre AP Cell");
            HealthLineCell.add("Post AP Cell");
            HealthLineCell.add("RF Cell01");
            HealthLineCell.add("RF Cell02");
            HealthLineCell.add("RF Cell03");
            HealthLineCell.add("RF Cell04");
            HealthLineCell.add("RF Cell05");
            HealthLineCell.add("RF Cell06");
            HealthLineCell.add("RF Cell07");
            HealthLineCell.add("PD Cell");
        }


        return HealthLineCell;
    }
    public  String  CCDEquipmentid(String LineNameCell,String LineName){
        String CCDEquipmentid="";
        if (LineNameCell.equals(LineName+"-P")){
            CCDEquipmentid="PRE_AP_LOADER";
        }else if (LineNameCell.equals(LineName+"-A")){
            CCDEquipmentid="POST_AP_LOADER";
        }else if (LineNameCell.equals(LineName+"-R")){
            CCDEquipmentid="Router_Cell";
        }else if (LineNameCell.equals(LineName+"-B")){
            CCDEquipmentid="MODULE_CELL-B";
        }else if (LineNameCell.equals(LineName+"-C")){
            CCDEquipmentid="MODULE_CELL-C";
        }else if (LineNameCell.equals(LineName+"-D")){
            CCDEquipmentid="MODULE_CELL-D";
        }else if (LineNameCell.equals(LineName+"-E")){
            CCDEquipmentid="MODULE_CELL-E";
        }else if (LineNameCell.equals(LineName+"-F")){
            CCDEquipmentid="MODULE_CELL-F";
        }else if (LineNameCell.equals(LineName+"-G")){
            CCDEquipmentid="MODULE_CELL-G";
        }else if (LineNameCell.equals(LineName+"-H")){
            CCDEquipmentid="MODULE_CELL-H";
        }else if (LineNameCell.equals(LineName+"-PD")){
            CCDEquipmentid="MODULE_PD";
        }

        return CCDEquipmentid;
    }

    public Integer RobotCOMMTimes(String TypeTime, String StartDate, String EndDate, String LineName, String lineNameCell) {
        int RunTime = 0;
        List<AUTOFLOOR_Robot_Comm_Times> autofloor_robot_comm_times = new ArrayList<>();
        if (TwoTimeType.equals(TypeTime)) {
            String TwoStartDate = "";
            String TwoEndDate = "";
            if (timeUtility.Timequantum("08:30", "10:30")) {
                TwoStartDate = StartDate.substring(0, 11) + "08:30:00";
                TwoEndDate = EndDate.substring(0, 11) + "10:30:00";
            } else if (timeUtility.Timequantum("20:30", "22:30")) {
                TwoStartDate = StartDate.substring(0, 11) + "20:30:00";
                TwoEndDate = EndDate.substring(0, 11) + "22:30:00";
            }
            autofloor_robot_comm_times = autofloor_robot_comm_timesMapper.ROBOTCellRunTime(TwoStartDate, TwoEndDate, LineName);
        } else if (DayTimeType.equals(TypeTime)) {
            String DayMealStartDate = StartDate.substring(0, 11) + "11:30:00";
            String DayMealEndDate = EndDate.substring(0, 11) + "12:30:00";
            autofloor_robot_comm_times.addAll(autofloor_robot_comm_timesMapper.ROBOTCellRunTime(StartDate, DayMealStartDate, LineName));
            autofloor_robot_comm_times.addAll(autofloor_robot_comm_timesMapper.ROBOTCellRunTime(DayMealEndDate, EndDate, LineName));

        } else if (NightTimeType.equals(TypeTime)) {
            String NightMealStartDate = StartDate.substring(0, 11) + "23:30:00";
            String NightMealEndDate = EndDate.substring(0, 11) + "00:30:00";
            autofloor_robot_comm_times.addAll(autofloor_robot_comm_timesMapper.ROBOTCellRunTime(StartDate, NightMealStartDate, LineName));
            autofloor_robot_comm_times.addAll(autofloor_robot_comm_timesMapper.ROBOTCellRunTime(NightMealEndDate, EndDate, LineName));
        } else if (YestrerDayTimeType.equals(TypeTime)) {
            String DayMealStartDate = StartDate.substring(0, 11) + "11:30:00";
            String DayMealEndDate = EndDate.substring(0, 11) + "12:30:00";
            autofloor_robot_comm_times.addAll(autofloor_robot_comm_timesMapper.ROBOTCellRunTime(StartDate, DayMealStartDate, LineName));
            autofloor_robot_comm_times.addAll(autofloor_robot_comm_timesMapper.ROBOTCellRunTime(DayMealEndDate, EndDate, LineName));
            String NightMealStartDate = StartDate.substring(0, 11) + "23:30:00";
            String NightMealEndDate = EndDate.substring(0, 11) + "00:30:00";
            autofloor_robot_comm_times.addAll(autofloor_robot_comm_timesMapper.ROBOTCellRunTime(StartDate, NightMealStartDate, LineName));
            autofloor_robot_comm_times.addAll(autofloor_robot_comm_timesMapper.ROBOTCellRunTime(NightMealEndDate, EndDate, LineName));
        }
        for (AUTOFLOOR_Robot_Comm_Times ROBOTRunTime : autofloor_robot_comm_times) {
            String lineName = ROBOTRunTime.getLineName();
            if (lineName.equals(lineNameCell)) {
                RunTime += ROBOTRunTime.getTimes().intValue();
            }
        }

        return RunTime;
    }

    public Integer DayActionRunTime(Double WorkingHours, Integer actionMinuteD) {
        //11小时 运行分钟数
        int WorkingMinute11 = 630;
        //10小时 运行分钟数
        int WorkingMinute10 = 570;
        //9小时 运行分钟数
        int WorkingMinute9 = 520;
        //8小时 运行分钟数
        int WorkingMinute8 = 460;
        Integer actionTimeh = 0;
        /*白班實時目標*/
        if (WorkingHours == 10.5) {
            if (timeUtility.Timequantum("08:30", "09:30")) {
                actionTimeh = actionMinuteD;
            } else if (timeUtility.Timequantum("09:30", "12:30")) {
                actionTimeh = actionMinuteD;
            } else if (timeUtility.Timequantum("12:30", "20:30")) {
                actionTimeh = actionMinuteD - 50;
            } else {
                actionTimeh = (WorkingMinute11);
            }
        } else if (WorkingHours == 9.5) {
            if (timeUtility.Timequantum("08:30", "09:30")) {
                actionTimeh = actionMinuteD;
            } else if (timeUtility.Timequantum("09:30", "12:30")) {
                actionTimeh = actionMinuteD;

            } else if (timeUtility.Timequantum("12:30", "19:30")) {
                actionTimeh = actionMinuteD - 50;
            } else {
                actionTimeh = (WorkingMinute10);
            }
        } else if (WorkingHours == 8.67) {
            if (timeUtility.Timequantum("08:30", "09:30")) {
                actionTimeh = actionMinuteD;
            } else if (timeUtility.Timequantum("09:30", "12:30")) {
                actionTimeh = actionMinuteD;

            } else if (timeUtility.Timequantum("12:30", "18:30")) {
                actionTimeh = actionMinuteD - 50;
            } else {
                actionTimeh = (WorkingMinute9);
            }
        } else if (WorkingHours == 7.67) {
            if (timeUtility.Timequantum("08:30", "09:30")) {
                actionTimeh = actionMinuteD;
            } else if (timeUtility.Timequantum("09:30", "12:30")) {
                actionTimeh = actionMinuteD;
            } else if (timeUtility.Timequantum("12:30", "17:30")) {
                actionTimeh = actionMinuteD - 50;
            } else {
                actionTimeh = (WorkingMinute8);
            }
        }
        return actionTimeh;
    }

    public Integer NightActionRunTime(Double WorkingHours, Integer actionMinuteD) {
        Integer actionTimeh = 0;
        //11小时 运行分钟数
        int WorkingMinute11 = 630;
        //10小时 运行分钟数
        int WorkingMinute10 = 570;
        //9小时 运行分钟数
        int WorkingMinute9 = 520;
        //8小时 运行分钟数
        int WorkingMinute8 = 460;
        /*夜班實時目標*/

        if (WorkingHours == 10.5) {
            if (timeUtility.Timequantum("20:30", "21:30")) {
                actionTimeh = actionMinuteD;
            } else if (timeUtility.Timequantum("23:30", "00:30")) {
                actionTimeh = actionMinuteD;
            } else if (timeUtility.Timequantum("00:30", "08:30")) {
                actionTimeh = actionMinuteD - 50;
            } else {
                actionTimeh = (WorkingMinute11);
            }
        } else if (WorkingHours == 9.5) {
            if (timeUtility.Timequantum("20:30", "21:30")) {
                actionTimeh = actionMinuteD;
            } else if (timeUtility.Timequantum("23:30", "00:30")) {
                actionTimeh = actionMinuteD;
            } else if (timeUtility.Timequantum("00:30", "07:30")) {
                actionTimeh = actionMinuteD - 50;
            } else {
                actionTimeh = (WorkingMinute10);
            }
        } else if (WorkingHours == 8.67) {
            if (timeUtility.Timequantum("20:30", "21:30")) {
                actionTimeh = actionMinuteD;
            } else if (timeUtility.Timequantum("23:30", "00:30")) {
                actionTimeh = actionMinuteD;
            } else if (timeUtility.Timequantum("00:30", "06:30")) {
                actionTimeh = actionMinuteD - 50;
            } else {
                actionTimeh = (WorkingMinute9);
            }
        } else if (WorkingHours == 7.67) {
            if (timeUtility.Timequantum("20:30", "21:30")) {
                actionTimeh = (actionMinuteD);
            } else if (timeUtility.Timequantum("23:30", "00:30")) {
                actionTimeh = actionMinuteD;
            } else if (timeUtility.Timequantum("00:30", "05:30")) {
                actionTimeh = actionMinuteD - 50;
            } else {
                actionTimeh = (WorkingMinute8);
            }
        }
        return actionTimeh;
    }

    public Integer YestrerActionRunTime(Double WorkingHours, Integer actionMinuteD) {
        Integer actionTimeh = 0;
        Double WorkingMinu = WorkingHours * 60;
        actionTimeh = WorkingMinu.intValue();
        /*//11小时 运行分钟数
        int WorkingMinute11 = 630;
        //10小时 运行分钟数
        int WorkingMinute10 = 570;
        //9小时 运行分钟数
        int WorkingMinute9 = 520;
        //8小时 运行分钟数
        int WorkingMinute8 = 460;
        *//*夜班實時目標*//*

        if (WorkingHours == 10.5) {
            if (timeUtility.Timequantum("00:30", "08:30")) {
                actionTimeh = actionMinuteD - 60;
            } else {
                actionTimeh = (WorkingMinute11);
            }
        } else if (WorkingHours == 9.5) {
            if (timeUtility.Timequantum("00:30", "07:30")) {
                actionTimeh = actionMinuteD - 60;
            } else {
                actionTimeh = (WorkingMinute10);
            }
        } else if (WorkingHours == 8.67) {

            if (timeUtility.Timequantum("00:30", "06:30")) {
                actionTimeh = actionMinuteD - 60;
            } else {
                actionTimeh = (WorkingMinute9);
            }
        } else if (WorkingHours == 7.67) {
            if (timeUtility.Timequantum("00:30", "05:30")) {
                actionTimeh = actionMinuteD - 120;
            } else {
                actionTimeh =  (WorkingMinute8);
            }
        }*/
        return actionTimeh;
    }

    //機故分析
    public List<MachineRateAnalyze> MachineRateAnalyzeDataList(List<MachineRateData> OverstepMachineRateList, String TypeTime, Double WorkingHours, String StartDate, String EndDate, String FloorName, Integer actionMinuteD) {
        Base_Station DBDate = base_stationMapper.selectDBDate();
        Date ActionDate = DBDate.getAddTime();
        List<Integer> timeList = taskService.getTime(ActionDate);
        /*时*/
        Integer Htime = timeList.get(2);
        Integer Minute = timeList.get(3);
        Integer RunHtime = 0;
        if (TwoTimeType.equals(TypeTime)) {
            RunHtime = testAProductService.RobotRunHtime(TypeTime, WorkingHours);
            RunHtime *= 60;
            if (timeUtility.Timequantum("08:30", "10:30")) {
                RunHtime = ((Htime - 8) * 60 + Minute) - 30;
            } else if (timeUtility.Timequantum("20:30", "22:30")) {
                RunHtime = ((Htime - 20) * 60 + Minute) - 30;
            }
        } else if (DayTimeType.equals(TypeTime)) {
            RunHtime = DayActionRunTime(WorkingHours, actionMinuteD);
        } else if (NightTimeType.equals(TypeTime)) {
            RunHtime = NightActionRunTime(WorkingHours, actionMinuteD);
        } else if (YestrerDayTimeType.equals(TypeTime)) {
            RunHtime = YestrerActionRunTime(WorkingHours, actionMinuteD);
        }


        List<MachineRateAnalyze> MachineRateAnalyzeList = new ArrayList<>();
        for (MachineRateData machineRateData : OverstepMachineRateList) {
            MachineRateAnalyze MachineRateAnalyze = new MachineRateAnalyze();
            //線體
            String lineName = machineRateData.getCellName();
            MachineRateAnalyze.setLineName(lineName);
            //機故時間
            Integer machineTime = machineRateData.getMachineTime();
            MachineRateAnalyze.setMachineTime(machineTime);
            //TTL机故率
            Double machineRate = machineRateData.getMachineRate();
            MachineRateAnalyze.setTTLMachineRate(machineRate);
            //偏位
            AutoFloor_Wait_Time autoFloor_wait_time = autoFloor_wait_timeMapper.RobotAnalyzes(StartDate, EndDate, FloorName, "Macan", lineName);
            double NoNormalRate = 0;
            if (autoFloor_wait_time != null) {
                Double awaitNoNormalRate = autoFloor_wait_time.getAwaitNoNormalRate();
                NoNormalRate = awaitNoNormalRate / (RunHtime * 60);
            }
            BigDecimal NoNormalRateBig = new BigDecimal(NoNormalRate * 100);
            NoNormalRate = NoNormalRateBig.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            MachineRateAnalyze.setNoNormalRate(NoNormalRate);
            //治具
            String TestUNTimeline=lineName.substring(0,3)+"-"+lineName.substring(3,8)+"-"+lineName.substring(8,10)+lineName.substring(11);
            List<Warning_List_Slot> warning_list_slots = warning_list_slotMapper.TestUNTime(StartDate, EndDate, TestUNTimeline);
            Integer unStationSum = warning_list_slotMapper.TestUNStationSum(StartDate, EndDate, TestUNTimeline);
            double Untime=0;
            for (Warning_List_Slot warning_list_slot : warning_list_slots) {
                Date addTime = warning_list_slot.getAddTime();
                Date closeTime = warning_list_slot.getCloseTime();
                Untime += (closeTime.getTime()-addTime.getTime());
            }
            String Product = "D42";
            String Floor = lineName.substring(0, 3);
            String FloorNum = lineName.substring(3, 5);
            String Line = lineName.substring(8,10);
            String Cell=lineName.substring(11);
            String EquipmentLine = Floor + "-" + FloorNum + "ARF-" + Line+Cell;
            List<Base_Station> base_stations = base_stationMapper.selectEquipmentMachineSum(Product, EquipmentLine);
            int EquipmentMachineSum=0;
            for (Base_Station base_station : base_stations) {
                String line = base_station.getLine();
                Integer lineStatusNum = base_station.getLineStatusNum();
                if (TestUNTimeline.equals(line)){
                    EquipmentMachineSum=lineStatusNum;
                }
            }
            Double EquipmentRate=0.0;
            Untime=(Untime/1000/4);

            if(unStationSum!=null&&unStationSum!=0){
                Untime=(Untime/unStationSum);
                if (EquipmentMachineSum!=0){
                    Untime= (Untime/EquipmentMachineSum);
                }
            }
            EquipmentRate=Untime/(RunHtime*60);
            BigDecimal EquipmentRateBig = new BigDecimal(EquipmentRate * 100);
            EquipmentRate = EquipmentRateBig.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            MachineRateAnalyze.setEquipmentRate(EquipmentRate);
            //模組
            double ccdUntime=0;
            Integer CellWorkstationSum = autoFloor_uphMapper.selectCellWorkstationSum(lineName);
            String CCDlineName=lineName.substring(0,lineName.length()-2);
            List<AutoFloor_CCDData> autoFloor_ccdData = autoFloor_ccdDataMapper.selectCCDDATATime(StartDate, EndDate,CCDlineName);
            for (AutoFloor_CCDData autoFloor_ccdDatum : autoFloor_ccdData) {
                Date errorstarttime = autoFloor_ccdDatum.getErrorstarttime();
                Date errorendtime = autoFloor_ccdDatum.getErrorendtime();
                String equipmentid = autoFloor_ccdDatum.getEquipmentid();
                String CCDEquipmentid = CCDEquipmentid(lineName, CCDlineName);
                if (CCDEquipmentid.equals(equipmentid)) {
                    ccdUntime+=errorendtime.getTime()-errorstarttime.getTime();
                }
            }
            if (CellWorkstationSum!=0){
                ccdUntime/=CellWorkstationSum;
            }
            Double AcrossGroupRate=(ccdUntime/1000)/(RunHtime * 60);
            BigDecimal AcrossGroupRateBig = new BigDecimal(AcrossGroupRate * 100);
            AcrossGroupRate = AcrossGroupRateBig.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            MachineRateAnalyze.setAcrossGroupRate(AcrossGroupRate);

            //異常原因
            String UnusualMessage="";
            //偏位異常原因
            if (NoNormalRate>2){
                UnusualMessage+="Robot偏位;";
            }
            //模組異常原因
            if (AcrossGroupRate>1){
                UnusualMessage+="模組機故;";
                /*for (AutoFloor_CCDData autoFloor_ccdDatum : autoFloor_ccdData) {
                    String errorcode   = autoFloor_ccdDatum.getErrorcode();
                    String equipmentid = autoFloor_ccdDatum.getEquipmentid();
                    String CCDMessage  = equipmentid+":錯誤代碼"+errorcode;
                    UnusualMessage+=CCDMessage;
                }*/
            }
            //治具異常原因
            if (EquipmentRate>2){
                UnusualMessage+="治具機故;";
                /*List<Warning_List_Slot> warning_UNMessage = warning_list_slotMapper.TestUNMessage(StartDate, EndDate, TestUNTimeline);
                for (Warning_List_Slot warning_list_slot : warning_UNMessage) {
                    String station = warning_list_slot.getStation();
                    String stationNo = warning_list_slot.getStationNo();
                    String warningDetail = warning_list_slot.getWarningDetail();
                    String UNMessage=station+stationNo+"號-"+warningDetail;
                    UnusualMessage+=UNMessage;
                }*/
            }
            MachineRateAnalyze.setUnusualMessage(UnusualMessage);
            //異常預判
            String UnusualPrognosis="";
            if (NoNormalRate>2){
                UnusualPrognosis+="Robot偏位預判:";
                List<AutoFloor_Slant_Count> cellSlantCount = autoFloor_slant_countMapper.getCellSlantCount(StartDate, EndDate, lineName);
                for (AutoFloor_Slant_Count autoFloor_slant_count : cellSlantCount) {
                    String panelPcs = autoFloor_slant_count.getPanelPcs();
                    String pos = autoFloor_slant_count.getPos();
                    String leftRight = autoFloor_slant_count.getLeftRight();
                    String slantLocal = autoFloor_slant_count.getSlantLocal();
                    UnusualPrognosis+=leftRight+" "+panelPcs+" "+slantLocal+"*"+pos+"次";
                }
            }
            if (EquipmentRate>2){
                UnusualPrognosis+="治具機故預判:";
                Warning_List_Slot TestUNASCMessage = warning_list_slotMapper.TestUNASCMessage(StartDate, EndDate, TestUNTimeline);
                    String station = TestUNASCMessage.getStation();
                    String stationNo = TestUNASCMessage.getStationNo();
                    BigDecimal slot = TestUNASCMessage.getSlot();
                    String UNMessage=station+stationNo+"號-"+slot+"通道";
                    UnusualPrognosis+=UNMessage;
            }
            if (AcrossGroupRate>1){
                UnusualPrognosis+="模組機故預判:";
                AutoFloor_CCDData CDDATAUnMessage = autoFloor_ccdDataMapper.selectCCDDATAUnMessage(StartDate, EndDate, lineName);
                if (CDDATAUnMessage!=null){
                    String equipmentid = CDDATAUnMessage.getEquipmentid();
                    String CCDMessage  = equipmentid;
                    UnusualPrognosis+=CCDMessage;
                }


            }
            MachineRateAnalyze.setUnusualPrognosis(UnusualPrognosis);
            MachineRateAnalyzeList.add(MachineRateAnalyze);
        }
        return MachineRateAnalyzeList;
    }
    //超時主板確認信息
    public List<OutTimePathData> OutTimePathData(List<AutoFloor_Rate> EquipmentHealthRate, String StartDate, String EndDate, String FloorName) {
        AutoFloor_Rate autoFloor_rate = new AutoFloor_Rate();
        autoFloor_rate = HealthRatesort(EquipmentHealthRate);

        String stationName = autoFloor_rate.getStationName();
        String line_name = autoFloor_rate.getLINE_NAME();
        List<AutoFloor_Fail_RETest> failReTestList = autoFloor_fail_reTestMapper.FAILRETEST(StartDate, EndDate, FloorName);
        List<OutTimePathData> OutTimePathDataList = new ArrayList<>();
        List<OutTimePathData> twoOutTimePathDataList = new ArrayList<>();
        int i = 0;
        for (AutoFloor_Fail_RETest autoFloor_fail_reTest : failReTestList) {
            OutTimePathData outTimePathData = new OutTimePathData();
            String FaillineName = autoFloor_fail_reTest.getLineName();
            String FailstationName = autoFloor_fail_reTest.getStationName();
            String retestFinish = autoFloor_fail_reTest.getRetestFinish();
            Date retestFinishTime = autoFloor_fail_reTest.getRetestFinishTime();
            if (FaillineName.equals(line_name)) {
                if (stationName.equals(FailstationName)) {
                    i++;
                    String product = autoFloor_fail_reTest.getProduct();
                    String sn = autoFloor_fail_reTest.getSn();
                    String OccuruTime;
                    Date firstFailTime = autoFloor_fail_reTest.getFirstFailTime();
                    OccuruTime = df.format(firstFailTime);
                    Integer Confirmedint = OutTimePathConfirmed(autoFloor_fail_reTest);
                    String ConfirmedTime;
                    if ("YES".equals(retestFinish)) {
                        ConfirmedTime = df.format(retestFinishTime);
                    } else {
                        ConfirmedTime = "/";
                    }
                    String fail_item = autoFloor_fail_reTest.getFail_item();

                    outTimePathData.setLineName(FaillineName);
                    outTimePathData.setProduct(product);
                    outTimePathData.setSnNum(sn);
                    outTimePathData.setUnStation(stationName);
                    outTimePathData.setUnOccuruTime(OccuruTime);
                    outTimePathData.setUnConfirmedTime(ConfirmedTime);
                    outTimePathData.setUnSteps(fail_item);
                    if (i <= 5) {
                        if (ConfirmedTime.equals("/")) {
                            OutTimePathDataList.add(outTimePathData);
                        } else {
                            twoOutTimePathDataList.add(outTimePathData);
                        }
                    }
                }
            }
        }
        OutTimePathDataList.addAll(twoOutTimePathDataList);
        int k = 0;
        for (OutTimePathData outTimePathData : OutTimePathDataList) {
            k++;
            outTimePathData.setSerialNum(k);
        }
        return OutTimePathDataList;
    }

    public List<OutTimePathData> OutTimePathAjaxData(String station, String StartDate, String EndDate, String line_name, String FloorName, String Productname) {
        String stationName = "";
        List<AmountsUPH> amountsUPHS = MachineNoList(Productname);
        for (AmountsUPH amountsUPH : amountsUPHS) {
            String station_name_no = amountsUPH.getStation_Name_No();
            String station_name = amountsUPH.getStation_Name();
            if (station_name_no.equals(station)) {
                stationName = station_name;
            }
        }
        List<AutoFloor_Fail_RETest> failReTestList = autoFloor_fail_reTestMapper.FAILRETEST(StartDate, EndDate, FloorName);
        List<OutTimePathData> OutTimePathDataList = new ArrayList<>();
        List<OutTimePathData> twoOutTimePathDataList = new ArrayList<>();
        int i = 0;
        for (AutoFloor_Fail_RETest autoFloor_fail_reTest : failReTestList) {
            OutTimePathData outTimePathData = new OutTimePathData();
            String FaillineName = autoFloor_fail_reTest.getLineName();
            String FailstationName = autoFloor_fail_reTest.getStationName();
            String retestFinish = autoFloor_fail_reTest.getRetestFinish();
            Date retestFinishTime = autoFloor_fail_reTest.getRetestFinishTime();
            if (FaillineName.equals(line_name)) {
                if (stationName.equals(FailstationName)) {
                    i++;
                    String product = autoFloor_fail_reTest.getProduct();
                    String sn = autoFloor_fail_reTest.getSn();
                    String OccuruTime;
                    Date firstFailTime = autoFloor_fail_reTest.getFirstFailTime();
                    OccuruTime = df.format(firstFailTime);
                    Integer Confirmedint = OutTimePathConfirmed(autoFloor_fail_reTest);
                    String ConfirmedTime;
                    if ("YES".equals(retestFinish)) {
                        ConfirmedTime = df.format(retestFinishTime);
                    } else {
                        ConfirmedTime = "/";
                    }
                    String fail_item = autoFloor_fail_reTest.getFail_item();

                    outTimePathData.setLineName(FaillineName);
                    outTimePathData.setProduct(product);
                    outTimePathData.setSnNum(sn);
                    outTimePathData.setUnStation(stationName);
                    outTimePathData.setUnOccuruTime(OccuruTime);
                    outTimePathData.setUnConfirmedTime(ConfirmedTime);
                    outTimePathData.setUnSteps(fail_item);
                    if (i <= 5) {
                        if (ConfirmedTime.equals("/")) {
                            OutTimePathDataList.add(outTimePathData);
                        } else {
                            twoOutTimePathDataList.add(outTimePathData);
                        }

                    }
                }
            }
        }
        OutTimePathDataList.addAll(twoOutTimePathDataList);
        int k = 0;
        for (OutTimePathData outTimePathData : OutTimePathDataList) {
            k++;
            outTimePathData.setSerialNum(k);
        }
        return OutTimePathDataList;
    }

    private AutoFloor_Rate HealthRatesort(List<AutoFloor_Rate> EquipmentHealthRate) {
        AutoFloor_Rate autoFloor_rate = new AutoFloor_Rate();
        double max = 0;
        for (int i = 0; i < EquipmentHealthRate.size(); i++) {
            for (int j = i - 1; j >= 0; j--) {
                if (EquipmentHealthRate.get(j + 1).getUnconfirmedRate() > EquipmentHealthRate.get(j).getUnconfirmedRate() && EquipmentHealthRate.get(j + 1).getUnconfirmedRate() > max) {
                    max = EquipmentHealthRate.get(j + 1).getUnconfirmedRate();
                    autoFloor_rate = EquipmentHealthRate.get(j + 1);
                }
            }
        }

        return autoFloor_rate;
    }
}
