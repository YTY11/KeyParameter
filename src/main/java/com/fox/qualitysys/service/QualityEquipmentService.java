package com.fox.qualitysys.service;


import com.fox.http.HttpClient;
import com.fox.qualitysys.entity.*;
import com.fox.qualitysys.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author
 * @create 2020-05-18 14:26
 */

@Service
public class QualityEquipmentService {
    @Autowired
    private  QualityEquipmentSubService qualityEquipmentSubService;

    @Autowired
    private TE_Software_versionMapper te_software_versionMapper;

    @Autowired
    private AutoFloor_Key_KeyPMSMapper autoFloor_key_keyPMSMapper;
    @Autowired
    private AutoFloor_Key_CheCkUpMapper autoFloor_key_cheCkUpMapper;

    @Autowired
    private TE_Base_StationMapper te_base_stationMapper;
    @Autowired
    private HttpClient httpClient;

    @Autowired
    private Warning_List_SlotMapper warning_list_slotMapper;

    public List<QualityequipmentUN> ALLLineList(String FloorName) {
        List<String> floorLineALL = FloorLineALL(FloorName);
        List<QualityequipmentUN> ALLLineList = new ArrayList<>();
        for (String Line : floorLineALL) {
            QualityequipmentUN qualityequipmentUN = new QualityequipmentUN();
            qualityequipmentUN.setLineName(Line);
            ALLLineList.add(qualityequipmentUN);
        }
        return ALLLineList;
    }

    public Map<String, List<QualityequipmentUN>> TestALLFloorLineListMap(String FloorName, String WorkStation) {
        List<String> floorLineALL = FloorLineALL(FloorName);
        Map<String, List<QualityequipmentUN>> ALLFloorLineListMap = new HashMap<>();
        List<QualityequipmentUN> ALLLineList = new ArrayList<>();
        List<String> StationList = TestfloorLineALL(WorkStation);
        List<String> StationListCH = TestfloorLineALL(WorkStation + "-CH");
        for (int i = 0; i < StationList.size(); i++) {
            String Station = StationList.get(i);
            String StationCH = StationListCH.get(i);
            QualityequipmentUN qualityequipmentUN = new QualityequipmentUN();
            qualityequipmentUN.setLineName(Station);
            qualityequipmentUN.setLineNameCH(StationCH);
            ALLLineList.add(qualityequipmentUN);
            List<QualityequipmentUN> ALLLineDataList = new ArrayList<>();
            for (String Line : floorLineALL) {
                QualityequipmentUN qualityequipmentIntUN = new QualityequipmentUN();
                Integer UNNum = 0;
                String WARNING_CASE = "SW ERROR";
                if (WorkStation.equals("Wipas")) {
                    WARNING_CASE = "SW ERROR";
                } else if (WorkStation.equals("Pathloss")) {
                    WARNING_CASE = "Pathloss ERROR";
                }
                String StationFloor = FloorName.substring(0, 3) + "-" + FloorName.substring(3, 5);
                String StationLineName = Line.substring(0, 3) + "-" + Line.substring(3, 5) + "ARF-" + Line.substring(Line.length() - 2, Line.length());
                List<EquipmentSub> equipmentSubList = qualityEquipmentSubService.TestUnStationData(Station, WARNING_CASE, StationFloor, StationLineName);
                UNNum += equipmentSubList.size();
                qualityequipmentIntUN.setLineName(Line);
                qualityequipmentIntUN.setUNNum(UNNum);
                ALLLineDataList.add(qualityequipmentIntUN);
            }
            ALLFloorLineListMap.put(Station, ALLLineDataList);
        }
        ALLFloorLineListMap.put(WorkStation, ALLLineList);
        return ALLFloorLineListMap;

        /*
        *
        * You are not expected to understand this.
        *
        * Do NOT delete this comment
        *
        * When I wrote this, only God and I understood what I was doing
        * Now,God only knows.
        *
        * */

    }

