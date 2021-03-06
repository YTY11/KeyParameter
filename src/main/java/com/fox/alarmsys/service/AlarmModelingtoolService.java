package com.fox.alarmsys.service;


import com.fox.alarmsys.entity.AlarmModelEntity;
import com.fox.alarmsys.entity.AlarmModelingtoolData;
import com.fox.alarmsys.entity.AlarmModelingtoolTabData;
import com.fox.alarmsys.entity.AutoFloor_Test_Exception;
import com.fox.alarmsys.mapper.AutoFloor_Test_ExceptionMapper;
import com.fox.testsys.entity.Base_Station;
import com.fox.testsys.mapper.Base_StationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author
 * @create 2020-09-18 11:26
 */
@Service
public class AlarmModelingtoolService {

    @Autowired
    private Base_StationMapper base_stationMapper;

    @Autowired
    private AutoFloor_Test_ExceptionMapper autoFloor_test_exceptionMapper;

    private  static    DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    public    List<AlarmModelingtoolData>   AlarmModelingtoolData(String LineName, String StartDate, String EndDate){
        String TELineName="";
        if (LineName!=null&&LineName.length()>8){
            TELineName=LineName.substring(0,3)+"-"+LineName.substring(3,5)+"AT-"+LineName.substring( 8);
        }

        List<String> ModelingToolStation = ModelingToolLineStation(LineName);
        List<Base_Station> base_stations = base_stationMapper.TestEquipmentWipasStation(TELineName);
        List<AlarmModelingtoolData> AlarmModelingtoolDataList=new ArrayList<>();

        List<Base_Station> base_stationsset=new ArrayList<>();
        for (Base_Station base_station : base_stations) {
            if (!base_stationsset.contains(base_station)) {
                base_stationsset.add(base_station);
            }
        }

        for (String Modeling : ModelingToolStation) {
            AlarmModelingtoolData alarmModelingtoolData=new AlarmModelingtoolData();
            List<AlarmModelEntity> StationNoList=new ArrayList<>();
            int TTLSum=0;
            int HealthSum=0;
            int UnusualSum=0;
            double HealthRate=0;
            for (Base_Station base_station : base_stationsset) {
                String station = base_station.getStation();
                if (Modeling.equals(station)){
                    TTLSum++;
                    AlarmModelEntity alarmModelEntity=new AlarmModelEntity();
                    String stationno = base_station.getStation_no();
                    String result = base_station.getResult();
                    int StationType=0;
                    if ("pass".equals(result)){
                        StationType=1;
                        HealthSum++;
                    }
                    else if ("fail".equals(result)){
                        StationType=2;
                        UnusualSum++;
                    }
                    alarmModelEntity.setStationNo(stationno);
                    alarmModelEntity.setStationType(StationType);
                    StationNoList.add(alarmModelEntity);
                }

            }

            if (TTLSum!=0){
                BigDecimal HealthRateBig=new BigDecimal((HealthSum/(double)TTLSum)*100);
                HealthRate=HealthRateBig.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
            }
            alarmModelingtoolData.setTTLSum(TTLSum);
            alarmModelingtoolData.setHealthSum(HealthSum);
            alarmModelingtoolData.setUnusualSum(UnusualSum);
            alarmModelingtoolData.setHealthRate(HealthRate);
            alarmModelingtoolData.setStationName(Modeling);
            alarmModelingtoolData.setStationNoList(StationNoList);
            AlarmModelingtoolDataList.add( alarmModelingtoolData);
        }
        return AlarmModelingtoolDataList;
    }

