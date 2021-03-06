package com.fox.qualitysys.service;


import com.fox.qualitysys.entity.AutoFloor_ESW_Monitor;
import com.fox.qualitysys.entity.QualityequipmentUN;
import com.fox.qualitysys.mapper.AutoFloor_ESW_MonitorMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author
 * @create 2020-04-23 13:47
 */
@Service
public class QualityEquipmentMonitoringService {

    @Autowired
    AutoFloor_ESW_MonitorMapper autoFloor_esw_monitorMapper;

    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public  List<AutoFloor_ESW_Monitor> QualityEquipmentMonitoringData(String FloorName, String Product) {
        List<String>D061FList=new ArrayList<>();
        D061FList.add("D061FS01");
        D061FList.add("D061FS02");
        List<AutoFloor_ESW_Monitor>  ESWMonitorDataList=new ArrayList<>();
        List<QualityequipmentUN> qualityequipmentUNList = QualityNameList();
        List<AutoFloor_ESW_Monitor> autoFloor_esw_monitors = autoFloor_esw_monitorMapper.selectESWMonitor(FloorName, Product, FloorName);
        List<AutoFloor_ESW_Monitor> UNautoFloor_esw_monitors =autoFloor_esw_monitorMapper.selectUNESWMonitor(FloorName, Product, FloorName);
        for (QualityequipmentUN qualityequipmentUN : qualityequipmentUNList) {
            AutoFloor_ESW_Monitor ESWMonitorData=new AutoFloor_ESW_Monitor();
            String quality = qualityequipmentUN.getQuality();
            String qualityCH = qualityequipmentUN.getQualityCH();
            ESWMonitorData.setQualityeCH(qualityCH);
            Map<String,List<AutoFloor_ESW_Monitor>>EquipmentMonitoringLine=new HashMap<>();
            Map<String,List<AutoFloor_ESW_Monitor>>UNEquipmentMonitoringLine=new HashMap<>();
            for (String Line : D061FList) {
                List<AutoFloor_ESW_Monitor>  ESWMonitorList=new ArrayList<>();
                List<AutoFloor_ESW_Monitor>  UNESWMonitorList=new ArrayList<>();
                    for (AutoFloor_ESW_Monitor autoFloor_esw_monitor : autoFloor_esw_monitors) {
                        String floor = autoFloor_esw_monitor.getFloor();
                        String linename = autoFloor_esw_monitor.getLinename();
                        String workstation = autoFloor_esw_monitor.getWorkstation();
                        String machinetype = autoFloor_esw_monitor.getMachinetype();
                        Date updatetime = autoFloor_esw_monitor.getUpdatetime();
                        String UpdateStr = df.format(updatetime);
                        String LineName="";
                        if (workstation.equals(quality)){
                            if (workstation.equals("AVI")){
                                LineName=FloorName+"ARF"+Line.substring(Line.length()-2);
                            }else {
                                LineName=Line;
                            }
                            if (LineName.equals(linename)){
                                AutoFloor_ESW_Monitor ESWMonitor=new AutoFloor_ESW_Monitor();
                                ESWMonitor.setFloor(floor);
                                ESWMonitor.setLinename(linename);
                                ESWMonitor.setWorkstation(workstation);
                                ESWMonitor.setMachinetype(machinetype);
                                ESWMonitor.setUpdatetime(updatetime);
                                ESWMonitor.setUpDateTimeStr(UpdateStr);
                                ESWMonitorList.add(ESWMonitor);
                            }
                        }
                    }
                    for (AutoFloor_ESW_Monitor autoFloor_esw_monitor : UNautoFloor_esw_monitors) {
                        String floor = autoFloor_esw_monitor.getFloor();
                        String linename = autoFloor_esw_monitor.getLinename();
                        String workstation = autoFloor_esw_monitor.getWorkstation();
                        String machinetype = autoFloor_esw_monitor.getMachinetype();
                        Date updatetime = autoFloor_esw_monitor.getUpdatetime();
                        String UpdateStr = df.format(updatetime);
                        String LineName="";
                        if (workstation.equals(quality)){
                            if (workstation.equals("AVI")){
                                LineName=FloorName+"ARF"+Line.substring(Line.length()-2);
                            }else {
                                LineName=Line;
                            }
                            if (LineName.equals(linename)){
                                AutoFloor_ESW_Monitor ESWMonitor=new AutoFloor_ESW_Monitor();
                                ESWMonitor.setFloor(floor);
                                ESWMonitor.setLinename(linename);
                                ESWMonitor.setWorkstation(workstation);
                                ESWMonitor.setMachinetype(machinetype);
                                ESWMonitor.setUpdatetime(updatetime);
                                ESWMonitor.setUpDateTimeStr(UpdateStr);
                                UNESWMonitorList.add(ESWMonitor);
                            }
                        }
                    }
                EquipmentMonitoringLine.put(Line,ESWMonitorList);
                UNEquipmentMonitoringLine.put(Line,UNESWMonitorList);
            }
            ESWMonitorData.setLineNameMap(EquipmentMonitoringLine);
            ESWMonitorData.setUNLineNameMap(UNEquipmentMonitoringLine);
            ESWMonitorDataList.add(ESWMonitorData);
        }
        return ESWMonitorDataList;
    }