    //每个工站 对应的 工站名称 和 页面显示的中文信息
    public List<String> TestfloorLineALL(String WorkStation) {
        List<String> floorLineALL = new ArrayList<>();
        if (WorkStation.equals("Wipas")) {
            floorLineALL.add("DFU");
            floorLineALL.add("DFU-NAND-INIT");
            floorLineALL.add("FCT");
            floorLineALL.add("CELL-S1");
            floorLineALL.add("CELL-S2");
            floorLineALL.add("CELL-S3");
            floorLineALL.add("CELL-S4");
            floorLineALL.add("SMT-PAT1");
            floorLineALL.add("WIFI-BT-COND");
            floorLineALL.add("WIFI-BT-COND-2");
            floorLineALL.add("A-COND");
            floorLineALL.add("S-COND");
        } else if (WorkStation.equals("Pathloss")) {
            floorLineALL.add("CELL-S1");
            floorLineALL.add("CELL-S2");
            floorLineALL.add("CELL-S3");
            floorLineALL.add("CELL-S4");
            floorLineALL.add("WIFI-BT-COND");
            floorLineALL.add("WIFI-BT-COND2");
            floorLineALL.add("S-COND");
        } else if (WorkStation.equals("Wipas-CH")) {
            floorLineALL.add("軟體版本PreDFU");
            floorLineALL.add("軟體版本DFU");
            floorLineALL.add("軟體版本FCT");
            floorLineALL.add("軟體版本S1");
            floorLineALL.add("軟體版本S2");
            floorLineALL.add("軟體版本S3");
            floorLineALL.add("軟體版本S4");
            floorLineALL.add("軟體版本CCT");
            floorLineALL.add("軟體版本W1");
            floorLineALL.add("軟體版本W2");
            floorLineALL.add("軟體版本UWB");
            floorLineALL.add("軟體版本SCOND");
        } else if (WorkStation.equals("Pathloss-CH")) {
            floorLineALL.add("Pathloss-S1");
            floorLineALL.add("Pathloss-S2");
            floorLineALL.add("Pathloss-S3");
            floorLineALL.add("Pathloss-S4");
            floorLineALL.add("Pathloss-W1");
            floorLineALL.add("Pathloss-W2");
            floorLineALL.add("Pathloss-SCOND");
        }

        return floorLineALL;
    }