    public List<AlarmModelingtoolTabData> AlarmModelingtoolTabData(String FloorName, String LineName, String StartDate, String EndDate, String Station){
        String Stationreplace ="";
        if (Station!=null){
            Stationreplace = Station.replace("-", "");//去除-字符后的工站名稱
        }
        String TELineName="";
        if (LineName!=null&&LineName.length()>8){
            TELineName=LineName.substring(0,3)+"-"+LineName.substring(3,5)+"AT-"+LineName.substring( 8);
        }
        List<String> modelingLineList=getModelingLineList(TELineName);


        List<AutoFloor_Test_Exception> autoFloor_test_exceptions = autoFloor_test_exceptionMapper.selectModelingtoolData(FloorName, StartDate, EndDate,"TEST");
        List<String> ExceptionMachineNo=new ArrayList<>();

        List<Base_Station> base_stations = base_stationMapper.TestEquipmentWipasStation(TELineName);
        List<Base_Station> base_stationsset=new ArrayList<>();
        for (Base_Station base_station : base_stations) {
            if (!base_stationsset.contains(base_station)) {
                base_stationsset.add(base_station);
            }
        }
        for (Base_Station base_station : base_stationsset) {
            String station = base_station.getStation();
            if (Station.equals(station)){
                String result = base_station.getResult();
                if ("fail".equals(result)){
                    String station_no = base_station.getStation_no();
                    ExceptionMachineNo.add(station_no);
                }
            }
        }
        List<AlarmModelingtoolTabData> AlarmModelingtoolTabDataList=new ArrayList<>();

        for (String ExMachineNo : ExceptionMachineNo) {
            for (AutoFloor_Test_Exception autoFloor_test_exception : autoFloor_test_exceptions) {
                String machine = autoFloor_test_exception.getMachine();
                String  exceptionLinename = autoFloor_test_exception.getLinename();
                for (String modelingLineName : modelingLineList) {
                    String modelingReplace = modelingLineName.replace("-", "");
                    if (modelingReplace.length()>10){
                        modelingReplace=modelingReplace.substring(0,modelingReplace.length()-1)+"-"+modelingReplace.substring(modelingReplace.length()-1);
                    }

                    if (modelingReplace.equals(exceptionLinename)){

                        if (machine!=null){
                            String machinereplace ="";
                            if ("WIFIBTCOND".equals(Stationreplace)){
                                Stationreplace ="WIFIBT";
                            }else if ("WIFIBTCOND2".equals(Stationreplace)){
                                Stationreplace ="WIFIBT2";
                            }else if ("ACOND".equals(Stationreplace)){
                                Stationreplace ="UWB";
                            }else {
                                machinereplace =machine.replace("-", "").replace("_","").replace("/","");
                            }

                            if (Stationreplace.equals(machinereplace)){

                                String exceptionDescribe = autoFloor_test_exception.getExceptionDescribe();
                                String[] Split1 = exceptionDescribe.split("_");
                                String MachineNo = Split1[0]; //機臺號
                                String ExceptionStep = exceptionDescribe.substring(MachineNo.length()+1);
                                String T1 = Split1[1]; //通道后内容
                                String[] split1 = T1.split("%");
                                String MisdetectRate =split1[0].substring(14);  // 誤測率
                                String MachineNoreplace = MachineNo.replace("#", "");
                                String ExMachineNosub = ExMachineNo.replaceAll("[A-Za-z]","");
                                if (MachineNoreplace.equals(ExMachineNosub)){
                                    String repairPropose = autoFloor_test_exception.getRepairPropose();
                                    String repairContent = autoFloor_test_exception.getRepairContent();
                                    Date exceptionTime = autoFloor_test_exception.getExceptionTime();
                                    BigDecimal id = autoFloor_test_exception.getId();
                                    Integer flag = autoFloor_test_exception.getFlag();
                                    AlarmModelingtoolTabData alarmModelingtoolTabData=new AlarmModelingtoolTabData();
                                    alarmModelingtoolTabData.setId(id);
                                    alarmModelingtoolTabData.setFloorName(FloorName);
                                    alarmModelingtoolTabData.setLineName(LineName);
                                    alarmModelingtoolTabData.setMachineNO(MachineNo);
                                    alarmModelingtoolTabData.setMisdetectRate(Double.parseDouble(MisdetectRate));
                                    alarmModelingtoolTabData.setStation(Station);
                                    alarmModelingtoolTabData.setExceptionType("治具");
                                    alarmModelingtoolTabData.setExceptionStep(ExceptionStep);
                                    alarmModelingtoolTabData.setExceptionPre(repairPropose);
                                    alarmModelingtoolTabData.setRepairContent(repairContent);
                                    alarmModelingtoolTabData.setExceptionTime(df.format(exceptionTime));
                                    alarmModelingtoolTabData.setFlag(flag);
                                    AlarmModelingtoolTabDataList.add(alarmModelingtoolTabData);
                                }
                            }

                        }
                    }
                }

            }
        }
        return AlarmModelingtoolTabDataList;
    }

    private List<String> getModelingLineList(String sLineName){
        List<String> modelingLineList = base_stationMapper.selectBySLineEquipmentLine(sLineName);

        return modelingLineList;
    }
    private List<String> ModelingToolLineStation(String LineName){
        List<String> ModelingToolLineStationList=new ArrayList<>();
        switch (LineName){
            case "D061FARF01":
            case "D061FARF02":
            case "D061FARF03":
            case "D061FARF04":
                ModelingToolLineStationList.add("DFU");
                ModelingToolLineStationList.add("DFU-NAND-INIT");
                ModelingToolLineStationList.add("FCT");
                ModelingToolLineStationList.add("CELL-S1");
                ModelingToolLineStationList.add("CELL-S2");
                ModelingToolLineStationList.add("CELL-S3");
                ModelingToolLineStationList.add("CELL-S4");
                ModelingToolLineStationList.add("SMT-PAT1");
                ModelingToolLineStationList.add("WIFI-BT-COND");
                ModelingToolLineStationList.add("WIFI-BT-COND-2");
                ModelingToolLineStationList.add("A-COND");
                ModelingToolLineStationList.add("S-COND");
                break;
            case "K051FARF08":
                ModelingToolLineStationList.add("DFU-NAND-INIT");
                ModelingToolLineStationList.add("FCT");
                ModelingToolLineStationList.add("CELL-S1");
                ModelingToolLineStationList.add("CELL-S2");
                ModelingToolLineStationList.add("CELL-S3");
                ModelingToolLineStationList.add("CELL-S4");
                ModelingToolLineStationList.add("SMT-PAT1");
                ModelingToolLineStationList.add("WIFI-BT-COND");
                ModelingToolLineStationList.add("WIFI-BT-COND-2");
                ModelingToolLineStationList.add("A-COND");
                ModelingToolLineStationList.add("S-COND");
                ModelingToolLineStationList.add("WIFI-COND");
                ModelingToolLineStationList.add("RF-COND-1");
                ModelingToolLineStationList.add("SMT-DEVELOPMENT8");
                break;


        }



        return ModelingToolLineStationList;
    }

}