    //主页面 异常预判的机器信息
    public List<QualityequipmentUN> QualityNameList(){
        List<QualityequipmentUN>  qualityequipmentUNList=new ArrayList<>();

        QualityequipmentUN QualityePRINTER=new QualityequipmentUN();
        QualityePRINTER.setQuality("PRINTER");
        QualityePRINTER.setQualityCH("錫膏印刷");
        qualityequipmentUNList.add(QualityePRINTER);

       /* QualityequipmentUN QualityePRINTER1=new QualityequipmentUN();
        QualityePRINTER.setQuality("鐳射");
        QualityePRINTER.setQualityCH("鐳射");
        qualityequipmentUNList.add(QualityePRINTER1);

        QualityequipmentUN QualityePRINTER2=new QualityequipmentUN();
        QualityePRINTER.setQuality("DEDUSTING");
        QualityePRINTER.setQualityCH("除塵");
        qualityequipmentUNList.add(QualityePRINTER2);

        QualityequipmentUN QualityePRINTER3=new QualityequipmentUN();
        QualityePRINTER.setQuality("HEIGHTTEST");
        QualityePRINTER.setQualityCH("高度檢測");
        qualityequipmentUNList.add(QualityePRINTER3);*/

        QualityequipmentUN QualityeSPI=new QualityequipmentUN();
        QualityeSPI.setQuality("SPI");
        QualityeSPI.setQualityCH("SPI");
        qualityequipmentUNList.add(QualityeSPI);

        QualityequipmentUN QualityeMOUNTER=new QualityequipmentUN();
        QualityeMOUNTER.setQuality("MOUNTER");
        QualityeMOUNTER.setQualityCH("貼片機");
        qualityequipmentUNList.add(QualityeMOUNTER);

        QualityequipmentUN QualityePRE_AOI=new QualityequipmentUN();
        QualityePRE_AOI.setQuality("PRE-AOI");
        QualityePRE_AOI.setQualityCH("PRE-AOI");
        qualityequipmentUNList.add(QualityePRE_AOI);

        QualityequipmentUN QualityeREFLOW=new QualityequipmentUN();
        QualityeREFLOW.setQuality("REFLOW");
        QualityeREFLOW.setQualityCH("回焊爐");
        qualityequipmentUNList.add(QualityeREFLOW);

        QualityequipmentUN QualityePOST_AOI=new QualityequipmentUN();
        QualityePOST_AOI.setQuality("POST-AOI");
        QualityePOST_AOI.setQualityCH("POST-AOI");
        qualityequipmentUNList.add(QualityePOST_AOI);

        QualityequipmentUN Qualitye87A=new QualityequipmentUN();
        Qualitye87A.setQuality("87A");
        Qualitye87A.setQualityCH("87A");
        qualityequipmentUNList.add(Qualitye87A);

        QualityequipmentUN Qualitye50A=new QualityequipmentUN();
        Qualitye50A.setQuality("50A");
        Qualitye50A.setQualityCH("50A");
        qualityequipmentUNList.add(Qualitye50A);

        QualityequipmentUN Qualitye98A=new QualityequipmentUN();
        Qualitye98A.setQuality("98A");
        Qualitye98A.setQualityCH("98A");
        qualityequipmentUNList.add(Qualitye98A);

        QualityequipmentUN QualityeAP_UF_OVEN=new QualityequipmentUN();
        QualityeAP_UF_OVEN.setQuality("AP_UF_OVEN");
        QualityeAP_UF_OVEN.setQualityCH("UF烘烤(AP)");
        qualityequipmentUNList.add(QualityeAP_UF_OVEN);

        QualityequipmentUN QualityeRF_UF_OVEN=new QualityequipmentUN();
        QualityeRF_UF_OVEN.setQuality("RF_UF_OVEN");
        QualityeRF_UF_OVEN.setQualityCH("UF烤箱(RF)");
        qualityequipmentUNList.add(QualityeRF_UF_OVEN);


        QualityequipmentUN QualityeTIM=new QualityequipmentUN();
        QualityeTIM.setQuality("TIM");
        QualityeTIM.setQualityCH("點導熱膠");
        qualityequipmentUNList.add(QualityeTIM);

        QualityequipmentUN QualityeFLUX=new QualityequipmentUN();
        QualityeFLUX.setQuality("FLUX");
        QualityeFLUX.setQualityCH("點助焊劑");
        qualityequipmentUNList.add(QualityeFLUX);

        QualityequipmentUN QualityeROUTER=new QualityequipmentUN();
        QualityeROUTER.setQuality("ROUTER");
        QualityeROUTER.setQualityCH("ROUTER");
        qualityequipmentUNList.add(QualityeROUTER);

        QualityequipmentUN QualityePD=new QualityequipmentUN();
        QualityePD.setQuality("PD");
        QualityePD.setQualityCH("PD");
        qualityequipmentUNList.add(QualityePD);

        QualityequipmentUN QualityeAVI=new QualityequipmentUN();
        QualityeAVI.setQuality("AVI");
        QualityeAVI.setQualityCH("AVI");
        qualityequipmentUNList.add(QualityeAVI);

        return qualityequipmentUNList;
    }

}