    public Map<String,Object>  ALLFloorLineList(String FloorName,String StartDate,String EndDate) {
        Map<String,Object>  ALLFloorLineMap=new HashMap<>();
        Map<String,List<QualityequipmentUN>>  QualityequipmentUNMap=new HashMap<>();
        List<QualityequipmentUN> qualityequipmentUNList = QualityNameList();
        List<AutoFloor_Key_KeyPMS> autoFloor_key_keyPMS = autoFloor_key_keyPMSMapper.selectQuality(FloorName);
        List<AutoFloor_Key_CheCkUp> autoFloor_key_cheCkUps = autoFloor_key_cheCkUpMapper.selectUNQuality(FloorName, StartDate, EndDate);
        String floorNameTE="";
        if (FloorName!=null&&FloorName.length()>3){ floorNameTE=FloorName.substring(0,3)+"-"+FloorName.substring(3);}
        List<TE_Base_Station> te_base_stations = te_base_stationMapper.selectByFloorALLBaseStation(floorNameTE);
        List<EquipmentSub> equipmentSubList = qualityEquipmentSubService.TestALLUnStationData(qualityequipmentUNList,FloorName);
        List<String> floorLineALL = FloorLineALL(FloorName);
        for (QualityequipmentUN qualityequipmentUN : qualityequipmentUNList) {
            String quality = qualityequipmentUN.getQuality();
            String qualityCH = qualityequipmentUN.getQualityCH();
            List<QualityequipmentUN> ALLLineList = new ArrayList<>();
            for (AutoFloor_Key_KeyPMS autoFloor_key_keyPM : autoFloor_key_keyPMS) {
                String workstation = autoFloor_key_keyPM.getWorkstation();
                if (quality.equals(workstation)){
                    String linename = autoFloor_key_keyPM.getLinename();
                    Integer UNNum = 0;
                    for (AutoFloor_Key_CheCkUp autoFloor_key_cheCkUp : autoFloor_key_cheCkUps) {
                        String keylinename = autoFloor_key_cheCkUp.getLinename();
                        String keyworkstation = autoFloor_key_cheCkUp.getWorkstation();
                        String keycheckResult = autoFloor_key_cheCkUp.getCheckResult();
                        if (linename!=null&&linename.equals(keylinename)){
                            if (workstation!=null&&workstation.equals(keyworkstation)){
                                if (keycheckResult!=null){
                                    if (!keycheckResult.equals("NG")){
                                        //2 为正常
                                        UNNum =2;
                                    }else{
                                        //1 为异常
                                        UNNum =1;
                                    }
                                }
                            }
                        }
                    }
                    QualityequipmentUN qualityeUNData = new QualityequipmentUN();
                    qualityeUNData.setLineName(linename);
                    qualityeUNData.setQuality(workstation);
                    qualityeUNData.setType(0);
                    qualityeUNData.setUNNum(UNNum);
                    ALLLineList.add(qualityeUNData);
                }
            }
            for (String Line : floorLineALL) {
                String  WARNING_CASE = null;
                WARNING_CASE =WARNING_CASE(qualityCH);
                if (WARNING_CASE!=null){
                    int UNNum = 0;
                    String StationLineName = Line.substring(0, 3) + "-" + Line.substring(3, 5) + "AT-" + Line.substring(Line.length() - 2);
                   int allBaseStation=0;
                    for (TE_Base_Station te_base_station : te_base_stations) {
                        String station = te_base_station.getStation();
                        String sLine = te_base_station.getSLine();
                        if (sLine!=null&&sLine.equals(StationLineName)){
                                if (quality!=null&&quality.equals(station)){
                                    allBaseStation++;
                                }
                        }
                    }
                    int equipmentSubSizi=0;
                    for (EquipmentSub equipmentSub : equipmentSubList) {
                        String warningCase = equipmentSub.getWarningCase();
                        String sLine = equipmentSub.getSLine();
                        String station = equipmentSub.getStation();
                        if (WARNING_CASE.equals(warningCase)){
                            if (StationLineName.equals(sLine)){
                                if (quality.equals(station)){
                                    equipmentSubSizi++;
                                }
                            }
                        }
                    }

                    if (allBaseStation>0){
                            if (equipmentSubSizi>0){
                                //1为异常
                                UNNum =1;
                            }else {
                                //2为正常
                                UNNum =2;
                            }

                    }
                    QualityequipmentUN qualityequipmentIntUN = new QualityequipmentUN();
                    qualityequipmentIntUN.setLineName(Line);
                    qualityequipmentIntUN.setQuality(quality);
                    qualityequipmentIntUN.setUNNum(UNNum);
                    int Type=1;
                    if (WARNING_CASE.equals("Pathloss ERROR")){
                        Type=2;
                    }
                    qualityequipmentIntUN.setType(Type);
                    ALLLineList.add(qualityequipmentIntUN);
                }
            }
            QualityequipmentUNMap.put(qualityCH,ALLLineList);
        }
        ALLFloorLineMap.put("qualityequipmentUNList",qualityequipmentUNList);
        ALLFloorLineMap.put("QualityequipmentUNMap",QualityequipmentUNMap);

        return ALLFloorLineMap;
    }
    //指定楼层有几条线体
    public List<String> FloorLineALL(String Floor) {
        Map<String, List<String>> ALLFLoorMap = new HashMap<>();
        List<String> D061FineList = new ArrayList<>();
        for (int i = 1; i <= 2; i++) {
            D061FineList.add(Floor + "S0" + i);
        }
        ALLFLoorMap.put(Floor, D061FineList);
        List<String> Floorline = ALLFLoorMap.get(Floor);

        return Floorline;
    }

    //子页面维护机器信息
    public List<QualityequipmentUN> QualityNameList(){
        List<QualityequipmentUN>  qualityequipmentList=new ArrayList<>();

        QualityequipmentUN QualityePRINTER=new QualityequipmentUN();
        QualityePRINTER.setQuality("PRINTER");
        QualityePRINTER.setQualityCH("錫膏印刷");
        qualityequipmentList.add(QualityePRINTER);

        /*QualityequipmentUN QualityePRINTER1=new QualityequipmentUN();
        QualityePRINTER1.setQuality("鐳射");
        QualityePRINTER1.setQualityCH("鐳射");
        qualityequipmentList.add(QualityePRINTER1);*/

        QualityequipmentUN QualityePRINTER2=new QualityequipmentUN();
        QualityePRINTER2.setQuality("DEDUSTING");
        QualityePRINTER2.setQualityCH("除塵");
        qualityequipmentList.add(QualityePRINTER2);

        QualityequipmentUN QualityePRINTER3=new QualityequipmentUN();
        QualityePRINTER3.setQuality("HEIGHTTEST");
        QualityePRINTER3.setQualityCH("高度檢測");
        qualityequipmentList.add(QualityePRINTER3);

        QualityequipmentUN QualityeSPI=new QualityequipmentUN();
        QualityeSPI.setQuality("SPI");
        QualityeSPI.setQualityCH("SPI");
        qualityequipmentList.add(QualityeSPI);

        QualityequipmentUN QualityeMOUNTER=new QualityequipmentUN();
        QualityeMOUNTER.setQuality("MOUNTER");
        QualityeMOUNTER.setQualityCH("貼片機");
        qualityequipmentList.add(QualityeMOUNTER);

        QualityequipmentUN QualityePRE_AOI=new QualityequipmentUN();
        QualityePRE_AOI.setQuality("PRE-AOI");
        QualityePRE_AOI.setQualityCH("PRE-AOI");
        qualityequipmentList.add(QualityePRE_AOI);

        QualityequipmentUN QualityeREFLOW=new QualityequipmentUN();
        QualityeREFLOW.setQuality("REFLOW");
        QualityeREFLOW.setQualityCH("回焊爐");
        qualityequipmentList.add(QualityeREFLOW);

        QualityequipmentUN QualityePOST_AOI=new QualityequipmentUN();
        QualityePOST_AOI.setQuality("POST-AOI");
        QualityePOST_AOI.setQualityCH("POST-AOI");
        qualityequipmentList.add(QualityePOST_AOI);

        QualityequipmentUN Qualitye87A=new QualityequipmentUN();
        Qualitye87A.setQuality("87A");
        Qualitye87A.setQualityCH("87A");
        qualityequipmentList.add(Qualitye87A);


        QualityequipmentUN Qualitye50A=new QualityequipmentUN();
        Qualitye50A.setQuality("50A");
        Qualitye50A.setQualityCH("50A");
        qualityequipmentList.add(Qualitye50A);


        QualityequipmentUN Qualitye98A=new QualityequipmentUN();
        Qualitye98A.setQuality("98A");
        Qualitye98A.setQualityCH("98A");
        qualityequipmentList.add(Qualitye98A);

        QualityequipmentUN QualityeAP_UF_OVEN=new QualityequipmentUN();
        QualityeAP_UF_OVEN.setQuality("AP_UF_OVEN");
        QualityeAP_UF_OVEN.setQualityCH("UF烘烤(AP)");
        qualityequipmentList.add(QualityeAP_UF_OVEN);

        QualityequipmentUN QualityeRF_UF_OVEN=new QualityequipmentUN();
        QualityeRF_UF_OVEN.setQuality("RF_UF_OVEN");
        QualityeRF_UF_OVEN.setQualityCH("UF烤箱(RF)");
        qualityequipmentList.add(QualityeRF_UF_OVEN);

     /*   QualityequipmentUN Qualitye38H=new QualityequipmentUN();
        Qualitye38H.setQuality("38H");
        Qualitye38H.setQualityCH("點38H膠");
        qualityequipmentList.add(Qualitye38H);*/

        QualityequipmentUN QualityeTIM=new QualityequipmentUN();
        QualityeTIM.setQuality("TIM");
        QualityeTIM.setQualityCH("點導熱膠");
        qualityequipmentList.add(QualityeTIM);

        QualityequipmentUN QualityeFLUX=new QualityequipmentUN();
        QualityeFLUX.setQuality("FLUX");
        QualityeFLUX.setQualityCH("點助焊劑");
        qualityequipmentList.add(QualityeFLUX);

        QualityequipmentUN QualityeROUTER=new QualityequipmentUN();
        QualityeROUTER.setQuality("ROUTER");
        QualityeROUTER.setQualityCH("ROUTER");
        qualityequipmentList.add(QualityeROUTER);

        QualityequipmentUN QualityeSWPreDFU=new QualityequipmentUN();
        QualityeSWPreDFU.setQuality("DFU");
        QualityeSWPreDFU.setQualityCH("軟體版本PreDFU");
        qualityequipmentList.add(QualityeSWPreDFU);

        QualityequipmentUN QualityeSWDFU=new QualityequipmentUN();
        QualityeSWDFU.setQuality("DFU-NAND-INIT");
        QualityeSWDFU.setQualityCH("軟體版本DFU");
        qualityequipmentList.add(QualityeSWDFU);

        QualityequipmentUN QualityeSWFCT=new QualityequipmentUN();
        QualityeSWFCT.setQuality("FCT");
        QualityeSWFCT.setQualityCH("軟體版本FCT");
        qualityequipmentList.add(QualityeSWFCT);

        QualityequipmentUN QualityeSWCELLS1=new QualityequipmentUN();
        QualityeSWCELLS1.setQuality("CELL-S1");
        QualityeSWCELLS1.setQualityCH("軟體版本S1");
        qualityequipmentList.add(QualityeSWCELLS1);

        QualityequipmentUN QualityeSWCELLS2=new QualityequipmentUN();
        QualityeSWCELLS2.setQuality("CELL-S2");
        QualityeSWCELLS2.setQualityCH("軟體版本S2");
        qualityequipmentList.add(QualityeSWCELLS2);

        QualityequipmentUN QualityeSWCELLS3=new QualityequipmentUN();
        QualityeSWCELLS3.setQuality("CELL-S3");
        QualityeSWCELLS3.setQualityCH("軟體版本S3");
        qualityequipmentList.add(QualityeSWCELLS3);

        QualityequipmentUN QualityeSWCELLS4=new QualityequipmentUN();
        QualityeSWCELLS4.setQuality("CELL-S4");
        QualityeSWCELLS4.setQualityCH("軟體版本S4");
        qualityequipmentList.add(QualityeSWCELLS4);

        QualityequipmentUN QualityeSWCCT=new QualityequipmentUN();
        QualityeSWCCT.setQuality("SMT-PAT1");
        QualityeSWCCT.setQualityCH("軟體版本CCT");
        qualityequipmentList.add(QualityeSWCCT);

        QualityequipmentUN QualityeSWW1=new QualityequipmentUN();
        QualityeSWW1.setQuality("WIFI-BT-COND");
        QualityeSWW1.setQualityCH("軟體版本W1");
        qualityequipmentList.add(QualityeSWW1);

        QualityequipmentUN QualityeSWW2=new QualityequipmentUN();
        QualityeSWW2.setQuality("WIFI-BT-COND-2");
        QualityeSWW2.setQualityCH("軟體版本W2");
        qualityequipmentList.add(QualityeSWW2);

        QualityequipmentUN QualityeSWUWB=new QualityequipmentUN();
        QualityeSWUWB.setQuality("A-COND");
        QualityeSWUWB.setQualityCH("軟體版本UWB");
        qualityequipmentList.add(QualityeSWUWB);

        QualityequipmentUN QualityeSWSCOND=new QualityequipmentUN();
        QualityeSWSCOND.setQuality("S-COND");
        QualityeSWSCOND.setQualityCH("軟體版本SCOND");
        qualityequipmentList.add(QualityeSWSCOND);

        QualityequipmentUN QualityePathlossS1=new QualityequipmentUN();
        QualityePathlossS1.setQuality("CELL-S1");
        QualityePathlossS1.setQualityCH("Pathloss-S1");
        qualityequipmentList.add(QualityePathlossS1);

        QualityequipmentUN QualityePathlossS2=new QualityequipmentUN();
        QualityePathlossS2.setQuality("CELL-S2");
        QualityePathlossS2.setQualityCH("Pathloss-S2");
        qualityequipmentList.add(QualityePathlossS2);

        QualityequipmentUN QualityePathlossS3=new QualityequipmentUN();
        QualityePathlossS3.setQuality("CELL-S3");
        QualityePathlossS3.setQualityCH("Pathloss-S3");
        qualityequipmentList.add(QualityePathlossS3);

        QualityequipmentUN QualityePathlossS4=new QualityequipmentUN();
        QualityePathlossS4.setQuality("CELL-S4");
        QualityePathlossS4.setQualityCH("Pathloss-S4");
        qualityequipmentList.add(QualityePathlossS4);

        QualityequipmentUN QualityePathlossW1=new QualityequipmentUN();
        QualityePathlossW1.setQuality("WIFI-BT-COND");
        QualityePathlossW1.setQualityCH("Pathloss-W1");
        qualityequipmentList.add(QualityePathlossW1);

        QualityequipmentUN QualityePathlossW2=new QualityequipmentUN();
        QualityePathlossW2.setQuality("WIFI-BT-COND-2");
        QualityePathlossW2.setQualityCH("Pathloss-W2");
        qualityequipmentList.add(QualityePathlossW2);

        QualityequipmentUN QualityePathlossSCOND=new QualityequipmentUN();
        QualityePathlossSCOND.setQuality("S-COND");
        QualityePathlossSCOND.setQualityCH("PathlossSCOND");
        qualityequipmentList.add(QualityePathlossSCOND);

        QualityequipmentUN QualityePD=new QualityequipmentUN();
        QualityePD.setQuality("PD");
        QualityePD.setQualityCH("PD");
        qualityequipmentList.add(QualityePD);

        QualityequipmentUN QualityeAVI=new QualityequipmentUN();
        QualityeAVI.setQuality("AVI");
        QualityeAVI.setQualityCH("AVI");
        qualityequipmentList.add(QualityeAVI);

        return qualityequipmentList;
    }


    //子页面每个工站对应的 预警信息
    public  String  WARNING_CASE(String qualityCH){
        String WARNING_CASE = null;
        if (qualityCH.equals("Pathloss-S1")){
            WARNING_CASE = "Pathloss ERROR";
        }else if (qualityCH.equals("Pathloss-S2")){
            WARNING_CASE = "Pathloss ERROR";
        }else if (qualityCH.equals("Pathloss-S3")){
            WARNING_CASE = "Pathloss ERROR";
        }else if (qualityCH.equals("Pathloss-S4")){
            WARNING_CASE = "Pathloss ERROR";
        }else if (qualityCH.equals("Pathloss-W1")){
            WARNING_CASE = "Pathloss ERROR";
        }else if (qualityCH.equals("Pathloss-W2")){
            WARNING_CASE = "Pathloss ERROR";
        }else if (qualityCH.equals("PathlossSCOND")){
            WARNING_CASE = "Pathloss ERROR";
        }else if (qualityCH.equals("軟體版本PreDFU")){
            WARNING_CASE = "SW ERROR";
        }else if (qualityCH.equals("軟體版本DFU")){
            WARNING_CASE = "SW ERROR";
        }else if (qualityCH.equals("軟體版本FCT")){
            WARNING_CASE = "SW ERROR";
        }else if (qualityCH.equals("軟體版本S1")){
            WARNING_CASE = "SW ERROR";
        }else if (qualityCH.equals("軟體版本S2")){
            WARNING_CASE = "SW ERROR";
        }else if (qualityCH.equals("軟體版本S3")){
            WARNING_CASE = "SW ERROR";
        }else if (qualityCH.equals("軟體版本S4")){
            WARNING_CASE = "SW ERROR";
        }else if (qualityCH.equals("軟體版本CCT")){
            WARNING_CASE = "SW ERROR";
        }else if (qualityCH.equals("軟體版本W1")){
            WARNING_CASE = "SW ERROR";
        }else if (qualityCH.equals("軟體版本W2")){
            WARNING_CASE = "SW ERROR";
        }else if (qualityCH.equals("軟體版本UWB")){
            WARNING_CASE = "SW ERROR";
        }else if (qualityCH.equals("軟體版本SCOND")){
            WARNING_CASE = "SW ERROR";
        }

        return WARNING_CASE;

    }

}
